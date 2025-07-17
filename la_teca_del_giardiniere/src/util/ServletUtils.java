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
        String nomeComune = request.getParameter("nomeComune");
        if (nomeComune == null || nomeComune.trim().isEmpty()) {
            errori.add("Il campo 'Nome Comune' è obbligatorio.");
        } else {
            pianta.setNomeComune(nomeComune.trim());
        }
        
        String tipo = request.getParameter("tipo"); // ⭐ AGGIUNTA/CORREZIONE QUI ⭐
        if (tipo == null || tipo.trim().isEmpty()) {
            errori.add("Il campo 'Tipo di Pianta' è obbligatorio.");
        } else {
            pianta.setTipo(tipo.trim());
        }

        // NomeScientifico - opzionale
        String nomeScientificoBotanico = request.getParameter("nomeScientificoBotanico");
        if (nomeScientificoBotanico != null && !nomeScientificoBotanico.trim().isEmpty()) {
            pianta.setNomeScientificoBotanico(nomeScientificoBotanico.trim());
        }

        // Descrizione - opzionale
        String descrizione = request.getParameter("descrizioneBreve");
        if (descrizione != null && !descrizione.trim().isEmpty()) {
            pianta.setDescrizioneBreve(descrizione.trim());
        }
        
       // Descrizione Dettagliata - opzionale
        String descrizioneDettagliata = request.getParameter("descrizioneDettagliata");
        if (descrizioneDettagliata != null && !descrizioneDettagliata.trim().isEmpty()) {
            pianta.setDescrizioneDettagliata(descrizioneDettagliata.trim());
        }
        
     // Esposizione Luminosa - opzionale 
        String esposizioneLuminosa = request.getParameter("esposizioneLuminosa");
        if (esposizioneLuminosa != null && !esposizioneLuminosa.trim().isEmpty()) {
            pianta.setEsposizioneLuminosa(esposizioneLuminosa.trim());
        }

        // Tipo di Terreno - opzionale 
        String tipoDiTerreno = request.getParameter("tipoDiTerreno"); 
        if (tipoDiTerreno != null && !tipoDiTerreno.trim().isEmpty()) {
            pianta.setTipoDiTerreno(tipoDiTerreno.trim());
        }

        // Temperatura Ideale - opzionale (mancava nel tuo snippet, ma è nel form)
        String temperaturaIdeale = request.getParameter("temperaturaIdeale");
        if (temperaturaIdeale != null && !temperaturaIdeale.trim().isEmpty()) {
            pianta.setTemperaturaIdeale(temperaturaIdeale.trim());
        }

        // Frequenza Irrigazione - opzionale (mancava nel tuo snippet, ma è nel form)
        String frequenzaIrrigazione = request.getParameter("frequenzaIrrigazione"); 
        if (frequenzaIrrigazione != null && !frequenzaIrrigazione.trim().isEmpty()) {
            pianta.setFrequenzaIrrigazione(frequenzaIrrigazione.trim());
        }


        // Disponibilita - obbligatorio (numero intero)
        String disponibilitaStr = request.getParameter("disponibilita");
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
      String prezzoStr = request.getParameter("prezzo");
        if (prezzoStr == null || prezzoStr.trim().isEmpty()) {
            errori.add("Il campo 'Prezzo' è obbligatorio.");
        } else {
            try {
                BigDecimal prezzo = new BigDecimal(prezzoStr.trim());
                if (prezzo.compareTo(BigDecimal.ZERO) < 0) {
                    errori.add("Il campo 'Prezzo' deve essere zero o un numero positivo.");
                } else {
                    pianta.setPrezzo(prezzo);
                }
            } catch (NumberFormatException e) {
                errori.add("Il campo 'Prezzo' deve essere un numero valido (es. 10.50).");
            }
        }


        // Categoria - opzionale 
        String categoria = request.getParameter("categoria");
        if (categoria != null && !categoria.trim().isEmpty()) {
            pianta.setCategoria(categoria.trim());
        }

        // Immagine - opzionale (stringa, nome file o URL)
        String immagine = request.getParameter("immagine");
        if (immagine != null && !immagine.trim().isEmpty()) {
            pianta.setImmagine(immagine.trim());
        }

        return pianta;
    }

}