package admin; 

import la_teca_del_giardiniere.DAO.UtenteDAO;
import la_teca_del_giardiniere.classes.Utente;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/admin/ListaUtentiServlet")
public class ListaUtentiServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ListaUtentiServlet.class.getName());

    private UtenteDAO utenteDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            utenteDAO = new UtenteDAO();
            LOGGER.info("UtenteDAO inizializzato con successo.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore nell'inizializzazione di UtenteDAO", e);
            throw new ServletException("Errore nel caricamento delle risorse DAO", e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
    		
    		throws ServletException, IOException {
        
            handleList(request, response);
    }

    private void handleList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Utente> listaUtenti = utenteDAO.getAllUtentiConRuoli();
            request.setAttribute("listaUtenti", listaUtenti);
            
            // Gestione dei messaggi di successo/errore dalla sessione
            // (invocati da EliminaUtenteServlet dopo il redirect)
            String messaggio = (String) request.getSession().getAttribute("messaggio");
            String tipoMessaggio = (String) request.getSession().getAttribute("tipoMessaggio");
            
            if (messaggio != null) {
                request.setAttribute("messaggio", messaggio);
                request.setAttribute("tipoMessaggio", tipoMessaggio);
                // Rimuovi gli attributi dalla sessione per evitare che vengano visualizzati di nuovo
                request.getSession().removeAttribute("messaggio");
                request.getSession().removeAttribute("tipoMessaggio");
            }
            
            request.getRequestDispatcher("/admin/ListaUtenti.jsp").forward(request, response);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore nel recupero della lista utenti", e);
            request.setAttribute("messaggio", "Errore nel caricamento degli utenti: " + e.getMessage());
            request.setAttribute("tipoMessaggio", "error");
            request.getRequestDispatcher("/admin/AdminHome.jsp").forward(request, response);
        }
    }
        
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            doGet(request, response);
        }
    }
