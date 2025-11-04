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
import javax.servlet.http.HttpSession;

import la_teca_del_giardiniere.classes.Utente;
import la_teca_del_giardiniere.DAO.UtenteDAO;

@WebServlet("/admin/utentiServlet")
public class UtenteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(UtenteServlet.class.getName());
    private UtenteDAO utenteDAO;

    public UtenteServlet() throws ServletException {
        super();
        try {
            utenteDAO = new UtenteDAO();
            LOGGER.info("UtenteDAO inizializzato con successo in UtenteServlet.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'inizializzazione di UtenteDAO in UtenteServlet.", e);
            throw new ServletException("Errore durante l'inizializzazione del DAO: " + e.getMessage(), e);
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false); // getSession(false) per non creare una nuova sessione se non esiste

        Utente utenteLoggato = null;
        if (session != null) {
            utenteLoggato = (Utente) session.getAttribute("loggedInUser");
        }

        // Controllo se l'utente è loggato E se è un amministratore
        if (utenteLoggato != null && utenteLoggato.isAdmin()) { // Usa il metodo isAdmin() dell'oggetto Utente
            // L'utente è loggato ed è un amministratore
            try {
                List<Utente> listaUtenti = utenteDAO.getAllUtentiConRuoli();
                request.setAttribute("listaUtenti", listaUtenti);
                request.getRequestDispatcher("/admin/utenti.jsp").forward(request, response);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Errore SQL durante il recupero degli utenti.", e);
                request.setAttribute("messaggio", "Errore nel recupero degli utenti.");
                request.setAttribute("tipoMessaggio", "error");
                request.getRequestDispatcher("/admin/utenti.jsp").forward(request, response);
            }
        } else {
            // L'utente NON è loggato, O non è un amministratore
            LOGGER.warning("Accesso negato a /admin/utentiServlet. Utente loggato: " + (utenteLoggato != null ? utenteLoggato.getEmail() + " (Admin: " + utenteLoggato.isAdmin() + ")" : "Nessuno"));

            // Imposta un messaggio di errore nella sessione
            session.setAttribute("errorMessage", "Devi effettuare l'accesso come amministratore per accedere a questa risorsa.");

            // Reindirizza alla pagina di accesso negato o login
            response.sendRedirect(request.getContextPath() + "/accesso_negato.html");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response); // Per semplicità, gestisce anche il POST come GET per questa servlet
    }
}