package src.com.la_teca_del_giardiniere.admin; // Mantengo il package 'admin' se è dove si trova ora

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import src.com.la_teca_del_giardiniere.classes.Utente; // Import corretto per la classe Utente
import src.com.la_teca_del_giardiniere.dao.UtenteDAO;
import src.com.la_teca_del_giardiniere.util.PasswordHashing; // Assumi che PasswordHashing sia questo

@WebServlet("/admin/ModificaUtenteServlet")
public class ModificaUtenteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UtenteDAO utenteDAO;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            utenteDAO = new UtenteDAO();
        } catch (SQLException e) {
            System.err.println("Errore di inizializzazione di UtenteDAO in ModificaUtenteServlet: " + e.getMessage());
            throw new ServletException("Impossibile inizializzare UtenteDAO", e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        // 1. Controllo Autenticazione e Autorizzazione (Admin)
        if (session == null || session.getAttribute("loggedInUser") == null) { // <--- Controlla "loggedInUser"
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Recupera l'oggetto Utente loggato dalla sessione
        Utente utenteLoggato = (Utente) session.getAttribute("loggedInUser");
        if (utenteLoggato == null || !utenteLoggato.isAdmin()) { // <--- Usa isAdmin() del bean Utente
            response.sendRedirect(request.getContextPath() + "/accesso_negato.html");
            return;
        }

        // 2. Recupero ID Utente dalla request
        String utenteIdStr = request.getParameter("id");
        if (utenteIdStr == null || utenteIdStr.trim().isEmpty()) {
            request.setAttribute("messaggio", "ID utente non fornito per la modifica.");
            request.setAttribute("tipoMessaggio", "error");
            request.getRequestDispatcher("/admin/utenti.jsp").forward(request, response); // O pagina di errore
            return;
        }

        try {
            int utenteId = Integer.parseInt(utenteIdStr);
            // 3. Recupera l'utente dal database
            Utente utente = utenteDAO.getUtenteByIdWithRuoli(utenteId); // Usa il metodo che recupera i ruoli

            if (utente != null) {
                // 4. Invia l'oggetto utente alla JSP per pre-popolare il form
                request.setAttribute("utenteDaModificare", utente);
                // Ho cambiato la JSP di destinazione. È preferibile avere una JSP specifica per la modifica
                // invece di usare "registrati.jsp" che di solito è per nuove registrazioni.
                request.getRequestDispatcher("/admin/formModificaUtente.jsp").forward(request, response);
            } else {
                request.setAttribute("messaggio", "Utente non trovato per la modifica.");
                request.setAttribute("tipoMessaggio", "error");
                request.getRequestDispatcher("/admin/utenti.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("messaggio", "ID utente non valido.");
            request.setAttribute("tipoMessaggio", "error");
            request.getRequestDispatcher("/admin/utenti.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("messaggio", "Errore database durante il recupero utente per modifica: " + e.getMessage());
            request.setAttribute("tipoMessaggio", "error");
            request.getRequestDispatcher("/admin/utenti.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        // 1. Controllo Autenticazione e Autorizzazione (Admin)
        if (session == null || session.getAttribute("loggedInUser") == null) { // <--- Controlla "loggedInUser"
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Recupera l'oggetto Utente loggato dalla sessione
        Utente utenteLoggato = (Utente) session.getAttribute("loggedInUser");
        if (utenteLoggato == null || !utenteLoggato.isAdmin()) { // <--- Usa isAdmin() del bean Utente
            response.sendRedirect(request.getContextPath() + "/accesso_negato.html");
            return;
        }

        // 2. Recupero dati dal form
        List<String> errori = new ArrayList<>();
        int id = 0;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            errori.add("ID utente non valido.");
        }

        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String email = request.getParameter("email");
        String password = request.getParameter("password"); // Potrebbe essere vuota
        String indirizzo = request.getParameter("indirizzo");
        String citta = request.getParameter("citta");
        String provincia = request.getParameter("provincia"); // <--- RECUPERA LA PROVINCIA DAL FORM
        String capStr = request.getParameter("CAP"); // Il CAP è una stringa dal form
        String telefono = request.getParameter("telefono"); // Il telefono è una stringa
        // Il checkbox invia "on" se selezionato, o null se non selezionato
        boolean isAdmin = "on".equals(request.getParameter("isAdmin")); // <--- Gestione checkbox isAdmin

        // 3. Validazione dei dati (SERVER-SIDE)
        if (nome == null || nome.trim().isEmpty()) errori.add("Il nome è obbligatorio.");
        if (cognome == null || cognome.trim().isEmpty()) errori.add("Il cognome è obbligatorio.");
        // Validazione email più robusta
        if (email == null || email.trim().isEmpty() || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) errori.add("L'email non è valida.");
        if (indirizzo == null || indirizzo.trim().isEmpty()) errori.add("L'indirizzo è obbligatorio.");
        if (citta == null || citta.trim().isEmpty()) errori.add("La città è obbligatoria.");
        if (provincia == null || provincia.trim().isEmpty()) errori.add("La provincia è obbligatoria."); // <--- Validazione provincia
        if (capStr == null || !capStr.matches("\\d{5}")) errori.add("Il CAP deve essere di 5 cifre numeriche.");
        if (telefono == null || !telefono.matches("\\d+")) errori.add("Il telefono deve contenere solo cifre."); // Il telefono può essere di lunghezza variabile, quindi solo cifre

        Utente utenteDaModificare = null;
        try {
            utenteDaModificare = utenteDAO.getUtenteByIdWithRuoli(id); // Recupera l'utente corrente per mantenere dati non modificati
            if (utenteDaModificare == null) {
                errori.add("Utente non trovato nel database per la modifica.");
            }
        } catch (SQLException e) {
            errori.add("Errore database durante il recupero utente per validazione.");
            e.printStackTrace();
        }

        // 4. Se ci sono errori, reindirizza al form con i messaggi
        if (!errori.isEmpty()) {
            request.setAttribute("erroriModifica", errori); // Utilizzo un attributo diverso per gli errori di modifica
            request.setAttribute("utenteDaModificare", utenteDaModificare); // Ri-popola il form con i dati inseriti
            request.getRequestDispatcher("/admin/formModificaUtente.jsp").forward(request, response); // <--- JSP di modifica
            return;
        }

        // 5. Aggiorna l'oggetto utente con i nuovi dati
        // Assicurati di convertire CAP a int prima di impostarlo nell'oggetto Utente
        int CAP = 0;
        try {
            CAP = Integer.parseInt(capStr);
        } catch (NumberFormatException e) {
            // Questo caso dovrebbe essere già gestito dalla validazione regex, ma è una safety net
            errori.add("Formato CAP non valido.");
            request.setAttribute("erroriModifica", errori);
            request.setAttribute("utenteDaModificare", utenteDaModificare);
            request.getRequestDispatcher("/admin/formModificaUtente.jsp").forward(request, response);
            return;
        }

        utenteDaModificare.setNome(nome);
        utenteDaModificare.setCognome(cognome);
        utenteDaModificare.setEmail(email);
        utenteDaModificare.setIndirizzo(indirizzo);
        utenteDaModificare.setCitta(citta);
        utenteDaModificare.setProvincia(provincia); // <--- IMPOSTA LA PROVINCIA
        utenteDaModificare.setCAP(CAP); // Imposta il CAP convertito
        utenteDaModificare.setTelefono(telefono); // Il telefono è String
        utenteDaModificare.setAdmin(isAdmin); // <--- Imposta il ruolo admin (isAdmin)

        // Gestione password (aggiorna solo se fornita e valida)
        if (password != null && !password.trim().isEmpty()) {
            if (password.length() < 8) { // Aggiungi validazione minima per la password
                errori.add("La nuova password deve contenere almeno 8 caratteri.");
                request.setAttribute("erroriModifica", errori);
                request.setAttribute("utenteDaModificare", utenteDaModificare);
                request.getRequestDispatcher("/admin/formModificaUtente.jsp").forward(request, response);
                return;
            }
            utenteDaModificare.setPasswordHash(PasswordHashing.hashPassword(password)); // <--- Usa setPasswordHash
        }

        try {
            // 6. Invia l'oggetto aggiornato al DAO per il salvataggio
            boolean successo = utenteDAO.updateUtente(utenteDaModificare); // Assumi che UtenteDAO abbia un metodo updateUtente()

            if (successo) {
                // 7. Reindirizza ai dettagli utente o alla lista utenti con un messaggio di successo
                request.setAttribute("messaggio", "Profilo utente aggiornato con successo!");
                request.setAttribute("tipoMessaggio", "success");
                // È meglio reindirizzare a /admin/utentiServlet per ricaricare la lista aggiornata
                response.sendRedirect(request.getContextPath() + "/admin/utentiServlet?messaggio=modificaSuccesso");
            } else {
                errori.add("Impossibile aggiornare il profilo utente. Riprovare.");
                request.setAttribute("erroriModifica", errori);
                request.setAttribute("utenteDaModificare", utenteDaModificare);
                request.getRequestDispatcher("/admin/formModificaUtente.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            errori.add("Errore database durante l'aggiornamento del profilo: " + e.getMessage());
            request.setAttribute("erroriModifica", errori);
            request.setAttribute("utenteDaModificare", utenteDaModificare);
            request.getRequestDispatcher("/admin/formModificaUtente.jsp").forward(request, response);
        }
    }
}