package admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import la_teca_del_giardiniere.DAO.PianteDAO;
import util.ServletUtils;

@WebServlet("/admin/EliminaPiantaServlet")
public class EliminaPiantaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(EliminaPiantaServlet.class.getName());

    private PianteDAO pianteDAO;

    public EliminaPiantaServlet() {
        super();
    }

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            pianteDAO = new PianteDAO();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "ERRORE CRITICO: Impossibile inizializzare PianteDAO in EliminaPiantaServlet.", e);
            throw new ServletException("Errore di inizializzazione del database.", e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // L'eliminazione tramite GET è meno sicura, ma comune per azioni semplici con conferma client-side.
        // Puoi considerare di forzare un POST per maggiore sicurezza (es. tramite un form nascosto con token CSRF).
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!ServletUtils.checkAuthenticationAndAuthorization(request, response)) {
            return;
        }

        String contextPath = request.getContextPath();
        String idStr = request.getParameter("id");

        if (idStr == null || idStr.trim().isEmpty()) {
            request.getSession().setAttribute("messaggio", "ID pianta non specificato per l'eliminazione.");
            request.getSession().setAttribute("tipoMessaggio", "error");
            LOGGER.log(Level.WARNING, "Tentativo di eliminazione senza ID specificato.");
            response.sendRedirect(contextPath + "/admin/ListaPianteServlet");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            pianteDAO.eliminaPianta(id);
            request.getSession().setAttribute("messaggio", "Pianta eliminata con successo!");
            request.getSession().setAttribute("tipoMessaggio", "success");
            LOGGER.info("Pianta con ID " + id + " eliminata con successo.");
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "ID pianta non valido per l'eliminazione: " + idStr, e);
            request.getSession().setAttribute("messaggio", "ID pianta non valido: " + idStr);
            request.getSession().setAttribute("tipoMessaggio", "error");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'eliminazione della pianta con ID: " + idStr, e);
            if (e.getSQLState() != null && e.getSQLState().startsWith("23")) { // SQLSTATE per violazione di integrità
                request.getSession().setAttribute("messaggio", "Impossibile eliminare la pianta. Ci sono ordini o altre entità che dipendono da questa pianta.");
            } else {
                request.getSession().setAttribute("messaggio", "Errore del database durante l'eliminazione: " + e.getMessage());
            }
            request.getSession().setAttribute("tipoMessaggio", "error");
        }
        response.sendRedirect(contextPath + "/admin/ListaPianteServlet");
    }
}