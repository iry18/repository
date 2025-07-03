package src.com.la_teca_del_giardiniere;


import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level; // Aggiungi import per Logger
import java.util.logging.Logger; // Aggiungi import per Logger
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Assicurati che i percorsi siano corretti per le tue classi
import src.src.src.src.com.la_teca_del_giardiniere.classes.Utente;
import src.src.src.src.com.la_teca_del_giardiniere.dao.UtenteDAO;
import src.src.src.src.com.la_teca_del_giardiniere.util.PasswordHashing;

@WebServlet("/RegistrazioneServlet")
public class RegistrazioneServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(RegistrazioneServlet.class.getName()); // Logger

    private UtenteDAO utenteDao;

    // Inizializzazione del DAO nel metodo init() per una migliore gestione degli errori
    @Override
    public void init() throws ServletException {
        super.init();
        try {
            utenteDao = new UtenteDAO();
            LOGGER.info("UtenteDAO inizializzato con successo in RegistrazioneServlet.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'inizializzazione di UtenteDAO.", e);
            throw new ServletException("Errore durante l'inizializzazione del DAO: " + e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Errore generico durante l'inizializzazione di UtenteDAO.", e);
            throw new ServletException("Errore generico durante l'inizializzazione del DAO: " + e.getMessage(), e);
        }
    }

    // Questo metodo doGet è per test o per reindirizzare al form
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contextPath = request.getContextPath();
        response.sendRedirect(contextPath + "/registrati.jsp"); // Reindirizza sempre al form di registrazione
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8"); // Imposta la codifica dei caratteri per i parametri in entrata

        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String indirizzo = request.getParameter("indirizzo");
        String citta = request.getParameter("citta");
        String CAPStr = request.getParameter("cap");
        String telefonoStr = request.getParameter("telefono");
        String provincia = request.getParameter("provincia");

        List<String> errori = new ArrayList<>();

        // Validazione dei dati lato server
        if (nome == null || nome.trim().isEmpty()) {
            errori.add("Il nome è obbligatorio.");
        }
        if (cognome == null || cognome.trim().isEmpty()) {
            errori.add("Il cognome è obbligatorio.");
        }
        if (email == null || email.trim().isEmpty()) {
            errori.add("L'email è obbligatoria.");
        } else if (!isValidEmail(email)) {
            errori.add("L'email non è in un formato valido.");
        }
        if (password == null || password.trim().isEmpty()) {
            errori.add("La password è obbligatoria.");
        } else if (password.length() < 8) {
            errori.add("La password deve contenere almeno 8 caratteri.");
        }
        if (indirizzo == null || indirizzo.trim().isEmpty()) {
            errori.add("L'indirizzo è obbligatorio.");
        }
        if (citta == null || citta.trim().isEmpty()) {
            errori.add("La città è obbligatoria.");
        }
        if (CAPStr == null || CAPStr.trim().isEmpty()) {
            errori.add("Il CAP è obbligatorio.");
        } else if (!CAPStr.matches("\\d{5}")) {
            errori.add("Il CAP deve essere composto da 5 cifre.");
        }
        if (telefonoStr == null || telefonoStr.trim().isEmpty()) {
            errori.add("Il numero di telefono è obbligatorio.");
        } else if (!telefonoStr.matches("\\d+")) {
            errori.add("Il numero di telefono deve contenere solo cifre.");
        }
        if (provincia == null || provincia.trim().isEmpty()) {
            errori.add("La provincia è obbligatoria.");
        }

        // Se ci sono errori di validazione, reindirizza alla pagina di registrazione con i messaggi di errore
        if (!errori.isEmpty()) {
            request.setAttribute("erroriRegistrazione", errori);
            request.getRequestDispatcher("/registrati.jsp").forward(request, response); // Usa / per root contestuale
            return;
        }

        try {
            // Verifica se l'email è già registrata
            if (utenteDao != null) {
                if (utenteDao.checkEmailExists(email)) {
                    // Reindirizza al login con un messaggio specifico se l'email esiste
                    response.sendRedirect(request.getContextPath() + "/login.jsp?error=email_already_registered&email=" + email);
                    LOGGER.warning("Tentativo di registrazione con email già esistente: " + email);
                    return; // Importante per fermare l'esecuzione qui
                }
            } else {
                // Se il DAO non è stato inizializzato (dovrebbe essere gestito da init())
                LOGGER.log(Level.SEVERE, "UtenteDAO non inizializzato durante la registrazione.");
                errori.add("Errore di sistema durante la registrazione. Riprova più tardi.");
                request.setAttribute("erroriRegistrazione", errori);
                request.getRequestDispatcher("/registrati.jsp").forward(request, response);
                return;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante la verifica email per la registrazione: " + email, e);
            errori.add("Si è verificato un errore durante la verifica dell'email. Riprova.");
            request.setAttribute("erroriRegistrazione", errori);
            request.getRequestDispatcher("/registrati.jsp").forward(request, response);
            return;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Errore imprevisto durante la verifica email per la registrazione: " + email, e);
            errori.add("Si è verificato un errore imprevisto durante la registrazione. Riprova.");
            request.setAttribute("erroriRegistrazione", errori);
            request.getRequestDispatcher("/registrati.jsp").forward(request, response);
            return;
        }

        // Crea un'istanza della classe Utente
        Utente utente = new Utente();
        utente.setNome(nome);
        utente.setCognome(cognome);
        utente.setEmail(email);
        utente.setIndirizzo(indirizzo);
        utente.setCitta(citta);
        utente.setProvincia(provincia);

        try {
            utente.setCAP(Integer.parseInt(CAPStr));
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Formato CAP non valido fornito: " + CAPStr, e);
            errori.add("Il formato del CAP non è valido.");
            request.setAttribute("erroriRegistrazione", errori);
            request.getRequestDispatcher("/registrati.jsp").forward(request, response);
            return;
        }

        utente.setTelefono(telefonoStr);
        utente.setData_registrazione(new Timestamp(System.currentTimeMillis())); // Imposta la data corrente

        try {
            // Cifra la password prima di salvarla
            String hashedPassword = PasswordHashing.hashPassword(password);
            utente.setPasswordHash(hashedPassword);

            // Imposta isAdmin a false per default (o come da logica desiderata)
            utente.setAdmin(false);

            // Registra l'utente
            utenteDao.aggiungiUtenteRegistrato(utente);

            LOGGER.info("Registrazione utente riuscita per email: " + email);
            // Reindirizza alla pagina di login con un messaggio di successo
            response.sendRedirect(request.getContextPath() + "/login.jsp?message=registration_success");

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante la registrazione dell'utente " + email, e);
            request.setAttribute("erroreGenerico", "Si è verificato un errore del database durante la registrazione. Riprova più tardi.");
            request.getRequestDispatcher("/registrati.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Errore imprevisto durante la registrazione dell'utente " + email, e);
            request.setAttribute("erroreGenerico", "Si è verificato un errore inatteso durante la registrazione. Riprova più tardi.");
            request.getRequestDispatcher("/registrati.jsp").forward(request, response);
        }
    }

    // Metodo helper per la validazione dell'email
    private boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}