/**
 *Questa classe conterrà i metodi comuni utilizzati da tutte le servlet, come la gestione dell'autenticazione/autorizzazione e la costruzione di un oggetto Piante
*/
package util;

import la_teca_del_giardiniere.classes.Piante;
import la_teca_del_giardiniere.classes.Utente;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServletUtils {

    private static final Logger LOGGER = Logger.getLogger(ServletUtils.class.getName());

    public static boolean checkAuthenticationAndAuthorization(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        String contextPath = request.getContextPath();

        if (session == null || session.getAttribute("loggedInUser") == null) {
            LOGGER.info("Tentativo di accesso non autenticato. Reindirizzamento a Login.jsp");
            response.sendRedirect(contextPath + "/Login.jsp");
            return false;
        }

        Utente utenteLoggato = (Utente) session.getAttribute("loggedInUser");
        if (utenteLoggato == null) {
            LOGGER.info("Utente in sessione nullo. Reindirizzamento a Login.jsp");
            response.sendRedirect(contextPath + "/Login.jsp");
            return false;
        }

        if (!utenteLoggato.isAdmin() && !utenteLoggato.getRuoli().contains("venditore")) {
            String userEmail = (utenteLoggato != null) ? utenteLoggato.getEmail() : "sconosciuto";
            LOGGER.log(Level.WARNING, "Utente non autorizzato ({0}) ha tentato di accedere alla gestione piante. Reindirizzamento ad accesso_negato.html", userEmail);
            response.sendRedirect(contextPath + "/accesso_negato.html");
            return false;
        }
        return true;
    }

    public static Piante buildPiantaFromRequest(HttpServletRequest request, List<String> errori) {
        Piante pianta = new Piante();

        // Recupero l'ID se presente, utile per la modifica
        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                pianta.setId(Integer.parseInt(idStr.trim()));
            } catch (NumberFormatException e) {
                errori.add("ID pianta non valido.");
                LOGGER.log(Level.WARNING, "ID pianta non valido per buildPiantaFromRequest: " + idStr, e);
            }
        }

        String nomeComune = request.getParameter("nomeComune");
        if (nomeComune == null || nomeComune.trim().isEmpty()) {
            errori.add("Il Nome Comune è obbligatorio.");
        } else {
            pianta.setNomeComune(nomeComune.trim());
        }

        String tipoStr = request.getParameter("tipo");
        if (tipoStr == null || tipoStr.trim().isEmpty()) {
            errori.add("Il Tipo di Pianta (Interno/Esterno) è obbligatorio.");
        } else {
            pianta.setTipo(tipoStr.trim());
        }

        String nomeBotanico = request.getParameter("nomeBotanico");
        if (nomeBotanico == null || nomeBotanico.trim().isEmpty()) {
            errori.add("Il Nome Scientifico/Botanico è obbligatorio.");
        } else {
            pianta.setNomeBotanico(nomeBotanico.trim());
        }

        String categoria = request.getParameter("categoria");
        if (categoria == null || categoria.trim().isEmpty()) {
            errori.add("La Categoria è obbligatoria.");
        } else {
            pianta.setCategoria(categoria.trim());
        }

        String descrizione = request.getParameter("descrizione");
        if (descrizione == null || descrizione.trim().isEmpty()) {
            errori.add("La Descrizione è obbligatoria.");
        } else {
            pianta.setDescrizione(descrizione.trim());
        }

        String esposizioneLuminosa = request.getParameter("esposizioneLuminosa");
        if (esposizioneLuminosa == null || esposizioneLuminosa.trim().isEmpty()) {
            errori.add("L'Esposizione Luminosa è obbligatoria.");
        } else {
            pianta.setEsposizioneLuminosa(esposizioneLuminosa.trim());
        }

        String tipoDiTerreno = request.getParameter("tipoDiTerreno");
        if (tipoDiTerreno == null || tipoDiTerreno.trim().isEmpty()) {
            errori.add("Il Tipo di Terreno è obbligatorio.");
        } else {
            pianta.setTipoDiTerreno(tipoDiTerreno.trim());
        }

        String temperaturaIdealeStr = request.getParameter("temperaturaIdeale");
        if (temperaturaIdealeStr != null && !temperaturaIdealeStr.trim().isEmpty()) {
            try {
                pianta.setTemperaturaIdeale(Integer.parseInt(temperaturaIdealeStr.trim()));
            } catch (NumberFormatException e) {
                errori.add("Il formato della Temperatura Ideale non è valido. Inserire un numero intero.");
            }
        } else {
            pianta.setTemperaturaIdeale(null);
        }

        String frequenzaIrrigazione = request.getParameter("frequenzaIrrigazione");
        if (frequenzaIrrigazione == null || frequenzaIrrigazione.trim().isEmpty()) {
            errori.add("La Frequenza di Irrigazione è obbligatoria.");
        } else {
            pianta.setFrequenzaIrrigazione(frequenzaIrrigazione.trim());
        }

        String prezzoStr = request.getParameter("prezzo");
        if (prezzoStr == null || prezzoStr.trim().isEmpty()) {
            errori.add("Il Prezzo è obbligatorio.");
        } else {
            try {
                prezzoStr = prezzoStr.replace(',', '.');
                BigDecimal prezzo = new BigDecimal(prezzoStr.trim());
                if (prezzo.compareTo(BigDecimal.ZERO) < 0) {
                    errori.add("Il Prezzo non può essere negativo.");
                } else {
                    pianta.setPrezzo(prezzo);
                }
            } catch (NumberFormatException e) {
                errori.add("Il formato del Prezzo non è valido (es. 12.99).");
            }
        }

        String quantitaDisponibileStr = request.getParameter("quantitaDisponibile");
        if (quantitaDisponibileStr != null && !quantitaDisponibileStr.trim().isEmpty()) {
            try {
                int quantita = Integer.parseInt(quantitaDisponibileStr.trim());
                if (quantita < 0) {
                    errori.add("La Quantità Disponibile non può essere negativa.");
                } else {
                    pianta.setQuantitaDisponibile(quantita);
                }
            } catch (NumberFormatException e) {
                errori.add("Il formato della Quantità Disponibile non è valido. Inserire un numero intero.");
            }
        } else {
            pianta.setQuantitaDisponibile(null);
        }

        String urlImmagine = request.getParameter("urlImmagine");
        if (urlImmagine == null || urlImmagine.trim().isEmpty()) {
            pianta.setUrlImmagine(null);
        } else {
            pianta.setUrlImmagine(urlImmagine.trim());
        }
        return pianta;
    }
}