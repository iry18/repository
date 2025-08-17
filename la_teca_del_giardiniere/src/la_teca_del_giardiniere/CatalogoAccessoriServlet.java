package la_teca_del_giardiniere;

import la_teca_del_giardiniere.DAO.AccessoriDAO;
import la_teca_del_giardiniere.classes.Accessori;

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

@WebServlet("/catalogo-accessori") // Mappatura pubblica
public class CatalogoAccessoriServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(CatalogoAccessoriServlet.class.getName());
    private AccessoriDAO accessoriDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            accessoriDAO = new AccessoriDAO();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "ERRORE CRITICO: Impossibile inizializzare AccessoriDAO.", e);
            throw new ServletException("Errore di inizializzazione del database.", e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<Accessori> listaAccessori = accessoriDAO.getAllAccessori();
            request.setAttribute("listaAccessori", listaAccessori);
            LOGGER.info("Recuperati " + listaAccessori.size() + " accessori per il catalogo pubblico.");
            
            // Inoltra la richiesta alla JSP del catalogo pubblico
            request.getRequestDispatcher("/Accessori.jsp").forward(request, response);
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL nel recupero degli accessori per il catalogo.", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore del database.");
        }
    }
}