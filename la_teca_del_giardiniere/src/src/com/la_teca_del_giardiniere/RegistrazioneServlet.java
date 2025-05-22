package src.com.la_teca_del_giardiniere;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import src.com.la_teca_del_giardiniere.classes.registrazione;
import src.com.la_teca_del_giardiniere.dao.UtenteDAO;
import src.com.la_teca_del_giardiniere.util.PasswordHashing; // Assumi che tu abbia questa classe

@WebServlet("/RegistrazioneServlet")
public class RegistrazioneServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UtenteDAO utenteDao;

    public RegistrazioneServlet() {
        super();
        try {
            utenteDao = new UtenteDAO();
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestire l'errore di creazione del DAO (es. log, pagina di errore)
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String indirizzo = request.getParameter("indirizzo");
        String citta = request.getParameter("citta");
        String CAPStr = request.getParameter("cap");
        String telefonoStr = request.getParameter("telefono");
        String dataRegistrazioneStr = request.getParameter("data_registrazione");
        String provincia = request.getParameter("provincia"); // Recupera la provincia

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
            request.getRequestDispatcher("registrati.jsp").forward(request, response);
            return; // Non proseguire con la registrazione
        }

        registrazione utente = new registrazione();
        utente.setNome(nome);
        utente.setCognome(cognome);
        utente.setEmail(email);
        utente.setIndirizzo(indirizzo);
        utente.setCitta(citta);
        utente.setProvincia(provincia);

        // Conversione per CAP
        try {
            utente.setCAP(Integer.parseInt(CAPStr));
        } catch (NumberFormatException e) {
            errori.add("Il formato del CAP non è valido.");
        }

        // Conversione per telefono
        try {
            utente.setTelefono(Integer.parseInt(telefonoStr));
        } catch (NumberFormatException e) {
            errori.add("Il formato del numero di telefono non è valido.");
        }

        // Conversione per data_registrazione
        try {
            if (dataRegistrazioneStr != null && !dataRegistrazioneStr.isEmpty()) {
                utente.setData_registrazione(Timestamp.valueOf(dataRegistrazioneStr));
            } else {
                utente.setData_registrazione(new Timestamp(System.currentTimeMillis())); // Imposta la data/ora corrente se non fornita
            }
        } catch (IllegalArgumentException e) {
            errori.add("Il formato della data di registrazione non è valido (yyyy-mm-dd hh:mm:ss).");
        }

        // Se ci sono errori di conversione, reindirizza
        if (!errori.isEmpty()) {
            request.setAttribute("erroriRegistrazione", errori);
            request.getRequestDispatcher("registrati.jsp").forward(request, response);
            return;
        }

        try {
            if (utenteDao != null) {
                // Controlla se l'email esiste già
            	if (utenteDao.checkEmailExists(email)) {
            	    request.setAttribute("erroreRegistrazione", "L'email è già registrata. Se hai già un account, puoi effettuare il login.");
            	    request.getRequestDispatcher("registrati.jsp").forward(request, response);
            	    return;
            	}

                // Cifra la password prima di salvarla
                String hashedPassword = PasswordHashing.hashPassword(password);
                utente.setPassword(hashedPassword);

                // Registra l'utente con il ruolo predefinito di "compratore"
                utenteDao.aggiungiUtenteRegistrato(utente);

                // Reindirizza alla pagina di login con un messaggio di successo
                response.sendRedirect("login.jsp?registrazioneSuccesso=true");

            } else {
                request.setAttribute("erroreGenerico", "Errore interno del server. Riprova più tardi.");
                request.getRequestDispatcher("registrati.jsp").forward(request, response);
                // Log dell'errore: log.error("Errore nella creazione del DAO");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("erroreGenerico", "Si è verificato un errore durante la registrazione. Riprova più tardi.");
            request.getRequestDispatcher("registrati.jsp").forward(request, response);
            // Log dell'errore: log.error("Errore SQL durante la registrazione", e);
        }
    }

    // Funzione per la validazione dell'email
    private boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}