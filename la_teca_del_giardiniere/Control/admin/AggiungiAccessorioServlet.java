package admin; 

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
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

@WebServlet("/admin/AggiungiAccessorioServlet")
public class AggiungiAccessorioServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(AggiungiAccessorioServlet.class.getName());
    private AccessoriDAO accessoriDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            accessoriDAO = new AccessoriDAO();
            LOGGER.info("AccessoriDAO inizializzato con successo in AggiungiAccessorioServlet.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "ERRORE CRITICO: Impossibile inizializzare AccessoriDAO in AggiungiAccessorioServlet.", e);
            throw new ServletException("Errore di inizializzazione del database per gli accessori.", e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!ServletUtils.checkAuthenticationAndAuthorization(request, response)) {
            return;
        }
        
        request.setAttribute("modalita", "inserisci");
        request.setAttribute("accessorio", new Accessori()); // Pass an empty object for a fresh form
        LOGGER.info("Preparazione form per nuovo inserimento accessorio.");
        request.getRequestDispatcher("/WEB-INF/views/admin/FormInserimentoAccessori.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!ServletUtils.checkAuthenticationAndAuthorization(request, response)) {
            return;
        }

        List<String> errori = new ArrayList<>();
        Accessori accessorio = buildAccessorioFromRequest(request, errori); // Re-use the parsing logic

        // Only set dataInserimento for new items
        accessorio.setDataInserimento(new Timestamp(System.currentTimeMillis()));

        if (!errori.isEmpty()) {
            request.setAttribute("errori", errori);
            request.setAttribute("accessorio", accessorio); // Re-populate form with entered data
            request.setAttribute("modalita", "inserisci");
            LOGGER.warning("Errori di validazione durante l'aggiunta dell'accessorio: " + errori);
            request.getRequestDispatcher("/WEB-INF/views/admin/FormInserimentoAccessori.jsp").forward(request, response);
            return;
        }

        try {
            accessoriDAO.aggiungiAccessori(accessorio);
            request.getSession().setAttribute("messaggio", "Accessorio '" + accessorio.getNome() + "' aggiunto con successo!");
            request.getSession().setAttribute("tipoMessaggio", "success");
            LOGGER.info("Accessorio '" + accessorio.getNome() + "' aggiunto con successo. Reindirizzamento alla lista.");
            response.sendRedirect(request.getContextPath() + "/admin/ListaAccessoriServlet");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'aggiunta dell'accessorio: " + accessorio.getNome(), e);
            errori.add("Errore database: " + e.getMessage());
            request.setAttribute("errori", errori);
            request.setAttribute("accessorio", accessorio);
            request.setAttribute("modalita", "inserisci");
            request.getRequestDispatcher("/WEB-INF/views/admin/FormInserimentoAccessori.jsp").forward(request, response);
        }
    }

    private Accessori buildAccessorioFromRequest(HttpServletRequest request, List<String> errori) {
        Accessori accessorio = new Accessori();

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
        accessorio.setImmagine(request.getParameter("urlImmagine")); // Recupera il nome del file immagine

        
        return accessorio;
    }
}