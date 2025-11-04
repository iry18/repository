package admin;

import la_teca_del_giardiniere.DAO.UtenteDAO;
import la_teca_del_giardiniere.classes.Utente;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/admin/ModificaUtenteServlet")
public class ModificaUtenteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ModificaUtenteServlet.class.getName());

    private UtenteDAO utenteDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            utenteDAO = new UtenteDAO();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore nell'inizializzazione di UtenteDAO in ModificaUtenteServlet", e);
            throw new ServletException("Errore nel caricamento delle risorse DAO", e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int utenteId = 0;
        try {
            utenteId = Integer.parseInt(request.getParameter("id"));
            Utente utente = utenteDAO.getUtenteByIdWithRuoli(utenteId);

            if (utente != null) {
                request.setAttribute("utente", utente);
                request.getRequestDispatcher("/admin/FormModificaUtente.jsp").forward(request, response);
            } else {
                request.getSession().setAttribute("messaggio", "Utente non trovato per l'ID: " + utenteId);
                request.getSession().setAttribute("tipoMessaggio", "error");
                response.sendRedirect(request.getContextPath() + "/admin/ListaUtentiServlet");
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "ID utente non valido per modifica", e);
            request.getSession().setAttribute("messaggio", "ID utente non valido.");
            request.getSession().setAttribute("tipoMessaggio", "error");
            response.sendRedirect(request.getContextPath() + "/admin/ListaUtentiServlet");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante il recupero utente per modifica con ID: " + utenteId, e);
            request.getSession().setAttribute("messaggio", "Errore del database durante il recupero dell'utente: " + e.getMessage());
            request.getSession().setAttribute("tipoMessaggio", "error");
            response.sendRedirect(request.getContextPath() + "/admin/ListaUtentiServlet");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int utenteId = 0;
        try {
            utenteId = Integer.parseInt(request.getParameter("id"));
            String nome = request.getParameter("nome");
            String cognome = request.getParameter("cognome");
            String email = request.getParameter("email");
            String indirizzo = request.getParameter("indirizzo");
            String citta = request.getParameter("citta");
            int CAP = Integer.parseInt(request.getParameter("CAP"));
            String telefono = request.getParameter("telefono");
            String provincia = request.getParameter("provincia");
            boolean isAdmin = "true".equals(request.getParameter("isAdmin")); // Checkbox value
            boolean isVenditore = "true".equals(request.getParameter("isVenditore")); // Checkbox value

            // IMPORTANT: Password handling.
            // If the form doesn't send password, retrieve current hash.
            // If the form sends a new password, hash it.
            String passwordHash = request.getParameter("password"); // This would be the new, unhashed password from the form
            Utente existingUtente = utenteDAO.getUtenteByIdWithRuoli(utenteId);
            if (existingUtente == null) {
                request.getSession().setAttribute("messaggio", "Utente non trovato per l'aggiornamento.");
                request.getSession().setAttribute("tipoMessaggio", "error");
                response.sendRedirect(request.getContextPath() + "/admin/ListaUtentiServlet");
                return;
            }

            // Only update password if a new one is provided
            if (passwordHash != null && !passwordHash.trim().isEmpty()) {
                // You MUST hash the password here before saving!
                // Example (replace with your actual hashing utility):
                // passwordHash = PasswordUtility.hashPassword(passwordHash);
                // For demonstration, just using it as is (NOT SECURE for production):
                existingUtente.setPasswordHash(passwordHash); // !!! Hash this in a real app
            }
            // If no new password, retain the old one (already set from existingUtente)


            existingUtente.setNome(nome);
            existingUtente.setCognome(cognome);
            existingUtente.setEmail(email);
            existingUtente.setIndirizzo(indirizzo);
            existingUtente.setCitta(citta);
            existingUtente.setCAP(CAP);
            existingUtente.setTelefono(telefono);
            existingUtente.setProvincia(provincia);
            existingUtente.setAdmin(isAdmin);
            existingUtente.setVenditore(isVenditore);

            boolean updated = utenteDAO.updateUtente(existingUtente);

            if (updated) {
                request.getSession().setAttribute("messaggio", "Utente aggiornato con successo!");
                request.getSession().setAttribute("tipoMessaggio", "success");
            } else {
                request.getSession().setAttribute("messaggio", "Nessuna modifica effettuata o utente non trovato.");
                request.getSession().setAttribute("tipoMessaggio", "warning");
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Dati non validi forniti per l'aggiornamento utente.", e);
            request.getSession().setAttribute("messaggio", "Dati input non validi (es. CAP non numerico).");
            request.getSession().setAttribute("tipoMessaggio", "error");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'aggiornamento dell'utente con ID: " + utenteId, e);
            request.getSession().setAttribute("messaggio", "Errore del database durante l'aggiornamento: " + e.getMessage());
            request.getSession().setAttribute("tipoMessaggio", "error");
        }
        response.sendRedirect(request.getContextPath() + "/admin/ListaUtentiServlet");
    }
}