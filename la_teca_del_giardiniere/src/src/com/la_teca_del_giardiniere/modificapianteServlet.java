package src.com.la_teca_del_giardiniere;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import src.com.la_teca_del_giardiniere.classes.piante;
import src.com.la_teca_del_giardiniere.dao.PianteDAO;

@WebServlet("/modificapianteServlet")
public class modificapianteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private PianteDAO pianteDAO;

    public modificapianteServlet() {
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
                piante pianta = pianteDAO.getPiantaById(idPianta);
                if (pianta != null) {
                    request.setAttribute("pianta", pianta);
                    request.getRequestDispatcher("modificapiante.jsp").forward(request, response);
                } else {
                    // Pianta non trovata, reindirizza all'elenco con un messaggio di errore
                    request.setAttribute("messaggio", "Pianta non trovata con ID: " + idPianta);
                    request.setAttribute("tipoMessaggio", "error");
                    request.getRequestDispatcher("listapianteServlet").forward(request, response);
                }
            } catch (NumberFormatException e) {
                // ID non valido, reindirizza all'elenco con un messaggio di errore
                request.setAttribute("messaggio", "ID pianta non valido.");
                request.setAttribute("tipoMessaggio", "error");
                request.getRequestDispatcher("listapianteServlet").forward(request, response);
            } catch (SQLException e) {
                e.printStackTrace(); // Gestire l'errore in modo più appropriato
                request.setAttribute("messaggio", "Errore nel recupero della pianta.");
                request.setAttribute("tipoMessaggio", "error");
                request.getRequestDispatcher("listapianteServlet").forward(request, response);
            }
        } else {
            // Nessun ID fornito, reindirizza all'elenco con un messaggio di errore
            request.setAttribute("messaggio", "ID della pianta non fornito.");
            request.setAttribute("tipoMessaggio", "error");
            request.getRequestDispatcher("listapianteServlet").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // La modifica viene gestita tramite GET per visualizzare il form
        doGet(request, response);
    }
}

