package la_teca_del_giardiniere;

import la_teca_del_giardiniere.DAO.PianteDAO;
import la_teca_del_giardiniere.classes.Piante;

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

@WebServlet("/catalogo-piante")
public class CatalogoPianteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(CatalogoPianteServlet.class.getName());
    private PianteDAO pianteDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        try {
            pianteDAO = new PianteDAO();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "ERRORE CRITICO: Impossibile inizializzare PianteDAO.", e);
            pianteDAO = null;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        if (pianteDAO == null) {
            LOGGER.severe("DAO unavailable. Initialization error.");
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server configuration error.");
            return;
        }

        String tipo = request.getParameter("tipo");
        String searchQuery = request.getParameter("query");

        List<Piante> listaPiante = null;
        String destinazioneJSP = "/CatalogoPiante.jsp";

        try {
            if (tipo != null && !tipo.trim().isEmpty() && searchQuery != null && !searchQuery.trim().isEmpty()) {
                LOGGER.info("Ricerca per query e tipo: " + searchQuery + ", " + tipo);
            } else if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                // Caso: solo ricerca per nome (su tutte le piante)
                listaPiante = pianteDAO.getPianteByNomeComune(searchQuery);
                LOGGER.info("Ricerca per query: " + searchQuery);
            } else if ("interno".equalsIgnoreCase(tipo)) {
                // Caso: solo filtro per tipo "interno"
                listaPiante = pianteDAO.getPianteByTipo("interno");
                destinazioneJSP = "/Piantedainterno.jsp";
            } else if ("esterno".equalsIgnoreCase(tipo)) {
                // Caso: solo filtro per tipo "esterno"
                listaPiante = pianteDAO.getPianteByTipo("esterno");
                destinazioneJSP = "/Piantedaesterno.jsp";
            } else {
                // Caso: recupera tutte le piante (nessun filtro)
                listaPiante = pianteDAO.getAllPiante();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante il recupero delle piante.", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore del database.");
            return;
        }

        request.setAttribute("listaPiante", listaPiante);
        request.getRequestDispatcher(destinazioneJSP).forward(request, response);
    }
  }