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

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (pianteDAO == null) {
            LOGGER.severe("DAO non disponibile. Errore di inizializzazione.");
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore di configurazione del server.");
            return;
        }

        String tipo = request.getParameter("tipo");
        LOGGER.info("Richiesta per tipo: " + tipo);
        String destinazioneJSP;

        try {
            List<Piante> listaPiante = null;
            if ("interno".equalsIgnoreCase(tipo)) {
                listaPiante = pianteDAO.getPianteByTipo("interno");
                destinazioneJSP = "/Piantedainterno.jsp";
            } else if ("esterno".equalsIgnoreCase(tipo)) {
                listaPiante = pianteDAO.getPianteByTipo("esterno");
                destinazioneJSP = "/Piantedaesterno.jsp";
            } else {
                LOGGER.warning("Tipo di pianta non valido richiesto: " + tipo + ". Reindirizzamento al catalogo completo.");
                listaPiante = pianteDAO.getAllPiante();
                destinazioneJSP = "/CatalogoPiante.jsp"; 
            }

            // Logga il numero di piante trovate
            LOGGER.info("Trovate " + (listaPiante != null ? listaPiante.size() : 0) + " piante per il tipo: " + tipo);
            LOGGER.info("Reindirizzamento a: " + destinazioneJSP);

            request.setAttribute("listaPiante", listaPiante);
            request.getRequestDispatcher(destinazioneJSP).forward(request, response);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante il recupero delle piante.", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore del database.");
        }
    }
}