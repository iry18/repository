package la_teca_del_giardiniere;

import la_teca_del_giardiniere.DAO.PianteDAO;
import la_teca_del_giardiniere.classes.Piante;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
    private PianteDAO pianteDAO;
    private static final Logger LOGGER = Logger.getLogger(CatalogoPianteServlet.class.getName());

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            pianteDAO = new PianteDAO();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "ERRORE CRITICO: Impossibile inizializzare PianteDAO.", e);
            throw new ServletException("Errore di inizializzazione del database.", e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String tipo = request.getParameter("tipo");
        List<Piante> listaPiante = new ArrayList<>();

        if (pianteDAO == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "DAO non disponibile.");
            return;
        }

        try {
            if (tipo != null && !tipo.trim().isEmpty()) {
                listaPiante = pianteDAO.getPianteByTipo(tipo);
                request.setAttribute("listaPiante", listaPiante);

                if ("interno".equalsIgnoreCase(tipo)) {
                    request.getRequestDispatcher("/Piantedainterno.jsp").forward(request, response);
                } else if ("esterno".equalsIgnoreCase(tipo)) {
                    request.getRequestDispatcher("/Piantedaesterno.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Categoria non valida.");
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parametro 'categoria' mancante.");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante il recupero delle piante.", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore del database.");
        }
    }
}