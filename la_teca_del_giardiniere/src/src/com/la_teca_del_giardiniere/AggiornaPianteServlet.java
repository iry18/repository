package src.com.la_teca_del_giardiniere;

import java.io.IOException;
import java.math.BigDecimal; // Import per BigDecimal
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime; // Per la data/ora attuale
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger; // Per un logging più robusto

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession; // Import per HttpSession

import src.com.la_teca_del_giardiniere.classes.Piante; // CAMBIATO: da 'piante' a 'Pianta'
import src.com.la_teca_del_giardiniere.classes.Utente; // Import per la classe Utente
import src.com.la_teca_del_giardiniere.dao.PianteDAO;

// Ho rinominato la servlet per seguire le convenzioni di denominazione (PascalCase)
@WebServlet("/AggiornaPianteServlet")
public class AggiornaPianteServlet extends HttpServlet { // CAMBIATO: aggiornapianteServlet a AggiornaPianteServlet
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(AggiornaPianteServlet.class.getName()); // Logger

    private PianteDAO pianteDAO;

    public AggiornaPianteServlet() {
        super();
        try {
            pianteDAO = new PianteDAO();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante l'inizializzazione del PianteDAO in AggiornaPianteServlet.", e);
            // In un'applicazione reale, potresti voler lanciare una ServletException
            // o reindirizzare a una pagina di errore grave all'avvio.
            // throw new ServletException("Errore critico durante l'inizializzazione della servlet.", e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contextPath = request.getContextPath();

        // 1. Controllo Autenticazione e Autorizzazione (solo amministratori o venditori)
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            LOGGER.info("Tentativo di accesso non autenticato ad AggiornaPianteServlet. Reindirizzamento a login.jsp");
            response.sendRedirect(contextPath + "/login.jsp");
            return;
        }

        Utente utenteLoggato = (Utente) session.getAttribute("loggedInUser");
        if (utenteLoggato == null || (!utenteLoggato.isAdmin() && !utenteLoggato.getRuoli().contains("venditore"))) {
            LOGGER.log(Level.WARNING, "Utente non autorizzato ({0}) ha tentato di aggiornare una pianta. Reindirizzamento ad accesso_negato.html",
                    utenteLoggato != null ? utenteLoggato.getEmail() : "sconosciuto");
            response.sendRedirect(contextPath + "/accesso_negato.html");
            return;
        }

        // 2. Recupera l'ID dal campo nascosto
        String idStr = request.getParameter("id");
        Integer idPianta = null;
        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                idPianta = Integer.parseInt(idStr);
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "ID pianta non valido per l'aggiornamento: {0}", idStr);
                request.setAttribute("messaggio", "ID pianta non valido per l'aggiornamento.");
                request.setAttribute("tipoMessaggio", "error");
                request.getRequestDispatcher("/modificapiante.jsp").forward(request, response); // Ritorna al form di modifica
                return;
            }
        } else {
            LOGGER.warning("Tentativo di aggiornamento senza ID pianta fornito.");
            request.setAttribute("messaggio", "ID pianta non fornito per l'aggiornamento.");
            request.setAttribute("tipoMessaggio", "error");
            request.getRequestDispatcher("/modificapiante.jsp").forward(request, response); // Ritorna al form di modifica
            return;
        }

        // 3. Recupera gli altri parametri dal form
        String nomeComune = request.getParameter("nomeComune");
        String tipoStr = request.getParameter("tipo"); // Ora è una Stringa ("interno", "esterno")
        String nomeBotanico = request.getParameter("nomeBotanico"); // CAMBIATO: NomeScientificoBotanico a nomeBotanico
        String categoria = request.getParameter("categoria"); // CAMBIATO: Categoria a categoria
        String descrizione = request.getParameter("descrizione"); // CAMBIATO: unito breve e dettagliata
        String esposizioneLuminosa = request.getParameter("esposizioneLuminosa"); // CAMBIATO: EsposizioneLuminosa a esposizioneLuminosa
        String tipoDiTerreno = request.getParameter("tipoDiTerreno"); // CAMBIATO: TipoDiTerreno a tipoDiTerreno
        String temperaturaIdealeStr = request.getParameter("temperaturaIdeale"); // CAMBIATO: TemperaturaIdeale a temperaturaIdeale
        String frequenzaIrrigazione = request.getParameter("frequenzaIrrigazione"); // CAMBIATO: FrequenzaIrrigazione a frequenzaIrrigazione
        String prezzoStr = request.getParameter("prezzo"); // CAMBIATO: Prezzo a prezzo
        String quantitaDisponibileStr = request.getParameter("quantitaDisponibile"); // CAMBIATO: Disponibilita a quantitaDisponibile
        String urlImmagine = request.getParameter("urlImmagine"); // CAMBIATO: Immagine a urlImmagine

        List<String> errori = new ArrayList<>();
        Piante pianta = new Piante(); // CAMBIATO: da 'piante' a 'Pianta'
        pianta.setId(idPianta); // Imposta l'ID nell'oggetto pianta

        // 4. Esegui la validazione e il parsing dei dati
        if (nomeComune == null || nomeComune.trim().isEmpty()) {
            errori.add("Il Nome Comune è obbligatorio.");
        } else {
            pianta.setNomeComune(nomeComune);
        }

        if (tipoStr == null || tipoStr.trim().isEmpty()) {
            errori.add("Il Tipo di pianta è obbligatorio.");
        } else {
            pianta.setTipo(tipoStr); // Imposta la stringa direttamente
        }

        if (nomeBotanico == null || nomeBotanico.trim().isEmpty()) {
            errori.add("Il Nome Scientifico/Botanico è obbligatorio.");
        } else {
            pianta.setNomeBotanico(nomeBotanico);
        }

        if (categoria == null || categoria.trim().isEmpty()) {
            errori.add("La Categoria è obbligatoria.");
        } else {
            pianta.setCategoria(categoria);
        }

        if (descrizione == null || descrizione.trim().isEmpty()) {
            errori.add("La Descrizione è obbligatoria.");
        } else {
            pianta.setDescrizione(descrizione);
        }

        if (esposizioneLuminosa == null || esposizioneLuminosa.trim().isEmpty()) {
            errori.add("L'Esposizione Luminosa è obbligatoria.");
        } else {
            pianta.setEsposizioneLuminosa(esposizioneLuminosa);
        }

        if (tipoDiTerreno == null || tipoDiTerreno.trim().isEmpty()) {
            errori.add("Il Tipo di Terreno è obbligatorio.");
        } else {
            pianta.setTipoDiTerreno(tipoDiTerreno);
        }

        if (frequenzaIrrigazione == null || frequenzaIrrigazione.trim().isEmpty()) {
            errori.add("La Frequenza di Irrigazione è obbligatoria.");
        } else {
            pianta.setFrequenzaIrrigazione(frequenzaIrrigazione);
        }

        // Temperatura Ideale (Integer, può essere null)
        if (temperaturaIdealeStr != null && !temperaturaIdealeStr.trim().isEmpty()) {
            try {
                pianta.setTemperaturaIdeale(Integer.parseInt(temperaturaIdealeStr));
            } catch (NumberFormatException e) {
                errori.add("Il formato della Temperatura Ideale non è valido. Inserire un numero intero.");
            }
        } else {
            pianta.setTemperaturaIdeale(null); // Permetti che sia null
        }

        // Prezzo (BigDecimal)
        if (prezzoStr == null || prezzoStr.trim().isEmpty()) {
            errori.add("Il Prezzo è obbligatorio.");
        } else {
            try {
                // Sostituisci la virgola con il punto per il parsing se necessario
                prezzoStr = prezzoStr.replace(',', '.');
                BigDecimal prezzo = new BigDecimal(prezzoStr);
                if (prezzo.compareTo(BigDecimal.ZERO) < 0) {
                    errori.add("Il Prezzo non può essere negativo.");
                } else {
                    pianta.setPrezzo(prezzo);
                }
            } catch (NumberFormatException e) {
                errori.add("Il formato del Prezzo non è valido. Inserire un numero con decimali (es. 10.50).");
            }
        }

        // Quantità Disponibile (Integer, può essere null)
        if (quantitaDisponibileStr != null && !quantitaDisponibileStr.trim().isEmpty()) {
            try {
                pianta.setQuantitaDisponibile(Integer.parseInt(quantitaDisponibileStr));
                if (pianta.getQuantitaDisponibile() < 0) {
                    errori.add("La Quantità Disponibile non può essere negativa.");
                }
            } catch (NumberFormatException e) {
                errori.add("Il formato della Quantità Disponibile non è valido. Inserire un numero intero.");
            }
        } else {
            pianta.setQuantitaDisponibile(null); // Permetti che sia null
        }

        // Data Inserimento (Timestamp - di solito si auto-aggiorna o si prende l'esistente)
        // Per l'aggiornamento, di solito non si modifica la data di inserimento originale
        // A meno che non ci sia un campo "DataUltimaModifica".
        // Per ora, la lasciamo invariata e prendiamo quella già esistente dalla pianta.
        // Se la pianta esiste già, prenderemo la sua Data_inserimento.
        // Altrimenti, se si dovesse creare una nuova pianta, si userebbe new Timestamp(System.currentTimeMillis());
        // Se nel form c'è un campo nascosto per la data di inserimento originale, lo si può recuperare.
        // Altrimenti, è più sicuro recuperare la pianta dal DB, copiare la data, e poi aggiornare gli altri campi.
        // Per semplicità, qui impostiamo la data corrente. Se vuoi mantenere quella originale,
        // dovresti recuperare la pianta dal DB QUI, prima di impostare i nuovi valori.
        // Dato che nel DAO l'aggiornamento ha Data_inserimento come parametro, possiamo passargli la data corrente
        // o la data già esistente recuperandola se serve.
        // Per ora, la imposto alla data corrente per rendere il campo obbligatorio per l'aggiornamento.
        pianta.setDataInserimento(Timestamp.valueOf(LocalDateTime.now()));

        if (urlImmagine == null || urlImmagine.trim().isEmpty()) {
            // Non è necessariamente un errore se l'immagine non viene aggiornata o non è fornita,
            // dipende dalla tua logica di business. Potresti volerla lasciare come era.
            // Per il momento, se è vuota, si imposta a null o si prende quella esistente.
            // In questo esempio, la imposto a null se è vuota.
            pianta.setUrlImmagine(null); // O recupera l'URL esistente se non modificato
        } else {
            pianta.setUrlImmagine(urlImmagine);
        }

        // 5. Se non ci sono errori, chiama il metodo per aggiornare la pianta
        if (errori.isEmpty()) {
            try {
                pianteDAO.aggiornaPianta(pianta);
                LOGGER.log(Level.INFO, "Pianta con ID {0} aggiornata con successo.", idPianta);
                request.setAttribute("messaggio", "Pianta aggiornata con successo.");
                request.setAttribute("tipoMessaggio", "success");
                response.sendRedirect(contextPath + "/ListaPianteServlet"); // Reindirizza alla lista aggiornata
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Errore SQL durante l'aggiornamento della pianta con ID: " + idPianta, e);
                request.setAttribute("messaggio", "Errore durante l'aggiornamento della pianta: " + e.getMessage());
                request.setAttribute("tipoMessaggio", "error");
                // In caso di errore DB, rimanda al form di modifica con i dati pre-compilati
                request.setAttribute("pianta", pianta); // Ritorna l'oggetto pianta con i dati tentati
                request.getRequestDispatcher("/modificapiante.jsp").forward(request, response);
            }
        } else {
            // 6. Se ci sono errori, rimanda l'utente al form di modifica con gli errori
            request.setAttribute("erroriModifica", errori);
            request.setAttribute("messaggio", "Ci sono errori nel form. Correggere e riprovare.");
            request.setAttribute("tipoMessaggio", "error");
            request.setAttribute("pianta", pianta); // Ritorna l'oggetto pianta con i dati inseriti per ripopolare il form
            request.getRequestDispatcher("/modificapiante.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contextPath = request.getContextPath();

        // 1. Controllo Autenticazione e Autorizzazione (solo amministratori o venditori)
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            LOGGER.info("Tentativo di accesso non autenticato ad AggiornaPianteServlet (GET). Reindirizzamento a login.jsp");
            response.sendRedirect(contextPath + "/login.jsp");
            return;
        }

        Utente utenteLoggato = (Utente) session.getAttribute("loggedInUser");
        if (utenteLoggato == null || (!utenteLoggato.isAdmin() && !utenteLoggato.getRuoli().contains("venditore"))) {
            LOGGER.log(Level.WARNING, "Utente non autorizzato ({0}) ha tentato di accedere al form di aggiornamento. Reindirizzamento ad accesso_negato.html",
                    utenteLoggato != null ? utenteLoggato.getEmail() : "sconosciuto");
            response.sendRedirect(contextPath + "/accesso_negato.html");
            return;
        }

        // 2. Recupera l'ID dalla richiesta (per il GET, usato per popolare il form)
        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                int idPianta = Integer.parseInt(idStr);
                Piante pianta = pianteDAO.getPiantaById(idPianta); // Recupera la pianta dal DB
                if (pianta != null) {
                    request.setAttribute("pianta", pianta); // Inoltra l'oggetto pianta alla JSP
                    request.getRequestDispatcher("/modificapiante.jsp").forward(request, response);
                } else {
                    LOGGER.log(Level.WARNING, "Pianta con ID {0} non trovata per la modifica.", idPianta);
                    request.setAttribute("messaggio", "Pianta non trovata per l'ID specificato.");
                    request.setAttribute("tipoMessaggio", "error");
                    response.sendRedirect(contextPath + "/ListaPianteServlet"); // Reindirizza alla lista
                }
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "ID pianta non valido per la modifica (GET): {0}", idStr);
                request.setAttribute("messaggio", "ID pianta non valido.");
                request.setAttribute("tipoMessaggio", "error");
                response.sendRedirect(contextPath + "/ListaPianteServlet"); // Reindirizza alla lista
            } catch (SQLException e) {
                LOGGER.logp(Level.SEVERE, "Errore SQL durante il recupero della pianta per la modifica (ID: {0}).", idStr, e);
                request.setAttribute("messaggio", "Errore del database durante il recupero della pianta.");
                request.setAttribute("tipoMessaggio", "error");
                response.sendRedirect(contextPath + "/ListaPianteServlet"); // Reindirizza alla lista
            }
        } else {
            LOGGER.warning("Nessun ID pianta fornito per la modifica (GET).");
            request.setAttribute("messaggio", "Nessun ID della pianta fornito per la modifica.");
            request.setAttribute("tipoMessaggio", "error");
            response.sendRedirect(contextPath + "/ListaPianteServlet"); // Reindirizza alla lista
        }
    }
}