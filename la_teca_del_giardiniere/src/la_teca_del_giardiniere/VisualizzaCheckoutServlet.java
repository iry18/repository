package la_teca_del_giardiniere;

import la_teca_del_giardiniere.classes.RigaCarrello;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet che gestisce la visualizzazione della pagina di checkout.
 * Recupera il carrello dalla sessione, calcola il totale e prepara i dati per la JSP.
 */
@WebServlet("/visualizzaCheckout")
public class VisualizzaCheckoutServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(VisualizzaCheckoutServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Recupera il carrello dalla sessione. Il carrello è una Map, come stabilito prima.
        @SuppressWarnings("unchecked")
        Map<String, RigaCarrello> carrelloMap = (Map<String, RigaCarrello>) session.getAttribute("carrello");
        
        // Inizializza il totale del carrello a zero.
        BigDecimal totaleCarrello = BigDecimal.ZERO;
        
        // Verifica se il carrello esiste e non è vuoto.
        if (carrelloMap != null && !carrelloMap.isEmpty()) {
            // Se il carrello contiene articoli, calcola il totale complessivo.
            for (RigaCarrello riga : carrelloMap.values()) {
                totaleCarrello = totaleCarrello.add(riga.getTotaleArticolo());
            }
            
            // Imposta gli articoli del carrello (come Collection) e il totale
            // nell'oggetto della richiesta (request). Questo li rende disponibili
            // per la JSP.
            request.setAttribute("articoliCarrello", carrelloMap.values());
            request.setAttribute("totaleCarrello", totaleCarrello);
            
        } else {
            // Se il carrello è vuoto, imposta un messaggio di avviso e reindirizza al carrello.
            LOGGER.log(Level.INFO, "Tentativo di checkout con carrello vuoto.");
            session.setAttribute("messaggio", "Il tuo carrello è vuoto. Aggiungi prima dei prodotti per procedere al checkout.");
            session.setAttribute("tipoMessaggio", "warning");
            response.sendRedirect(request.getContextPath() + "/visualizzaCarrello");
            return;
        }

        // Recupera l'utente dalla sessione per precompilare i campi del form.
        // Questo è opzionale ma migliora l'esperienza utente.
        Object utenteCorrente = session.getAttribute("utente");
        if (utenteCorrente != null) {
            request.setAttribute("utenteCorrente", utenteCorrente);
        }

        // Inoltra (forward) la richiesta alla pagina JSP di checkout.
        // L'inoltro permette di mantenere gli attributi della richiesta.
        request.getRequestDispatcher("/checkout.jsp").forward(request, response);
    }
}