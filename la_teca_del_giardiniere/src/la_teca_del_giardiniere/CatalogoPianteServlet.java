package la_teca_del_giardiniere;

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

import la_teca_del_giardiniere.DAO.PianteDAO; // Assicurati che il percorso sia corretto
import la_teca_del_giardiniere.classes.Piante; // Assicurati che il percorso sia corretto

@WebServlet("/catalogo") // L'URL a cui gli utenti accederanno, ad esempio http://localhost:8080/nome_app/catalogo
public class CatalogoPianteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(CatalogoPianteServlet.class.getName());

    private PianteDAO pianteDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            pianteDAO = new PianteDAO();
            LOGGER.info("PianteDAO inizializzato con successo per CatalogoPianteServlet.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "ERRORE CRITICO: Impossibile inizializzare PianteDAO in CatalogoPianteServlet.", e);
            throw new ServletException("Errore di inizializzazione del database per il catalogo.", e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Recupera tutte le piante dal database
            List<Piante> listaPiante = pianteDAO.getAllPiante();
            
            // Imposta la lista delle piante come attributo della richiesta
            request.setAttribute("listaPiante", listaPiante);
            
            LOGGER.info("Recuperate " + listaPiante.size() + " piante per il catalogo pubblico.");
            
            // Inoltra la richiesta alla JSP che visualizzerà il catalogo
            // È buona pratica mettere le JSP accessibili solo via Servlet in WEB-INF
            request.getRequestDispatcher("/WEB-INF/jsp/public/catalogoPiante.jsp").forward(request, response);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante il recupero del catalogo delle piante.", e);
            // In caso di errore, reindirizza a una pagina di errore o mostra un messaggio generico
            request.setAttribute("messaggioErrore", "Si è verificato un errore nel caricamento del catalogo. Riprova più tardi.");
            request.getRequestDispatcher("/WEB-INF/jsp/public/erroreCatalogo.jsp").forward(request, response); // O una pagina più generica
        }
    }

    // Per una pagina di visualizzazione, di solito non serve il doPost
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response); // O semplicemente non implementarlo se non ci sono form POST
    }
}