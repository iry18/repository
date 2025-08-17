package admin;

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

@WebServlet("/homepage") // Mappa questa servlet all'URL /homepage
public class HomepageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(HomepageServlet.class.getName());

    private PianteDAO pianteDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            pianteDAO = new PianteDAO();
            LOGGER.info("PianteDAO inizializzato con successo in HomepageServlet.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "ERRORE CRITICO: Impossibile inizializzare PianteDAO.", e);
            throw new ServletException("Errore di inizializzazione del database.", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Piante> listaPiante = null;
        int numeroPianteInEvidenza = 4; // Imposta il numero desiderato

        try {
            // metodo nel DAo piante in evidenza
            listaPiante = pianteDAO.getPianteInEvidenza(numeroPianteInEvidenza);
            request.setAttribute("listaPianteInEvidenza", listaPiante);
        } catch (SQLException e) {
            // Gestione degli errori...
        } finally {
            request.getRequestDispatcher("/homepage.jsp").forward(request, response);
        }
    }
}