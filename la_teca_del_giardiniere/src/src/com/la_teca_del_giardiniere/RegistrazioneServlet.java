package la_teca_del_giardiniere;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import la_teca_del_giardiniere.classes.Utente;
import la_teca_del_giardiniere.DAO.UtenteDAO;
import util.PasswordHashing; // Import the PasswordHashing utility

@WebServlet("/RegistrazioneServlet")
public class RegistrazioneServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(RegistrazioneServlet.class.getName());
    private UtenteDAO utenteDAO; // Declare UtenteDAO instance

    public RegistrazioneServlet() {
        super();
        try {
            utenteDAO = new UtenteDAO(); // Initialize UtenteDAO
            LOGGER.info("UtenteDAO inizializzato con successo in RegistrazioneServlet.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'inizializzazione di UtenteDAO in RegistrazioneServlet.", e);
            // Re-throw as ServletException to indicate a serious startup problem
            throw new RuntimeException("Impossibile inizializzare UtenteDAO: " + e.getMessage(), e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Retrieve form parameters
        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String username = request.getParameter("username"); // You have a username field in JSP, but not in Utente class or DB schema. I'll map it to email for uniqueness or you might need to adjust your DB/Utente class. For now, it won't be explicitly stored in Utente if Utente only uses email as identifier. Let's assume 'email' is the unique identifier for login.
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String citta = request.getParameter("citta");
        String indirizzo = request.getParameter("indirizzo");
        String capStr = request.getParameter("cap"); // CAP is an int in Utente class
        String provincia = request.getParameter("provincia");
        String telefono = request.getParameter("telefono");

        // Set character encoding for correct parameter decoding
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // 2. Perform validation and data parsing
        StringBuilder errorMessage = new StringBuilder();

        if (nome == null || nome.trim().isEmpty()) {
            errorMessage.append("Il nome è obbligatorio.<br>");
        }
        if (cognome == null || cognome.trim().isEmpty()) {
            errorMessage.append("Il cognome è obbligatorio.<br>");
        }
        if (email == null || email.trim().isEmpty()) {
            errorMessage.append("L'email è obbligatoria.<br>");
        }
        if (password == null || password.trim().isEmpty()) {
            errorMessage.append("La password è obbligatoria.<br>");
        } else if (password.length() < 6) { // Example: minimum password length
            errorMessage.append("La password deve essere di almeno 6 caratteri.<br>");
        }

        int cap = 0;
        if (capStr != null && !capStr.trim().isEmpty()) {
            try {
                cap = Integer.parseInt(capStr);
            } catch (NumberFormatException e) {
                errorMessage.append("Il CAP deve essere un numero valido.<br>");
            }
        }

        // If there are any validation errors, forward back to the registration page
        if (errorMessage.length() > 0) {
            request.setAttribute("errorMessage", errorMessage.toString());
            // Preserve entered data for user convenience
            request.setAttribute("param.nome", nome);
            request.setAttribute("param.cognome", cognome);
            request.setAttribute("param.username", username);
            request.setAttribute("param.email", email);
            request.setAttribute("param.citta", citta);
            request.setAttribute("param.indirizzo", indirizzo);
            request.setAttribute("param.cap", capStr);
            request.setAttribute("param.provincia", provincia);
            request.setAttribute("param.telefono", telefono);
            request.getRequestDispatcher("/registrati.jsp").forward(request, response);
            return;
        }

        try {
            // Check if email already exists
            if (utenteDAO.checkEmailExists(email)) {
                request.setAttribute("errorMessage", "Questa email è già registrata. Per favore, usa un'altra email o effettua il login.");
                request.setAttribute("param.nome", nome);
                request.setAttribute("param.cognome", cognome);
                request.setAttribute("param.username", username);
                request.setAttribute("param.email", email); // Keep email so user sees it's the problem
                request.setAttribute("param.citta", citta);
                request.setAttribute("param.indirizzo", indirizzo);
                request.setAttribute("param.cap", capStr);
                request.setAttribute("param.provincia", provincia);
                request.setAttribute("param.telefono", telefono);
                request.getRequestDispatcher("/registrati.jsp").forward(request, response);
                return;
            }

            // Hash the password
            String hashedPassword = PasswordHashing.hashPassword(password);
            if (hashedPassword == null) {
                request.setAttribute("errorMessage", "Errore interno durante la registrazione della password. Riprova più tardi.");
                request.getRequestDispatcher("/registrati.jsp").forward(request, response);
                return;
            }

            // Create Utente object
            Utente newUser = new Utente();
            newUser.setNome(nome);
            newUser.setCognome(cognome);
            newUser.setEmail(email);
            newUser.setPasswordHash(hashedPassword);
            newUser.setIndirizzo(indirizzo);
            newUser.setCitta(citta);
            newUser.setCAP(cap);
            newUser.setProvincia(provincia);
            newUser.setTelefono(telefono);
            newUser.setData_registrazione(new Timestamp(new Date().getTime())); // Set current timestamp
            newUser.setAdmin(false); // New registered users are not administrators by default

            // Save user to database
            utenteDAO.aggiungiUtenteRegistrato(newUser);

            // Redirect to a success page
            response.sendRedirect(request.getContextPath() + "/registrazioneSuccesso.jsp");

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante la registrazione dell'utente: " + e.getMessage(), e);
            request.setAttribute("errorMessage", "Si è verificato un errore del database durante la registrazione. Riprova più tardi.");
            request.setAttribute("param.nome", nome);
            request.setAttribute("param.cognome", cognome);
            request.setAttribute("param.username", username);
            request.setAttribute("param.email", email);
            request.setAttribute("param.citta", citta);
            request.setAttribute("param.indirizzo", indirizzo);
            request.setAttribute("param.cap", capStr);
            request.setAttribute("param.provincia", provincia);
            request.setAttribute("param.telefono", telefono);
            request.getRequestDispatcher("/registrati.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Errore generico durante la registrazione dell'utente.", e);
            request.setAttribute("errorMessage", "Si è verificato un errore inaspettato. Riprova più tardi.");
            request.setAttribute("param.nome", nome);
            request.setAttribute("param.cognome", cognome);
            request.setAttribute("param.username", username);
            request.setAttribute("param.email", email);
            request.setAttribute("param.citta", citta);
            request.setAttribute("param.indirizzo", indirizzo);
            request.setAttribute("param.cap", capStr);
            request.setAttribute("param.provincia", provincia);
            request.setAttribute("param.telefono", telefono);
            request.getRequestDispatcher("/registrati.jsp").forward(request, response);
        }
    }
}