package admin;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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

@WebServlet("/admin/AggiungiPiantaServlet")
public class AggiungiPiantaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(AggiungiPiantaServlet.class.getName());

    private PianteDAO pianteDAO;

    public AggiungiPiantaServlet() {
        super();
    }

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            pianteDAO = new PianteDAO();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "ERRORE CRITICO: Impossibile inizializzare PianteDAO in AggiungiPiantaServlet.", e);
            throw new ServletException("Errore di inizializzazione del database.", e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!ServletUtils.checkAuthenticationAndAuthorization(request, response)) {
            return;
        }

        request.setAttribute("modalita", "inserisci");
        request.setAttribute("pianta", new Piante()); // Inizializza un oggetto pianta vuoto per il form
        LOGGER.info("Preparazione form per nuovo inserimento pianta.");
        request.getRequestDispatcher("/WEB-INF/jsp/admin/FormInserimentoPiante.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!ServletUtils.checkAuthenticationAndAuthorization(request, response)) {
            return;
        }

        String contextPath = request.getContextPath();
        List<String> errori = new ArrayList<>();
        Piante pianta = ServletUtils.buildPiantaFromRequest(request, errori);

        // Imposta la data di inserimento solo al momento dell'aggiunta
        pianta.setDataInserimento(Timestamp.valueOf(LocalDateTime.now()));

        if (!errori.isEmpty()) {
            request.setAttribute("erroriModifica", errori);
            request.setAttribute("pianta", pianta);
            request.setAttribute("modalita", "inserisci");
            LOGGER.warning("Errori di validazione durante l'aggiunta della pianta: " + errori);
            request.getRequestDispatcher("/WEB-INF/jsp/admin/FormInserimentoPiante.jsp").forward(request, response);
            return;
        }

        try {
            pianteDAO.aggiungiPianta(pianta);
            request.getSession().setAttribute("messaggio", "Pianta '" + pianta.getNomeComune() + "' aggiunta con successo!");
            request.getSession().setAttribute("tipoMessaggio", "success");
            LOGGER.info("Pianta '" + pianta.getNomeComune() + "' aggiunta con successo. Reindirizzamento alla lista.");
            response.sendRedirect(contextPath + "/admin/ListaPianteServlet");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'aggiunta della pianta: " + pianta.getNomeComune(), e);
            List<String> sqlErrors = new ArrayList<>();
            sqlErrors.add("Errore del database durante l'aggiunta: " + e.getMessage());
            request.setAttribute("erroriModifica", sqlErrors);
            request.setAttribute("pianta", pianta);
            request.setAttribute("modalita", "inserisci");
            request.getRequestDispatcher("/WEB-INF/jsp/admin/FormInserimentoPiante.jsp").forward(request, response);
        }
    }
}