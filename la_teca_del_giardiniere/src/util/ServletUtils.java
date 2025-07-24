package util;

import la_teca_del_giardiniere.classes.Piante;
import la_teca_del_giardiniere.classes.Utente;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

public class ServletUtils {

    private static final Logger LOGGER = Logger.getLogger(ServletUtils.class.getName());

    public static boolean checkAuthenticationAndAuthorization(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        String contextPath = request.getContextPath();

        if (session == null || session.getAttribute("currentUser") == null) {
            LOGGER.info("Tentativo di accesso non autenticato. Reindirizzamento a Login.jsp");
            session = request.getSession(true);
            session.setAttribute("messaggioErroreLogin", "Devi effettuare il login per accedere a questa risorsa.");
            session.setAttribute("tipoMessaggio", "error");
            response.sendRedirect(contextPath + "/Login.jsp");
            return false;
        }

        Utente utenteLoggato = (Utente) session.getAttribute("currentUser");
        if (utenteLoggato == null) {
            LOGGER.warning("Utente in sessione nullo. Reindirizzamento a Login.jsp");
            session.setAttribute("messaggioErroreLogin", "La tua sessione non è valida. Effettua nuovamente il login.");
            session.setAttribute("tipoMessaggio", "error");
            response.sendRedirect(contextPath + "/Login.jsp");
            return false;
        }

        // Authorization logic could go here
        return true;
    }

    public static Piante buildPiantaFromRequest(HttpServletRequest request, List<String> errori) {
        Piante pianta = new Piante();

        // Nome Comune (obbligatorio)
        String nomeComune = trimParam(request.getParameter("nomeComune"));
        if (nomeComune.isEmpty()) {
            errori.add("Il campo 'Nome Comune' è obbligatorio.");
        } else {
            pianta.setNomeComune(nomeComune);
        }

        // Tipo (obbligatorio)
        String tipo = trimParam(request.getParameter("tipo"));
        if (tipo.isEmpty()) {
            errori.add("Il campo 'Tipo di Pianta' è obbligatorio.");
        } else {
            pianta.setTipo(tipo);
        }

        // Campi opzionali
        pianta.setNomeScientificoBotanico(trimOrNull(request.getParameter("nomeScientificoBotanico")));
        pianta.setDescrizioneBreve(trimOrNull(request.getParameter("descrizioneBreve")));
        pianta.setDescrizioneDettagliata(trimOrNull(request.getParameter("descrizioneDettagliata")));
        pianta.setEsposizioneLuminosa(trimOrNull(request.getParameter("esposizioneLuminosa")));
        pianta.setTipoDiTerreno(trimOrNull(request.getParameter("tipoDiTerreno")));
        pianta.setTemperaturaIdeale(trimOrNull(request.getParameter("temperaturaIdeale")));
        pianta.setFrequenzaIrrigazione(trimOrNull(request.getParameter("frequenzaIrrigazione")));
        pianta.setCategoria(trimOrNull(request.getParameter("categoria")));
        pianta.setImmagine(trimOrNull(request.getParameter("immagine")));

        // Disponibilità (obbligatorio)
        String disponibilitaStr = trimParam(request.getParameter("disponibilita"));
        if (disponibilitaStr.isEmpty()) {
            errori.add("Il campo 'Disponibilità' è obbligatorio.");
        } else {
            try {
                int disponibilita = Integer.parseInt(disponibilitaStr);
                if (disponibilita < 0) {
                    errori.add("Il campo 'Disponibilità' deve essere zero o un numero positivo.");
                } else {
                    pianta.setDisponibilita(disponibilita);
                }
            } catch (NumberFormatException e) {
                errori.add("Il campo 'Disponibilità' deve essere un numero intero valido.");
            }
        }

        // Prezzo (obbligatorio)
        String prezzoStr = trimParam(request.getParameter("prezzo"));
        if (prezzoStr.isEmpty()) {
            errori.add("Il campo 'Prezzo' è obbligatorio.");
        } else {
            try {
                BigDecimal prezzo = new BigDecimal(prezzoStr);
                if (prezzo.scale() > 2) {
                    errori.add("Il campo 'Prezzo' non può avere più di due cifre decimali.");
                } else if (prezzo.compareTo(BigDecimal.ZERO) < 0) {
                    errori.add("Il campo 'Prezzo' deve essere zero o positivo.");
                } else {
                    pianta.setPrezzo(prezzo);
                }
            } catch (NumberFormatException e) {
                errori.add("Il campo 'Prezzo' deve essere un numero valido (es. 10.50).");
            }
        }

        return pianta;
    }

    // Utility helper methods
    private static String trimParam(String param) {
        return param != null ? param.trim() : "";
    }

    private static String trimOrNull(String param) {
        return (param != null && !param.trim().isEmpty()) ? param.trim() : null;
    }
}
