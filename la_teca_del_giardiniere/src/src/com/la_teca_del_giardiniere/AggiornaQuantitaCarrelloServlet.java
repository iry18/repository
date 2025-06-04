package src.com.la_teca_del_giardiniere;

import src.com.la_teca_del_giardiniere.classes.Utente;
import src.com.la_teca_del_giardiniere.dao.CarrelloDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/aggiornaQuantitaCarrello")
public class AggiornaQuantitaCarrelloServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(AggiornaQuantitaCarrelloServlet.class.getName());

    private CarrelloDAO carrelloDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            // Inizializza il DAO con la tua classe MysqlDataSource
            MysqlDataSource dataSource = new MysqlDataSource();
            carrelloDAO = new CarrelloDAO(dataSource);
        } catch (Exception e) { // Cattura Exception se MysqlDataSource può lanciare altre eccezioni
            LOGGER.log(Level.SEVERE, "Errore durante l'inizializzazione del DAO in AggiornaQuantitaCarrelloServlet", e);
            throw new ServletException("Errore di configurazione del database per AggiornaQuantitaCarrelloServlet.", e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utente utenteCorrente = (Utente) session.getAttribute("utenteCorrente");

        if (utenteCorrente == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?message=Accedi per gestire il carrello.");
            return;
        }

        int utenteId = utenteCorrente.getId(); // Ottieni l'ID dell'utente loggato
        String carrelloIdParam = request.getParameter("carrelloId");
        String quantitaParam = request.getParameter("quantita");

        String messaggio = null;
        String tipoMessaggio = "error"; // Default a errore

        try {
            if (carrelloIdParam == null || quantitaParam == null || carrelloIdParam.isEmpty() || quantitaParam.isEmpty()) {
                throw new IllegalArgumentException("Parametri mancanti per l'aggiornamento della quantità.");
            }

            int carrelloId = Integer.parseInt(carrelloIdParam);
            int nuovaQuantita = Integer.parseInt(quantitaParam);

            if (nuovaQuantita <= 0) {
                // Se la quantità è zero o negativa, rimuovi l'articolo dal carrello
                boolean removed = carrelloDAO.rimuoviArticoloCarrello(carrelloId, utenteId); // Passa utenteId per sicurezza
                if (removed) {
                    messaggio = "Articolo rimosso dal carrello.";
                    tipoMessaggio = "success";
                } else {
                    messaggio = "Impossibile rimuovere l'articolo. Articolo non trovato o non appartenente all'utente.";
                }
            } else {
                boolean updated = carrelloDAO.aggiornaQuantitaArticoloCarrello(carrelloId, utenteId, nuovaQuantita); // Passa utenteId per sicurezza
                if (updated) {
                    messaggio = "Quantità aggiornata con successo.";
                    tipoMessaggio = "success";
                } else {
                    messaggio = "Impossibile aggiornare la quantità. Articolo non trovato o non appartenente all'utente.";
                }
            }

        } catch (NumberFormatException e) {
            messaggio = "Quantità o ID carrello non validi.";
            LOGGER.log(Level.WARNING, "Errore di formato numero in AggiornaQuantitaCarrelloServlet: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            messaggio = e.getMessage();
            LOGGER.log(Level.WARNING, "Errore di parametro in AggiornaQuantitaCarrelloServlet: " + e.getMessage(), e);
        } catch (SQLException e) {
            messaggio = "Si è verificato un errore durante l'aggiornamento della quantità nel database.";
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'aggiornamento del carrello: " + e.getMessage(), e);
        } catch (Exception e) {
            messaggio = "Si è verificato un errore inaspettato durante l'aggiornamento del carrello.";
            LOGGER.log(Level.SEVERE, "Errore generico in AggiornaQuantitaCarrelloServlet: " + e.getMessage(), e);
        }

        // Imposta il messaggio nella request per la VisualizzaCarrelloServlet
        request.setAttribute("messaggio", messaggio);
        request.setAttribute("tipoMessaggio", tipoMessaggio);

        // Inoltra la richiesta alla VisualizzaCarrelloServlet
        request.getRequestDispatcher("/visualizzaCarrello").forward(request, response);
    }
}