package la_teca_del_giardiniere;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import la_teca_del_giardiniere.DAO.UtenteDAO;
import la_teca_del_giardiniere.classes.Utente;
import util.PasswordHashing; // Assumendo che questa sia la tua utility per le password

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());
    private UtenteDAO utenteDao;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            utenteDao = new UtenteDAO();
            LOGGER.info("UtenteDAO inizializzato con successo in LoginServlet.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'inizializzazione di UtenteDAO.", e);
            throw new ServletException("Errore durante l'inizializzazione del DAO: " + e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Errore generico durante l'inizializzazione di UtenteDAO.", e);
            throw new ServletException("Errore generico durante l'inizializzazione del DAO: " + e.getMessage(), e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String contextPath = request.getContextPath(); // Necessario per response.sendRedirect()

        HttpSession session = request.getSession();

        LOGGER.info("Tentativo di login per email: " + email);

        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            LOGGER.warning("Tentativo di login con campi vuoti.");
            session.setAttribute("loginError", "Email e password non possono essere vuoti."); // Salva l'errore in sessione
            response.sendRedirect(contextPath + "/Login.jsp"); // Reindirizza alla pagina di login
            return;
        }

        try {
            Utente utente = utenteDao.getUtenteByEmailWithRuoli(email); // Questo metodo dovrebbe popolare isAdmin e altri ruoli

            // --- DEBUG: Verifica il valore di isAdmin subito dopo aver recuperato l'utente ---
            if (utente != null) {
                LOGGER.info("Utente trovato dal DB: " + utente.getEmail() + ", isAdmin: " + utente.isAdmin() + ", isVenditore: " + utente.isVenditore());
            } else {
                LOGGER.warning("Nessun utente trovato per l'email: " + email + " nel database.");
            }
            // --- FINE DEBUG ---

            if (utente != null && PasswordHashing.verifyPassword(password, utente.getPasswordHash())) {
                // Autenticazione riuscita
                session.setAttribute("currentUser", utente);
                session.setMaxInactiveInterval(30 * 60); // Timeout della sessione: 30 minuti

                LOGGER.info("Login riuscito per utente con email: " + email + " (Admin: " + utente.isAdmin() + ", Ruoli: " + utente.getRuoli() + ")");

                // Logica di reindirizzamento basata sui ruoli
                if (utente.isAdmin()) {
                	response.sendRedirect(contextPath + "/admin/Dashboard"); // Reindirizza alla pagina di test admin
                	} else if (utente.isVenditore()) { // Controlla se è un venditore (assumendo il metodo isVenditore() nell'oggetto Utente)
                    response.sendRedirect(contextPath + "/venditore/dashboard.jsp"); // Reindirizza alla dashboard del venditore
                } else {
                    response.sendRedirect(contextPath + "/homepage.jsp"); // Predefinito per gli utenti normali
                }

            } else {
                // Autenticazione fallita: utente non trovato o password non corrispondente
                LOGGER.warning("Login fallito per email: " + email + " - Credenziali non valide.");
                session.setAttribute("loginError", "Credenziali non valide. Riprova."); // Salva l'errore in sessione
                response.sendRedirect(contextPath + "/Login.jsp"); // Reindirizza alla pagina di login
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante il tentativo di login per email: " + email, e);
            session.setAttribute("loginError", "Errore del server. Riprova più tardi."); // Salva l'errore
            response.sendRedirect(contextPath + "/Login.jsp"); // Reindirizza alla pagina di login
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Errore imprevisto durante il tentativo di login per email: " + email, e);
            session.setAttribute("loginError", "Si è verificato un errore inatteso. Riprova."); // Salva l'errore
            response.sendRedirect(contextPath + "/Login.jsp"); // Reindirizza alla pagina di login
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Nessun bisogno di String contextPath = request.getContextPath(); qui,
        // dato che request.getRequestDispatcher("/Login.jsp") usa un percorso relativo al contesto.

        // Controlla i messaggi di errore dai reindirizzamenti di doPost
        String error = request.getParameter("error");
        if ("invalid_credentials".equals(error)) {
            request.setAttribute("loginError", "Email o password non validi.");
        } else if ("server_error".equals(error)) {
            request.setAttribute("loginError", "Errore interno del server.");
        }
        request.getRequestDispatcher("/Login.jsp").forward(request, response);
    }
}