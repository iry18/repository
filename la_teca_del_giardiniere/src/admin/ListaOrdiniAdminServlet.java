package admin;

import la_teca_del_giardiniere.DAO.OrdineDAO;
import la_teca_del_giardiniere.classes.Ordine;
import la_teca_del_giardiniere.classes.Utente;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/admin/listaOrdini")
public class ListaOrdiniAdminServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ListaOrdiniAdminServlet.class.getName());

    private OrdineDAO ordineDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            ordineDAO = new OrdineDAO();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante l'inizializzazione del DAO", e);
            throw new ServletException("Errore di configurazione del database.", e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("utenteCorrente");

        // 1. Verifica i permessi dell'utente
        if (utente == null || !isAdminOrVenditore(utente)) {
            response.sendRedirect(request.getContextPath() + "/accesso_negato.html");
            return;
        }

        try {
            // 2. Recupera la lista di tutti gli ordini dal database
            List<Ordine> listaOrdini = ordineDAO.getAllOrdini();
            
            // 3. Imposta la lista come attributo della richiesta
            request.setAttribute("listaOrdini", listaOrdini);
            
            // 4. Inoltra la richiesta alla pagina JSP per la visualizzazione
            request.getRequestDispatcher("/WEB-INF/jsp/admin/listaOrdini.jsp").forward(request, response);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante il recupero della lista degli ordini.", e);
            request.setAttribute("errore", "Si è verificato un errore durante il recupero degli ordini.");
            request.getRequestDispatcher("/WEB-INF/jsp/admin/erroreAdmin.jsp").forward(request, response);
        }
    }

    private boolean isAdminOrVenditore(Utente utente) {
        return "ADMIN".equals(utente.getRuoli()) || "VENDITORE".equals(utente.getRuoli());
    }
}