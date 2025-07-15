package la_teca_del_giardiniere;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.sql.SQLException;
import java.util.List;


import la_teca_del_giardiniere.classes.Piante; // CAMBIATO: da 'piante' a 'Pianta'
import la_teca_del_giardiniere.classes.Utente; // Aggiunto per controllo autorizzazione
import la_teca_del_giardiniere.DAO.PianteDAO;

@WebServlet("/ListaPianteServlet")
public class ListaPianteServlet extends HttpServlet { // CAMBIATO: da 'listapianteServlet' a 'ListaPianteServlet'
    private static final long serialVersionUID = 1L;
    private PianteDAO pianteDAO;

    public ListaPianteServlet() {
        super();
        try {
            pianteDAO = new PianteDAO();
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestire l'errore in modo più appropriato (es. log)
            // Considera di lanciare una ServletException in init()
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String contextPath = request.getContextPath(); // Per URL assoluti

        // 1. Controllo Autenticazione e Autorizzazione (solo admin o venditore possono vedere la lista completa per la gestione)
        // Se questa servlet è anche per gli utenti normali per vedere il catalogo,
        // dovrai separare la logica o rimuovere l'autorizzazione.
        // Per ora, assumo che sia per la gestione lato admin/venditore.
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.sendRedirect(contextPath + "/login.jsp");
            return;
        }

        Utente utenteLoggato = (Utente) session.getAttribute("loggedInUser");
        if (utenteLoggato == null || (!utenteLoggato.isAdmin() && !utenteLoggato.getRuoli().contains("venditore"))) {
            response.sendRedirect(contextPath + "/accesso_negato.html");
            return;
        }

        try {
            List<Piante> listaPiante = pianteDAO.getAllPiante();
            request.setAttribute("listaPiante", listaPiante);
            String forwardPath = "/listapiante.jsp"; // Se è in una cartella specifica: "/admin/listapiante.jsp"
            request.getRequestDispatcher(forwardPath).forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("messaggio", "Errore nel recupero delle piante.");
            request.setAttribute("tipoMessaggio", "error");
            String forwardPath = "/listapiante.jsp"; // Se è in una cartella specifica: "/admin/listapiante.jsp"
            request.getRequestDispatcher(forwardPath).forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response); // Generalmente, la visualizzazione di una lista è un'operazione GET
    }
}