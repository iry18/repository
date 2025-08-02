package la_teca_del_giardiniere;

import la_teca_del_giardiniere.classes.RigaCarrello; // Importa la classe RigaCarrello
import la_teca_del_giardiniere.classes.Utente; // Per il controllo di autenticazione (se lo si vuole mantenere)

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/rimuoviDalCarrello")
public class RimuoviDalCarrelloServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(RimuoviDalCarrelloServlet.class.getName());

    // Non serve nessun DAO qui, dato che il carrello è solo in sessione.
    // Rimuoviamo 'private CarrelloDAO carrelloDAO;' e il blocco init().

    @Override
    public void init() throws ServletException {
        super.init();
        LOGGER.info("RimuoviDalCarrelloServlet inizializzata. Gestirà la rimozione dal carrello in sessione.");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Controllo autenticazione: mantienilo se vuoi che solo gli utenti loggati possano manipolare il carrello.
        // Se il carrello è libero per tutti, puoi rimuovere questo blocco.
        Utente utente = (Utente) session.getAttribute("currentUser"); // Assicurati che "currentUser" sia la chiave corretta
        if (utente == null) {
            LOGGER.warning("Tentativo di rimuovere articolo dal carrello senza autenticazione. Reindirizzo a login.jsp");
            response.sendRedirect(request.getContextPath() + "/Login.jsp"); // Assicurati sia Login.jsp
            return;
        }

        // Il parametro ora sarà l'ID della pianta, non l'ID della riga del carrello nel DB
        String idPiantaStr = request.getParameter("idPianta"); 

        // Validazione input
        if (idPiantaStr == null || idPiantaStr.trim().isEmpty()) {
            session.setAttribute("messaggio", "ID pianta non fornito per la rimozione.");
            session.setAttribute("tipoMessaggio", "error");
            LOGGER.warning("Tentativo di rimuovere articolo dal carrello senza specificare l'ID della pianta.");
            response.sendRedirect(request.getContextPath() + "/visualizzaCarrello");
            return;
        }

        // Recupera il carrello dalla sessione
        Map<String, RigaCarrello> carrello = (Map<String, RigaCarrello>) session.getAttribute("carrello");

        if (carrello == null || carrello.isEmpty()) {
            session.setAttribute("messaggio", "Il carrello è vuoto. Impossibile rimuovere articoli.");
            session.setAttribute("tipoMessaggio", "warning");
            LOGGER.info("Tentativo di rimuovere articolo da un carrello vuoto.");
            response.sendRedirect(request.getContextPath() + "/visualizzaCarrello");
            return;
        }

        // Rimuovi l'articolo dalla Map usando l'ID della pianta come chiave
        // Il metodo `remove` di HashMap restituisce l'oggetto rimosso, o null se la chiave non c'era.
        RigaCarrello rimosso = carrello.remove(idPiantaStr);

        if (rimosso != null) {
            session.setAttribute("messaggio", "Articolo rimosso con successo dal carrello.");
            session.setAttribute("tipoMessaggio", "success");
            LOGGER.info("Articolo rimosso dal carrello in sessione. ID Pianta: " + idPiantaStr + " per utente: " + utente.getId());
        } else {
            session.setAttribute("messaggio", "L'articolo non è stato trovato nel tuo carrello.");
            session.setAttribute("tipoMessaggio", "error");
            LOGGER.warning("Tentativo di rimuovere un articolo non presente nel carrello in sessione. ID Pianta: " + idPiantaStr + " per utente: " + utente.getId());
        }

        // Salva la Map aggiornata di nuovo nella sessione (anche se `remove` modifica l'oggetto direttamente)
        session.setAttribute("carrello", carrello); 

        // Reindirizza sempre alla VisualizzaCarrelloServlet per ricaricare la vista del carrello
        response.sendRedirect(request.getContextPath() + "/visualizzaCarrello");
    }
}