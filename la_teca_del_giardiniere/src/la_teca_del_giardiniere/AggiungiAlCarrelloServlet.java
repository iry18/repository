package la_teca_del_giardiniere;

import la_teca_del_giardiniere.DAO.CarrelloDAO;
import la_teca_del_giardiniere.classes.Carrello; // Usiamo la classe Carrello del DAO
import la_teca_del_giardiniere.classes.Utente; // Per ottenere l'ID dell'utente loggato

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/aggiungiAlCarrello")
public class AggiungiAlCarrelloServlet extends HttpServlet {
    // Add the serialVersionUID here to suppress the warning
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(AggiungiAlCarrelloServlet.class.getName());
    private CarrelloDAO carrelloDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            // Inizializza il DAO una sola volta all'avvio della servlet
            carrelloDAO = new CarrelloDAO();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante l'inizializzazione di CarrelloDAO in AggiungiAlCarrelloServlet", e);
            throw new ServletException("Errore di configurazione del database per AggiungiAlCarrelloServlet.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("utenteCorrente");

        if (utente == null) {
            // Se l'utente non è loggato, reindirizza alla pagina di login
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Recupera i parametri dal form
        String idProdottoStr = request.getParameter("idProdotto");
        String tipoProdotto = request.getParameter("tipoProdotto");
        String quantitaStr = request.getParameter("quantita");

        int idProdotto;
        int quantita;

        try {
            idProdotto = Integer.parseInt(idProdottoStr);
            quantita = Integer.parseInt(quantitaStr);

            if (quantita <= 0) {
                request.setAttribute("messaggio", "Quantità non valida per l'aggiunta al carrello.");
                request.setAttribute("tipoMessaggio", "error");
                response.sendRedirect(request.getContextPath() + "/Piantedainterno.jsp"); // O la pagina di provenienza
                return;
            }

            // Crea un oggetto Carrello per il DAO
            Carrello carrelloItem = new Carrello();
            carrelloItem.setUtenteId(utente.getId());
            carrelloItem.setProdottoId(idProdotto);
            carrelloItem.setTipoProdotto(tipoProdotto);
            carrelloItem.setQuantita(quantita);

            // Aggiungi o aggiorna l'articolo nel database
            boolean success = carrelloDAO.aggiungiOAggiornaArticoloCarrello(carrelloItem);

            if (success) {
                request.setAttribute("messaggio", "Articolo aggiunto/aggiornato nel carrello!");
                request.setAttribute("tipoMessaggio", "success");
                LOGGER.info("Articolo (ID Prodotto: " + idProdotto + ", Tipo: " + tipoProdotto + ") aggiunto/aggiornato nel DB per utente: " + utente.getId());
            } else {
                request.setAttribute("messaggio", "Si è verificato un problema durante l'aggiunta/aggiornamento dell'articolo.");
                request.setAttribute("tipoMessaggio", "error");
                LOGGER.warning("Fallimento nell'aggiunta/aggiornamento articolo nel DB per utente: " + utente.getId());
            }

        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Errore nel parsing dei parametri del carrello: " + e.getMessage());
            request.setAttribute("messaggio", "Dati prodotto non validi. Impossibile aggiungere al carrello.");
            request.setAttribute("tipoMessaggio", "error");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'aggiunta/aggiornamento del carrello: " + e.getMessage(), e);
            request.setAttribute("messaggio", "Errore del database durante l'aggiunta al carrello.");
            request.setAttribute("tipoMessaggio", "error");
        } finally {
            // Dopo ogni operazione, reindirizza sempre alla VisualizzaCarrelloServlet.
            // Questa servlet si occuperà di caricare il carrello aggiornato dal DB e ricalcolare il totale.
            response.sendRedirect(request.getContextPath() + "/visualizzaCarrello");
        }
    }
}