package src.com.la_teca_del_giardiniere;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import src.com.la_teca_del_giardiniere.classes.Utente;
import src.com.la_teca_del_giardiniere.dao.UtenteDAO;

@WebServlet("/VisualizzaUtentiServlet")
public class VisualizzaUtentiServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UtenteDAO utenteDao;

    @Override
    public void init() throws ServletException {
        try {
            utenteDao = new UtenteDAO();
        } catch (SQLException e) {
            throw new ServletException("Errore durante l'inizializzazione del DAO", e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Utente> listaUtenti = utenteDao.getAllUtentiConRuoli(); // Usa il metodo corretto
            request.setAttribute("listaUtenti", listaUtenti);
            request.getRequestDispatcher("/visualizza_utenti.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestisci l'errore (es. mostra una pagina di errore)
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response); // Solitamente si usa GET per visualizzare dati
    }
}