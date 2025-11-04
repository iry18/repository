package la_teca_del_giardiniere;

import la_teca_del_giardiniere.DAO.PianteDAO;
import la_teca_del_giardiniere.DAO.AccessoriDAO; 
import la_teca_del_giardiniere.classes.Piante;
import la_teca_del_giardiniere.classes.Accessori; 

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

@WebServlet({"/catalogo-piante", "/catalogo-accessori", "/catalogo"})
public class CatalogoServlet extends HttpServlet { 
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(CatalogoServlet.class.getName());
    private PianteDAO pianteDAO;
    private AccessoriDAO accessoriDAO; 

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            pianteDAO = new PianteDAO();
            accessoriDAO = new AccessoriDAO(); 
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "ERRORE CRITICO: Impossibile inizializzare DAO.", e);
            pianteDAO = null;
            accessoriDAO = null;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        
        if (pianteDAO == null || accessoriDAO == null) {
            LOGGER.severe("DAO non disponibile. Errore di inizializzazione.");
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore di configurazione del server.");
            return;
        }

        String tipo = request.getParameter("tipo");
        String searchQuery = request.getParameter("query");
        String requestUri = request.getRequestURI();

        // Variabili dinamiche per la JSP
        List<Object> listaRisultati = null;
        String titoloCatalogo = "Catalogo Generale Completo";
        String descrizioneCatalogo = "Esplora tutte le nostre piante e i nostri accessori.";

        try {
            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                // =========================================================
                // CASO 1: RICERCA UNIVERSALE (DA HEADER)
                // =========================================================
                LOGGER.info("Ricerca universale per query: " + searchQuery);

                List<Piante> risultatiPiante = pianteDAO.getPianteByNomeComune(searchQuery);
                List<Accessori> risultatiAccessori = accessoriDAO.searchByQuery(searchQuery);

                listaRisultati = new ArrayList<>();
                listaRisultati.addAll(risultatiPiante);
                listaRisultati.addAll(risultatiAccessori);

                titoloCatalogo = "Risultati di Ricerca per: \"" + searchQuery + "\"";
                descrizioneCatalogo = "Abbiamo trovato " + listaRisultati.size() + " prodotti tra piante e accessori.";

            } else if ("interno".equalsIgnoreCase(tipo)) {
                // =========================================================
                // CASO 2: FILTRO PIANTE DA INTERNO
                // =========================================================
                LOGGER.info("Caricamento catalogo piante da interno.");
                listaRisultati = new ArrayList<>(pianteDAO.getPianteByTipo("interno"));
                titoloCatalogo = "Piante da Appartamento";
                descrizioneCatalogo = "Scopri la nostra selezione di piante facilissime da curare e con qualità uniche per la casa e la propria salute.";


            } else if ("esterno".equalsIgnoreCase(tipo)) {
                // =========================================================
                // CASO 3: FILTRO PIANTE DA ESTERNO
                // =========================================================
                LOGGER.info("Caricamento catalogo piante da esterno.");
                listaRisultati = new ArrayList<>(pianteDAO.getPianteByTipo("esterno"));
                titoloCatalogo = "Piante da Esterno";
                descrizioneCatalogo = "Perfette per giardini, balconi e terrazzi. Resistenti al clima esterno.";

            } else if ("accessori".equalsIgnoreCase(tipo) || requestUri.endsWith("/catalogo-accessori")) {
                // =========================================================
                // CASO 4: SOLO ACCESSORI (NUOVA LOGICA)
                // =========================================================
                LOGGER.info("Caricamento catalogo Accessori.");
                listaRisultati = new ArrayList<>(accessoriDAO.getAllAccessori());
                titoloCatalogo = "Accessori per il Giardinaggio";
                descrizioneCatalogo = "Vasi, attrezzi e tutto il necessario per curare le tue piante.";

            } else {
                // =========================================================
                // CASO 5: DEFAULT - Catalogo Generale Completo (Piante + Accessori)
                // =========================================================
                LOGGER.info("Caricamento del catalogo generale (Piante + Accessori).");

                List<Piante> tutteLePiante = pianteDAO.getAllPiante();
                List<Accessori> tuttiGliAccessori = accessoriDAO.getAllAccessori();

                listaRisultati = new ArrayList<>();
                listaRisultati.addAll(tutteLePiante);
                listaRisultati.addAll(tuttiGliAccessori);

                titoloCatalogo = "Catalogo Generale Completo";
                descrizioneCatalogo = "Scopri l'intera gamma di prodotti, incluse tutte le piante e gli accessori.";
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante il recupero dei prodotti.", e);
            request.setAttribute("errorMessage", "Errore del database. Riprova più tardi.");
            listaRisultati = new ArrayList<>();
        }

        // Imposta gli attributi dinamici per la JSP
        request.setAttribute("titoloCatalogo", titoloCatalogo);
        request.setAttribute("descrizioneCatalogo", descrizioneCatalogo);
        request.setAttribute("listaProdotti", listaRisultati); 
        request.getRequestDispatcher("/CatalogoUnificato.jsp").forward(request, response);
    }
}