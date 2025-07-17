package la_teca_del_giardiniere;

import la_teca_del_giardiniere.DAO.CarrelloDAO;
import la_teca_del_giardiniere.classes.Utente;
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

@WebServlet("/aggiornaQuantitaCarrello")
public class AggiornaQuantitaCarrelloServlet extends HttpServlet {
    // Add the serialVersionUID here
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(AggiornaQuantitaCarrelloServlet.class.getName());
    private CarrelloDAO carrelloDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            // Initialize the DAO once when the servlet starts
            carrelloDAO = new CarrelloDAO();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante l'inizializzazione di CarrelloDAO in AggiornaQuantitaCarrelloServlet", e);
            throw new ServletException("Errore di configurazione del database per AggiornaQuantitaCarrelloServlet.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("utenteCorrente");

        if (utente == null) {
            // If the user is not logged in, redirect to the login page
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String carrelloIdStr = request.getParameter("carrelloId");
        String quantitaStr = request.getParameter("quantita");

        try {
            int carrelloId = Integer.parseInt(carrelloIdStr);
            int nuovaQuantita = Integer.parseInt(quantitaStr);
            int utenteId = utente.getId(); // Get user ID from session for security

            boolean success = false;

            if (nuovaQuantita <= 0) {
                // If the new quantity is 0 or less, remove the item
                success = carrelloDAO.rimuoviArticoloCarrello(carrelloId, utenteId);
                if (success) {
                    request.setAttribute("messaggio", "Articolo rimosso dal carrello.");
                    request.setAttribute("tipoMessaggio", "success");
                    LOGGER.info("Articolo carrello rimosso (ID: " + carrelloId + ") per utente: " + utenteId);
                } else {
                    request.setAttribute("messaggio", "Impossibile rimuovere l'articolo dal carrello.");
                    request.setAttribute("tipoMessaggio", "error");
                    LOGGER.warning("Fallimento rimozione articolo carrello (ID: " + carrelloId + ") per utente: " + utenteId);
                }
            } else {
                // Otherwise, update the quantity
                success = carrelloDAO.aggiornaQuantitaArticoloCarrello(carrelloId, utenteId, nuovaQuantita);
                if (success) {
                    request.setAttribute("messaggio", "Quantità aggiornata con successo.");
                    request.setAttribute("tipoMessaggio", "success");
                    LOGGER.info("Quantità carrello aggiornata per ID: " + carrelloId + " a " + nuovaQuantita + " per utente: " + utenteId);
                } else {
                    request.setAttribute("messaggio", "Impossibile aggiornare la quantità dell'articolo.");
                    request.setAttribute("tipoMessaggio", "error");
                    LOGGER.warning("Fallimento aggiornamento quantità articolo carrello (ID: " + carrelloId + ") a " + nuovaQuantita + " per utente: " + utenteId);
                }
            }

        } catch (NumberFormatException e) {
            request.setAttribute("messaggio", "Quantità o ID articolo non validi.");
            request.setAttribute("tipoMessaggio", "error");
            LOGGER.log(Level.WARNING, "Errore nel parsing per aggiornamento quantità: " + e.getMessage(), e);
        } catch (SQLException e) {
            request.setAttribute("messaggio", "Errore del database durante l'aggiornamento del carrello.");
            request.setAttribute("tipoMessaggio", "error");
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'aggiornamento del carrello: " + e.getMessage(), e);
        } finally {
            // Always redirect to the VisualizzaCarrelloServlet to reload the cart and total from the DB
            response.sendRedirect(request.getContextPath() + "/visualizzaCarrello");
        }
    }
}