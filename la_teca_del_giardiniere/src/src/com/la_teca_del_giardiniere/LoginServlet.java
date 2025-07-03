package src.com.la_teca_del_giardiniere;

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

// Assicurati che i percorsi siano corretti per le tue classi
import src.com.la_teca_del_giardiniere.classes.Utente;
import src.com.la_teca_del_giardiniere.dao.UtenteDAO;
import util.PasswordHashing;

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
        request.setCharacterEncoding("UTF-8"); // Imposta la codifica dei caratteri per i parametri in entrata

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String contextPath = request.getContextPath(); // Ottiene la root del contesto dell'applicazione

        LOGGER.info("Tentativo di login per email: " + email);

        // Controllo per campi email/password vuoti
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            LOGGER.warning("Tentativo di login con campi vuoti.");
            response.sendRedirect(contextPath + "/login.jsp?error=invalid_credentials");
            return;
        }

        try {
            Utente utente = utenteDao.getUtenteByEmailWithRuoli(email);

            if (utente != null && PasswordHashing.checkPassword(password, utente.getPasswordHash())) {
                // Autenticazione riuscita
                HttpSession session = request.getSession();
                session.setAttribute("loggedInUser", utente); // Memorizza l'oggetto Utente completo
                session.setAttribute("utenteId", utente.getId());
                session.setAttribute("email", utente.getEmail());
                
                // Assicurati che Utente.getRuoli() restituisca una collezione o null/vuota
                if (utente.getRuoli() != null && !utente.getRuoli().isEmpty()) {
                    session.setAttribute("ruoli", utente.getRuoli());
                } else {
                    LOGGER.warning("Utente " + email + " loggato senza ruoli definiti.");
                    session.removeAttribute("ruoli"); // Rimuovi l'attributo se non ci sono ruoli
                }

                session.setMaxInactiveInterval(30 * 60); // Timeout sessione: 30 minuti

                LOGGER.info("Login riuscito per utente con email: " + email);

                // Reindirizza l'utente alla pagina principale o alla sua area personale
                response.sendRedirect(contextPath + "/homepage.jsp");

            } else {
                // Autenticazione fallita: utente non trovato o password non corrispondente
                LOGGER.warning("Login fallito per email: " + email + " - Credenziali non valide.");
                response.sendRedirect(contextPath + "/login.jsp?error=invalid_credentials&email=" + email);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante il tentativo di login per email: " + email, e);
            response.sendRedirect(contextPath + "/login.jsp?error=server_error&email=" + email);
        } catch (Exception e) {
            // Cattura qualsiasi altra eccezione imprevista (es. problemi con PasswordHashing)
            LOGGER.log(Level.SEVERE, "Errore imprevisto durante il tentativo di login per email: " + email, e);
            response.sendRedirect(contextPath + "/login.jsp?error=server_error&email=" + email);
        }
    }

    // Gestisce le richieste GET, reindirizzando sempre al form di login
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String contextPath = request.getContextPath();
        response.sendRedirect(contextPath + "/login.jsp");
    }
}