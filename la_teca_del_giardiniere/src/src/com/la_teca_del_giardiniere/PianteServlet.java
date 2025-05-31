package src.com.la_teca_del_giardiniere;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.math.BigDecimal; // Import per BigDecimal
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import src.com.la_teca_del_giardiniere.classes.Piante; // CAMBIATO: da 'piante' a 'Pianta'
import src.com.la_teca_del_giardiniere.classes.Utente; // Import per il controllo isAdmin
import src.com.la_teca_del_giardiniere.dao.PianteDAO;

// Ho rinominato la servlet per seguire le convenzioni di denominazione (PascalCase)
@WebServlet("/PianteServlet")
public class PianteServlet extends HttpServlet { // CAMBIATO: da 'pianteServlet' a 'PianteServlet'

    private static final long serialVersionUID = 1L;
    private PianteDAO pianteDAO;

    public PianteServlet() {
        super();
        try {
            pianteDAO = new PianteDAO();
        } catch (SQLException e) {
            e.printStackTrace();
            // In un'applicazione reale, un errore qui dovrebbe impedire il funzionamento.
            // Considera di loggare l'errore e/o lanciare una ServletException in init().
            // throw new ServletException("Errore durante l'inizializzazione del PianteDAO", e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String contextPath = request.getContextPath(); // Per URL assoluti

        // 1. Controllo Autenticazione e Autorizzazione (Admin o Venditore)
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.sendRedirect(contextPath + "/login.jsp");
            return;
        }

        Utente utenteLoggato = (Utente) session.getAttribute("loggedInUser");
        if (utenteLoggato == null || (!utenteLoggato.isAdmin() && !utenteLoggato.getRuoli().contains("venditore"))) {
            // Se non è admin e non ha il ruolo "venditore"
            response.sendRedirect(contextPath + "/accesso_negato.html");
            return;
        }

        // 2. Recupero dati dal form
        List<String> errori = new ArrayList<>();
        Pianta pianta = new Pianta(); // CAMBIATO: da 'piante' a 'Pianta'

        // Recupera l'ID (se presente, per la modifica)
        String idStr = request.getParameter("id");
        Integer idPianta = null; // Usiamo Integer per gestire null in caso di nuovo inserimento
        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                idPianta = Integer.parseInt(idStr);
                pianta.setId(idPianta); // Imposta l'ID sull'oggetto pianta
            } catch (NumberFormatException e) {
                errori.add("L'ID della pianta non è in un formato valido.");
            }
        }

        // Recupero e validazione di tutti i campi
        String nomeComune = request.getParameter("nomeComune");
        if (nomeComune == null || nomeComune.trim().isEmpty()) {
            errori.add("Il Nome Comune è obbligatorio.");
        } else {
            pianta.setNomeComune(nomeComune);
        }

        String tipoStr = request.getParameter("tipo"); // Ora questo dovrebbe essere "interno" o "esterno"
        if (tipoStr == null || tipoStr.trim().isEmpty()) {
            errori.add("Il Tipo di Pianta (Interno/Esterno) è obbligatorio.");
        } else {
            // Ho cambiato a String nella classe Pianta per 'tipo'
            pianta.setTipo(tipoStr); // Assumendo che il metodo setTipo accetti una String
        }

        pianta.setNomeBotanico(request.getParameter("nomeScientificoBotanico")); // Corretto il nome del campo
        pianta.setCategoria(request.getParameter("categoria")); // Corretto il nome del campo

        // Descrizione (può essere null, ma trim() evita stringhe di soli spazi)
        pianta.setDescrizione(request.getParameter("descrizioneDettagliata")); // Ho unito descrizione breve e dettagliata in uno
                                                                             // Se vuoi separarle, devi aggiungere il campo in Pianta

        pianta.setEsposizioneLuminosa(request.getParameter("esposizioneLuminosa")); // Corretto il nome del campo
        pianta.setTerrenoIdeale(request.getParameter("tipoDiTerreno")); // Corretto il nome del campo

        // Temperatura Ideale (Integer per permettere null)
        String temperaturaIdealeStr = request.getParameter("temperaturaIdeale");
        if (temperaturaIdealeStr != null && !temperaturaIdealeStr.isEmpty()) {
            try {
                pianta.setTemperaturaIdeale(Integer.parseInt(temperaturaIdealeStr));
            } catch (NumberFormatException e) {
                errori.add("Il formato della Temperatura Ideale non è valido.");
            }
        } else {
            pianta.setTemperaturaIdeale(null); // Consente null nel DB e nel bean
        }

        pianta.setFrequenzaIrrigazione(request.getParameter("frequenzaIrrigazione")); // Corretto il nome del campo

        // Prezzo (BigDecimal)
        String prezzoStr = request.getParameter("prezzo");
        if (prezzoStr == null || prezzoStr.trim().isEmpty()) {
            errori.add("Il Prezzo è obbligatorio.");
        } else {
            try {
                // Sostituisci la virgola con il punto per la conversione
                prezzoStr = prezzoStr.replace(',', '.');
                pianta.setPrezzo(new BigDecimal(prezzoStr)); // CAMBIATO: da float a BigDecimal
            } catch (NumberFormatException e) {
                errori.add("Il formato del Prezzo non è valido (es. 12.99).");
            }
        }

        // Quantità Disponibile (Integer per permettere null)
        String quantitaDisponibileStr = request.getParameter("disponibilita"); // Corretto il nome del campo
        if (quantitaDisponibileStr != null && !quantitaDisponibileStr.isEmpty()) {
            try {
                pianta.setQuantitaDisponibile(Integer.parseInt(quantitaDisponibileStr));
            } catch (NumberFormatException e) {
                errori.add("Il formato della Disponibilità non è valido.");
            }
        } else {
            pianta.setQuantitaDisponibile(null); // Consente null nel DB e nel bean
        }

        // Data Inserimento (Timestamp)
        String dataInserimentoStr = request.getParameter("dataInserimento"); // Corretto il nome del campo
        if (dataInserimentoStr != null && !dataInserimentoStr.isEmpty()) {
            try {
                pianta.setDataInserimento(Timestamp.valueOf(dataInserimentoStr + " 00:00:00")); // Assumi data YYYY-MM-DD
            } catch (IllegalArgumentException e) {
                errori.add("Il formato della Data Inserimento non è valido (formato atteso: YYYY-MM-DD).");
            }
        } else if (idPianta == null) { // Solo se è un nuovo inserimento, imposta la data corrente
            pianta.setDataInserimento(new Timestamp(System.currentTimeMillis()));
        } else {
            // Se è una modifica e la data non viene fornita, recupera la data esistente dal DB
            // Questo richiede di recuperare l'oggetto pianta dal DB prima di aggiornarlo
            try {
                Pianta existingPianta = pianteDAO.getPiantaById(idPianta);
                if (existingPianta != null) {
                    pianta.setDataInserimento(existingPianta.getDataInserimento());
                }
            } catch (SQLException e) {
                errori.add("Errore nel recupero della data di inserimento esistente.");
                e.printStackTrace();
            }
        }
        
        pianta.setUrlImmagine(request.getParameter("immagine")); // Imposta il campo immagine

        // 3. Gestione degli errori di validazione
        if (!errori.isEmpty()) {
            request.setAttribute("errori", errori);
            request.setAttribute("pianta", pianta); // Ri-popola il form con i dati inseriti
            String forwardPath = "/forminserimentopiante.jsp"; // Se è in una cartella specifica: "/admin/forminserimentopiante.jsp"
            request.getRequestDispatcher(forwardPath).forward(request, response);
            return;
        }

        // 4. Esecuzione dell'operazione (aggiungi o aggiorna)
        try {
            if (idPianta != null) {
                // Se l'ID è presente, è un'operazione di AGGIORNAMENTO
                pianteDAO.aggiornaPianta(pianta);
                request.setAttribute("messaggio", "Pianta aggiornata con successo!");
                request.setAttribute("tipoMessaggio", "success");
            } else {
                // Se l'ID non è presente, è un'operazione di INSERIMENTO
                pianteDAO.aggiungiPianta(pianta);
                request.setAttribute("messaggio", "Pianta aggiunta con successo!");
                request.setAttribute("tipoMessaggio", "success");
            }
            // Dopo l'operazione, reindirizza alla lista delle piante
            response.sendRedirect(contextPath + "/listapianteServlet");
        } catch (SQLException e) {
            e.printStackTrace();
            errori.add("Errore database: " + e.getMessage());
            request.setAttribute("errori", errori);
            request.setAttribute("pianta", pianta);
            String forwardPath = "/forminserimentopiante.jsp"; // Se è in una cartella specifica: "/admin/forminserimentopiante.jsp"
            request.getRequestDispatcher(forwardPath).forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String contextPath = request.getContextPath(); // Per URL assoluti

        // 1. Controllo Autenticazione e Autorizzazione (Admin o Venditore)
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.sendRedirect(contextPath + "/login.jsp");
            return;
        }

        Utente utenteLoggato = (Utente) session.getAttribute("loggedInUser");
        if (utenteLoggato == null || (!utenteLoggato.isAdmin() && !utenteLoggato.getRuoli().contains("venditore"))) {
            // Se non è admin e non ha il ruolo "venditore"
            response.sendRedirect(contextPath + "/accesso_negato.html");
            return;
        }

        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.trim().isEmpty()) {
            // Richiesta di modifica di una pianta esistente: recupera la pianta e mostra il form pre-popolato
            try {
                int id = Integer.parseInt(idStr);
                Pianta piantaDaModificare = pianteDAO.getPiantaById(id);
                if (piantaDaModificare != null) {
                    request.setAttribute("pianta", piantaDaModificare);
                    request.setAttribute("modalita", "modifica"); // Indica che siamo in modalità modifica
                    String forwardPath = "/forminserimentopiante.jsp"; // Se è in una cartella specifica: "/admin/forminserimentopiante.jsp"
                    request.getRequestDispatcher(forwardPath).forward(request, response);
                } else {
                    request.setAttribute("messaggio", "Pianta non trovata per la modifica.");
                    request.setAttribute("tipoMessaggio", "error");
                    response.sendRedirect(contextPath + "/listapianteServlet");
                }
            } catch (NumberFormatException e) {
                request.setAttribute("messaggio", "ID pianta non valido.");
                request.setAttribute("tipoMessaggio", "error");
                response.sendRedirect(contextPath + "/listapianteServlet");
            } catch (SQLException e) {
                e.printStackTrace();
                request.setAttribute("messaggio", "Errore database durante il recupero della pianta.");
                request.setAttribute("tipoMessaggio", "error");
                response.sendRedirect(contextPath + "/listapianteServlet");
            }
        } else {
            // Nessun ID, è una richiesta per mostrare il form di inserimento di una nuova pianta
            request.setAttribute("modalita", "inserimento"); // Indica che siamo in modalità inserimento
            String forwardPath = "/forminserimentopiante.jsp"; // Se è in una cartella specifica: "/admin/forminserimentopiante.jsp"
            request.getRequestDispatcher(forwardPath).forward(request, response);
        }
    }
}