package src.com.la_teca_del_giardiniere;

import src.com.la_teca_del_giardiniere.classes.Ordine;
import src.com.la_teca_del_giardiniere.classes.DettaglioOrdine;
import src.com.la_teca_del_giardiniere.dao.OrdineDAO;
import src.com.la_teca_del_giardiniere.classes.Utente; // Assumi che l'utente sia in sessione

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/processaCheckout") // URL a cui risponderà questa Servlet
public class ProcessaCheckoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ProcessaCheckoutServlet.class.getName());

    private OrdineDAO ordineDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        try {
            ordineDAO = new OrdineDAO();
            // carrelloDAO = new CarrelloDAO(); // Inizializza il CarrelloDAO se lo usi
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante l'inizializzazione del DAO", e);
            throw new ServletException("Errore di configurazione del database.", e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("utenteCorrente"); // Recupera l'utente dalla sessione

        if (utente == null) {
            response.sendRedirect("login.jsp"); // O la tua pagina di login
            return;
        }

        // Recupera i dati del carrello (DA IMPLEMENTARE SECONDO LA TUA LOGICA DEL CARRELLO)
        // Per semplicità, qui simulo un carrello
        List<DettaglioOrdine> articoliCarrello = (List<DettaglioOrdine>) session.getAttribute("carrelloSimulato"); // Esempio: recupera dalla sessione
        if (articoliCarrello == null || articoliCarrello.isEmpty()) {
            request.setAttribute("errore", "Il carrello è vuoto. Impossibile creare l'ordine.");
            request.getRequestDispatcher("/carrello.jsp").forward(request, response); // O la tua pagina del carrello
            return;
        }

        // Recupera i dati dal form di checkout (es. indirizzo, metodo di pagamento)
        String cittaSpedizione = request.getParameter("cittaSpedizione");
        String paeseSpedizione = request.getParameter("paeseSpedizione");
        String capSpedizione = request.getParameter("capSpedizione");
        String metodoPagamento = request.getParameter("metodoPagamento");
        String note = request.getParameter("note");

        // Calcola totale ordine e IVA (potresti averlo già nel carrello, qui è un esempio)
        BigDecimal totaleOrdine = BigDecimal.ZERO;
        BigDecimal ivaPercentuale = new BigDecimal("0.22"); // Esempio IVA 22%
        for (DettaglioOrdine dettaglio : articoliCarrello) {
            totaleOrdine = totaleOrdine.add(dettaglio.getPrezzoUnitario().multiply(new BigDecimal(dettaglio.getQuantita())));
        }
        BigDecimal ivaCalcolata = totaleOrdine.multiply(ivaPercentuale);

        try {
            // 1. Crea l'oggetto Ordine
            Ordine nuovoOrdine = new Ordine();
            nuovoOrdine.setUtenteId(utente.getId()); // Assumi che Utente abbia un getId()
            nuovoOrdine.setDataOrdine(Timestamp.valueOf(LocalDateTime.now()));
            nuovoOrdine.setCittaSpedizione(cittaSpedizione);
            nuovoOrdine.setPaeseSpedizione(paeseSpedizione);
            nuovoOrdine.setCapSpedizione(capSpedizione);
            nuovoOrdine.setTotaleOrdine(totaleOrdine.add(ivaCalcolata)); // Totale + IVA
            nuovoOrdine.setMetodoPagamento(metodoPagamento);
            nuovoOrdine.setIva(ivaCalcolata);
            nuovoOrdine.setStatoOrdine("In attesa di pagamento"); // Stato iniziale
            nuovoOrdine.setNote(note);

            // 2. Inserisci l'ordine nel database e ottieni l'ID generato
            int ordineIdGenerato = ordineDAO.insertOrdine(nuovoOrdine); // Questo metodo popola l'ID nell'oggetto Ordine

            // 3. Inserisci i dettagli dell'ordine
            for (DettaglioOrdine dettaglio : articoliCarrello) {
                dettaglio.setOrdineId(ordineIdGenerato); // Associa il dettaglio all'ID dell'ordine appena creato
                ordineDAO.insertDettaglioOrdine(dettaglio);
            }

            // Pulisci il carrello dopo l'acquisto (se gestito in sessione o DB)
            session.removeAttribute("carrelloSimulato");
            // carrelloDAO.svuotaCarrello(utente.getId()); // Se il carrello è persistito nel DB

            request.setAttribute("messaggioSuccesso", "Ordine #" + ordineIdGenerato + " creato con successo!");
            response.sendRedirect(request.getContextPath() + "/visualizzaOrdiniUtente"); // Reindirizza allo storico ordini
            // O fai forward a una pagina di conferma
            // request.getRequestDispatcher("/ordineConfermato.jsp").forward(request, response);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante il processo di checkout", e);
            request.setAttribute("errore", "Si è verificato un errore durante la creazione dell'ordine. Riprova più tardi.");
            request.getRequestDispatcher("/checkout.jsp").forward(request, response); // Torna alla pagina di checkout con errore
        } catch (Exception e) { // Cattura altre eccezioni non SQL
            LOGGER.log(Level.SEVERE, "Errore generico durante il processo di checkout", e);
            request.setAttribute("errore", "Si è verificato un errore imprevisto. Contatta l'assistenza.");
            request.getRequestDispatcher("/checkout.jsp").forward(request, response);
        }
    }
}
