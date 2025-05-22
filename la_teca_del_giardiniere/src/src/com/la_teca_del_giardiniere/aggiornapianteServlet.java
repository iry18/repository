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

import src.com.la_teca_del_giardiniere.classes.piante;
import src.com.la_teca_del_giardiniere.dao.PianteDAO;

@WebServlet("/aggiornapianteServlet")
public class aggiornapianteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private PianteDAO pianteDAO;

    public aggiornapianteServlet() {
        super();
        try {
            pianteDAO = new PianteDAO();
        } catch (SQLException e) {
            e.printStackTrace(); // Gestire l'errore
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Recupera l'ID dal campo nascosto
        String idStr = request.getParameter("id");
        Integer idPianta = null;
        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                idPianta = Integer.parseInt(idStr);
            } catch (NumberFormatException e) {
                request.setAttribute("messaggio", "ID pianta non valido.");
                request.setAttribute("tipoMessaggio", "error");
                request.getRequestDispatcher("listapianteServlet").forward(request, response);
                return;
            }
        } else {
            request.setAttribute("messaggio", "ID pianta non fornito.");
            request.setAttribute("tipoMessaggio", "error");
            request.getRequestDispatcher("listapianteServlet").forward(request, response);
            return;
        }

        // 2. Recupera gli altri parametri dal form
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

        List<String> errori = new ArrayList<>();
        piante pianta = new piante();
        pianta.setId(idPianta); // Imposta l'ID nell'oggetto pianta

        // 3. Esegui la validazione (come nel tuo codice)
        if (nomeComune == null || nomeComune.trim().isEmpty()) {
            errori.add("Il Nome Comune è obbligatorio.");
        }
        // ... (altre validazioni) ...
        if (prezzoStr == null || prezzoStr.trim().isEmpty()) {
            errori.add("Il Prezzo è obbligatorio.");
        } else {
            try {
                pianta.setPrezzo(Float.parseFloat(prezzoStr));
                if (pianta.getPrezzo() < 0) {
                    errori.add("Il Prezzo non può essere negativo.");
                }
            } catch (NumberFormatException e) {
                errori.add("Il formato del Prezzo non è valido.");
            }
        }
        if (temperaturaIdealeStr != null && !temperaturaIdealeStr.trim().isEmpty()) {
            try {
                pianta.setTemperaturaIdeale(Integer.parseInt(temperaturaIdealeStr));
            } catch (NumberFormatException e) {
                errori.add("Il formato della Temperatura Ideale non è valido.");
            }
        }
        if (disponibilitaStr != null && !disponibilitaStr.trim().isEmpty()) {
            try {
                pianta.setDisponibilita(Integer.parseInt(disponibilitaStr));
                if (pianta.getDisponibilita() < 0) {
                    errori.add("La Disponibilità non può essere negativa.");
                }
            } catch (NumberFormatException e) {
                errori.add("Il formato della Disponibilità non è valido.");
            }
        }
        if (dataInserimentoStr != null && !dataInserimentoStr.trim().isEmpty()) {
            try {
                pianta.setData_inserimento(Timestamp.valueOf(dataInserimentoStr));
            } catch (IllegalArgumentException e) {
                errori.add("Il formato della Data Inserimento non è valido (yyyy-mm-dd hh:mm:ss).");
            }
        } else {
            pianta.setData_inserimento(new Timestamp(System.currentTimeMillis()));
        }
        boolean tipo = false;
        if (tipoStr != null && tipoStr.equals("1")) {
            tipo = true;
        } else if (tipoStr != null && tipoStr.equals("0")) {
            tipo = false;
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
        pianta.setFrequenzaIrrigazione(frequenzaIrrigazione);

        // 4. Se non ci sono errori, chiama il metodo per aggiornare la pianta
        if (errori.isEmpty()) {
            try {
                pianteDAO.aggiornaPianta(pianta); // **Ecco la chiamata al DAO!**
                request.setAttribute("messaggio", "Pianta aggiornata con successo.");
                request.setAttribute("tipoMessaggio", "success");
                response.sendRedirect("listapianteServlet"); // Reindirizza alla lista
            } catch (SQLException e) {
                e.printStackTrace();
                request.setAttribute("messaggio", "Errore durante l'aggiornamento della pianta: " + e.getMessage());
                request.setAttribute("tipoMessaggio", "error");
                request.getRequestDispatcher("listapianteServlet").forward(request, response);
            }
        } else {
            // 5. Se ci sono errori, rimanda l'utente al form di modifica
            request.setAttribute("erroriModifica", errori);
            request.setAttribute("pianta", pianta); // Ritorna l'oggetto pianta con i dati inseriti
            request.getRequestDispatcher("modificapiante.jsp").forward(request, response);
        }
    }
}