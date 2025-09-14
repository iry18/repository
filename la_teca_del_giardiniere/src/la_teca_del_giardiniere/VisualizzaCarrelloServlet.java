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
import java.util.logging.Logger;

/**
 * Servlet che gestisce la visualizzazione del carrello.
 * Recupera il carrello dalla sessione e inoltra la richiesta alla pagina JSP.
 */
@WebServlet("/visualizzaCarrello")
public class VisualizzaCarrelloServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(VisualizzaCarrelloServlet.class.getName());

    /**
     * Gestisce le richieste GET per la visualizzazione del carrello.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false); // Non crea una nuova sessione se non esiste

        // 1. Recupero del carrello dalla sessione
        Map<String, RigaCarrello> carrello = null;
        BigDecimal totaleCarrello = BigDecimal.ZERO;
        int numeroArticoliCarrello = 0; 

        if (session != null) {
            Object carrelloObject = session.getAttribute("carrello");
            if (carrelloObject instanceof Map) {
                carrello = (Map<String, RigaCarrello>) carrelloObject;
                
                // 2. Calcolo del totale del carrello
                if (carrello != null && !carrello.isEmpty()) {
                    for (RigaCarrello riga : carrello.values()) {
                        BigDecimal subtotale = riga.getPrezzoUnitario().multiply(new BigDecimal(riga.getQuantita()));
                        totaleCarrello = totaleCarrello.add(subtotale);
                     // Calcolo del numero totale di articoli
                        numeroArticoliCarrello += riga.getQuantita();
                    }
                }
            }
        }

        // 3. Impostazione degli attributi per la JSP
        request.setAttribute("carrello", carrello);
        request.setAttribute("totaleCarrello", totaleCarrello);
        request.setAttribute("numeroArticoliCarrello", numeroArticoliCarrello); // Passa il conteggio alla JSP
        
        // 4. Inoltro alla pagina JSP per la visualizzazione
        request.getRequestDispatcher("/Carrello.jsp").forward(request, response);
    }
    
    /**
     * Gestisce le richieste POST reindirizzandole al metodo GET.
     * Questo è utile per la coerenza del reindirizzamento da altre servlet.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}