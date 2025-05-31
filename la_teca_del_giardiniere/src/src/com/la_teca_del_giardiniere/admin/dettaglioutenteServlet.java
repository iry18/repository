package src.com.la_teca_del_giardiniere.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import src.com.la_teca_del_giardiniere.classes.Ordine;
import src.com.la_teca_del_giardiniere.classes.registrazione; // La tua classe Utente, rinomina in Utente per chiarezza?
import src.com.la_teca_del_giardiniere.dao.OrdineDAO;
import src.com.la_teca_del_giardiniere.dao.UtenteDAO;

@WebServlet("/admin/dettaglioutenteServlet")
public class dettaglioutenteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UtenteDAO utenteDAO;
    private OrdineDAO ordineDAO;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            utenteDAO = new UtenteDAO();
            ordineDAO = new OrdineDAO();
        } catch (SQLException e) {
            System.err.println("Errore di inizializzazione dei DAO in dettaglioutenteServlet: " + e.getMessage());
            e.printStackTrace();
            // Se l'inizializzazione fallisce, la Servlet non sarà in grado di funzionare.
            // È utile propagare l'eccezione o renderla non disponibile.
            throw new ServletException("Impossibile inizializzare i DAO per dettaglioutenteServlet", e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false); // Non creare una nuova sessione se non esiste
        
        // Controllo autenticazione: l'utente deve essere loggato
        if (session == null || session.getAttribute("utenteLoggato") == null) { // Assumi che "utenteLoggato" sia l'attributo in sessione
            response.sendRedirect(request.getContextPath() + "/login.jsp"); // Reindirizza al login
            return; // Termina l'esecuzione del metodo
        }

        List<String> ruoli = null;
        Object ruoliObj = session.getAttribute("ruoli");
        if (ruoliObj instanceof List<?>) {
            List<?> tempRuoli = (List<?>) ruoliObj;
            boolean allStrings = true;
            for (Object item : tempRuoli) {
                if (!(item instanceof String)) {
                    allStrings = false;
                    break;
                }
            }
            if (allStrings) {
                ruoli = new ArrayList<>();
                for (Object item : tempRuoli) {
                    ruoli.add((String) item);
                }
            } else {
                System.err.println("Errore: la lista dei ruoli nella sessione contiene elementi non-String.");
                ruoli = null;
            }
        } else {
            System.err.println("Avviso: l'attributo 'ruoli' non è una lista o è null nella sessione.");
            ruoli = null;
        }

        // Controllo autorizzazione: solo gli amministratori possono vedere i dettagli di altri utenti
        if (ruoli != null && ruoli.contains("amministratore")) {
            String utenteIdStr = request.getParameter("id"); // Recupera l'ID dell'utente dalla URL
            if (utenteIdStr != null && !utenteIdStr.trim().isEmpty()) {
                try {
                    int utenteId = Integer.parseInt(utenteIdStr);

                    // Recupera i dettagli dell'utente
                    registrazione utente = utenteDAO.getUtenteByIdWithRuoli(utenteId);

                    if (utente != null) {
                        // Recupera gli ordini dell'utente
                        List<Ordine> ordiniUtente = ordineDAO.getOrdiniByUtenteId(utenteId);

                        request.setAttribute("utente", utente);
                        request.setAttribute("ordiniUtente", ordiniUtente);
                        // Assicurati che il percorso della JSP sia corretto
                        request.getRequestDispatcher("/admin/dettaglio_utente.jsp").forward(request, response);
                    } else {
                        request.setAttribute("messaggio", "Utente non trovato.");
                        request.setAttribute("tipoMessaggio", "error");
                        // Reindirizza alla lista utenti o a una pagina di errore più generica
                        request.getRequestDispatcher("/admin/utenti.jsp").forward(request, response);
                    }

                } catch (NumberFormatException e) {
                    request.setAttribute("messaggio", "ID utente non valido.");
                    request.setAttribute("tipoMessaggio", "error");
                    request.getRequestDispatcher("/admin/utenti.jsp").forward(request, response);
                } catch (SQLException e) {
                    e.printStackTrace();
                    request.setAttribute("messaggio", "Errore del database durante il recupero dei dettagli utente: " + e.getMessage());
                    request.setAttribute("tipoMessaggio", "error");
                    request.getRequestDispatcher("/admin/utenti.jsp").forward(request, response);
                }
            } else {
                // Se l'ID non è fornito, reindirizza alla lista utenti o a una pagina di errore
                request.setAttribute("messaggio", "ID utente non fornito.");
                request.setAttribute("tipoMessaggio", "error");
                request.getRequestDispatcher("/admin/utenti.jsp").forward(request, response);
            }
        } else {
            // Utente non autorizzato (non è un amministratore)
            response.sendRedirect(request.getContextPath() + "/accesso_negato.html");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Per questa Servlet che è principalmente per la visualizzazione, delegare a doGet è OK.
        // Se in futuro aggiungerai funzionalità di modifica tramite POST, dovrai implementare una logica qui.
        doGet(request, response);
    }
}