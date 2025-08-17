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
        // L'utente è già stato autenticato e autorizzato dal filtro!
        HttpSession session = request.getSession(false);
        Utente currentUser = (Utente) session.getAttribute("currentUser"); // Sicuro che sia non nullo e admin

        LOGGER.info("Accesso all'area amministrativa consentito per: " + currentUser.getEmail());

        request.getRequestDispatcher("/admin/AdminHome.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}