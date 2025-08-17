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

@WebServlet("/aggiornaQuantitaCarrello")
public class AggiornaQuantitaCarrelloServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(AggiornaQuantitaCarrelloServlet.class.getName());

    private PianteDAO pianteDAO;
    private AccessoriDAO accessoriDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            pianteDAO = new PianteDAO();
            accessoriDAO = new AccessoriDAO();
            LOGGER.info("PianteDAO e AccessoriDAO inizializzati in AggiornaQuantitaCarrelloServlet.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore critico durante l'inizializzazione dei DAO in AggiornaQuantitaCarrelloServlet.", e);
            throw new ServletException("Errore di configurazione del database per AggiornaQuantitaCarrelloServlet.", e);
        } catch (Exception e) {
             LOGGER.log(Level.SEVERE, "Errore generico durante l'inizializzazione di AggiornaQuantitaCarrelloServlet: " + e.getMessage(), e);
             throw new ServletException("Errore generico nell'inizializzazione.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        String idProdottoStr = request.getParameter("idProdotto");
        String tipoProdotto = request.getParameter("tipoProdotto"); // "PIANTA" o "ACCESSORIO"
        String quantitaStr = request.getParameter("quantita");

        // Valida i parametri
        if (idProdottoStr == null || idProdottoStr.trim().isEmpty() || tipoProdotto == null || tipoProdotto.trim().isEmpty()) {
            session.setAttribute("messaggio", "ID o tipo di prodotto mancante per l'aggiornamento.");
            session.setAttribute("tipoMessaggio", "error");
            LOGGER.warning("Tentativo di aggiornare carrello senza ID o tipo di prodotto.");
            response.sendRedirect(request.getContextPath() + "/visualizzaCarrello");
            return;
        }

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

        // Recupera il carrello dalla sessione usando la chiave generica
        Map<String, RigaCarrello> carrello = (Map<String, RigaCarrello>) session.getAttribute("carrello");

        if (carrello == null || carrello.isEmpty()) {
            session.setAttribute("messaggio", "Il carrello è vuoto o non trovato.");
            session.setAttribute("tipoMessaggio", "warning");
            LOGGER.info("Tentativo di aggiornare un carrello vuoto o inesistente.");
            response.sendRedirect(request.getContextPath() + "/visualizzaCarrello");
            return;
        }

        // Crea la chiave per identificare la riga del carrello
        String chiaveRiga = tipoProdotto.toUpperCase() + "-" + idProdottoStr;
        RigaCarrello rigaDaAggiornare = carrello.get(chiaveRiga);

        if (rigaDaAggiornare == null) {
            session.setAttribute("messaggio", "L'articolo non è presente nel carrello.");
            session.setAttribute("tipoMessaggio", "error");
            LOGGER.warning("Tentativo di aggiornare articolo non presente nel carrello. ID Prodotto: " + idProdottoStr);
            response.sendRedirect(request.getContextPath() + "/visualizzaCarrello");
            return;
        }

        // --- Logica di Aggiornamento e Controllo Disponibilità ---
        if (nuovaQuantita <= 0) {
            // Se la nuova quantità è 0 o meno, rimuovi l'articolo
            carrello.remove(chiaveRiga);
            session.setAttribute("messaggio", "Articolo rimosso dal carrello.");
            session.setAttribute("tipoMessaggio", "success");
            LOGGER.info("Articolo rimosso dal carrello. Prodotto: " + chiaveRiga);
        } else {
            int disponibilitaMassima = 0;
            try {
                int id = Integer.parseInt(idProdottoStr);
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
                // Non interrompere, l'aggiornamento continuerà con la quantità richiesta
                disponibilitaMassima = Integer.MAX_VALUE; // Fallback per non bloccare
            }

            if (nuovaQuantita > disponibilitaMassima) {
                // Imposta la quantità massima disponibile e avvisa l'utente
                nuovaQuantita = disponibilitaMassima;
                session.setAttribute("messaggio", "Quantità massima disponibile per il prodotto è " + disponibilitaMassima + ". Quantità aggiornata di conseguenza.");
                session.setAttribute("tipoMessaggio", "warning");
                LOGGER.warning("Quantità richiesta (" + quantitaStr + ") supera disponibilità (" + disponibilitaMassima + ") per prodotto: " + chiaveRiga);
            }

            rigaDaAggiornare.setQuantita(nuovaQuantita);
            session.setAttribute("messaggio", "Quantità aggiornata con successo.");
            session.setAttribute("tipoMessaggio", "success");
            LOGGER.info("Quantità aggiornata nel carrello per prodotto: " + chiaveRiga + ", Nuova Quantità: " + nuovaQuantita);
        }

        session.setAttribute("carrello", carrello);

        response.sendRedirect(request.getContextPath() + "/visualizzaCarrello");
    }
}