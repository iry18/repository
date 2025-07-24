package admin; // Assuming 'admin' package for Servlets

import java.io.IOException;
import java.math.BigDecimal;
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

import la_teca_del_giardiniere.DAO.AccessoriDAO;
import la_teca_del_giardiniere.classes.Accessori;
import util.ServletUtils; // Assuming this utility class exists for auth and buildAccessorioFromRequest

@WebServlet("/admin/ModificaAccessorioServlet")
public class ModificaAccessorioServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ModificaAccessorioServlet.class.getName());
    private AccessoriDAO accessoriDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            accessoriDAO = new AccessoriDAO();
            LOGGER.info("AccessoriDAO inizializzato con successo in ModificaAccessorioServlet.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "ERRORE CRITICO: Impossibile inizializzare AccessoriDAO in ModificaAccessorioServlet.", e);
            throw new ServletException("Errore di inizializzazione del database per gli accessori.", e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!ServletUtils.checkAuthenticationAndAuthorization(request, response)) {
            return;
        }

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            LOGGER.warning("ID accessorio non fornito per la modifica.");
            request.getSession().setAttribute("messaggio", "ID accessorio non specificato per la modifica.");
            request.getSession().setAttribute("tipoMessaggio", "error");
            response.sendRedirect(request.getContextPath() + "/admin/ListaAccessoriServlet");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            Accessori accessorioDaModificare = accessoriDAO.getAccessorioByaccessorio_id(id);

            if (accessorioDaModificare == null) {
                LOGGER.warning("Accessorio con ID " + id + " non trovato per la modifica.");
                request.getSession().setAttribute("messaggio", "Accessorio non trovato per ID: " + id);
                request.getSession().setAttribute("tipoMessaggio", "error");
                response.sendRedirect(request.getContextPath() + "/admin/ListaAccessoriServlet");
                return;
            }

            request.setAttribute("modalita", "modifica");
            request.setAttribute("accessorio", accessorioDaModificare); // Pass the existing object to pre-fill the form
            LOGGER.info("Preparazione form per modifica accessorio con ID: " + id);
            request.getRequestDispatcher("/WEB-INF/views/admin/FormInserimentoAccessori.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "ID accessorio non valido per la modifica: " + idParam, e);
            request.getSession().setAttribute("messaggio", "ID accessorio non valido.");
            request.getSession().setAttribute("tipoMessaggio", "error");
            response.sendRedirect(request.getContextPath() + "/admin/ListaAccessoriServlet");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante il recupero dell'accessorio per la modifica (ID: " + idParam + ").", e);
            request.getSession().setAttribute("messaggio", "Errore database durante il caricamento dell'accessorio per la modifica.");
            request.getSession().setAttribute("tipoMessaggio", "error");
            response.sendRedirect(request.getContextPath() + "/admin/ListaAccessoriServlet");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!ServletUtils.checkAuthenticationAndAuthorization(request, response)) {
            return;
        }

        List<String> errori = new ArrayList<>();
        Accessori accessorio = buildAccessorioFromRequest(request, errori); // Re-use the parsing logic

        // Get ID from hidden field and set it in the accessorio object
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            errori.add("ID dell'accessorio mancante per l'aggiornamento.");
        } else {
            try {
                accessorio.setAccessorio_id(Integer.parseInt(idStr));
            } catch (NumberFormatException e) {
                errori.add("L'ID dell'accessorio non è in un formato valido.");
            }
        }
        
        // Preserve original dataInserimento as it's typically not updated via form
        if (accessorio.getAccessorio_id() != null) { // Only if ID is valid
            try {
                Accessori originalAccessorio = accessoriDAO.getAccessorioByaccessorio_id(accessorio.getAccessorio_id());
                if (originalAccessorio != null) {
                    accessorio.setDataInserimento(originalAccessorio.getDataInserimento());
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Errore nel recupero della data di inserimento originale per l'aggiornamento.", e);
                errori.add("Errore nel recupero della data di inserimento originale.");
            }
        }

        if (!errori.isEmpty()) {
            request.setAttribute("errori", errori);
            request.setAttribute("accessorio", accessorio); // Re-populate form with entered data
            request.setAttribute("modalita", "modifica"); // Stay in modifica mode
            LOGGER.warning("Errori di validazione durante la modifica dell'accessorio: " + errori);
            request.getRequestDispatcher("/WEB-INF/views/admin/FormInserimentoAccessori.jsp").forward(request, response);
            return;
        }

        try {
            accessoriDAO.aggiungiAccessori(accessorio);
            request.getSession().setAttribute("messaggio", "Accessorio '" + accessorio.getNome() + "' aggiornato con successo!");
            request.getSession().setAttribute("tipoMessaggio", "success");
            LOGGER.info("Accessorio '" + accessorio.getNome() + "' aggiornato con successo. Reindirizzamento alla lista.");
            response.sendRedirect(request.getContextPath() + "/admin/ListaAccessoriServlet");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'aggiornamento dell'accessorio: " + accessorio.getNome(), e);
            errori.add("Errore database: " + e.getMessage());
            request.setAttribute("errori", errori);
            request.setAttribute("accessorio", accessorio);
            request.setAttribute("modalita", "modifica");
            request.getRequestDispatcher("/WEB-INF/views/admin/FormInserimentoAccessori.jsp").forward(request, response);
        }
    }

    // Helper method to build Accessori object from request parameters
    // This was previously part of your main AccessoriServlet's doPost
    private Accessori buildAccessorioFromRequest(HttpServletRequest request, List<String> errori) {
        Accessori accessorio = new Accessori();

        // Recupero e validazione di tutti i campi
        String nome = request.getParameter("nome");
        if (nome == null || nome.trim().isEmpty()) {
            errori.add("Il Nome è obbligatorio.");
        } else {
            accessorio.setNome(nome);
        }

        String prezzoStr = request.getParameter("prezzo");
        if (prezzoStr == null || prezzoStr.trim().isEmpty()) {
            errori.add("Il Prezzo è obbligatorio.");
        } else {
            try {
                prezzoStr = prezzoStr.replace(',', '.'); // Sostituisci la virgola con il punto per la conversione
                accessorio.setPrezzo(new BigDecimal(prezzoStr));
            } catch (NumberFormatException e) {
                errori.add("Il formato del Prezzo non è valido (es. 12.99).");
            }
        }

        String disponibilitaStr = request.getParameter("disponibilita");
        if (disponibilitaStr == null || disponibilitaStr.trim().isEmpty()) {
            errori.add("La Disponibilità è obbligatoria.");
        } else {
            try {
                accessorio.setDisponibilita(Integer.parseInt(disponibilitaStr));
            } catch (NumberFormatException e) {
                errori.add("Il formato della Disponibilità non è valido.");
            }
        }

        accessorio.setDescrizioneBreve(request.getParameter("descrizione"));
        accessorio.setDimensioni(request.getParameter("dimensioni"));
        accessorio.setImmagine(request.getParameter("urlImmagine"));

        // DataInserimento is handled in doPost of this servlet (preserved)
        
        return accessorio;
    }
}