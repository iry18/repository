package la_teca_del_giardiniere;

import la_teca_del_giardiniere.classes.RigaCarrello; // La classe che rappresenta una riga nel carrello

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal; // Per i calcoli del totale
import java.util.HashMap; // Per il tipo di carrello in sessione
import java.util.Map; // Per il tipo di carrello in sessione
import java.util.logging.Logger;

@WebServlet("/visualizzaCarrello")
public class VisualizzaCarrelloServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(VisualizzaCarrelloServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false); // Non creare una nuova sessione se non esiste

        // --- 1. Gestione Messaggi Flash (opzionale ma utile) ---
        String messaggio = (String) session.getAttribute("messaggio");
        String tipoMessaggio = (String) session.getAttribute("tipoMessaggio");

        if (messaggio != null && tipoMessaggio != null) {
            request.setAttribute("messaggio", messaggio);
            request.setAttribute("tipoMessaggio", tipoMessaggio);
            session.removeAttribute("messaggio");
            session.removeAttribute("tipoMessaggio");
            LOGGER.info("Messaggio flash recuperato dalla sessione: " + messaggio);
        }

        // --- 2. Recupero Carrello dalla Sessione ---
        // Ora il carrello è una Map<String, RigaCarrello> (Chiave -> RigaCarrello)
        Map<String, RigaCarrello> carrello = null;
        if (session != null) {
            carrello = (Map<String, RigaCarrello>) session.getAttribute("carrello");
        }

        // Se il carrello non esiste in sessione, creane uno vuoto per non avere NullPointerException nella JSP
        if (carrello == null) {
            carrello = new HashMap<>();
            LOGGER.info("Carrello non trovato in sessione, creato un carrello vuoto per la visualizzazione.");
        }

        // --- 3. Calcolo del Totale del Carrello ---
        BigDecimal totaleCarrello = BigDecimal.ZERO;
        for (RigaCarrello riga : carrello.values()) {
            if (riga.getPrezzoUnitario() != null) {
                // Utilizza il metodo getTotaleArticolo() della RigaCarrello per calcolare il subtotale
                totaleCarrello = totaleCarrello.add(riga.getTotaleArticolo());
            } else {
                LOGGER.warning("Riga carrello con prezzo nullo per il prodotto: " + riga.getNomeProdotto());
            }
        }

        // --- 4. Inoltro alla JSP ---
        request.setAttribute("carrello", carrello); // Passa il carrello alla JSP
        request.setAttribute("totaleCarrello", totaleCarrello);

        LOGGER.info("Visualizzazione carrello dalla sessione. Articoli: " + carrello.size() + ", Totale: " + totaleCarrello);
        request.getRequestDispatcher("/WEB-INF/jsp/carrello.jsp").forward(request, response);
    }
}