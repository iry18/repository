package src.com.la_teca_del_giardiniere;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger; // Per un logging più robusto

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import src.com.la_teca_del_giardiniere.classes.Accessori; 
import src.com.la_teca_del_giardiniere.classes.Utente; 
import src.com.la_teca_del_giardiniere.dao.AccessoriDAO; 

@WebServlet("/admin/AccessoriServlet") 
public class AccessoriServlet extends HttpServlet { 
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(AccessoriServlet.class.getName()); 

    private AccessoriDAO accessoriDAO; 
    private boolean daoInitialized = false;

    public AccessoriServlet() {
        super();
        try {
            accessoriDAO = new AccessoriDAO(); // CAMBIATO: nome DAO
            daoInitialized = true;
            LOGGER.info("AccessoriServlet: AccessoriDAO inizializzato con successo.");
        } catch (SQLException e) {
            daoInitialized = false;
            LOGGER.log(Level.SEVERE, "CRITICO: Errore durante l'inizializzazione di AccessoriDAO in AccessoriServlet.", e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contextPath = request.getContextPath();
        HttpSession session = request.getSession(false); // Non creare una nuova sessione se non esiste

        // 1. Controllo Autenticazione
        if (session == null || session.getAttribute("loggedInUser") == null) {
            LOGGER.info("Tentativo di accesso non autenticato ad AccessoriServlet. Reindirizzamento a login.jsp");
            response.sendRedirect(contextPath + "/login.jsp");
            return;
        }

        // 2. Controllo Autorizzazione (solo amministratori o venditori)
        Utente utenteLoggato = (Utente) session.getAttribute("loggedInUser");
        if (utenteLoggato == null || (!utenteLoggato.isAdmin() && !utenteLoggato.getRuoli().contains("venditore"))) {
            LOGGER.log(Level.WARNING, "Utente non autorizzato ({0}) ha tentato di accedere alla gestione accessori. Reindirizzamento ad accesso_negato.html",
                    utenteLoggato != null ? utenteLoggato.getEmail() : "sconosciuto");
            response.sendRedirect(contextPath + "/accesso_negato.html");
            return;
        }

        // 3. Controllo Inizializzazione DAO
        if (!daoInitialized || accessoriDAO == null) {
            LOGGER.log(Level.SEVERE, "AccessoriServlet: Tentativo di usare la servlet con DAO non inizializzato.");
            // Imposta il messaggio nella REQUEST Scope, non nella SESSION, se è per la pagina di errore immediata
            request.setAttribute("messaggio", "Errore critico del sistema. Impossibile caricare i dati degli accessori. Contattare l'amministratore.");
            request.setAttribute("tipoMessaggio", "error");
            // Inoltra a una pagina di errore generica o alla dashboard
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/error.jsp"); // Esempio di pagina di errore
            // Alternativa: response.sendRedirect(contextPath + "/admin/dashboard.jsp");
            dispatcher.forward(request, response); // Usa forward per mostrare il messaggio
            return;
        }

        String action = request.getParameter("action");
        if (action == null || action.trim().isEmpty()) {
            action = "list"; // Azione di default è mostrare la lista
        }

        try {
            switch (action.toLowerCase()) {
                case "list":
                    listAccessori(request, response); // Non serve passare la sessione se i messaggi sono in request scope
                    break;
                // Puoi aggiungere qui altri case, es:
                // case "edit":
                //     showEditForm(request, response);
                //     break;
                // case "delete":
                //     deleteAccessorio(request, response);
                //     break;
                default:
                    // Se l'azione non è riconosciuta, impostiamo un messaggio di avviso
                    request.setAttribute("messaggio", "Azione non riconosciuta: " + action);
                    request.setAttribute("tipoMessaggio", "warning");
                    listAccessori(request, response); // Fallback alla lista
                    break;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQLException in AccessoriServlet (action: " + action + "): " + e.getMessage(), e);
            request.setAttribute("messaggio", "Errore del database durante l'operazione: " + e.getMessage());
            request.setAttribute("tipoMessaggio", "error");
            // Inoltra alla pagina di lista per mostrare l'errore o a una pagina di errore generica
            request.getRequestDispatcher("/admin/lista_accessori.jsp").forward(request, response); // O una pagina di errore specifica
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception in AccessoriServlet (action: " + action + "): " + e.getMessage(), e);
            request.setAttribute("messaggio", "Si è verificato un errore imprevisto: " + e.getMessage());
            request.setAttribute("tipoMessaggio", "error");
            request.getRequestDispatcher("/admin/lista_accessori.jsp").forward(request, response); // O una pagina di errore specifica
        }
    }

    // Ho rimosso il parametro HttpSession da questo metodo, i messaggi vanno nel request scope
    private void listAccessori(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<Accessori> lista = Collections.emptyList(); 

        try {
            lista = accessoriDAO.getAllAccessori(); 
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante il recupero della lista accessori.", e);
            request.setAttribute("messaggio", "Errore nel recupero della lista degli accessori: " + e.getMessage());
            request.setAttribute("tipoMessaggio", "error");
            // Lasciamo che la lista rimanga vuota ma prepariamo il dispatcher
        }

        request.setAttribute("listaAccessori", lista);

        // Recupera eventuali messaggi di successo/errore che potrebbero essere stati impostati da altre servlet
        // (es. da una servlet di aggiunta/modifica/eliminazione che reindirizza qui).
        // Se un messaggio è stato impostato nella sessione per un redirect, spostalo nel request scope.
        if (request.getSession().getAttribute("messaggio") != null) {
            request.setAttribute("messaggio", request.getSession().getAttribute("messaggio"));
            request.setAttribute("tipoMessaggio", request.getSession().getAttribute("tipoMessaggio"));
            request.getSession().removeAttribute("messaggio"); // Rimuovi dalla sessione dopo l'uso
            request.getSession().removeAttribute("tipoMessaggio"); // Rimuovi dalla sessione dopo l'uso
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/lista_accessori.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Questa servlet è principalmente per visualizzare la lista (GET).
        // Se si desidera gestire operazioni POST come la ricerca con filtri,
        // la logica andrebbe qui. Per ora, semplicemente invia al doGet.
        // Se hai un form di ricerca che invia via POST, potresti voler implementare
        // un metodo `searchAccessori(request, response);` e chiamarlo qui.
        doGet(request, response);
    }
}