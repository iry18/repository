package la_teca_del_giardiniere;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import la_teca_del_giardiniere.classes.Utente;

import java.util.logging.Logger;

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(LogoutServlet.class.getName());

    public LogoutServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false); // Ottieni la sessione esistente, non crearne una nuova
        if (session != null) {
            String userEmail = null;
            Utente currentUser = (Utente) session.getAttribute("currentUser"); // ✨ Corretto: usa "currentUser" qui
            if (currentUser != null) {
                userEmail = currentUser.getEmail();
            }

            session.invalidate(); // Invalida la sessione, rimuovendo tutti gli attributi
            LOGGER.info("Utente " + (userEmail != null ? userEmail : "sconosciuto") + " ha effettuato il logout. Sessione invalidata.");
        } else {
            LOGGER.info("Tentativo di logout senza una sessione attiva.");
        }

        // Reindirizza alla homepage o alla pagina di login, con messaggio di successo
        response.sendRedirect(request.getContextPath() + "/Login.jsp?message=logged_out"); // ✨ Aggiunto messaggio di successo
    }

    // Puoi reindirizzare doGet a doPost se preferisci un logout con POST,
    // ma GET è comune per il logout in quanto non modifica lo stato del server (a parte la sessione).
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}