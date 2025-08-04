package admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import la_teca_del_giardiniere.DAO.AccessoriDAO;
import la_teca_del_giardiniere.classes.Accessori;
import util.ServletUtils; // Assumendo che tu abbia questa utility per i controlli di autenticazione

@WebServlet("/admin/ListaAccessoriServlet")
public class ListaAccessoriServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ListaAccessoriServlet.class.getName());
    private AccessoriDAO accessoriDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            accessoriDAO = new AccessoriDAO();
            LOGGER.info("AccessoriDAO inizializzato con successo in ListaAccessoriServlet.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "ERRORE CRITICO: Impossibile inizializzare AccessoriDAO in ListaAccessoriServlet.", e);
            throw new ServletException("Errore di inizializzazione del database per gli accessori.", e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Controllo di autenticazione e autorizzazione come prima (ESSENZIALE per la sicurezza)
        if (!ServletUtils.checkAuthenticationAndAuthorization(request, response)) {
            return;
        }

        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            handleDelete(request, response);
        } else {
            handleList(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Metodo POST non supportato per questa risorsa. Utilizzare GET per list/delete.");
    }

    /**
     * Gestisce la visualizzazione della lista degli accessori.
     */
    private void handleList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Accessori> listaAccessori = accessoriDAO.getAllAccessori();
            request.setAttribute("listaAccessori", listaAccessori);

            // Recupera e pulisci i messaggi di sessione (CORRETTO)
            String messaggio = (String) request.getSession().getAttribute("messaggio");
            String tipoMessaggio = (String) request.getSession().getAttribute("tipoMessaggio");

            if (messaggio != null) {
                request.setAttribute("messaggio", messaggio);
                request.setAttribute("tipoMessaggio", tipoMessaggio);
                request.getSession().removeAttribute("messaggio");
                request.getSession().removeAttribute("tipoMessaggio");
            }
            LOGGER.info("Visualizzazione lista accessori. Numero di accessori: " + listaAccessori.size());
            request.getRequestDispatcher("/admin/ListaAccessori.jsp").forward(request, response);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL nel recupero della lista accessori.", e);
            // Reindirizza o mostra un messaggio di errore all'utente
            request.getSession().setAttribute("messaggio", "Errore nel caricamento degli accessori: " + e.getMessage());
            request.getSession().setAttribute("tipoMessaggio", "error");
            response.sendRedirect(request.getContextPath() + "/admin/AdminHome.jsp"); // Reindirizza a una pagina admin home o simile in caso di errore critico
        }
    }

    /**
     * Gestisce l'eliminazione di un accessorio.
     */
    private void handleDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idAccessorio = 0;
        try {
            idAccessorio = Integer.parseInt(request.getParameter("id"));
            boolean deleted = accessoriDAO.deleteAccessori(idAccessorio);

            if (deleted) {
                request.getSession().setAttribute("messaggio", "Accessorio eliminato con successo!");
                request.getSession().setAttribute("tipoMessaggio", "success");
                LOGGER.info("Accessorio con ID " + idAccessorio + " eliminato con successo.");
            } else {
                request.getSession().setAttribute("messaggio", "Impossibile eliminare l'accessorio. Potrebbe non esistere o esserci un vincolo.");
                request.getSession().setAttribute("tipoMessaggio", "error");
                LOGGER.warning("Tentativo di eliminare un accessorio non esistente o con vincoli con ID: " + idAccessorio);
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "ID accessorio non valido per l'eliminazione.", e);
            request.getSession().setAttribute("messaggio", "ID accessorio non valido per l'eliminazione.");
            request.getSession().setAttribute("tipoMessaggio", "error");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'eliminazione dell'accessorio con ID: " + idAccessorio, e);
            request.getSession().setAttribute("messaggio", "Errore database durante l'eliminazione: " + e.getMessage());
            request.getSession().setAttribute("tipoMessaggio", "error");
        }
        response.sendRedirect(request.getContextPath() + "/admin/ListaAccessoriServlet"); // Reindirizza sempre per aggiornare la lista
    }
}