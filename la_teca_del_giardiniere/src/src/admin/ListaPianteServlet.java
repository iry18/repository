package admin;


import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import la_teca_del_giardiniere.DAO.PianteDAO;
import la_teca_del_giardiniere.classes.Piante;
import util.ServletUtils; 

@WebServlet("/admin/ListaPianteServlet")
public class ListaPianteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ListaPianteServlet.class.getName());

    private PianteDAO pianteDAO;

    public ListaPianteServlet() {
        super();
    }

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            pianteDAO = new PianteDAO();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "ERRORE CRITICO: Impossibile inizializzare PianteDAO in ListaPianteServlet.", e);
            throw new ServletException("Errore di inizializzazione del database.", e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!ServletUtils.checkAuthenticationAndAuthorization(request, response)) {
            return;
        }

        List<Piante> listaPiante = new ArrayList<>();
        try {
            listaPiante = pianteDAO.getAllPiante();
            LOGGER.info("Numero di piante recuperate dal database per la lista: " + listaPiante.size());
            request.setAttribute("listaPiante", listaPiante);

            // Recupera e pulisce i messaggi di sessione
            String messaggio = (String) request.getSession().getAttribute("messaggio");
            String tipoMessaggio = (String) request.getSession().getAttribute("tipoMessaggio");
            if (messaggio != null) {
                request.setAttribute("messaggio", messaggio);
                request.setAttribute("tipoMessaggio", tipoMessaggio);
                request.getSession().removeAttribute("messaggio");
                request.getSession().removeAttribute("tipoMessaggio");
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante il recupero di tutte le piante per la lista.", e);
            request.setAttribute("messaggio", "Errore database nel caricamento delle piante: " + e.getMessage());
            request.setAttribute("tipoMessaggio", "error");
            request.setAttribute("listaPiante", new ArrayList<>()); // Ensures JSP doesn't get null
        }
        request.getRequestDispatcher("/WEB-INF/jsp/admin/listaPianteAdmin.jsp").forward(request, response);
        }
    
    // Non sono previste operazioni POST dirette su questa servlet,
    // gestisce solo la visualizzazione e i reindirizzamenti.
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Metodo POST non supportato per questa risorsa. Utilizzare GET.");
    }
}