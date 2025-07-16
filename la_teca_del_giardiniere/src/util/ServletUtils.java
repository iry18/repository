// Snippet from util/ServletUtils.java (for verification)
package util;

import la_teca_del_giardiniere.classes.Piante; // Make sure this import is here
import la_teca_del_giardiniere.classes.Utente; // Make sure this import is here

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import java.util.List;

import java.util.logging.Logger;

public class ServletUtils {

    private static final Logger LOGGER = Logger.getLogger(ServletUtils.class.getName());

    // --- YOUR checkAuthenticationAndAuthorization METHOD ---
    public static boolean checkAuthenticationAndAuthorization(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        String contextPath = request.getContextPath();

        if (session == null || session.getAttribute("currentUser") == null) { // Or "loggedInUser" if you changed LoginServlet
            LOGGER.info("Tentativo di accesso non autenticato. Reindirizzamento a Login.jsp");
            if (session == null) {
                 session = request.getSession(true);
            }
            session.setAttribute("messaggioErroreLogin", "Devi effettuare il login per accedere a questa risorsa.");
            session.setAttribute("tipoMessaggio", "error");
            response.sendRedirect(contextPath + "/Login.jsp");
            return false;
        }

        Utente utenteLoggato = (Utente) session.getAttribute("currentUser"); // Or "loggedInUser"
        if (utenteLoggato == null) {
            LOGGER.info("Utente in sessione nullo dopo il recupero. Reindirizzamento a Login.jsp");
            session.setAttribute("messaggioErroreLogin", "La tua sessione non è valida. Effettua nuovamente il login.");
            session.setAttribute("tipoMessaggio", "error");
            response.sendRedirect(contextPath + "/Login.jsp");
            return false;
        }
        // ... (authorization logic) ...
        return true;
    }


    // --- YOUR buildPiantaFromRequest METHOD ---
    // Make sure the method signature is exactly as below: public static
    public static Piante buildPiantaFromRequest(HttpServletRequest request, List<String> errori) {
        Piante pianta = new Piante();
        // ... (all the logic for building Piante from request parameters) ...
        return pianta;
    }
}