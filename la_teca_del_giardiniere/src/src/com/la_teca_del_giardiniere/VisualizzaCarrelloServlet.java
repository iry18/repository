package src.com.la_teca_del_giardiniere;

import src.src.src.src.com.la_teca_del_giardiniere.classes.Carrello;
import src.src.src.src.com.la_teca_del_giardiniere.classes.Utente;
import src.src.src.src.com.la_teca_del_giardiniere.dao.CarrelloDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/visualizzaCarrello")
public class VisualizzaCarrelloServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(VisualizzaCarrelloServlet.class.getName());

    private CarrelloDAO carrelloDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            carrelloDAO = new CarrelloDAO();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante l'inizializzazione del DAO", e);
            throw new ServletException("Errore di configurazione del database.", e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("utenteCorrente");

        if (utente == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            List<Carrello> articoliCarrello = carrelloDAO.getArticoliCarrelloByUtenteId(utente.getId());
            
            BigDecimal totaleCarrello = BigDecimal.ZERO;
            for (Carrello articolo : articoliCarrello) {
                totaleCarrello = totaleCarrello.add(articolo.getTotaleArticolo());
            }

            request.setAttribute("articoliCarrello", articoliCarrello);
            request.setAttribute("totaleCarrello", totaleCarrello);
            request.getRequestDispatcher("/WEB-INF/jsp/carrello.jsp").forward(request, response); // Assumi che carrello.jsp si trovi qui
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante il recupero del carrello per utente ID: " + utente.getId(), e);
            request.setAttribute("errore", "Errore durante il recupero del carrello.");
            request.getRequestDispatcher("/WEB-INF/jsp/errore.jsp").forward(request, response);
        }
    }
}