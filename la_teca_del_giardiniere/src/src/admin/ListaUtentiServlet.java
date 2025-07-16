package admin; // Adjust package as necessary

import la_teca_del_giardiniere.DAO.UtenteDAO;
import la_teca_del_giardiniere.classes.Utente;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/admin/ListaUtentiServlet")
public class ListaUtentiServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ListaUtentiServlet.class.getName());

    private UtenteDAO utenteDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            utenteDAO = new UtenteDAO();
            LOGGER.info("UtenteDAO inizializzato con successo.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore nell'inizializzazione di UtenteDAO", e);
            throw new ServletException("Errore nel caricamento delle risorse DAO", e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            handleDelete(request, response);
        } else {
            handleList(request, response);
        }
    }

    private void handleList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Utente> listaUtenti = utenteDAO.getAllUtentiConRuoli();
            request.setAttribute("listaUtenti", listaUtenti);
            request.getRequestDispatcher("/admin/ListaUtenti.jsp").forward(request, response);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore nel recupero della lista utenti", e);
            request.setAttribute("messaggio", "Errore nel caricamento degli utenti: " + e.getMessage());
            request.setAttribute("tipoMessaggio", "error");
            request.getRequestDispatcher("/admin/adminHome.jsp").forward(request, response);
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idUtente = 0;
        try {
            idUtente = Integer.parseInt(request.getParameter("id"));
            // !!! CORRECTED LINE: Call deleteUtente instead of updateUtente !!!
            boolean deleted = utenteDAO.deleteUtente(idUtente);

            if (deleted) {
                request.getSession().setAttribute("messaggio", "Utente eliminato con successo!");
                request.getSession().setAttribute("tipoMessaggio", "success");
            } else {
                request.getSession().setAttribute("messaggio", "Impossibile eliminare l'utente. Potrebbe non esistere.");
                request.getSession().setAttribute("tipoMessaggio", "error");
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "ID utente non valido per l'eliminazione", e);
            request.getSession().setAttribute("messaggio", "ID utente non valido.");
            request.getSession().setAttribute("tipoMessaggio", "error");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'eliminazione dell'utente con ID: " + idUtente, e);
            request.getSession().setAttribute("messaggio", "Errore del database durante l'eliminazione: " + e.getMessage());
            request.getSession().setAttribute("tipoMessaggio", "error");
        }
        response.sendRedirect(request.getContextPath() + "/admin/ListaUtentiServlet"); // Redirect to refresh the list
    }
}
