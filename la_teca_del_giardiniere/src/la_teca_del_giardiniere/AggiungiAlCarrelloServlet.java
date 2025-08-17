package la_teca_del_giardiniere;

import la_teca_del_giardiniere.DAO.PianteDAO;
import la_teca_del_giardiniere.DAO.AccessoriDAO;
import la_teca_del_giardiniere.classes.Piante;
import la_teca_del_giardiniere.classes.Accessori;
import la_teca_del_giardiniere.classes.RigaCarrello;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.math.BigDecimal;

@WebServlet("/aggiungiAlCarrello")
public class AggiungiAlCarrelloServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(AggiungiAlCarrelloServlet.class.getName());

    private PianteDAO pianteDAO;
    private AccessoriDAO accessoriDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            pianteDAO = new PianteDAO();
            accessoriDAO = new AccessoriDAO();
            LOGGER.info("PianteDAO e AccessoriDAO inizializzati con successo.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore critico durante l'inizializzazione dei DAO.", e);
            throw new ServletException("Impossibile inizializzare i DAO del database.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        String idProdottoStr = request.getParameter("productId");
        String tipoProdotto = request.getParameter("productType");
        String quantitaStr = request.getParameter("quantita");

        if (idProdottoStr == null || idProdottoStr.trim().isEmpty() || tipoProdotto == null || tipoProdotto.trim().isEmpty()) {
            LOGGER.log(Level.WARNING, "Tentativo di aggiungere al carrello senza ID prodotto o tipo prodotto.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID Prodotto o Tipo Prodotto mancante.");
            return;
        }

        int quantitaAggiungere = 1;
        try {
            if (quantitaStr != null && !quantitaStr.trim().isEmpty()) {
                quantitaAggiungere = Integer.parseInt(quantitaStr.trim());
            }
            if (quantitaAggiungere <= 0) {
                quantitaAggiungere = 1;
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Quantità non valida per prodotto: " + idProdottoStr + ". Usando quantità di default 1.", e);
            quantitaAggiungere = 1;
        }

        Object prodottoDaAggiungere = null;
        try {
            int id = Integer.parseInt(idProdottoStr);
            if ("pianta".equalsIgnoreCase(tipoProdotto)) {
                prodottoDaAggiungere = pianteDAO.getPiantaById(id);
            } else if ("accessorio".equalsIgnoreCase(tipoProdotto)) {
                prodottoDaAggiungere = accessoriDAO.getAccessorioByaccessorio_id(id);
            } else {
                LOGGER.warning("Tipo prodotto non valido: " + tipoProdotto);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Tipo di prodotto non supportato.");
                return;
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "ID prodotto non valido: " + idProdottoStr, e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID prodotto non valido.");
            return;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL nel recupero del prodotto con ID: " + idProdottoStr, e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore database durante l'aggiunta al carrello.");
            return;
        }

        if (prodottoDaAggiungere == null) {
            LOGGER.log(Level.WARNING, "Prodotto con ID " + idProdottoStr + " e tipo " + tipoProdotto + " non trovato nel database.");
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Prodotto non trovato.");
            return;
        }

        Map<String, RigaCarrello> carrello = (Map<String, RigaCarrello>) session.getAttribute("carrello");
        if (carrello == null) {
            carrello = new HashMap<>();
            session.setAttribute("carrello", carrello);
            LOGGER.info("Carrello creato in sessione.");
        }

        String chiaveRiga = tipoProdotto.toUpperCase() + "-" + idProdottoStr;
        RigaCarrello rigaEsistente = carrello.get(chiaveRiga);

        if (rigaEsistente == null) {
            String nomeProdotto = "";
            BigDecimal prezzoUnitario = BigDecimal.ZERO;
            
            if (prodottoDaAggiungere instanceof Piante) {
                Piante p = (Piante) prodottoDaAggiungere;
                nomeProdotto = p.getNomeComune();
                prezzoUnitario = p.getPrezzo();
            } else if (prodottoDaAggiungere instanceof Accessori) {
                Accessori a = (Accessori) prodottoDaAggiungere;
                nomeProdotto = a.getNome();
                prezzoUnitario = a.getPrezzo();
            }

            rigaEsistente = new RigaCarrello(Integer.parseInt(idProdottoStr), tipoProdotto, quantitaAggiungere, nomeProdotto, prezzoUnitario);
            carrello.put(chiaveRiga, rigaEsistente);
            LOGGER.info("Aggiunta nuova riga al carrello. Prodotto: " + chiaveRiga + ", Quantità: " + quantitaAggiungere);
        } else {
            rigaEsistente.incrementaQuantita(quantitaAggiungere);
            LOGGER.info("Incrementata quantità per prodotto: " + chiaveRiga + ". Nuova quantità: " + rigaEsistente.getQuantita());
        }

        // Reindirizza alla pagina del carrello
        response.sendRedirect(request.getContextPath() + "/Carrello.jsp");
    }
}