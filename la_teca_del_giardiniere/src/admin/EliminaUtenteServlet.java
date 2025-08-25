package admin;

import la_teca_del_giardiniere.DAO.UtenteDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/admin/EliminaUtenteServlet")
public class EliminaUtenteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(EliminaUtenteServlet.class.getName());

    private UtenteDAO utenteDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            utenteDAO = new UtenteDAO();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore nell'inizializzazione di UtenteDAO", e);
            throw new ServletException("Errore nel caricamento delle risorse DAO", e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idUtente = 0;
        try {
            idUtente = Integer.parseInt(request.getParameter("id"));
            boolean deleted = utenteDAO.deleteUtente(idUtente);

            if (deleted) {
                request.getSession().setAttribute("messaggio", "Utente eliminato con successo!");
                request.getSession().setAttribute("tipoMessaggio", "success");
            } else {
                request.getSession().setAttribute("messaggio", "Impossibile eliminare l'utente. Potrebbe non esistere.");
                request.getSession().setAttribute("tipoMessaggio", "error");
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "ID utente non valido per l'eliminazione.", e);
            request.getSession().setAttribute("messaggio", "ID utente non valido per l'eliminazione.");
            request.getSession().setAttribute("tipoMessaggio", "error");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'eliminazione dell'utente con ID: " + idUtente, e);
            request.getSession().setAttribute("messaggio", "Errore database durante l'eliminazione: " + e.getMessage());
            request.getSession().setAttribute("tipoMessaggio", "error");
        }
        
        response.sendRedirect(request.getContextPath() + "/admin/ListaUtentiServlet");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}