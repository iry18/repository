package admin;

import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import la_teca_del_giardiniere.classes.Utente; // Assicurati di importare la tua classe Utente

@WebServlet("/admin/Dashboard") // Questo è l'URL pubblico a cui reindirizzerai
public class AdminDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(AdminDashboardServlet.class.getName());

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false); // Non creare una nuova sessione se non esiste

        if (session == null) {
            LOGGER.warning("Sessione nulla (non esiste).");
        } else {
            Object obj = session.getAttribute("currentUser");
            if (obj == null) {
                LOGGER.warning("Attributo currentUser mancante nella sessione.");
            } else {
                LOGGER.info("Utente in sessione: " + ((Utente) obj).getEmail());
            }
        }

         // --- 1. Autenticazione e Autorizzazione (Cruciale!) ---
        Utente currentUser = (session != null) ? (Utente) session.getAttribute("currentUser") : null;
        String contextPath = request.getContextPath();

        if (currentUser == null || !currentUser.isAdmin()) {
            // L'utente non è loggato O non è un amministratore
            LOGGER.warning("Accesso non autorizzato all'area amministrativa. Utente: " + (currentUser != null ? currentUser.getEmail() : "Nessuno"));
            if (session != null) {
                session.setAttribute("errorMessage", "Accesso non autorizzato. Effettua il login come amministratore.");
            }
            response.sendRedirect(contextPath + "/Login.jsp"); // Reindirizza alla pagina di login
            return; 
        }
       

        LOGGER.info("Accesso all'area amministrativa consentito per: " + currentUser.getEmail());

        // --- 2. Inoltra alla JSP protetta ---
        // Se il controllo è passato, inoltra la richiesta alla JSP della dashboard
        request.getRequestDispatcher("/admin/AdminHome.jsp").forward(request, response);
}

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response); // Per semplicità, gestisce le richieste POST come GET per la dashboard
    }
}