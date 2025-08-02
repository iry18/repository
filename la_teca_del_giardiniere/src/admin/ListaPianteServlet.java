package admin;

import la_teca_del_giardiniere.DAO.PianteDAO;
import la_teca_del_giardiniere.classes.Piante;
import util.ServletUtils;

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
import javax.servlet.http.HttpSession;

@WebServlet("/admin/ListaPianteServlet")
public class ListaPianteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ListaPianteServlet.class.getName());

    private PianteDAO pianteDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            pianteDAO = new PianteDAO();
            LOGGER.info("PianteDAO inizializzato con successo.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "ERRORE CRITICO: Impossibile inizializzare PianteDAO.", e);
            throw new ServletException("Errore di inizializzazione del database.", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Controllo autenticazione e autorizzazione
        if (!ServletUtils.checkAuthenticationAndAuthorization(request, response)) {
            return;
        }

        // Verifica che PianteDAO sia inizializzato
        if (pianteDAO == null) {
            LOGGER.severe("PianteDAO non è stato inizializzato.");
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Errore interno del server: DAO non disponibile.");
            return;
        }

        handleList(request, response);
    }

    private void handleList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Piante> listaPiante;
            String query = request.getParameter("query");

            // Gestione messaggi dalla sessione (es. dopo un'eliminazione)
            HttpSession session = request.getSession(false);
            if (session != null) {
                String messaggio = (String) session.getAttribute("messaggio");
                String tipoMessaggio = (String) session.getAttribute("tipoMessaggio");
                if (messaggio != null) {
                    request.setAttribute("messaggio", messaggio);
                    request.setAttribute("tipoMessaggio", tipoMessaggio);
                    session.removeAttribute("messaggio");
                    session.removeAttribute("tipoMessaggio");
                }
            }

            // Esecuzione della ricerca o recupero di tutte le piante
            if (query != null && !query.trim().isEmpty()) {
                listaPiante = pianteDAO.getPianteByNomeComune(query.trim());
                LOGGER.info("Risultati ricerca per: " + query + " | Piante trovate: " + listaPiante.size());
                if (listaPiante.isEmpty()) {
                    request.setAttribute("messaggio", "Nessun risultato trovato per: \"" + query + "\"");
                    request.setAttribute("tipoMessaggio", "error");
                } else {
                    request.setAttribute("messaggio", "Risultati per: \"" + query + "\"");
                    request.setAttribute("tipoMessaggio", "success");
                }
            } else {
                listaPiante = pianteDAO.getAllPiante();
                LOGGER.info("Recuperate tutte le piante: " + listaPiante.size());
            }

            request.setAttribute("listaPiante", listaPiante);
            request.getRequestDispatcher("/admin/ListaPiante.jsp").forward(request, response);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante il caricamento o la ricerca delle piante.", e);
            request.setAttribute("messaggio", "Errore durante il caricamento delle piante: " + e.getMessage());
            request.setAttribute("tipoMessaggio", "error");
            request.setAttribute("listaPiante", new ArrayList<>());
            request.getRequestDispatcher("/admin/ListaPiante.jsp").forward(request, response);
        }
    }
}