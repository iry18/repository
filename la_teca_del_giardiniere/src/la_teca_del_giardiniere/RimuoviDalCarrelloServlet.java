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

@WebServlet("/rimuoviDalCarrello")
public class RimuoviDalCarrelloServlet extends HttpServlet {
    // Add the serialVersionUID here
    private static final long serialVersionUID = 1L; 

    private static final Logger LOGGER = Logger.getLogger(RimuoviDalCarrelloServlet.class.getName());
    private CarrelloDAO carrelloDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            // Inizializza il DAO una sola volta all'avvio della servlet
            carrelloDAO = new CarrelloDAO();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante l'inizializzazione di CarrelloDAO in RimuoviDalCarrelloServlet", e);
            throw new ServletException("Errore di configurazione del database per RimuoviDalCarrelloServlet.", e);
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

        String carrelloIdStr = request.getParameter("carrelloId");

        try {
            int carrelloId = Integer.parseInt(carrelloIdStr);
            int utenteId = utente.getId(); // Ottieni l'ID dell'utente dalla sessione per sicurezza

            // Rimuovi l'articolo dal database
            boolean rimossoDB = carrelloDAO.rimuoviArticoloCarrello(carrelloId, utenteId);

            if (rimossoDB) {
                request.setAttribute("messaggio", "Articolo rimosso con successo dal carrello.");
                request.setAttribute("tipoMessaggio", "success");
                LOGGER.info("Articolo carrello rimosso dal database con ID: " + carrelloId + " per utente: " + utenteId);
            } else {
                request.setAttribute("messaggio", "Articolo non trovato nel carrello o non appartenente all'utente.");
                request.setAttribute("tipoMessaggio", "error");
                LOGGER.warning("Tentativo di rimuovere un articolo non presente nel database o non appartenente all'utente con ID: " + carrelloId + " per utente: " + utenteId);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("messaggio", "ID articolo non valido per la rimozione.");
            request.setAttribute("tipoMessaggio", "error");
            LOGGER.log(Level.WARNING, "Errore nel parsing ID per rimozione dal carrello: " + e.getMessage(), e);
        } catch (SQLException e) {
            request.setAttribute("messaggio", "Errore del database durante la rimozione dell'articolo dal carrello.");
            request.setAttribute("tipoMessaggio", "error");
            LOGGER.log(Level.SEVERE, "Errore SQL durante la rimozione dell'articolo dal carrello: " + e.getMessage(), e);
        } finally {
            // Reindirizza sempre alla VisualizzaCarrelloServlet per ricaricare il carrello e il totale
            response.sendRedirect(request.getContextPath() + "/visualizzaCarrello");
        }
    }
}