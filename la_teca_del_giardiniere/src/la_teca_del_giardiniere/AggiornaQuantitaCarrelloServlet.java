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
            LOGGER.log(Level.SEVERE, "Errore critico durante l'inizializzazione dei DAO.", e);
            throw new ServletException("Errore di configurazione del database per AggiornaQuantitaCarrelloServlet.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        String idProdottoStr = request.getParameter("idProdotto");
        String tipoProdotto = request.getParameter("tipoProdotto");
        String quantitaStr = request.getParameter("quantita");

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

        Map<String, RigaCarrello> carrello = (Map<String, RigaCarrello>) session.getAttribute("carrello");
        if (carrello == null || carrello.isEmpty()) {
            session.setAttribute("messaggio", "Il carrello è vuoto o non trovato.");
            session.setAttribute("tipoMessaggio", "warning");
            response.sendRedirect(request.getContextPath() + "/visualizzaCarrello");
            return;
        }

        String chiaveRiga = tipoProdotto.toUpperCase() + "-" + idProdottoStr;
        RigaCarrello rigaDaAggiornare = carrello.get(chiaveRiga);

        if (rigaDaAggiornare == null) {
            session.setAttribute("messaggio", "L'articolo non è presente nel carrello.");
            session.setAttribute("tipoMessaggio", "error");
            LOGGER.warning("Tentativo di aggiornare articolo non presente nel carrello. ID Prodotto: " + idProdottoStr);
            response.sendRedirect(request.getContextPath() + "/visualizzaCarrello");
            return;
        }

        // Logica di aggiornamento o rimozione
        if (nuovaQuantita <= 0) {
            carrello.remove(chiaveRiga);
            session.setAttribute("messaggio", "Articolo rimosso dal carrello.");
            session.setAttribute("tipoMessaggio", "success");
            LOGGER.info("Articolo rimosso dal carrello. Prodotto: " + chiaveRiga);
        } else {
            try {
                int id = Integer.parseInt(idProdottoStr);
                int disponibilitaMassima;
                
                if ("PIANTA".equalsIgnoreCase(tipoProdotto)) {
                    Piante piantaOriginale = pianteDAO.getPiantaById(id);
                    disponibilitaMassima = (piantaOriginale != null) ? piantaOriginale.getDisponibilita() : 0;
                } else if ("ACCESSORIO".equalsIgnoreCase(tipoProdotto)) {
                    Accessori accessorioOriginale = accessoriDAO.getAccessorioByaccessorio_id(id);
                    disponibilitaMassima = (accessorioOriginale != null) ? accessorioOriginale.getDisponibilita() : 0;
                } else {
                    disponibilitaMassima = 0;
                }

                if (disponibilitaMassima == 0) {
                    session.setAttribute("messaggio", "Il prodotto non è più disponibile. Rimozione dal carrello.");
                    session.setAttribute("tipoMessaggio", "warning");
                    carrello.remove(chiaveRiga);
                } else if (nuovaQuantita > disponibilitaMassima) {
                    rigaDaAggiornare.setQuantita(disponibilitaMassima);
                    session.setAttribute("messaggio", "Quantità massima disponibile per il prodotto è " + disponibilitaMassima + ". Quantità aggiornata di conseguenza.");
                    session.setAttribute("tipoMessaggio", "warning");
                    LOGGER.warning("Quantità richiesta (" + quantitaStr + ") supera disponibilità (" + disponibilitaMassima + ").");
                } else {
                    rigaDaAggiornare.setQuantita(nuovaQuantita);
                    session.setAttribute("messaggio", "Quantità aggiornata con successo.");
                    session.setAttribute("tipoMessaggio", "success");
                    LOGGER.info("Quantità aggiornata nel carrello. Prodotto: " + chiaveRiga + ", Nuova Quantità: " + nuovaQuantita);
                }
            } catch (SQLException | NumberFormatException e) {
                LOGGER.log(Level.SEVERE, "Errore nel controllo disponibilità per prodotto: " + chiaveRiga, e);
                session.setAttribute("messaggio", "Errore durante il controllo disponibilità. Riprova.");
                session.setAttribute("tipoMessaggio", "error");
            }
        }
        
        session.setAttribute("carrello", carrello);
        response.sendRedirect(request.getContextPath() + "/visualizzaCarrello");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.log(Level.WARNING, "Tentativo di accesso con metodo GET a AggiornaQuantitaCarrelloServlet. Reindirizzamento.");
        HttpSession session = request.getSession();
        session.setAttribute("messaggio", "Azione non consentita. Usa il metodo POST per aggiornare il carrello.");
        session.setAttribute("tipoMessaggio", "error");
        response.sendRedirect(request.getContextPath() + "/visualizzaCarrello");
    }
}