package src.com.la_teca_del_giardiniere;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import src.com.la_teca_del_giardiniere.classes.piante;
import src.com.la_teca_del_giardiniere.dao.PianteDAO;

@WebServlet("/listapianteServlet")
public class listapianteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private PianteDAO pianteDAO;

    public listapianteServlet() {
        super();
        try {
            pianteDAO = new PianteDAO();
        } catch (SQLException e) {
            e.printStackTrace(); // Gestire l'errore in modo più appropriato (es. log)
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<piante> listaPiante = pianteDAO.getAllPiante();
            request.setAttribute("listaPiante", listaPiante);
            request.getRequestDispatcher("listapiante.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace(); // Gestire l'errore in modo più appropriato (es. visualizzare una pagina di errore)
            request.setAttribute("messaggio", "Errore nel recupero delle piante.");
            request.setAttribute("tipoMessaggio", "error");
            request.getRequestDispatcher("listapiante.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response); // Generalmente, la visualizzazione di una lista è un'operazione GET
    }
}