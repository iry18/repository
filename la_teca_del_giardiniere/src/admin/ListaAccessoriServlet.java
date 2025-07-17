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
import util.ServletUtils; // Assuming you have this utility for auth checks

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
        if (!ServletUtils.checkAuthenticationAndAuthorization(request, response)) {
            return;
        }

        String action = request.getParameter("action");
        try {
            if ("delete".equals(action)) {
                deleteAccessorio(request, response);
            } else {
                listAccessori(request, response);
            }
        } catch (SQLException ex) {
        	  LOGGER.log(Level.SEVERE, "Errore SQL in ListaAccessoriServlet durante l'azione.", ex);
              request.getSession().setAttribute("messaggio", "Errore database: " + ex.getMessage());
              request.getSession().setAttribute("tipoMessaggio", "error");
              response.sendRedirect(request.getContextPath() + "/admin/ListaAccessoriServlet");
          }
    }

   @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Metodo POST non supportato per questa risorsa. Utilizzare GET per list/delete.");
    }

    private void listAccessori(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        List<Accessori> listaAccessori = accessoriDAO.getAllAccessori();
        request.setAttribute("listaAccessori", listaAccessori);

        String messaggio = (String) request.getSession().getAttribute("messaggio");
        String tipoMessaggio = (String) request.getSession().getAttribute("tipoMessaggio");
        if (messaggio != null) {
            request.setAttribute("messaggio", messaggio);
            request.setAttribute("tipoMessaggio", tipoMessaggio);
            request.getSession().removeAttribute("messaggio"); // Rimuove il messaggio dalla sessione dopo l'uso
            request.getSession().removeAttribute("tipoMessaggio");
        }
        LOGGER.info("Visualizzazione lista accessori. Numero di accessori: " + listaAccessori.size());
        request.getRequestDispatcher("/WEB-INF/views/admin/ListaAccessori.jsp").forward(request, response);
    }

    private void deleteAccessorio(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            // Qui assumiamo che AccessoriDAO.deleteAccessori restituisca un boolean
            boolean deleted = accessoriDAO.deleteAccessori(id);

            if(deleted) {
                request.getSession().setAttribute("messaggio", "Accessorio eliminato con successo!");
                request.getSession().setAttribute("tipoMessaggio", "success");
                LOGGER.info("Accessorio con ID " + id + " eliminato con successo.");
            } else {
                request.getSession().setAttribute("messaggio", "Impossibile eliminare l'accessorio. Potrebbe non esistere o esserci un vincolo.");
                request.getSession().setAttribute("tipoMessaggio", "error");
                LOGGER.warning("Tentativo di eliminare un accessorio non esistente o con vincoli con ID: " + id);
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "ID accessorio non valido per l'eliminazione.", e);
            request.getSession().setAttribute("messaggio", "ID accessorio non valido per l'eliminazione.");
            request.getSession().setAttribute("tipoMessaggio", "error");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'eliminazione dell'accessorio con ID: " + request.getParameter("id"), e); // Logga l'ID
            request.getSession().setAttribute("messaggio", "Errore database durante l'eliminazione: " + e.getMessage());
            request.getSession().setAttribute("tipoMessaggio", "error");
        }
        response.sendRedirect(request.getContextPath() + "/admin/ListaAccessoriServlet");
    }
}