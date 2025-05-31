package src.com.la_teca_del_giardiniere; // Assicurati che il package sia corretto

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import src.com.la_teca_del_giardiniere.dao.accessoriDAO;
import src.com.la_teca_del_giardiniere.classes.accessori; // Corretto nome classe

@WebServlet("/admin/modificaAccessorioServlet")
public class ModificaAccessorioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private accessoriDAO accessorioDAO;

    public ModificaAccessorioServlet() {
        super();
        try {
            accessorioDAO = new accessoriDAO();
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestione più robusta dell'errore di inizializzazione DAO sarebbe opportuna
            System.err.println("Errore durante l'inizializzazione di AccessorioDAO per modifica: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<String> ruoli = (List<String>) session.getAttribute("ruoli");

        if (ruoli == null || (!ruoli.contains("amministratore") && !ruoli.contains("venditore"))) {
            session.setAttribute("messaggio", "Non hai i permessi per eseguire questa operazione.");
            session.setAttribute("tipoMessaggio", "error");
            response.sendRedirect(request.getContextPath() + "/accesso_negato.jsp");
            return;
        }

        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            session.setAttribute("messaggio", "ID accessorio non fornito per la modifica.");
            session.setAttribute("tipoMessaggio", "error");
            response.sendRedirect(request.getContextPath() + "/admin/accessoriServlet?action=list");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            accessori accessorioEsistente = accessorioDAO.getAccessorioById(id);

            if (accessorioEsistente == null) {
                session.setAttribute("messaggio", "Accessorio non trovato con ID: " + id);
                session.setAttribute("tipoMessaggio", "error");
                response.sendRedirect(request.getContextPath() + "/admin/accessoriServlet?action=list");
            } else {
                request.setAttribute("accessorio", accessorioEsistente);
                // Assicurati di avere una JSP chiamata "form_modifica_accessorio.jsp" in /admin/
                RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/form_modifica_accessorio.jsp");
                dispatcher.forward(request, response);
            }
        } catch (NumberFormatException e) {
            session.setAttribute("messaggio", "ID accessorio non valido per la modifica.");
            session.setAttribute("tipoMessaggio", "error");
            response.sendRedirect(request.getContextPath() + "/admin/accessoriServlet?action=list");
        } catch (SQLException e) {
            session.setAttribute("messaggio", "Errore durante il recupero dell'accessorio: " + e.getMessage());
            session.setAttribute("tipoMessaggio", "error");
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/accessoriServlet?action=list");
        }
    }

    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8"); // Importante per caratteri speciali
        HttpSession session = request.getSession();
        List<String> ruoli = (List<String>) session.getAttribute("ruoli");

        if (ruoli == null || (!ruoli.contains("amministratore") && !ruoli.contains("venditore"))) {
            session.setAttribute("messaggio", "Non hai i permessi per eseguire questa operazione.");
            session.setAttribute("tipoMessaggio", "error");
            response.sendRedirect(request.getContextPath() + "/accesso_negato.jsp");
            return;
        }

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String nome = request.getParameter("nome");
            BigDecimal prezzo = new BigDecimal(request.getParameter("prezzo"));
            int disponibilita = Integer.parseInt(request.getParameter("disponibilita"));
            String descrizioneBreve = request.getParameter("descrizioneBreve");
            String descrizioneDettagliata = request.getParameter("descrizioneDettagliata");
            String dimensioni = request.getParameter("dimensioni");
            String immagine = request.getParameter("immagine"); // Potrebbe richiedere gestione file upload
            String categoria = request.getParameter("categoria");
            // Data inserimento solitamente non si modifica, ma se si potesse:
            // Timestamp dataInserimento = Timestamp.valueOf(request.getParameter("dataInserimento"));

            // Validazione base (dovrebbe essere più robusta)
            if (nome == null || nome.trim().isEmpty() || prezzo.compareTo(BigDecimal.ZERO) < 0) {
                 session.setAttribute("messaggio", "Dati non validi per l'aggiornamento. Nome e prezzo sono obbligatori e il prezzo non può essere negativo.");
                 session.setAttribute("tipoMessaggio", "error");
                 // Ricarica il form con i dati (magari passando l'oggetto accessorio non valido)
                 // Per semplicità, reindirizziamo alla lista, ma sarebbe meglio ricaricare il form
                 response.sendRedirect(request.getContextPath() + "/admin/modificaAccessorioServlet?id=" + id);
                 return;
            }


            accessori accessorio = new accessori();
            accessorio.setId(id);
            accessorio.setNome(nome);
            accessorio.setPrezzo(prezzo);
            accessorio.setDisponibilita(disponibilita);
            accessorio.setDescrizioneBreve(descrizioneBreve);
            accessorio.setDescrizioneDettagliata(descrizioneDettagliata);
            accessorio.setDimensioni(dimensioni);
            accessorio.setImmagine(immagine);
            accessorio.setCategoria(categoria);
            // accessorio.setDataInserimento(dataInserimento); // Se modificabile

            boolean aggiornato = accessorioDAO.updateAccessori(accessorio);

            if (aggiornato) {
                session.setAttribute("messaggio", "Accessorio aggiornato con successo!");
                session.setAttribute("tipoMessaggio", "success");
            } else {
                session.setAttribute("messaggio", "Nessuna modifica apportata o accessorio non trovato.");
                session.setAttribute("tipoMessaggio", "warning");
            }

        } catch (NumberFormatException e) {
            session.setAttribute("messaggio", "Dati numerici non validi (ID, prezzo, disponibilità).");
            session.setAttribute("tipoMessaggio", "error");
            e.printStackTrace();
        } catch (IllegalArgumentException e) { // Per new BigDecimal se la stringa non è valida
            session.setAttribute("messaggio", "Formato prezzo non valido.");
            session.setAttribute("tipoMessaggio", "error");
            e.printStackTrace();
        }
        catch (SQLException e) {
            session.setAttribute("messaggio", "Errore durante l'aggiornamento dell'accessorio: " + e.getMessage());
            session.setAttribute("tipoMessaggio", "error");
            e.printStackTrace();
        }
        
        // Reindirizza alla pagina di gestione accessori
        response.sendRedirect(request.getContextPath() + "/admin/accessoriServlet?action=list");
    }
}