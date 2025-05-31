package src.com.la_teca_del_giardiniere; // Assicurati che il package sia corretto

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import src.com.la_teca_del_giardiniere.dao.AccessoriDAO;

@WebServlet("/admin/EliminaAccessorioServlet") // Mappatura per l'area amministrativa
public class EliminaAccessorioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private AccessoriDAO accessoriDAO;

    public EliminaAccessorioServlet() {
        super();
        try {
        	accessoriDAO = new AccessoriDAO();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Errore durante l'inizializzazione di AccessorioDAO per eliminazione: " + e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Controllo autorizzazione (solo Amministratore o Venditore)
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
            accessoriDAO.deleteAccessori(id);

            session.setAttribute("messaggio", "Accessorio eliminato con successo!");
            session.setAttribute("tipoMessaggio", "success");

        } catch (NumberFormatException e) {
            session.setAttribute("messaggio", "ID accessorio non valido per l'eliminazione.");
            session.setAttribute("tipoMessaggio", "error");
            e.printStackTrace();
        } catch (SQLException e) {
            session.setAttribute("messaggio", "Errore durante l'eliminazione dell'accessorio: " + e.getMessage());
            session.setAttribute("tipoMessaggio", "error");
            e.printStackTrace();
        }

        // Reindirizza alla pagina di gestione accessori per mostrare la lista aggiornata
        response.sendRedirect(request.getContextPath() + "/admin/accessoriServlet?action=list");
    }
}