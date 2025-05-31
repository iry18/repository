package src.com.la_teca_del_giardiniere;

import src.com.la_teca_del_giardiniere.classes.Ordine;
import src.com.la_teca_del_giardiniere.classes.Utente;
import src.com.la_teca_del_giardiniere.dao.OrdineDAO;

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

@WebServlet("/dettaglioOrdineUtente")
public class DettaglioOrdineUtenteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(DettaglioOrdineUtenteServlet.class.getName());

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

        if (utente == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int ordineId = 0;
        try {
            ordineId = Integer.parseInt(request.getParameter("id")); // L'ID dell'ordine viene passato come parametro
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "ID ordine non valido fornito: " + request.getParameter("id"), e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID ordine non valido.");
            return;
        }

        try {
            // Aggiungi un metodo getOrdineById nel tuo OrdineDAO
            // Questo metodo dovrebbe anche recuperare i dettagli dell'ordine
            Ordine ordine = ordineDAO.getOrdineById(ordineId);

            if (ordine == null || ordine.getUtenteId() != utente.getId()) {
                // Se l'ordine non esiste o non appartiene all'utente corrente
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accesso non autorizzato all'ordine.");
                return;
            }

            request.setAttribute("ordine", ordine);
            request.getRequestDispatcher("/WEB-INF/jsp/dettaglioOrdineUtente.jsp").forward(request, response);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante il recupero del dettaglio ordine: " + ordineId, e);
            request.setAttribute("errore", "Errore durante il recupero del dettaglio ordine.");
            request.getRequestDispatcher("/WEB-INF/jsp/errore.jsp").forward(request, response);
        }
    }
}