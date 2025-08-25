package la_teca_del_giardiniere;

import la_teca_del_giardiniere.classes.RigaCarrello;
import la_teca_del_giardiniere.classes.Piante;
import la_teca_del_giardiniere.classes.Accessori;
import la_teca_del_giardiniere.DAO.PianteDAO;
import la_teca_del_giardiniere.DAO.AccessoriDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet che gestisce l'aggiornamento della quantità degli articoli nel carrello.
 * Permette di modificare la quantità di un articolo o di rimuoverlo se la quantità è 0 o meno.
 */
@WebServlet("/aggiornaQuantitaCarrello")
public class AggiornaQuantitaCarrelloServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(AggiornaQuantitaCarrelloServlet.class.getName());

    private PianteDAO pianteDAO;
    private AccessoriDAO accessoriDAO;

    /**
     * Metodo di inizializzazione del servlet. Inizializza i DAO per l'interazione con il database.
     */
    @Override
    public void init() throws ServletException {
        super.init();
        try {
            pianteDAO = new PianteDAO();
            accessoriDAO = new AccessoriDAO();
            LOGGER.info("PianteDAO e AccessoriDAO inizializzati in AggiornaQuantitaCarrelloServlet.");
        } catch (SQLException e) {
            // Logga l'errore critico e lancia un'eccezione per interrompere il deployment.
            LOGGER.log(Level.SEVERE, "Errore critico durante l'inizializzazione dei DAO in AggiornaQuantitaCarrelloServlet.", e);
            throw new ServletException("Errore di configurazione del database per AggiornaQuantitaCarrelloServlet.", e);
        } catch (Exception e) {
            // Gestione di altre eccezioni generiche.
            LOGGER.log(Level.SEVERE, "Errore generico durante l'inizializzazione di AggiornaQuantitaCarrelloServlet: " + e.getMessage(), e);
            throw new ServletException("Errore generico nell'inizializzazione.", e);
        }
    }

    /**
     * Gestisce le richieste POST per l'aggiornamento delle quantità del carrello.
     * Recupera i parametri, esegue le operazioni e reindirizza l'utente.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        String idProdottoStr = request.getParameter("idProdotto");
        String tipoProdotto = request.getParameter("tipoProdotto");
        String quantitaStr = request.getParameter("quantita");

        // 1. Validazione iniziale dei parametri
        if (idProdottoStr == null || idProdottoStr.trim().isEmpty() || tipoProdotto == null || tipoProdotto.trim().isEmpty()) {
            session.setAttribute("messaggio", "ID o tipo di prodotto mancante per l'aggiornamento.");
            session.setAttribute("tipoMessaggio", "error");
            LOGGER.warning("Tentativo di aggiornare carrello senza ID o tipo di prodotto.");
            response.sendRedirect(request.getContextPath() + "/visualizzaCarrello");
            return;
        }

        // 2. Parsing e validazione della quantità
        int nuovaQuantita = 0;
        try {
            nuovaQuantita = Integer.parseInt(quantitaStr.trim());
        } catch (NumberFormatException e) {
            session.setAttribute("messaggio", "Quantità non valida per l'aggiornamento.");
            session.setAttribute("tipoMessaggio", "error");
            LOGGER.log(Level.WARNING, "Quantità non valida per ID prodotto: " + idProdottoStr, e);
            response.sendRedirect(request.getContextPath() + "/visualizzaCarrello");
            return;
        }

        // 3. Recupero del carrello dalla sessione
        Map<String, RigaCarrello> carrello = (Map<String, RigaCarrello>) session.getAttribute("carrello");

        if (carrello == null || carrello.isEmpty()) {
            session.setAttribute("messaggio", "Il carrello è vuoto o non trovato.");
            session.setAttribute("tipoMessaggio", "warning");
            LOGGER.info("Tentativo di aggiornare un carrello vuoto o inesistente.");
            response.sendRedirect(request.getContextPath() + "/visualizzaCarrello");
            return;
        }

        // 4. Identificazione e recupero della riga da aggiornare
        String chiaveRiga = tipoProdotto.toUpperCase() + "-" + idProdottoStr;
        RigaCarrello rigaDaAggiornare = carrello.get(chiaveRiga);

        if (rigaDaAggiornare == null) {
            session.setAttribute("messaggio", "L'articolo non è presente nel carrello.");
            session.setAttribute("tipoMessaggio", "error");
            LOGGER.warning("Tentativo di aggiornare articolo non presente nel carrello. ID Prodotto: " + idProdottoStr);
            response.sendRedirect(request.getContextPath() + "/visualizzaCarrello");
            return;
        }

        // 5. Logica di aggiornamento o rimozione
        if (nuovaQuantita <= 0) {
            carrello.remove(chiaveRiga);
            session.setAttribute("messaggio", "Articolo rimosso dal carrello.");
            session.setAttribute("tipoMessaggio", "success");
            LOGGER.info("Articolo rimosso dal carrello. Prodotto: " + chiaveRiga);
        } else {
            int disponibilitaMassima = 0;
            try {
                int id = Integer.parseInt(idProdottoStr);
                // Controllo della disponibilità del prodotto nel database
                if ("PIANTA".equalsIgnoreCase(tipoProdotto)) {
                    Piante piantaOriginale = pianteDAO.getPiantaById(id);
                    if (piantaOriginale != null) {
                        disponibilitaMassima = piantaOriginale.getDisponibilita();
                    }
                } else if ("ACCESSORIO".equalsIgnoreCase(tipoProdotto)) {
                    Accessori accessorioOriginale = accessoriDAO.getAccessorioByaccessorio_id(id);
                    if (accessorioOriginale != null) {
                        disponibilitaMassima = accessorioOriginale.getDisponibilita();
                    }
                }
            } catch (SQLException | NumberFormatException e) {
                LOGGER.log(Level.SEVERE, "Errore nel controllo disponibilità per prodotto: " + chiaveRiga, e);
                session.setAttribute("messaggio", "Errore durante il controllo disponibilità. Riprova.");
                session.setAttribute("tipoMessaggio", "error");
                disponibilitaMassima = Integer.MAX_VALUE; // In caso di errore, si assume disponibilità massima.
            }

            // Se la quantità richiesta supera la disponibilità, viene limitata.
            if (nuovaQuantita > disponibilitaMassima) {
                nuovaQuantita = disponibilitaMassima;
                session.setAttribute("messaggio", "Quantità massima disponibile per il prodotto è " + disponibilitaMassima + ". Quantità aggiornata di conseguenza.");
                session.setAttribute("tipoMessaggio", "warning");
                LOGGER.warning("Quantità richiesta (" + quantitaStr + ") supera disponibilità (" + disponibilitaMassima + ") per prodotto: " + chiaveRiga);
            }

            // Aggiornamento effettivo della quantità
            rigaDaAggiornare.setQuantita(nuovaQuantita);
            session.setAttribute("messaggio", "Quantità aggiornata con successo.");
            session.setAttribute("tipoMessaggio", "success");
            LOGGER.info("Quantità aggiornata nel carrello per prodotto: " + chiaveRiga + ", Nuova Quantità: " + nuovaQuantita);
        }

        // 6. Salvataggio e reindirizzamento
        session.setAttribute("carrello", carrello);
        response.sendRedirect(request.getContextPath() + "/visualizzaCarrello");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Logga l'accesso non corretto
        LOGGER.log(Level.WARNING, "Tentativo di accesso con metodo GET a AggiornaQuantitaCarrelloServlet. Reindirizzamento.");

        // Imposta un messaggio di avviso per l'utente (opzionale)
        HttpSession session = request.getSession();
        session.setAttribute("messaggio", "Azione non consentita.");
        session.setAttribute("tipoMessaggio", "error");

        // Reindirizza l'utente alla pagina del carrello
        response.sendRedirect(request.getContextPath() + "/visualizzaCarrello");
    }
}