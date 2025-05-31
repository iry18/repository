package src.com.la_teca_del_giardiniere;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// Importa la classe Utente che hai rinominato
import src.com.la_teca_del_giardiniere.classes.Utente; // <--- CAMBIATO QUI!
import src.com.la_teca_del_giardiniere.dao.UtenteDAO;

@WebServlet("/admin/utentiServlet") // Mantieni l'URL del mapping per ora
public class UtenteServlet extends HttpServlet { // <--- CAMBIATO QUI: UtentiServlet
    private static final long serialVersionUID = 1L;
    private UtenteDAO utenteDAO;

    public UtenteServlet() { // <--- CAMBIATO QUI: UtentiServlet
        super();
        try {
            utenteDAO = new UtenteDAO();
        } catch (SQLException e) {
            e.printStackTrace(); // Gestire l'errore in modo più appropriato (es. log)
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            // Recupera l'oggetto Utente loggato dalla sessione
            Utente utenteLoggato = (Utente) session.getAttribute("loggedInUser"); // <--- Recupera l'oggetto Utente

            List<String> ruoli = null;
            if (utenteLoggato != null) {
                ruoli = utenteLoggato.getRuoli(); // <--- Usa il metodo getRuoli() della classe Utente
            }

            // Ho rimosso il blocco di cast e controllo per ruoliObj instanceof List<?>
            // dato che ora ci affidiamo al metodo getRuoli() della classe Utente.
            // Questo assume che getRuoli() restituisca sempre una List<String> (anche vuota)

            if (ruoli != null && ruoli.contains("amministratore")) {
                try {
                    List<Utente> listaUtenti = utenteDAO.getAllUtentiConRuoli(); 
                    request.setAttribute("listaUtenti", listaUtenti);
                    request.getRequestDispatcher("/admin/utenti.jsp").forward(request, response);
                } catch (SQLException e) {
                    e.printStackTrace();
                    request.setAttribute("messaggio", "Errore nel recupero degli utenti.");
                    request.setAttribute("tipoMessaggio", "error");
                    request.getRequestDispatcher("/admin/utenti.jsp").forward(request, response);
                }
            } else {
                // Utente non autorizzato
                response.sendRedirect(request.getContextPath() + "/accesso_negato.html");
            }
        } else {
            // Utente non loggato
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}