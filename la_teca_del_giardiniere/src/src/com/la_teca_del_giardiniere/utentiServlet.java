package src.com.la_teca_del_giardiniere;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import src.com.la_teca_del_giardiniere.classes.registrazione;
import src.com.la_teca_del_giardiniere.dao.UtenteDAO;

@WebServlet("/admin/utentiServlet")
public class utentiServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UtenteDAO utenteDAO;

    public utentiServlet() {
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
            List<String> ruoli = null;
            Object ruoliObj = session.getAttribute("ruoli");
            if (ruoliObj instanceof List<?>) {
                // È una lista, ora possiamo fare il cast sicuro
                List<?> tempRuoli = (List<?>) ruoliObj;
                // Verifica che tutti gli elementi della lista siano effettivamente String
                boolean allStrings = true;
                for (Object item : tempRuoli) {
                    if (!(item instanceof String)) {
                        allStrings = false;
                        break;
                    }
                }

                if (allStrings) {
                    ruoli = new ArrayList<>(); // Crea una nuova ArrayList di String
                    for (Object item : tempRuoli) {
                        ruoli.add((String) item); // Esegui un cast individuale sicuro
                    }
                } else {
                    // Gestisci il caso in cui la lista contiene elementi non-String
                    System.err.println("Errore: la lista dei ruoli nella sessione contiene elementi non-String.");
                    ruoli = null; // O una lista vuota, a seconda della tua logica
                }
            } // <--- QUESTA PARENTESI CHIUSA ERA IL PROBLEMA

            // Il resto della logica del doGet deve stare all'interno del blocco if (session != null)
            if (ruoli != null && ruoli.contains("amministratore")) {
                try {
                    List<registrazione> listaUtenti = utenteDAO.getAllUtentiConRuoli();
                    request.setAttribute("listaUtenti", listaUtenti);
                    request.getRequestDispatcher("/admin/utenti.jsp").forward(request, response);
                } catch (SQLException e) {
                    e.printStackTrace(); // Gestire l'errore in modo più appropriato (es. pagina di errore)
                    request.setAttribute("messaggio", "Errore nel recupero degli utenti.");
                    request.setAttribute("tipoMessaggio", "error");
                    request.getRequestDispatcher("/admin/utenti.jsp").forward(request, response);
                }
            } else {
                // Utente non autorizzato
                response.sendRedirect(request.getContextPath() + "/accesso_negato.html"); // Crea questa pagina
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