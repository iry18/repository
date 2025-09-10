package la_teca_del_giardiniere;

import la_teca_del_giardiniere.DAO.OrdineDAO;
import la_teca_del_giardiniere.classes.DettaglioOrdine;
import la_teca_del_giardiniere.classes.Ordine;
import la_teca_del_giardiniere.classes.RigaCarrello;
import la_teca_del_giardiniere.classes.Utente;

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
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/ProcessaCheckoutServlet")
public class ProcessaCheckoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ProcessaCheckoutServlet.class.getName());

    private OrdineDAO ordineDAO;

    public void init() throws ServletException {
        super.init();
        try {
            ordineDAO = new OrdineDAO();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante l'inizializzazione di OrdineDAO", e);
            throw new ServletException("Errore di configurazione del database.", e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        // RECUPERA CARRELLO E UTENTE DALLA SESSIONE
        @SuppressWarnings("unchecked")
        Map<String, RigaCarrello> carrello = (Map<String, RigaCarrello>) session.getAttribute("carrello");
        Utente utente = (Utente) session.getAttribute("utente");

        // 1. Verifica la presenza del carrello
        if (carrello == null || carrello.isEmpty()) {
            LOGGER.log(Level.WARNING, "Tentativo di processare un checkout con carrello vuoto.");
            request.setAttribute("errorMessage", "Il carrello è vuoto. Impossibile procedere con l'ordine.");
            request.getRequestDispatcher("/checkout.jsp").forward(request, response);
            return;
        }

        // 2. Verifica la presenza dell'utente (se il login è obbligatorio)
        if (utente == null) {
            LOGGER.log(Level.WARNING, "Tentativo di processare un checkout senza utente loggato.");
            response.sendRedirect(request.getContextPath() + "/login.jsp"); // Reindirizza al login
            return;
        }

        // 3. Calcola il totale e l'IVA
        BigDecimal totaleOrdine = BigDecimal.ZERO;
        for (RigaCarrello riga : carrello.values()) {
            totaleOrdine = totaleOrdine.add(riga.getTotaleArticolo());
        }
        BigDecimal iva = totaleOrdine.multiply(new BigDecimal("0.22")); // Esempio IVA 22%

        // 4. Recupera i dati di spedizione dal form
        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String indirizzo = request.getParameter("indirizzo");
        String citta = request.getParameter("citta");
        String cap = request.getParameter("cap");
        String paese = request.getParameter("paese");
        String telefono = request.getParameter("telefono");
        String email = request.getParameter("email");
        String metodoPagamento = request.getParameter("metodoPagamento");

        // 5. Crea l'oggetto Ordine
        Ordine nuovoOrdine = new Ordine();
        nuovoOrdine.setUtenteId(utente.getId()); // Assumendo che la classe Utente abbia getId()
        nuovoOrdine.setDataOrdine(Timestamp.from(Instant.now()));
        nuovoOrdine.setNomeSpedizione(nome);
        nuovoOrdine.setCognomeSpedizione(cognome);
        nuovoOrdine.setIndirizzoSpedizione(indirizzo);
        nuovoOrdine.setCittaSpedizione(citta);
        nuovoOrdine.setCapSpedizione(cap);
        nuovoOrdine.setPaeseSpedizione(paese);
        nuovoOrdine.setTelefonoSpedizione(telefono);
        nuovoOrdine.setEmailSpedizione(email);
        nuovoOrdine.setMetodoPagamento(metodoPagamento);
        nuovoOrdine.setTotaleOrdine(totaleOrdine);
        nuovoOrdine.setIva(iva);
        nuovoOrdine.setStatoOrdine("In lavorazione");
        
        // 6. Crea la lista dei DettagliOrdine dal carrello
        List<DettaglioOrdine> dettagliOrdineList = new ArrayList<>();
        for (RigaCarrello riga : carrello.values()) {
            DettaglioOrdine dettaglio = new DettaglioOrdine();
            dettaglio.setProdottoId(riga.getTipoProdotto().equalsIgnoreCase("pianta") ? riga.getIdProdotto() : null);
            dettaglio.setAccessorioId(riga.getTipoProdotto().equalsIgnoreCase("accessorio") ? riga.getIdProdotto() : null);
            dettaglio.setPrezzoUnitario(riga.getPrezzoUnitario());
            dettaglio.setQuantita(riga.getQuantita());
            dettagliOrdineList.add(dettaglio);
        }
        nuovoOrdine.setDettagliOrdine(dettagliOrdineList);

        try {
            // 7. Salva l'ordine e i dettagli nel database
            int ordineId = ordineDAO.insertOrdineConDettagli(nuovoOrdine);

            // 8. Svuota il carrello dopo l'ordine
            session.removeAttribute("carrello");
            
            // 9. Reindirizza alla pagina di conferma
            request.setAttribute("ordineId", ordineId);
            request.getRequestDispatcher("/WEB-INF/jsp/confermaOrdine.jsp").forward(request, response);
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'inserimento dell'ordine.", e);
            request.setAttribute("errorMessage", "Si è verificato un errore durante la finalizzazione dell'ordine. Riprova più tardi.");
            request.getRequestDispatcher("/checkout.jsp").forward(request, response);
        }
    }
}