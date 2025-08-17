package admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession; // Importa HttpSession

import la_teca_del_giardiniere.DAO.PianteDAO;
import util.ServletUtils;

@WebServlet("/admin/EliminaPiantaServlet")
public class EliminaPiantaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(EliminaPiantaServlet.class.getName());

    private PianteDAO pianteDAO;

    // Il costruttore non ha bisogno di modifiche significative se chiami super()
    public EliminaPiantaServlet() {
        super();
    }

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            pianteDAO = new PianteDAO();
            LOGGER.info("PianteDAO inizializzato con successo in EliminaPiantaServlet.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "ERRORE CRITICO: Impossibile inizializzare PianteDAO in EliminaPiantaServlet.", e);
            throw new ServletException("Errore di inizializzazione del database.", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // È corretto non supportare l'eliminazione diretta tramite GET per motivi di sicurezza.
        // Reindirizza l'utente e mostra un messaggio.
        LOGGER.warning("Tentativo di accesso diretto a EliminaPiantaServlet tramite GET. Metodo non consentito per l'eliminazione.");
        
        HttpSession session = request.getSession(); // Ottieni la sessione
        session.setAttribute("messaggio", "Operazione di eliminazione non consentita tramite link diretto. Utilizzare il pulsante Elimina.");
        session.setAttribute("tipoMessaggio", "error");
        
        response.sendRedirect(request.getContextPath() + "/admin/ListaPianteServlet");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Controllo autenticazione e autorizzazione prima di procedere con l'eliminazione
        if (!ServletUtils.checkAuthenticationAndAuthorization(request, response)) {
            return; // Se non autenticato/autorizzato, ServletUtils gestisce il reindirizzamento.
        }

        String contextPath = request.getContextPath();
        String idStr = request.getParameter("id"); // Recupera l'ID dalla richiesta POST

        HttpSession session = request.getSession(); // Ottieni la sessione per i messaggi di feedback

        // Controllo per ID mancante o vuoto
        if (idStr == null || idStr.trim().isEmpty()) {
            session.setAttribute("messaggio", "ID pianta non specificato per l'eliminazione.");
            session.setAttribute("tipoMessaggio", "error");
            LOGGER.log(Level.WARNING, "Tentativo di eliminazione senza ID specificato.");
            response.sendRedirect(contextPath + "/admin/ListaPianteServlet");
            return;
        }

        try {
            int id = Integer.parseInt(idStr); // Converti l'ID da String a int
            
            // ⭐ MODIFICA QUI: Chiama il metodo deletePianta che restituisce boolean
            boolean deleted = pianteDAO.deletePianta(id); 

            if (deleted) {
                session.setAttribute("messaggio", "Pianta eliminata con successo!");
                session.setAttribute("tipoMessaggio", "success");
                LOGGER.info("Pianta con ID " + id + " eliminata con successo.");
            } else {
                // Se deleted è false, significa che la pianta non è stata trovata o eliminata
                session.setAttribute("messaggio", "Impossibile eliminare la pianta. Potrebbe non esistere o non è stata trovata.");
                session.setAttribute("tipoMessaggio", "error");
                LOGGER.log(Level.WARNING, "Tentativo di eliminazione di una pianta inesistente o non eliminabile. ID: " + id);
            }

        } catch (NumberFormatException e) {
            // Gestione dell'errore se l'ID non è un numero valido
            LOGGER.log(Level.WARNING, "ID pianta non valido per l'eliminazione: " + idStr, e);
            session.setAttribute("messaggio", "ID pianta non valido: " + idStr);
            session.setAttribute("tipoMessaggio", "error");
        } catch (SQLException e) {
            // Gestione degli errori SQL, inclusa la violazione dell'integrità referenziale
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'eliminazione della pianta con ID: " + idStr, e);
            if (e.getSQLState() != null && e.getSQLState().startsWith("23")) { 
                session.setAttribute("messaggio", "Impossibile eliminare la pianta. Ci sono entità correlate (es. ordini, dettagli) che dipendono da questa pianta.");
            } else {
                session.setAttribute("messaggio", "Errore del database durante l'eliminazione: " + e.getMessage());
            }
            session.setAttribute("tipoMessaggio", "error");
        }
        
        // Reindirizza sempre a ListaPianteServlet per mostrare l'elenco aggiornato e il messaggio di feedback
        response.sendRedirect(contextPath + "/admin/ListaPianteServlet");
    }
}