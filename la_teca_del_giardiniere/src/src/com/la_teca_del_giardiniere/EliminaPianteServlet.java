package src.com.la_teca_del_giardiniere;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger; // Per un logging più robusto

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import src.com.la_teca_del_giardiniere.classes.Utente; // Import per la classe Utente
import src.com.la_teca_del_giardiniere.dao.PianteDAO;

// Ho rinominato la servlet per seguire le convenzioni di denominazione (PascalCase)
@WebServlet("/EliminaPianteServlet")
public class EliminaPianteServlet extends HttpServlet { // CAMBIATO: eliminapianteServlet a EliminaPianteServlet
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(EliminaPianteServlet.class.getName()); // Logger

    private PianteDAO pianteDAO;

    public EliminaPianteServlet() {
        super();
        try {
            pianteDAO = new PianteDAO();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante l'inizializzazione del PianteDAO in EliminaPianteServlet.", e);
            // In un'applicazione reale, potresti voler lanciare una ServletException
            // o reindirizzare a una pagina di errore grave all'avvio.
            // throw new ServletException("Errore critico durante l'inizializzazione della servlet.", e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false); // Non creare una nuova sessione se non esiste
        String contextPath = request.getContextPath(); // Per URL assoluti

        // 1. Controllo Autenticazione
        if (session == null || session.getAttribute("loggedInUser") == null) {
            LOGGER.info("Tentativo di accesso non autenticato a EliminaPianteServlet. Reindirizzamento a login.jsp");
            response.sendRedirect(contextPath + "/login.jsp");
            return;
        }

        // 2. Controllo Autorizzazione (solo amministratori o venditori)
        Utente utenteLoggato = (Utente) session.getAttribute("loggedInUser");
        // Ho rimosso il controllo complesso sui ruoli dalla sessione, usando direttamente il metodo isAdmin() e getRuoli() dell'oggetto Utente
        if (utenteLoggato == null || (!utenteLoggato.isAdmin() && !utenteLoggato.getRuoli().contains("venditore"))) {
            LOGGER.log(Level.WARNING, "Utente non autorizzato ({0}) ha tentato di eliminare una pianta. Reindirizzamento ad accesso_negato.html",
                    utenteLoggato != null ? utenteLoggato.getEmail() : "sconosciuto");
            response.sendRedirect(contextPath + "/accesso_negato.html");
            return;
        }

        // 3. Recupero e validazione dell'ID della pianta
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            LOGGER.warning("Tentativo di eliminazione senza ID pianta fornito.");
            request.setAttribute("messaggio", "ID della pianta non fornito per l'eliminazione.");
            request.setAttribute("tipoMessaggio", "error");
        } else {
            try {
                int idPianta = Integer.parseInt(idStr);
                try {
                    pianteDAO.eliminaPianta(idPianta);
                    LOGGER.log(Level.INFO, "Pianta con ID {0} eliminata con successo.", idPianta);
                    request.setAttribute("messaggio", "Pianta eliminata con successo.");
                    request.setAttribute("tipoMessaggio", "success");
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Errore SQL durante l'eliminazione della pianta con ID: " + idPianta, e);
                    String errorMessage;
                    // Controllo specifico per violazione di vincolo di integrità referenziale (tipico di MySQL)
                    if (e.getMessage() != null && e.getMessage().contains("Cannot delete or update a parent row: a foreign key constraint fails")) {
                        errorMessage = "Impossibile eliminare la pianta: è associata ad altri dati (es. ordini o dettagli ordini).";
                    } else {
                        errorMessage = "Errore durante l'eliminazione della pianta: " + e.getMessage();
                    }
                    request.setAttribute("messaggio", errorMessage);
                    request.setAttribute("tipoMessaggio", "error");
                }
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "ID pianta non valido fornito per l'eliminazione: {0}", idStr);
                request.setAttribute("messaggio", "L'ID della pianta fornito non è in un formato numerico valido.");
                request.setAttribute("tipoMessaggio", "error");
            }
        }

        // Reindirizza sempre alla lista delle piante dopo aver tentato l'eliminazione
        // I messaggi di successo/errore verranno visualizzati sulla pagina della lista (listapiante.jsp)
        // che la listapianteServlet preleverà dagli attributi di request.
        response.sendRedirect(contextPath + "/ListaPianteServlet"); // CAMBIATO: listapianteServlet a ListaPianteServlet
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // L'eliminazione tramite link (che è una richiesta GET) è gestita da doGet.
        // Se un form submit dovesse arrivare qui (anche se non è lo scenario tipico per l'eliminazione),
        // reindirizziamo a doGet per centralizzare la logica.
        doGet(request, response);
    }
}