package src.com.la_teca_del_giardiniere;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import src.com.la_teca_del_giardiniere.classes.piante;
import src.com.la_teca_del_giardiniere.dao.PianteDAO;

@WebServlet("/pianteServlet")
public class pianteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private PianteDAO pianteDAO;

    public pianteServlet() {
        super();
        try {
            pianteDAO = new PianteDAO();
        } catch (SQLException e) {
            e.printStackTrace();
            // Considera di lanciare una ServletException o di reindirizzare a una pagina di errore grave all'avvio
            // In un'applicazione reale, un errore qui impedirebbe il funzionamento dell'intera servlet.
            // throw new ServletException("Errore durante l'inizializzazione del PianteDAO", e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            List<String> ruoli = null;
            Object ruoliObj = session.getAttribute("ruoli");
            if (ruoliObj instanceof List<?>) {
                List<?> tempRuoli = (List<?>) ruoliObj;
                boolean allStrings = true;
                for (Object item : tempRuoli) {
                    if (!(item instanceof String)) {
                        allStrings = false;
                        break;
                    }
                }
                if (allStrings) {
                    ruoli = (List<String>) tempRuoli;
                } else {
                    System.err.println("Errore: la lista dei ruoli nella sessione contiene elementi non-String.");
                    ruoli = null;
                }
            } else {
                System.err.println("Avviso: l'attributo 'ruoli' non è una lista o è null nella sessione.");
            }

            // Controllo autorizzazione
            if (ruoli != null && (ruoli.contains("amministratore") || ruoli.contains("venditore"))) {
                // L'utente è autorizzato, procedi con l'inserimento/aggiornamento della pianta

                String nomeComune = request.getParameter("nomeComune");
                String tipoStr = request.getParameter("tipo");
                String nomeScientificoBotanico = request.getParameter("NomeScientificoBotanico");
                String categoria = request.getParameter("Categoria");
                String descrizioneBreve = request.getParameter("DescrizioneBreve");
                String descrizioneDettagliata = request.getParameter("DescrizioneDettagliata");
                String esposizioneLuminosa = request.getParameter("EsposizioneLuminosa");
                String tipoDiTerreno = request.getParameter("TipoDiTerreno");
                String temperaturaIdealeStr = request.getParameter("TemperaturaIdeale");
                String frequenzaIrrigazione = request.getParameter("FrequenzaIrrigazione");
                String prezzoStr = request.getParameter("Prezzo");
                String disponibilitaStr = request.getParameter("Disponibilita");
                String dataInserimentoStr = request.getParameter("data_inserimento");
                String idStr = request.getParameter("id"); // QUESTO È IL PARAMETRO ID DAL FORM

                List<String> errori = new ArrayList<>();
                piante pianta = new piante();

                // 1. Recupero e validazione dell'ID (se presente, per l'aggiornamento)
                Integer idPianta = null;
                if (idStr != null && !idStr.trim().isEmpty()) {
                    try {
                        idPianta = Integer.parseInt(idStr);
                        pianta.setId(idPianta); // Imposta l'ID sull'oggetto pianta
                    } catch (NumberFormatException e) {
                        errori.add("L'ID della pianta non è in un formato valido.");
                    }
                }

                // 2. Popolamento dei dati della pianta e validazione
                if (nomeComune == null || nomeComune.trim().isEmpty()) {
                    errori.add("Il Nome Comune è obbligatorio.");
                } else {
                    pianta.setNomeComune(nomeComune);
                }

                if (prezzoStr == null || prezzoStr.trim().isEmpty()) {
                    errori.add("Il Prezzo è obbligatorio.");
                } else {
                    try {
                        float prezzo = Float.parseFloat(prezzoStr);
                        pianta.setPrezzo(prezzo);
                    } catch (NumberFormatException e) {
                        errori.add("Il formato del Prezzo non è valido.");
                    }
                }

                boolean tipo = false;
                if (tipoStr != null && tipoStr.equals("1")) {
                    tipo = true; // Esterno
                } else if (tipoStr != null && tipoStr.equals("0")) {
                    tipo = false; // Interno
                } else {
                    errori.add("Valore non valido per il Tipo di Pianta.");
                }
                pianta.setTipo(tipo);

                pianta.setNomeScientificoBotanico(nomeScientificoBotanico);
                pianta.setCategoria(categoria);
                pianta.setDescrizioneBreve(descrizioneBreve);
                pianta.setDescrizioneDettagliata(descrizioneDettagliata);
                pianta.setEsposizioneLuminosa(esposizioneLuminosa);
                pianta.setTipoDiTerreno(tipoDiTerreno);

                try {
                    if (temperaturaIdealeStr != null && !temperaturaIdealeStr.isEmpty()) {
                        pianta.setTemperaturaIdeale(Integer.parseInt(temperaturaIdealeStr));
                    } else {
                        pianta.setTemperaturaIdeale(null); // Permetti valori null se non obbligatorio
                    }
                } catch (NumberFormatException e) {
                    errori.add("Il formato della Temperatura Ideale non è valido.");
                }

                pianta.setFrequenzaIrrigazione(frequenzaIrrigazione);

                try {
                    if (disponibilitaStr != null && !disponibilitaStr.isEmpty()) {
                        pianta.setDisponibilita(Integer.parseInt(disponibilitaStr));
                    } else {
                        pianta.setDisponibilita(null); // Permetti valori null se non obbligatorio
                    }
                } catch (NumberFormatException e) {
                    errori.add("Il formato della Disponibilità non è valido.");
                }

                try {
                    if (dataInserimentoStr != null && !dataInserimentoStr.isEmpty()) {
                        pianta.setData_inserimento(Timestamp.valueOf(dataInserimentoStr));
                    } else {
                        // Se la data non è fornita, imposta la data/ora corrente
                        pianta.setData_inserimento(new Timestamp(System.currentTimeMillis()));
                    }
                } catch (IllegalArgumentException e) {
                    errori.add("Il formato della Data Inserimento non è valido (yyyy-mm-dd hh:mm:ss).");
                }


                // 3. Esecuzione dell'operazione (aggiungi o aggiorna)
                if (errori.isEmpty()) {
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
                        response.sendRedirect("listapianteServlet"); // Reindirizza alla servlet che mostra la lista
                    } catch (SQLException e) {
                        e.printStackTrace();
                        errori.add("Errore database: " + e.getMessage());
                        request.setAttribute("erroriInserimento", errori);
                        request.getRequestDispatcher("forminserimentopiante.jsp").forward(request, response);
                    }
                } else {
                    // Ci sono errori di validazione, rimanda l'utente al form con i messaggi di errore
                    // Mantieni i valori inseriti dall'utente per non fargli reinserire tutto
                    request.setAttribute("pianta", pianta); // Ri-popola il bean 'pianta' nel request scope
                    request.setAttribute("erroriInserimento", errori);
                    request.getRequestDispatcher("forminserimentopiante.jsp").forward(request, response);
                }
            } else {
                // Utente non autorizzato
                response.sendRedirect("accesso_negato.html");
            }
        } else {
            // Utente non loggato
            response.sendRedirect("login.jsp");
        }
    }

    // Aggiungi un metodo doGet per visualizzare il form di inserimento/modifica o per recuperare una pianta per la modifica
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            List<String> ruoli = null;
            Object ruoliObj = session.getAttribute("ruoli");
            if (ruoliObj instanceof List<?>) {
                List<?> tempRuoli = (List<?>) ruoliObj;
                boolean allStrings = true;
                for (Object item : tempRuoli) {
                    if (!(item instanceof String)) {
                        allStrings = false;
                        break;
                    }
                }
                if (allStrings) {
                    ruoli = (List<String>) tempRuoli;
                } else {
                    System.err.println("Errore: la lista dei ruoli nella sessione contiene elementi non-String.");
                    ruoli = null;
                }
            } else {
                System.err.println("Avviso: l'attributo 'ruoli' non è una lista o è null nella sessione.");
            }

            if (ruoli != null && (ruoli.contains("amministratore") || ruoli.contains("venditore"))) {
                String idStr = request.getParameter("id");
                if (idStr != null && !idStr.trim().isEmpty()) {
                    // Richiesta di modifica di una pianta esistente
                    try {
                        int id = Integer.parseInt(idStr);
                        piante piantaDaModificare = pianteDAO.getPiantaById(id);
                        if (piantaDaModificare != null) {
                            request.setAttribute("pianta", piantaDaModificare);
                            request.setAttribute("modalita", "modifica"); // Indica che siamo in modalità modifica
                            request.getRequestDispatcher("forminserimentopiante.jsp").forward(request, response);
                        } else {
                            // Pianta non trovata, reindirizza alla lista o a una pagina di errore
                            request.setAttribute("messaggio", "Pianta non trovata per la modifica.");
                            request.setAttribute("tipoMessaggio", "error");
                            response.sendRedirect("listapianteServlet"); // O error.jsp
                        }
                    } catch (NumberFormatException e) {
                        request.setAttribute("messaggio", "ID pianta non valido.");
                        request.setAttribute("tipoMessaggio", "error");
                        response.sendRedirect("listapianteServlet"); // O error.jsp
                    } catch (SQLException e) {
                        e.printStackTrace();
                        request.setAttribute("messaggio", "Errore database durante il recupero della pianta.");
                        request.setAttribute("tipoMessaggio", "error");
                        response.sendRedirect("listapianteServlet"); // O error.jsp
                    }
                } else {
                    // Nessun ID, è una richiesta di inserimento di una nuova pianta
                    request.setAttribute("modalita", "inserimento"); // Indica che siamo in modalità inserimento
                    request.getRequestDispatcher("forminserimentopiante.jsp").forward(request, response);
                }
            } else {
                response.sendRedirect("accesso_negato.html");
            }
        } else {
            response.sendRedirect("login.jsp");
        }
    }
}