package src.com.la_teca_del_giardiniere;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import src.com.la_teca_del_giardiniere.dao.PianteDAO;

@WebServlet("/eliminapianteServlet")
public class eliminapianteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private PianteDAO pianteDAO;

    public eliminapianteServlet() {
        super();
        try {
            pianteDAO = new PianteDAO();
        } catch (SQLException e) {
            e.printStackTrace(); // Gestire l'errore in modo più appropriato
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                int idPianta = Integer.parseInt(idStr);
                try {
                    pianteDAO.eliminaPianta(idPianta);
                    request.setAttribute("messaggio", "Pianta eliminata con successo.");
                    request.setAttribute("tipoMessaggio", "success");
                } catch (SQLException e) {
                    e.printStackTrace(); // Gestire l'errore in modo più appropriato (es. log)
                    // Qui potresti voler controllare il codice di errore SQL per una violazione di foreign key
                    request.setAttribute("messaggio", "Errore durante l'eliminazione della pianta: " + e.getMessage());
                    request.setAttribute("tipoMessaggio", "error");
                }
            } catch (NumberFormatException e) {
                request.setAttribute("messaggio", "ID pianta non valido.");
                request.setAttribute("tipoMessaggio", "error");
            }
        } else {
            request.setAttribute("messaggio", "ID della pianta non fornito.");
            request.setAttribute("tipoMessaggio", "error");
        }
        response.sendRedirect("listapianteServlet"); // Reindirizza sempre all'elenco
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response); // L'eliminazione è gestita tramite GET (da un link)
    }
}