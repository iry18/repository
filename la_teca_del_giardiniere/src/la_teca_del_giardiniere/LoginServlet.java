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
        request.setCharacterEncoding("UTF-8");

        // Recupera l'email (ora è il nome del campo nel form di login)
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String contextPath = request.getContextPath();

        HttpSession session = request.getSession();

        LOGGER.info("Tentativo di login per email: " + email);

        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            LOGGER.warning("Tentativo di login con campi vuoti.");
            response.sendRedirect(contextPath + "/Login.jsp?error=invalid_credentials"); // Rimosso email dalla URL
            return;
        }

        try {
            Utente utente = utenteDao.getUtenteByEmailWithRuoli(email);

            if (utente != null && PasswordHashing.verifyPassword(password, utente.getPasswordHash())) {
                // Autenticazione riuscita
                session.setAttribute("currentUser", utente);
                session.setMaxInactiveInterval(30 * 60); // Timeout sessione: 30 minuti

                LOGGER.info("Login riuscito per utente con email: " + email);
                response.sendRedirect(contextPath + "/homepage.jsp"); // Reindirizza alla homepage

            } else {
                // Autenticazione fallita: utente non trovato o password non corrispondente
                LOGGER.warning("Login fallito per email: " + email + " - Credenziali non valide.");
                response.sendRedirect(contextPath + "/Login.jsp?error=invalid_credentials"); // Rimosso email dalla URL
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante il tentativo di login per email: " + email, e);
            response.sendRedirect(contextPath + "/Login.jsp?error=server_error"); // Rimosso email dalla URL
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Errore imprevisto durante il tentativo di login per email: " + email, e);
            response.sendRedirect(contextPath + "/Login.jsp?error=server_error"); // Rimosso email dalla URL
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String contextPath = request.getContextPath();
        response.sendRedirect(contextPath + "/Login.jsp");
    }
}