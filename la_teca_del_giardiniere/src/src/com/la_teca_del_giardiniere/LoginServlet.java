package src.com.la_teca_del_giardiniere; 

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger; // Per un logging più robusto

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import src.com.la_teca_del_giardiniere.classes.Utente;
import src.com.la_teca_del_giardiniere.dao.UtenteDAO;
import src.com.la_teca_del_giardiniere.util.PasswordHashing; 

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName()); // Logger per il debug
    private UtenteDAO utenteDao;

    @Override
    public void init() throws ServletException {
        // Il metodo init viene chiamato una volta all'avvio della servlet
        super.init(); // Chiamata al metodo init della superclasse
        try {
            utenteDao = new UtenteDAO();
            LOGGER.info("UtenteDAO inizializzato con successo in LoginServlet.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'inizializzazione di UtenteDAO.", e);
            throw new ServletException("Errore durante l'inizializzazione del DAO: " + e.getMessage(), e);
        } catch (Exception e) { // Cattura anche altre possibili eccezioni
            LOGGER.log(Level.SEVERE, "Errore generico durante l'inizializzazione di UtenteDAO.", e);
            throw new ServletException("Errore generico durante l'inizializzazione del DAO: " + e.getMessage(), e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String contextPath = request.getContextPath(); // Per costruire URL relativi alla radice dell'applicazione

        LOGGER.info("Tentativo di login per email: " + email);

        try {
            // Assumo che getUtenteByEmailWithRuoli restituisca un oggetto Utente (ex registrazione)
            // che contenga sia la password hashata che la lista dei ruoli.
            Utente utente = utenteDao.getUtenteByEmailWithRuoli(email);

            // Verifica se l'utente esiste e se la password fornita corrisponde all'hash salvato
            if (utente != null && PasswordHashing.checkPassword(password, utente.getPasswordHash())) { 
                // Autenticazione riuscita
                HttpSession session = request.getSession();
                session.setAttribute("loggedInUser", utente); // Salva l'intero oggetto Utente (più comodo)
                session.setAttribute("utenteId", utente.getId()); // ID dell'utente
                session.setAttribute("email", utente.getEmail());         // Email dell'utente
                session.setAttribute("ruoli", utente.getRuoli());         // Lista dei ruoli (es. ArrayList<String>)

                // Imposta un timeout per la sessione (es. 30 minuti)
                session.setMaxInactiveInterval(30 * 60);

                LOGGER.info("Login riuscito per utente con email: " + email);

                // Reindirizza l'utente alla pagina principale o alla sua area personale
                // Usa contextPath per garantire che il reindirizzamento sia corretto indipendentemente dal deployment path
                response.sendRedirect(contextPath + "/homepage.jsp"); // Modifica con la tua pagina principale
            } else {
                // Autenticazione fallita: credenziali non valide (utente non trovato o password errata)
                LOGGER.warning("Login fallito per email: " + email + " - Credenziali non valide.");
                request.setAttribute("erroreLogin", "Email o password non validi.");
                request.getRequestDispatcher("/login.jsp").forward(request, response); // Inoltra alla pagina di login
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante il tentativo di login per email: " + email, e);
            request.setAttribute("erroreLogin", "Errore durante l'accesso al database. Riprova più tardi.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } catch (Exception e) {
            // Cattura qualsiasi altra eccezione imprevista
            LOGGER.log(Level.SEVERE, "Errore imprevisto durante il tentativo di login per email: " + email, e);
            request.setAttribute("erroreLogin", "Si è verificato un errore inaspettato. Riprova più tardi.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    // Le richieste GET a /LoginServlet dovrebbero reindirizzare al form di login
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String contextPath = request.getContextPath();
        response.sendRedirect(contextPath + "/login.jsp");
    }
}