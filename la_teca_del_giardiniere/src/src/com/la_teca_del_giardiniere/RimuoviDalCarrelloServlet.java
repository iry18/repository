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

@WebServlet("/rimuoviDalCarrello")
public class RimuoviDalCarrelloServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(RimuoviDalCarrelloServlet.class.getName());

    private CarrelloDAO carrelloDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            // Inizializza il DAO con la tua classe MysqlDataSource
            MysqlDataSource dataSource = new MysqlDataSource();
            carrelloDAO = new CarrelloDAO(dataSource);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Errore durante l'inizializzazione del DAO in RimuoviDalCarrelloServlet", e);
            throw new ServletException("Errore di configurazione del database per RimuoviDalCarrelloServlet.", e);
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

        String messaggio = null;
        String tipoMessaggio = "error";

        try {
            if (carrelloIdParam == null || carrelloIdParam.isEmpty()) {
                throw new IllegalArgumentException("ID carrello mancante per la rimozione.");
            }

            int carrelloId = Integer.parseInt(carrelloIdParam);

            // Passa l'ID dell'utente al DAO per garantire che si rimuova solo l'articolo dell'utente corrente
            boolean removed = carrelloDAO.aggiornaQuantitaArticoloCarrello(carrelloId, utenteId);
            if (removed) {
                messaggio = "Articolo rimosso con successo dal carrello.";
                tipoMessaggio = "success";
            } else {
                messaggio = "Impossibile rimuovere l'articolo. Articolo non trovato o non appartenente all'utente.";
            }

        } catch (NumberFormatException e) {
            messaggio = "ID carrello non valido.";
            LOGGER.log(Level.WARNING, "Errore di formato numero in RimuoviDalCarrelloServlet: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            messaggio = e.getMessage();
            LOGGER.log(Level.WARNING, "Errore di parametro in RimuoviDalCarrelloServlet: " + e.getMessage(), e);
        } catch (SQLException e) {
            messaggio = "Si è verificato un errore durante la rimozione dell'articolo dal database.";
            LOGGER.log(Level.SEVERE, "Errore SQL durante la rimozione dal carrello: " + e.getMessage(), e);
        } catch (Exception e) {
            messaggio = "Si è verificato un errore inaspettato durante la rimozione dal carrello.";
            LOGGER.log(Level.SEVERE, "Errore generico in RimuoviDalCarrelloServlet: " + e.getMessage(), e);
        }

        // Imposta il messaggio nella request per la VisualizzaCarrelloServlet
        request.setAttribute("messaggio", messaggio);
        request.setAttribute("tipoMessaggio", tipoMessaggio);

        // Inoltra la richiesta alla VisualizzaCarrelloServlet
        request.getRequestDispatcher("/visualizzaCarrello").forward(request, response);
    }
}