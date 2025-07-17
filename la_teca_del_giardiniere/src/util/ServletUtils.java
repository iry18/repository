// Snippet from util/ServletUtils.java (for verification)
package util;

import la_teca_del_giardiniere.classes.Piante; // Make sure this import is here
import la_teca_del_giardiniere.classes.Utente; // Make sure this import is here

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import java.util.logging.Logger;

public class ServletUtils {

    private static final Logger LOGGER = Logger.getLogger(ServletUtils.class.getName());

    // --- YOUR checkAuthenticationAndAuthorization METHOD ---
    public static boolean checkAuthenticationAndAuthorization(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        String contextPath = request.getContextPath();

        if (session == null || session.getAttribute("currentUser") == null) { // Or "loggedInUser" if you changed LoginServlet
            LOGGER.info("Tentativo di accesso non autenticato. Reindirizzamento a Login.jsp");
            if (session == null) {
                 session = request.getSession(true);
            }
            session.setAttribute("messaggioErroreLogin", "Devi effettuare il login per accedere a questa risorsa.");
            session.setAttribute("tipoMessaggio", "error");
            response.sendRedirect(contextPath + "/Login.jsp");
            return false;
        }

        Utente utenteLoggato = (Utente) session.getAttribute("currentUser"); // Or "loggedInUser"
        if (utenteLoggato == null) {
            LOGGER.info("Utente in sessione nullo dopo il recupero. Reindirizzamento a Login.jsp");
            session.setAttribute("messaggioErroreLogin", "La tua sessione non è valida. Effettua nuovamente il login.");
            session.setAttribute("tipoMessaggio", "error");
            response.sendRedirect(contextPath + "/Login.jsp");
            return false;
        }
        // ... (authorization logic) ...
        return true;
    }


    // --- YOUR buildPiantaFromRequest METHOD ---
    // Make sure the method signature is exactly as below: public static
    public static Piante buildPiantaFromRequest(HttpServletRequest request, List<String> errori) {
        Piante pianta = new Piante();

        // NomeComune - obbligatorio
        String nomeComune = request.getParameter("NomeComune");
        if (nomeComune == null || nomeComune.trim().isEmpty()) {
            errori.add("Il campo 'Nome Comune' è obbligatorio.");
        } else {
            pianta.setNomeComune(nomeComune.trim());
        }

        // NomeScientifico - opzionale
        String nomeScientifico = request.getParameter("NomeScientifico");
        if (nomeScientifico != null && !nomeScientifico.trim().isEmpty()) {
            pianta.setNomeScientificoBotanico(nomeScientifico.trim());
        }

        // Descrizione - opzionale
        String descrizione = request.getParameter("Descrizione");
        if (descrizione != null && !descrizione.trim().isEmpty()) {
            pianta.setDescrizioneBreve(descrizione.trim());
        }

        // Disponibilita - obbligatorio (numero intero)
        String disponibilitaStr = request.getParameter("Disponibilita");
        if (disponibilitaStr == null || disponibilitaStr.trim().isEmpty()) {
            errori.add("Il campo 'Disponibilità' è obbligatorio.");
        } else {
            try {
                int disponibilita = Integer.parseInt(disponibilitaStr.trim());
                if (disponibilita < 0) {
                    errori.add("Il campo 'Disponibilità' deve essere zero o un numero positivo.");
                } else {
                    pianta.setdisponibilita(disponibilita);
                }
            } catch (NumberFormatException e) {
                errori.add("Il campo 'Disponibilità' deve essere un numero intero valido.");
            }
        }

        // Prezzo - opzionale (numero decimale)
        String prezzoStr = request.getParameter("Prezzo");
        if (prezzoStr != null && !prezzoStr.trim().isEmpty()) {
            try {
                BigDecimal prezzo = new BigDecimal(prezzoStr.trim());
                if (prezzo.compareTo(BigDecimal.ZERO) < 0) {
                    errori.add("Il campo 'Prezzo' deve essere zero o un numero positivo.");
                } else {
                    pianta.setPrezzo(prezzo);
                }
            } catch (NumberFormatException | NullPointerException e) {
                errori.add("Il campo 'Prezzo' deve essere un numero valido.");
            }
        }

        // Categoria - opzionale 
        String categoria = request.getParameter("Categoria");
        if (categoria != null && !categoria.trim().isEmpty()) {
            pianta.setCategoria(categoria.trim());
        }

        // Immagine - opzionale (stringa, nome file o URL)
        String immagine = request.getParameter("Immagine");
        if (immagine != null && !immagine.trim().isEmpty()) {
            pianta.setImmagine(immagine.trim());
        }

        return pianta;
    }

}