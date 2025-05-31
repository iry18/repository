package src.com.la_teca_del_giardiniere.admin;

import src.com.la_teca_del_giardiniere.classes.Ordine;
import src.com.la_teca_del_giardiniere.dao.OrdineDAO; // O un OrdineAdminDAO se preferisci separarli
import src.com.la_teca_del_giardiniere.classes.Utente; // Per recuperare l'utente loggato

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/admin/gestioneOrdini") // URL specifico per l'area admin
public class GestioneOrdiniServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(GestioneOrdiniServlet.class.getName());

    private OrdineDAO ordineDAO; // Potresti usare un OrdineAdminDAO dedicato

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            ordineDAO = new OrdineDAO();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante l'inizializzazione del DAO", e);
            throw new ServletException("Errore di configurazione del database.", e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("utenteCorrente");

        // Qui dovresti avere una logica per verificare se l'utente ha il ruolo di amministratore/venditore
        if (utente == null || !isAdminOrVenditore(utente)) { // Implementa isAdminOrVenditore()
            response.sendRedirect(request.getContextPath() + "/accesso_negato.html"); // O la tua pagina di accesso negato
            return;
        }

        try {
            List<Ordine> ordini;
            String statoFiltro = request.getParameter("stato"); // Permette di filtrare per stato
            int utenteIdFiltro = 0;
            try {
                if (request.getParameter("utenteId") != null && !request.getParameter("utenteId").isEmpty()) {
                    utenteIdFiltro = Integer.parseInt(request.getParameter("utenteId"));
                }
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "ID utente filtro non valido: " + request.getParameter("utenteId"));
            }

            // Aggiungi metodi nel tuo OrdineDAO per recuperare tutti gli ordini o ordini filtrati
            if (statoFiltro != null && !statoFiltro.isEmpty()) {
                ordini = ordineDAO.getOrdiniByStato(statoFiltro); // Metodo da aggiungere al DAO
            } else if (utenteIdFiltro > 0) {
                ordini = ordineDAO.getOrdiniByUtenteId(utenteIdFiltro); // Già presente
            } else {
                ordini = ordineDAO.getAllOrdini(); // Metodo da aggiungere al DAO
            }

            request.setAttribute("ordini", ordini);
            request.getRequestDispatcher("/WEB-INF/jsp/admin/gestioneOrdini.jsp").forward(request, response);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante il recupero degli ordini per l'admin.", e);
            request.setAttribute("errore", "Errore durante il recupero degli ordini.");
            request.getRequestDispatcher("/WEB-INF/jsp/admin/erroreAdmin.jsp").forward(request, response);
        }
    }

    // Metodo helper per la verifica del ruolo (implementalo in base alla tua logica ruoli)
    private boolean isAdminOrVenditore(Utente utente) {
        // Esempio: assumendo che l'utente abbia un campo ruolo o una lista di ruoli
        // return utente.getRuolo().equals("admin") || utente.getRuolo().equals("venditore");
        // O se hai una lista di ruoli:
        // return utente.getRuoli().contains("admin") || utente.getRuoli().contains("venditore");
        return true; // Per ora, per test, considera tutti autorizzati
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Potresti usare doPost per aggiornare lo stato di un ordine
        int ordineId = 0;
        String nuovoStato = request.getParameter("nuovoStato");
        try {
            ordineId = Integer.parseInt(request.getParameter("ordineId"));
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "ID ordine non valido per aggiornamento stato: " + request.getParameter("ordineId"));
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID ordine non valido.");
            return;
        }

        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("utenteCorrente");
        if (utente == null || !isAdminOrVenditore(utente)) {
            response.sendRedirect(request.getContextPath() + "/accesso_negato.html");
            return;
        }

        try {
            Ordine ordineDaAggiornare = ordineDAO.getOrdineById(ordineId);
            if (ordineDaAggiornare != null) {
                ordineDaAggiornare.setStatoOrdine(nuovoStato);
                ordineDAO.updateStatoOrdine(ordineDaAggiornare); // Metodo da aggiungere al DAO
                LOGGER.log(Level.INFO, "Stato ordine {0} aggiornato a: {1}", new Object[]{ordineId, nuovoStato});
                response.sendRedirect(request.getContextPath() + "/admin/gestioneOrdini?messaggioSuccesso=Stato aggiornato.");
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Ordine non trovato.");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'aggiornamento dello stato dell'ordine: " + ordineId, e);
            request.setAttribute("errore", "Errore durante l'aggiornamento dello stato dell'ordine.");
            request.getRequestDispatcher("/WEB-INF/jsp/admin/gestioneOrdini.jsp").forward(request, response);
        }
    }
}