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
import util.PasswordHashing;

@WebServlet("/RegistrazioneServlet")
public class RegistrazioneServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(RegistrazioneServlet.class.getName());
    private UtenteDAO utenteDAO;

    public RegistrazioneServlet() {
        super();
    }

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            utenteDAO = new UtenteDAO();
            LOGGER.info("UtenteDAO inizializzato con successo nel metodo init() di RegistrazioneServlet.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'inizializzazione di UtenteDAO in RegistrazioneServlet (init()).", e);
            throw new ServletException("Errore di configurazione del database per RegistrazioneServlet.", e);
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // Recupera i parametri dal form
        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String email = request.getParameter("email"); // L'email sarà l'username
        String password = request.getParameter("password");
        String citta = request.getParameter("citta");
        String indirizzo = request.getParameter("indirizzo");
        String capStr = request.getParameter("cap");
        String provincia = request.getParameter("provincia");
        String telefono = request.getParameter("telefono");

        StringBuilder errorMessage = new StringBuilder();

        // Validazione dei campi obbligatori e formati (omessa per brevità, assumendo sia già corretta)
        // ... (il tuo codice di validazione qui) ...

        // Se ci sono errori di validazione, reindirizza al form con i messaggi
        if (errorMessage.length() > 0) {
            request.setAttribute("errorMessage", errorMessage.toString());
            // Ri-popola i campi del form con i valori inseriti dall'utente
            request.setAttribute("param.nome", nome);
            request.setAttribute("param.cognome", cognome);
            request.setAttribute("param.email", email);
            request.setAttribute("param.citta", citta);
            request.setAttribute("param.indirizzo", indirizzo);
            request.setAttribute("param.cap", capStr);
            request.setAttribute("param.provincia", provincia);
            request.setAttribute("param.telefono", telefono);
            request.getRequestDispatcher("/Registra.jsp").forward(request, response);
            return;
        }

        try {
            // 1. Controlla se l'email esiste già nel database
            if (utenteDAO.checkEmailExists(email)) {
                // Email già registrata: reindirizza alla pagina di login con messaggio di errore
                LOGGER.warning("Tentativo di registrazione con email già esistente: " + email);
                response.sendRedirect(request.getContextPath() + "/Login.jsp?error=email_already_registered");
                return;
            }

            // Hash della password
            String hashedPassword = PasswordHashing.hashPassword(password);
            if (hashedPassword == null) {
                LOGGER.log(Level.SEVERE, "Impossibile generare l'hash della password.");
                request.setAttribute("errorMessage", "Errore interno durante la registrazione della password. Riprova più tardi.");
                request.getRequestDispatcher("/Registra.jsp").forward(request, response);
                return;
            }

            // Crea l'oggetto Utente
            Utente newUser = new Utente();
            newUser.setNome(nome);
            newUser.setCognome(cognome);
            newUser.setEmail(email); // L'email è l'username
            newUser.setPasswordHash(hashedPassword);
            newUser.setIndirizzo(indirizzo);
            newUser.setCitta(citta);
            // Assicurati che 'cap' sia stato convertito correttamente a int prima di questo punto
            int cap = 0; // Inserisci qui la logica di conversione e validazione del CAP
            try {
                cap = Integer.parseInt(capStr);
            } catch (NumberFormatException e) {
                 LOGGER.log(Level.WARNING, "Errore nella conversione del CAP: " + capStr, e);
                 // Gestisci l'errore o reindirizza
                 request.setAttribute("errorMessage", "Il CAP deve essere un numero valido.");
                 request.getRequestDispatcher("/Registra.jsp").forward(request, response);
                 return;
            }
            newUser.setCAP(cap);
            newUser.setProvincia(provincia);
            newUser.setTelefono(telefono);
            newUser.setData_registrazione(new Timestamp(new Date().getTime()));
            newUser.setAdmin(false); // Nuovo utente non è amministratore di default

            // Registra l'utente nel database
            utenteDAO.aggiungiUtenteRegistrato(newUser);

            // 2. Reindirizza alla pagina di login con un messaggio di successo
            LOGGER.info("Utente " + email + " registrato con successo.");
            response.sendRedirect(request.getContextPath() + "/Login.jsp?registration=success");

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante la registrazione dell'utente: " + e.getMessage(), e);
            request.setAttribute("errorMessage", "Si è verificato un errore del database durante la registrazione. Riprova più tardi.");
            // Ri-popola i campi in caso di errore DB
            request.setAttribute("param.nome", nome);
            request.setAttribute("param.cognome", cognome);
            request.setAttribute("param.email", email);
            request.setAttribute("param.citta", citta);
            request.setAttribute("param.indirizzo", indirizzo);
            request.setAttribute("param.cap", capStr);
            request.setAttribute("param.provincia", provincia);
            request.setAttribute("param.telefono", telefono);
            request.getRequestDispatcher("/Registra.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Errore generico durante la registrazione dell'utente.", e);
            request.setAttribute("errorMessage", "Si è verificato un errore inaspettato. Riprova più tardi.");
            // Ri-popola i campi in caso di errore generico
            request.setAttribute("param.nome", nome);
            request.setAttribute("param.cognome", cognome);
            request.setAttribute("param.email", email);
            request.setAttribute("param.citta", citta);
            request.setAttribute("param.indirizzo", indirizzo);
            request.setAttribute("param.cap", capStr);
            request.setAttribute("param.provincia", provincia);
            request.setAttribute("param.telefono", telefono);
            request.getRequestDispatcher("/Registra.jsp").forward(request, response);
        }
    }
}