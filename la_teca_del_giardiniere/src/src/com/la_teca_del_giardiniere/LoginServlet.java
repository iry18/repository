package src.com.la_teca_del_giardiniere;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import src.com.la_teca_del_giardiniere.classes.registrazione;
import src.com.la_teca_del_giardiniere.dao.UtenteDAO;
import src.com.la_teca_del_giardiniere.util.PasswordHashing;


@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            registrazione utente = utenteDao.getUtenteByEmailWithRuoli(email);

            if (utente != null && PasswordHashing.checkPassword(password, utente.getPassword())) {
                
            	// Autenticazione
                HttpSession session = request.getSession();
                session.setAttribute("utenteId", utente.getUtente_id()); // Memorizza l'utente_id nella sessione
                session.setAttribute("email", utente.getEmail());       // Potresti memorizzare anche l'email
                session.setAttribute("ruoli", utente.getRuoli());     // Memorizza la lista dei ruoli nella sessione

                // Reindirizza l'utente alla pagina principale o alla sua area personale
                response.sendRedirect("index.html"); // Modifica con la tua pagina principale
            } else {
                // Autenticazione fallita
                request.setAttribute("erroreLogin", "Credenziali non valide.");
                request.getRequestDispatcher("login.jsp").forward(request, response); // Modifica con la tua pagina di login
            }

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("erroreLogin", "Errore durante l'accesso al database.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}