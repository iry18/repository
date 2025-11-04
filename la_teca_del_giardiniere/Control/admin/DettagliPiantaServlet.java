package admin;

import la_teca_del_giardiniere.DAO.PianteDAO;
import la_teca_del_giardiniere.classes.Piante;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/dettagliPianta")
public class DettagliPiantaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(DettagliPiantaServlet.class.getName());

    private PianteDAO pianteDAO;

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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID della pianta non specificato.");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            Piante pianta = pianteDAO.getPiantaById(id);

            if (pianta != null) {
                request.setAttribute("pianta", pianta);
                request.getRequestDispatcher("/DettagliPianta.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Pianta non trovata.");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID della pianta non valido.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante il caricamento dei dettagli della pianta.", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore del database.");
        }
    }
}