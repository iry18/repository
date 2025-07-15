package la_teca_del_giardiniere;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import la_teca_del_giardiniere.DAO.PianteDAO;
import la_teca_del_giardiniere.classes.Utente;

@WebServlet("/EliminaPiantaServlet")
public class EliminaPiantaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(EliminaPiantaServlet.class.getName());

    private PianteDAO pianteDAO;

    public EliminaPiantaServlet() {
        super();
        try {
            pianteDAO = new PianteDAO();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante l'inizializzazione del PianteDAO in EliminaPiantaServlet.", e);
            // Considera di lanciare una ServletException se l'inizializzazione fallisce
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contextPath = request.getContextPath();
        HttpSession session = request.getSession(false);

        // 1. Controllo Autenticazione e Autorizzazione
        if (session == null || session.getAttribute("loggedInUser") == null) {
            LOGGER.info("Tentativo di accesso non autenticato a EliminaPiantaServlet. Reindirizzamento a login.jsp");
            response.sendRedirect(contextPath + "/login.jsp");
            return;
        }

        Utente utenteLoggato = (Utente) session.getAttribute("loggedInUser");
        if (utenteLoggato == null || (!utenteLoggato.isAdmin() && !utenteLoggato.getRuoli().contains("venditore"))) {
            LOGGER.log(Level.WARNING, "Utente non autorizzato ({0}) ha tentato di eliminare una pianta. Reindirizzamento ad accesso_negato.html",
                    utenteLoggato != null ? utenteLoggato.getEmail() : "sconosciuto");
            response.sendRedirect(contextPath + "/accesso_negato.html");
            return;
        }

        // 2. Recupera l'ID della pianta da eliminare
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            LOGGER.warning("Tentativo di eliminazione senza ID pianta fornito.");
            request.getSession().setAttribute("messaggio", "ID pianta non fornito per l'eliminazione.");
            request.getSession().setAttribute("tipoMessaggio", "error");
            response.sendRedirect(contextPath + "/ListaPianteServlet");
            return;
        }

        try {
            int idPianta = Integer.parseInt(idStr);
            pianteDAO.eliminaPianta(idPianta); // Chiama il metodo DAO per l'eliminazione
            LOGGER.log(Level.INFO, "Pianta con ID {0} eliminata con successo.", idPianta);
            request.getSession().setAttribute("messaggio", "Pianta eliminata con successo.");
            request.getSession().setAttribute("tipoMessaggio", "success");
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "ID pianta non valido per l'eliminazione: {0}", idStr);
            request.getSession().setAttribute("messaggio", "ID pianta non valido per l'eliminazione.");
            request.getSession().setAttribute("tipoMessaggio", "error");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'eliminazione della pianta con ID: " + idStr, e);
            request.getSession().setAttribute("messaggio", "Errore durante l'eliminazione della pianta: " + e.getMessage());
            request.getSession().setAttribute("tipoMessaggio", "error");
            // TODO: Gestire il vincolo di integrità referenziale in modo più user-friendly
            // Ad esempio, "Non è possibile eliminare questa pianta perché è associata a degli ordini."
        }

        // Reindirizza sempre alla lista delle piante dopo l'operazione (PRG pattern)
        response.sendRedirect(contextPath + "/ListaPianteServlet");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Le eliminazioni dovrebbero idealmente essere gestite tramite POST per il PRG pattern
        // Per semplicità e coerenza con i link GET, useremo doGet, ma una POST è più robusta.
        // Se usi POST, il link di eliminazione nel JSP dovrebbe essere un form con method="post".
        doGet(request, response);
    }
}