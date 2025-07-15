package la_teca_del_giardiniere;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime; // Import per LocalDateTime
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level; // Per il logging
import java.util.logging.Logger; // Per il logging

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import la_teca_del_giardiniere.DAO.PianteDAO;
import la_teca_del_giardiniere.classes.Piante;
import la_teca_del_giardiniere.classes.Utente;

@WebServlet("/PianteServlet") // Rimane così se vuoi una servlet unica per CUD
public class PianteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(PianteServlet.class.getName()); // Logger

    private PianteDAO pianteDAO;

    public PianteServlet() {
        super();
        try {
            pianteDAO = new PianteDAO();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante l'inizializzazione del PianteDAO in PianteServlet.", e);
            // In un'applicazione reale, un errore qui dovrebbe impedire il funzionamento.
            // throw new ServletException("Errore durante l'inizializzazione del PianteDAO", e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String contextPath = request.getContextPath();

        // 1. Controllo Autenticazione e Autorizzazione (Admin o Venditore)
        if (session == null || session.getAttribute("loggedInUser") == null) {
            LOGGER.info("Tentativo di accesso non autenticato a PianteServlet (POST). Reindirizzamento a Login.jsp");
            response.sendRedirect(contextPath + "/Login.jsp");
            return;
        }

        Utente utenteLoggato = (Utente) session.getAttribute("loggedInUser");
        if (utenteLoggato == null || (!utenteLoggato.isAdmin() && !utenteLoggato.getRuoli().contains("venditore"))) {
            LOGGER.log(Level.WARNING, "Utente non autorizzato ({0}) ha tentato di modificare/inserire una pianta. Reindirizzamento ad accesso_negato.html",
                    utenteLoggato != null ? utenteLoggato.getEmail() : "sconosciuto");
            response.sendRedirect(contextPath + "/accesso_negato.html");
            return;
        }

        // 2. Recupero dati dal form e inizializzazione oggetto Piante
        List<String> errori = new ArrayList<>();
        Piante pianta = new Piante(); // Crea un nuovo oggetto Piante

        // Determina se è un aggiornamento o un inserimento
        String idStr = request.getParameter("id");
        Integer idPianta = null;
        String modalita = "inserisci"; // Default: inserimento

        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                idPianta = Integer.parseInt(idStr);
                pianta.setId(idPianta); // Imposta l'ID sull'oggetto pianta
                modalita = "modifica"; // Se l'ID è presente, è modalità modifica
            } catch (NumberFormatException e) {
                errori.add("L'ID della pianta non è in un formato valido.");
                LOGGER.log(Level.WARNING, "ID pianta non valido (POST): {0}", idStr);
            }
        }
        request.setAttribute("modalita", modalita); // Imposta la modalità per la JSP in caso di errori

        // Recupero e validazione di tutti i campi
        String nomeComune = request.getParameter("nomeComune");
        if (nomeComune == null || nomeComune.trim().isEmpty()) {
            errori.add("Il Nome Comune è obbligatorio.");
        } else {
            pianta.setNomeComune(nomeComune.trim());
        }

        String tipoStr = request.getParameter("tipo"); // Ora questo sarà "interno" o "esterno" dalla JSP
        if (tipoStr == null || tipoStr.trim().isEmpty()) {
            errori.add("Il Tipo di Pianta (Interno/Esterno) è obbligatorio.");
        } else {
            pianta.setTipo(tipoStr.trim());
        }

        String nomeBotanico = request.getParameter("nomeBotanico"); // Allineato al JSP
        if (nomeBotanico == null || nomeBotanico.trim().isEmpty()) {
            errori.add("Il Nome Scientifico/Botanico è obbligatorio.");
        } else {
            pianta.setNomeBotanico(nomeBotanico.trim());
        }

        String categoria = request.getParameter("categoria"); // Allineato al JSP
        if (categoria == null || categoria.trim().isEmpty()) {
            errori.add("La Categoria è obbligatoria.");
        } else {
            pianta.setCategoria(categoria.trim());
        }

        // Descrizione: Gestiamo un unico campo 'descrizione' come suggerito, unendo breve e dettagliata nel JSP
        String descrizione = request.getParameter("descrizione"); // Allineato al JSP (campo unificato)
        if (descrizione == null || descrizione.trim().isEmpty()) {
            errori.add("La Descrizione è obbligatoria.");
        } else {
            pianta.setDescrizione(descrizione.trim());
        }

        String esposizioneLuminosa = request.getParameter("esposizioneLuminosa"); // Allineato al JSP
        if (esposizioneLuminosa == null || esposizioneLuminosa.trim().isEmpty()) {
            errori.add("L'Esposizione Luminosa è obbligatoria.");
        } else {
            pianta.setEsposizioneLuminosa(esposizioneLuminosa.trim());
        }

        String tipoDiTerreno = request.getParameter("tipoDiTerreno"); // Allineato al JSP
        if (tipoDiTerreno == null || tipoDiTerreno.trim().isEmpty()) {
            errori.add("Il Tipo di Terreno è obbligatorio.");
        } else {
            pianta.setTipoDiTerreno(tipoDiTerreno.trim());
        }

        String temperaturaIdealeStr = request.getParameter("temperaturaIdeale"); // Allineato al JSP
        if (temperaturaIdealeStr != null && !temperaturaIdealeStr.trim().isEmpty()) {
            try {
                pianta.setTemperaturaIdeale(Integer.parseInt(temperaturaIdealeStr.trim()));
            } catch (NumberFormatException e) {
                errori.add("Il formato della Temperatura Ideale non è valido. Inserire un numero intero.");
            }
        } else {
            pianta.setTemperaturaIdeale(null); // Consente null nel DB e nel bean
        }

        String frequenzaIrrigazione = request.getParameter("frequenzaIrrigazione"); // Allineato al JSP
        if (frequenzaIrrigazione == null || frequenzaIrrigazione.trim().isEmpty()) {
            errori.add("La Frequenza di Irrigazione è obbligatoria.");
        } else {
            pianta.setFrequenzaIrrigazione(frequenzaIrrigazione.trim());
        }

        String prezzoStr = request.getParameter("prezzo"); // Allineato al JSP
        if (prezzoStr == null || prezzoStr.trim().isEmpty()) {
            errori.add("Il Prezzo è obbligatorio.");
        } else {
            try {
                prezzoStr = prezzoStr.replace(',', '.'); // Sostituisci la virgola con il punto per la conversione
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

        String quantitaDisponibileStr = request.getParameter("quantitaDisponibile"); // Allineato al JSP
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
            pianta.setQuantitaDisponibile(null); // Consente null nel DB e nel bean
        }

        // Data Inserimento (Gestione differenziata per inserimento/aggiornamento)
        if (modalita.equals("inserisci")) {
            // Per nuovo inserimento: imposta la data corrente
            pianta.setDataInserimento(Timestamp.valueOf(LocalDateTime.now()));
        } else { // modalita.equals("modifica")
            // Per modifica: recupera la data originale dal DB o da un campo hidden (meglio DB)
            // L'approccio migliore è recuperare la pianta esistente per preservare la data di inserimento originale
            // e poi aggiornare gli altri campi.
            try {
                Piante existingPianta = pianteDAO.getPiantaById(idPianta);
                if (existingPianta != null && existingPianta.getDataInserimento() != null) {
                    pianta.setDataInserimento(existingPianta.getDataInserimento());
                } else {
                    // Fallback se la pianta esistente non ha data o non è stata trovata (strano)
                    pianta.setDataInserimento(Timestamp.valueOf(LocalDateTime.now()));
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Errore nel recupero della data di inserimento originale per pianta ID: " + idPianta, e);
                errori.add("Errore nel recupero della data di inserimento esistente.");
                // Anche in caso di errore, si tenta di procedere o si reindirizza a una pagina di errore.
                pianta.setDataInserimento(Timestamp.valueOf(LocalDateTime.now())); // Fallback
            }
        }

        String urlImmagine = request.getParameter("urlImmagine"); // Allineato al JSP
        if (urlImmagine == null || urlImmagine.trim().isEmpty()) {
            pianta.setUrlImmagine(null); // O imposta un'immagine di default
        } else {
            pianta.setUrlImmagine(urlImmagine.trim());
        }

        // 3. Gestione degli errori di validazione
        if (!errori.isEmpty()) {
            request.setAttribute("erroriModifica", errori); // Usa erroriModifica come nel JSP
            request.setAttribute("pianta", pianta); // Ri-popola il form con i dati inseriti (e ID se modifica)
            // Inoltra alla JSP
            request.getRequestDispatcher("/FormInserimentoPiante.jsp").forward(request, response);
            return;
        }

        // 4. Esecuzione dell'operazione (aggiungi o aggiorna)
        try {
            if (modalita.equals("modifica")) {
                pianteDAO.aggiornaPianta(pianta);
                request.getSession().setAttribute("messaggio", "Pianta aggiornata con successo!");
                request.getSession().setAttribute("tipoMessaggio", "success");
            } else { // modalita.equals("inserisci")
                pianteDAO.aggiungiPianta(pianta);
                request.getSession().setAttribute("messaggio", "Pianta aggiunta con successo!");
                request.getSession().setAttribute("tipoMessaggio", "success");
            }
            // Dopo l'operazione, reindirizza alla lista delle piante (PRG pattern)
            response.sendRedirect(contextPath + "/ListaPianteServlet");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'operazione su pianta (ID: " + idPianta + ", Modalità: " + modalita + ")", e);
            errori.add("Errore del database: " + e.getMessage());
            request.setAttribute("erroriModifica", errori);
            request.setAttribute("pianta", pianta); // Mantiene i dati nel form in caso di errore DB
            request.setAttribute("modalita", modalita); // Mantiene la modalità corretta
            request.getRequestDispatcher("/FormInserimentoPiante.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String contextPath = request.getContextPath();

        // 1. Controllo Autenticazione e Autorizzazione (Admin o Venditore)
        if (session == null || session.getAttribute("loggedInUser") == null) {
            LOGGER.info("Tentativo di accesso non autenticato a PianteServlet (GET). Reindirizzamento a Login.jsp");
            response.sendRedirect(contextPath + "/Login.jsp");
            return;
        }

        Utente utenteLoggato = (Utente) session.getAttribute("loggedInUser");
        if (utenteLoggato == null || (!utenteLoggato.isAdmin() && !utenteLoggato.getRuoli().contains("venditore"))) {
            LOGGER.log(Level.WARNING, "Utente non autorizzato ({0}) ha tentato di accedere al form. Reindirizzamento ad accesso_negato.html",
                    utenteLoggato != null ? utenteLoggato.getEmail() : "sconosciuto");
            response.sendRedirect(contextPath + "/accesso_negato.html");
            return;
        }

        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.trim().isEmpty()) {
            // Richiesta di modifica di una pianta esistente: recupera la pianta e mostra il form pre-popolato
            try {
                int id = Integer.parseInt(idStr.trim());
                Piante piantaDaModificare = pianteDAO.getPiantaById(id);
                if (piantaDaModificare != null) {
                    request.setAttribute("pianta", piantaDaModificare);
                    request.setAttribute("modalita", "modifica"); // Indica che siamo in modalità modifica
                    LOGGER.log(Level.INFO, "Preparazione form modifica per pianta ID: {0}", id);
                } else {
                    LOGGER.log(Level.WARNING, "Pianta con ID {0} non trovata per la modifica (GET). Reindirizzamento.", id);
                    request.setAttribute("messaggio", "Pianta non trovata per la modifica.");
                    request.setAttribute("tipoMessaggio", "error");
                    response.sendRedirect(contextPath + "/ListaPianteServlet");
                    return; // Importante per fermare l'esecuzione dopo il redirect
                }
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "ID pianta non valido per la modifica (GET): {0}", idStr);
                request.setAttribute("messaggio", "ID pianta non valido.");
                request.setAttribute("tipoMessaggio", "error");
                response.sendRedirect(contextPath + "/ListaPianteServlet");
                return;
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Errore SQL durante il recupero della pianta per la modifica (ID: " + idStr + ").", e);
                request.setAttribute("messaggio", "Errore database durante il recupero della pianta.");
                request.setAttribute("tipoMessaggio", "error");
                response.sendRedirect(contextPath + "/ListaPianteServlet");
                return;
            }
        } else {
            // Nessun ID, è una richiesta per mostrare il form di inserimento di una nuova pianta
            request.setAttribute("modalita", "inserisci"); // Indica che siamo in modalità inserimento
            LOGGER.info("Preparazione form per nuovo inserimento pianta.");
        }
        // Inoltra sempre a FormInserimentoPiante.jsp, che gestirà la visualizzazione in base alla "modalita"
        request.getRequestDispatcher("/FormInserimentoPiante.jsp").forward(request, response);
    }
}