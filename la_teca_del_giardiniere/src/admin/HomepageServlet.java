package admin;

import la_teca_del_giardiniere.DAO.AccessoriDAO;
import la_teca_del_giardiniere.DAO.PianteDAO;
import la_teca_del_giardiniere.classes.Accessori;
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

@WebServlet("/homepage") // Mappa questa servlet all'URL /homepage
public class HomepageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(HomepageServlet.class.getName());

    private PianteDAO pianteDAO;
    private AccessoriDAO accessoriDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            pianteDAO = new PianteDAO();
            accessoriDAO = new AccessoriDAO();
            LOGGER.info("PianteDAO inizializzato con successo in HomepageServlet.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "ERRORE CRITICO: Impossibile inizializzare PianteDAO.", e);
            throw new ServletException("Errore di inizializzazione del database.", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Piante> listaPianteInEvidenza = null;
        List<Accessori> listaAccessoriInEvidenza = null;
        List<Piante> listaPianteDaEsterno = null;

        int numeroEvidenza = 4; // Numero di prodotti da mostrare per categoria

        try {
            // Recupera le piante in evidenza
            listaPianteInEvidenza = pianteDAO.getPianteInEvidenza(numeroEvidenza);
            request.setAttribute("listaPianteInEvidenza", listaPianteInEvidenza);

            // Recupera gli accessori in evidenza
            listaAccessoriInEvidenza = accessoriDAO.getAccessoriInEvidenza(numeroEvidenza);
            request.setAttribute("listaAccessoriInEvidenza", listaAccessoriInEvidenza);

            // Recupera le piante da esterno
            listaPianteDaEsterno = pianteDAO.getPianteByTipo("esterno");
            request.setAttribute("listaPianteDaEsterno", listaPianteDaEsterno);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore nel recupero dei dati per la homepage.", e);
            request.setAttribute("errore", "Si è verificato un errore nel caricamento dei dati.");
            // Non reindirizzare qui, lascia che il dispatcher gestisca la pagina di errore
        } finally {
            request.getRequestDispatcher("/homepage.jsp").forward(request, response);
        }
    }
}