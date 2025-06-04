package src.com.la_teca_del_giardiniere;

import src.com.la_teca_del_giardiniere.classes.Carrello;
import src.com.la_teca_del_giardiniere.classes.Utente;
import src.com.la_teca_del_giardiniere.dao.CarrelloDAO;
import src.com.la_teca_del_giardiniere.dao.PianteDAO; // Assicurati che anche PianteDAO sia aggiornato
import src.com.la_teca_del_giardiniere.dao.AccessoriDAO; // Assicurati che anche AccessoriDAO sia aggiornato

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/aggiungiAlCarrello")
public class AggiungiAlCarrelloServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(AggiungiAlCarrelloServlet.class.getName());

    private CarrelloDAO carrelloDAO;
    private PianteDAO pianteDAO;
    private AccessoriDAO accessoriDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            // I DAO ora si auto-configurano con MysqlDataSource al momento dell'istanziazione
            carrelloDAO = new CarrelloDAO();
            pianteDAO = new PianteDAO();
            accessoriDAO = new AccessoriDAO();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante l'inizializzazione del DAO", e);
            throw new ServletException("Errore di configurazione del database.", e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("utenteCorrente");

        if (utente == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String tipoArticolo = request.getParameter("tipo");
        int articoloId = 0;
        int quantita = 1;

        try {
            articoloId = Integer.parseInt(request.getParameter("id"));
            if (request.getParameter("quantita") != null) {
                quantita = Integer.parseInt(request.getParameter("quantita"));
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "ID articolo o quantità non validi: ", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID articolo o quantità non validi.");
            return;
        }

        if (quantita <= 0) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "La quantità deve essere maggiore di zero.");
            return;
        }

        try {
            // Nota: Non è più necessario recuperare nome e prezzo qui, CarrelloDAO lo fa con la query JOIN
            // La `Carrello` class ha campi per nome e prezzo per la visualizzazione nella JSP.
            Carrello carrelloItem = new Carrello();
            carrelloItem.setUtenteId(utente.getId());
            carrelloItem.setProdottoId(articoloId);
            carrelloItem.setQuantita(quantita);
            carrelloItem.setDataAggiunta(new Timestamp(System.currentTimeMillis()));

            if ("PIANTA".equalsIgnoreCase(tipoArticolo)) {
                carrelloItem.setTipoProdotto("PIANTA");
            } else if ("ACCESSORIO".equalsIgnoreCase(tipoArticolo)) {
                carrelloItem.setTipoProdotto("ACCESSORIO");
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Tipo articolo non valido. Deve essere 'PIANTA' o 'ACCESSORIO'.");
                return;
            }

            carrelloDAO.aggiungiOAggiornaArticoloCarrello(carrelloItem);

            response.sendRedirect(request.getContextPath() + "/visualizzaCarrello");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'aggiunta al carrello.", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore durante l'aggiunta al carrello.");
        }
    }
}