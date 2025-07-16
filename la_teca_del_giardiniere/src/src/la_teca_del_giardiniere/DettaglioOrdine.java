package la_teca_del_giardiniere;

import la_teca_del_giardiniere.DAO.OrdineDAO;
import la_teca_del_giardiniere.classes.Ordine;
import la_teca_del_giardiniere.classes.Utente;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/admin/dettaglioOrdine") // This remains the URL mapping
public class DettaglioOrdine extends HttpServlet { // Renamed the class here
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(DettaglioOrdine.class.getName()); // Update logger name

    private OrdineDAO ordineDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            ordineDAO = new OrdineDAO();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante l'inizializzazione del DAO", e);
            throw new ServletException("Errore di configurazione del database.", e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("utenteCorrente");

        if (utente == null || !isAdminOrVenditore(utente)) {
            response.sendRedirect(request.getContextPath() + "/accesso_negato.html");
            return;
        }

        int ordineId = 0;
        try {
            ordineId = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "ID ordine non valido fornito: " + request.getParameter("id"), e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID ordine non valido.");
            return;
        }

        try {
            Ordine ordine = ordineDAO.getOrdineById(ordineId);

            if (ordine == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Ordine non trovato.");
                return;
            }

            request.setAttribute("ordine", ordine);
            request.setAttribute("isAdminView", true); // Indica che è una vista admin
            request.getRequestDispatcher("/WEB-INF/jsp/dettaglioOrdine.jsp").forward(request, response); // Inoltra al JSP unico

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante il recupero del dettaglio ordine per admin: " + ordineId, e);
            request.setAttribute("errore", "Errore durante il recupero del dettaglio ordine.");
            request.getRequestDispatcher("/WEB-INF/jsp/admin/erroreAdmin.jsp").forward(request, response);
        }
    }

    private boolean isAdminOrVenditore(Utente utente) {
        // Implementa la tua logica qui. Esempio:
        return utente != null && ("ADMIN".equals(utente.getRuoli()) || "VENDITORE".equals(utente.getRuoli()));
        // Assicurati che la tua classe Utente abbia un metodo getRuolo() o simile.
    }
}