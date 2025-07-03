package src.com.la_teca_del_giardiniere; // Assicurati che il package sia corretto

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import src.com.la_teca_del_giardiniere.dao.AccessoriDAO;
import src.com.la_teca_del_giardiniere.classes.Accessori;

@WebServlet("/admin/aggiungiAccessorioServlet")
public class AggiungiAccessorioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AccessoriDAO accessorioDAO;

    public AggiungiAccessorioServlet() {
        super();
        try {
            accessorioDAO = new AccessoriDAO();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Errore durante l'inizializzazione di AccessorioDAO per aggiunta: " + e.getMessage());
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
        // Assicurati di avere una JSP chiamata "form_aggiungi_accessorio.jsp" in /admin/
        RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/form_aggiungi_accessorio.jsp");
        dispatcher.forward(request, response);
    }

    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        List<String> ruoli = (List<String>) session.getAttribute("ruoli");

        if (ruoli == null || (!ruoli.contains("amministratore") && !ruoli.contains("venditore"))) {
            session.setAttribute("messaggio", "Non hai i permessi per eseguire questa operazione.");
            session.setAttribute("tipoMessaggio", "error");
            response.sendRedirect(request.getContextPath() + "/accesso_negato.jsp");
            return;
        }

        try {
            String nome = request.getParameter("nome");
            BigDecimal prezzo = new BigDecimal(request.getParameter("prezzo"));
            int disponibilita = Integer.parseInt(request.getParameter("disponibilita"));
            String descrizioneBreve = request.getParameter("descrizioneBreve");
            String descrizioneDettagliata = request.getParameter("descrizioneDettagliata");
            String dimensioni = request.getParameter("dimensioni");
            String immagine = request.getParameter("immagine"); // Gestione file upload?
            String categoria = request.getParameter("categoria");
            
            // Validazione base
            if (nome == null || nome.trim().isEmpty() || prezzo.compareTo(BigDecimal.ZERO) < 0 ) {
                 session.setAttribute("messaggio", "Dati non validi. Nome e prezzo sono obbligatori e il prezzo non può essere negativo.");
                 session.setAttribute("tipoMessaggio", "error");
                 // Sarebbe meglio ricaricare il form con i dati inseriti e messaggi di errore specifici per campo
                 RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/form_aggiungi_accessorio.jsp");
                 // Potresti ripopolare i campi settando attributi nella request
                 request.setAttribute("nomeErr", nome); 
                 // ... altri campi ...
                 dispatcher.forward(request, response);
                 return;
            }

            Accessori nuovoAccessorio = new Accessori();
            nuovoAccessorio.setNome(nome);
            nuovoAccessorio.setPrezzo(prezzo);
            nuovoAccessorio.setDisponibilita(disponibilita);
            nuovoAccessorio.setDescrizioneBreve(descrizioneBreve);
            nuovoAccessorio.setDescrizioneDettagliata(descrizioneDettagliata);
            nuovoAccessorio.setDimensioni(dimensioni);
            nuovoAccessorio.setImmagine(immagine);
            nuovoAccessorio.setCategoria(categoria);
            nuovoAccessorio.setDataInserimento(new Timestamp(System.currentTimeMillis())); // Data attuale

            accessorioDAO.aggiungiAccessori(nuovoAccessorio);

            session.setAttribute("messaggio", "Accessorio '" + nome + "' aggiunto con successo!");
            session.setAttribute("tipoMessaggio", "success");

        } catch (NumberFormatException e) {
            session.setAttribute("messaggio", "Dati numerici non validi (prezzo, disponibilità).");
            session.setAttribute("tipoMessaggio", "error");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
             session.setAttribute("messaggio", "Formato prezzo non valido.");
             session.setAttribute("tipoMessaggio", "error");
             e.printStackTrace();
        } catch (SQLException e) {
            session.setAttribute("messaggio", "Errore durante l'aggiunta dell'accessorio: " + e.getMessage());
            session.setAttribute("tipoMessaggio", "error");
            e.printStackTrace();
        }
        
        response.sendRedirect(request.getContextPath() + "/admin/accessoriServlet?action=list");
    }
}
