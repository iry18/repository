package admin; 

import la_teca_del_giardiniere.DAO.PianteDAO;
import la_teca_del_giardiniere.classes.Piante;
import util.ServletUtils; // Assicurati che questa classe esista e gestisca l'autenticazione/autorizzazione

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
            return; // Interrompe l'esecuzione se l'utente non è autorizzato
        }

        // Verifica che PianteDAO sia  inizializzato
        if (pianteDAO == null) {
            LOGGER.severe("PianteDAO non è stato inizializzato.");
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore interno del server: DAO non disponibile.");
            return;
        }

        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            handleDelete(request, response);
        } else {
            handleList(request, response);
        }
    }

    private void handleList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Piante> listaPiante = pianteDAO.getAllPiante();
            LOGGER.info("Numero di piante recuperate dal database: " + listaPiante.size());
            request.setAttribute("listaPiante", listaPiante);

            // Recupera messaggi da sessione, se presenti (per feedback dopo eliminazione/altre operazioni)
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
            request.getRequestDispatcher("/admin/ListaPiante.jsp").forward(request, response);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante il recupero delle piante.", e);
            request.setAttribute("messaggio", "Errore durante il caricamento delle piante: " + e.getMessage());
            request.setAttribute("tipoMessaggio", "error");
            request.setAttribute("listaPiante", new ArrayList<Piante>()); // Previene NullPointerException in JSP
            request.getRequestDispatcher("/admin/AdminHome.jsp").forward(request, response); // Reindirizza a una pagina di errore o home
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int idPianta = 0;
        try {
            idPianta = Integer.parseInt(request.getParameter("id"));
            // Chiama il metodo deletePianta dal tuo DAO
            boolean deleted = pianteDAO.deletePianta(idPianta);

            if (deleted) {
                request.getSession().setAttribute("messaggio", "Pianta eliminata con successo!");
                request.getSession().setAttribute("tipoMessaggio", "success");
            } else {
                request.getSession().setAttribute("messaggio", "Impossibile eliminare la pianta. Potrebbe non esistere.");
                request.getSession().setAttribute("tipoMessaggio", "error");
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "ID pianta non valido per l'eliminazione", e);
            request.getSession().setAttribute("messaggio", "ID pianta non valido.");
            request.getSession().setAttribute("tipoMessaggio", "error");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'eliminazione della pianta con ID: " + idPianta, e);
            request.getSession().setAttribute("messaggio", "Errore del database durante l'eliminazione: " + e.getMessage());
            request.getSession().setAttribute("tipoMessaggio", "error");
        }
        // Reindirizza alla pagina di elenco per aggiornare la visualizzazione e mostrare il messaggio
        response.sendRedirect(request.getContextPath() + "/admin/ListaPianteServlet");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                "Metodo POST non supportato per questa risorsa. Utilizzare GET per listare o eliminare.");
    }
}