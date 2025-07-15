package la_teca_del_giardiniere;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import la_teca_del_giardiniere.DAO.AccessoriDAO;
import la_teca_del_giardiniere.classes.Accessori;
import la_teca_del_giardiniere.classes.Utente;

@WebServlet("/admin/AccessoriServlet")
public class AccessoriServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private AccessoriDAO accessoriDAO;

    public AccessoriServlet() {
        super();
        try {
            accessoriDAO = new AccessoriDAO();
        } catch (SQLException e) {
            e.printStackTrace();
            // In un'applicazione reale, un errore qui dovrebbe impedire il funzionamento.
            // Considera di loggare l'errore e/o lanciare una ServletException in init().
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String contextPath = request.getContextPath();

        // 1. Controllo Autenticazione e Autorizzazione (Admin o Venditore)
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.sendRedirect(contextPath + "/Login.jsp");
            return;
        }

        Utente utenteLoggato = (Utente) session.getAttribute("loggedInUser");
        if (utenteLoggato == null || (!utenteLoggato.isAdmin() && !utenteLoggato.getRuoli().contains("venditore"))) {
            response.sendRedirect(contextPath + "/accesso_negato.html");
            return;
        }

        // 2. Recupero dati dal form
        List<String> errori = new ArrayList<>();
        Accessori accessorio = new Accessori();

        // Recupera l'ID (se presente, per la modifica)
        String idStr = request.getParameter("id");
        Integer idAccessorio = null;
        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                idAccessorio = Integer.parseInt(idStr);
                accessorio.setId(idAccessorio);
            } catch (NumberFormatException e) {
                errori.add("L'ID dell'accessorio non è in un formato valido.");
            }
        }

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
        accessorio.setImmagine(request.getParameter("urlImmagine")); // Recupera il nome del file immagine

        // Data Inserimento (Timestamp)
        String dataInserimentoStr = request.getParameter("dataInserimento");
        if (dataInserimentoStr != null && !dataInserimentoStr.isEmpty()) {
            try {
                // Se la data è nel formato "YYYY-MM-DD", aggiungi un'ora di default
                if (dataInserimentoStr.length() == 10) {
                    accessorio.setDataInserimento(Timestamp.valueOf(dataInserimentoStr + " 00:00:00"));
                } else { // Altrimenti, assumi che sia già un timestamp completo
                    accessorio.setDataInserimento(Timestamp.valueOf(dataInserimentoStr));
                }
            } catch (IllegalArgumentException e) {
                errori.add("Il formato della Data Inserimento non è valido (formato atteso: YYYY-MM-DD o YYYY-MM-DD HH:MM:SS).");
            }
        } else if (idAccessorio == null) { // Solo se è un nuovo inserimento, imposta la data corrente
            accessorio.setDataInserimento(new Timestamp(System.currentTimeMillis()));
        } else {
            // Se è una modifica e la data non viene fornita, recupera la data esistente dal DB
            try {
                Accessori existingAccessorio = accessoriDAO.getAccessorioById(idAccessorio);
                if (existingAccessorio != null) {
                    accessorio.setDataInserimento(existingAccessorio.getDataInserimento());
                }
            } catch (SQLException e) {
                errori.add("Errore nel recupero della data di inserimento esistente.");
                e.printStackTrace();
            }
        }

        // 3. Gestione degli errori di validazione
        if (!errori.isEmpty()) {
            request.setAttribute("errori", errori);
            request.setAttribute("accessorio", accessorio); // Ri-popola il form con i dati inseriti
            request.setAttribute("modalita", (idAccessorio != null ? "modifica" : "inserisci"));
            request.getRequestDispatcher("/WEB-INF/views/admin/FormInserimentoAccessori.jsp").forward(request, response);
            return;
        }

        // 4. Esecuzione dell'operazione (aggiungi o aggiorna)
        try {
            if (idAccessorio != null) {
                // È un'operazione di AGGIORNAMENTO
                accessoriDAO.aggiornaAccessori(accessori);
                session.setAttribute("messaggio", "Accessorio aggiornato con successo!");
                session.setAttribute("tipoMessaggio", "success");
            } else {
                // È un'operazione di INSERIMENTO
                accessoriDAO.aggiungiAccessori(accessorio);
                session.setAttribute("messaggio", "Accessorio aggiunto con successo!");
                session.setAttribute("tipoMessaggio", "success");
            }
            // Dopo l'operazione, reindirizza alla lista degli accessori
            response.sendRedirect(contextPath + "/admin/AccessoriServlet?action=list");
        } catch (SQLException e) {
            e.printStackTrace();
            errori.add("Errore database: " + e.getMessage());
            request.setAttribute("errori", errori);
            request.setAttribute("accessorio", accessorio);
            request.setAttribute("modalita", (idAccessorio != null ? "modifica" : "inserisci"));
            request.getRequestDispatcher("/WEB-INF/views/admin/FormInserimentoAccessori.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String contextPath = request.getContextPath();

        // 1. Controllo Autenticazione e Autorizzazione (Admin o Venditore)
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.sendRedirect(contextPath + "/Login.jsp");
            return;
        }

        Utente utenteLoggato = (Utente) session.getAttribute("loggedInUser");
        if (utenteLoggato == null || (!utenteLoggato.isAdmin() && !utenteLoggato.getRuoli().contains("venditore"))) {
            response.sendRedirect(contextPath + "/accesso_negato.html");
            return;
        }

        String action = request.getParameter("action");

        try {
            if (action == null || action.equals("list")) {
                listAccessori(request, response);
            } else if (action.equals("new")) {
                showNewForm(request, response);
            } else if (action.equals("edit")) {
                showEditForm(request, response);
            } else if (action.equals("delete")) {
                deleteAccessorio(request, response);
            } else {
                listAccessori(request, response); // Azione di default
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void listAccessori(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        List<Accessori> listaAccessori = accessoriDAO.getAllAccessori();
        request.setAttribute("listaAccessori", listaAccessori);
        // Sposta il messaggio di sessione agli attributi della richiesta per una visualizzazione immediata
        if (request.getSession().getAttribute("messaggio") != null) {
            request.setAttribute("messaggio", request.getSession().getAttribute("messaggio"));
            request.setAttribute("tipoMessaggio", request.getSession().getAttribute("tipoMessaggio"));
            request.getSession().removeAttribute("messaggio"); // Rimuove il messaggio dalla sessione dopo l'uso
            request.getSession().removeAttribute("tipoMessaggio");
        }
        request.getRequestDispatcher("/WEB-INF/views/admin/ListaAccessori.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("modalita", "inserisci");
        request.getRequestDispatcher("/WEB-INF/views/admin/FormInserimentoAccessori.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Accessori existingAccessorio = accessoriDAO.getAccessorioById(id);
        if (existingAccessorio != null) {
            request.setAttribute("accessorio", existingAccessorio);
            request.setAttribute("modalita", "modifica");
            request.getRequestDispatcher("/WEB-INF/views/admin/FormInserimentoAccessori.jsp").forward(request, response);
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("messaggio", "Accessorio non trovato per la modifica.");
            session.setAttribute("tipoMessaggio", "error");
            response.sendRedirect(request.getContextPath() + "/admin/AccessoriServlet?action=list");
        }
    }

    private void deleteAccessorio(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        accessoriDAO.eliminaAccessori(id);
        HttpSession session = request.getSession();
        session.setAttribute("messaggio", "Accessorio eliminato con successo!");
        session.setAttribute("tipoMessaggio", "success");
        response.sendRedirect(request.getContextPath() + "/admin/AccessoriServlet?action=list");
    }
}