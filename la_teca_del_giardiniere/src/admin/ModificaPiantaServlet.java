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

@WebServlet("/admin/ModificaPiantaServlet") // Changed URL to ModificaPiantaServlet
public class ModificaPiantaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ModificaPiantaServlet.class.getName());

    private PianteDAO pianteDAO;

    public ModificaPiantaServlet() {
        super();
    }

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            pianteDAO = new PianteDAO();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "ERRORE CRITICO: Impossibile inizializzare PianteDAO in ModificaPiantaServlet.", e);
            throw new ServletException("Errore di inizializzazione del database.", e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!ServletUtils.checkAuthenticationAndAuthorization(request, response)) {
            return;
        }

        // 1. Get the ID of the plant to edit
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            LOGGER.warning("ID pianta non fornito per la modifica.");
            request.getSession().setAttribute("messaggio", "ID pianta non specificato per la modifica.");
            request.getSession().setAttribute("tipoMessaggio", "error");
            response.sendRedirect(request.getContextPath() + "/admin/ListaPianteServlet");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            Piante piantaDaModificare = pianteDAO.getPiantaById(id); // Retrieve plant from DB

            if (piantaDaModificare == null) {
                LOGGER.warning("Pianta con ID " + id + " non trovata per la modifica.");
                request.getSession().setAttribute("messaggio", "Pianta non trovata per ID: " + id);
                request.getSession().setAttribute("tipoMessaggio", "error");
                response.sendRedirect(request.getContextPath() + "/admin/ListaPianteServlet");
                return;
            }

            // 2. Set attributes for the JSP
            request.setAttribute("modalita", "modifica"); // Set mode to "modifica"
            request.setAttribute("pianta", piantaDaModificare); // Pass the retrieved plant object to JSP
            LOGGER.info("Preparazione form per modifica pianta con ID: " + id);

            // Forward to the same form JSP
            request.getRequestDispatcher("/WEB-INF/jsp/admin/FormInserimentoPiante.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "ID pianta non valido per la modifica: " + idParam, e);
            request.getSession().setAttribute("messaggio", "ID pianta non valido.");
            request.getSession().setAttribute("tipoMessaggio", "error");
            response.sendRedirect(request.getContextPath() + "/admin/ListaPianteServlet");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante il recupero della pianta per la modifica (ID: " + idParam + ").", e);
            request.getSession().setAttribute("messaggio", "Errore database durante il caricamento della pianta per la modifica.");
            request.getSession().setAttribute("tipoMessaggio", "error");
            response.sendRedirect(request.getContextPath() + "/admin/ListaPianteServlet");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!ServletUtils.checkAuthenticationAndAuthorization(request, response)) {
            return;
        }

        String contextPath = request.getContextPath();
        List<String> errori = new ArrayList<>();
        Piante pianta = ServletUtils.buildPiantaFromRequest(request, errori); // Populates Piante object from request params

        // Get the ID from the hidden field
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            errori.add("ID della pianta mancante per l'aggiornamento.");
        } else {
            try {
                pianta.setId(Integer.parseInt(idParam));
            } catch (NumberFormatException e) {
                errori.add("ID della pianta non valido.");
            }
        }
        
        // When modifying, we should ideally keep the original dataInserimento
        // or ensure it's not overwritten unless explicitly changed.
        // For simplicity, we can fetch the existing plant to preserve dataInserimento
        // if it's not being explicitly set by the form.
        // If dataInserimento IS editable, you'd parse it from request.
        // For now, let's assume it's NOT editable and we need to preserve it from DB.
        if (pianta.getId() > 0) { // If ID is valid, try to fetch original dataInserimento
            try {
                Piante originalPianta = pianteDAO.getPiantaById(pianta.getId());
                if (originalPianta != null) {
                    pianta.setDataInserimento(originalPianta.getDataInserimento());
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Errore nel recupero della data di inserimento originale per l'aggiornamento.", e);
                errori.add("Errore nel recupero dei dati originali per l'aggiornamento.");
            }
        }

        if (!errori.isEmpty()) {
            request.setAttribute("erroriModifica", errori);
            request.setAttribute("pianta", pianta);
            request.setAttribute("modalita", "modifica"); // Stay in modifica mode on error
            LOGGER.warning("Errori di validazione durante la modifica della pianta: " + errori);
            request.getRequestDispatcher("/WEB-INF/jsp/admin/FormInserimentoPiante.jsp").forward(request, response);
            return;
        }

        try {
            pianteDAO.aggiornaPianta(pianta); // **Calls aggiornaPianta for updates**
            request.getSession().setAttribute("messaggio", "Pianta '" + pianta.getNomeComune() + "' aggiornata con successo!");
            request.getSession().setAttribute("tipoMessaggio", "success");
            LOGGER.info("Pianta '" + pianta.getNomeComune() + "' aggiornata con successo. Reindirizzamento alla lista.");
            response.sendRedirect(contextPath + "/admin/ListaPianteServlet");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'aggiornamento della pianta: " + pianta.getNomeComune(), e);
            List<String> sqlErrors = new ArrayList<>();
            sqlErrors.add("Errore del database durante l'aggiornamento: " + e.getMessage());
            request.setAttribute("erroriModifica", sqlErrors);
            request.setAttribute("pianta", pianta);
            request.setAttribute("modalita", "modifica"); // Stay in modifica mode on error
            request.getRequestDispatcher("/WEB-INF/jsp/admin/FormInserimentoPiante.jsp").forward(request, response);
        }
    }
}