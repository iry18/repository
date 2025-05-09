//ricevere i dati dal form HTML, creare un oggetto piante e utilizzare  PianteDAO per salvarlo nel database, gestendo anche le possibili eccezioni
//Controller: (Responsabile di gestire le richieste dell'utente e di interagire con il Model per aggiornare la View)
package com.la_teca_del_giardiniere;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.la_teca_del_giardiniere.classes.piante; // Import della classe Piante
import com.la_teca_del_giardiniere.dao.PianteDAO;

@WebServlet("/pianteServlet")
public class pianteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private PianteDAO pianteDAO; // Dichiarazione

    public pianteServlet() {
        super();
        try {
            pianteDAO = new PianteDAO(); // Inizializzazione con gestione dell'eccezione
        } catch (SQLException e) {
            // Gestisci l'errore se la creazione del DAO fallisce
            e.printStackTrace(); // Stampa l'errore per il debugging
            // Potresti anche reindirizzare a una pagina di errore o mostrare un messaggio
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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

        piante pianta = new piante(); // Usa 'piante' (minuscolo) se è il nome della tua classe
        pianta.setNomeComune(nomeComune);

        try {
            boolean tipo = Boolean.parseBoolean(tipoStr);
            pianta.setTipo(tipo);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        pianta.setNomeScientificoBotanico(nomeScientificoBotanico);
        pianta.setCategoria(categoria);
        pianta.setDescrizioneBreve(descrizioneBreve);
        pianta.setDescrizioneDettagliata(descrizioneDettagliata);
        pianta.setEsposizioneLuminosa(esposizioneLuminosa);
        pianta.setTipoDiTerreno(tipoDiTerreno);

        try {
            int temperaturaIdeale = Integer.parseInt(temperaturaIdealeStr);
            pianta.setTemperaturaIdeale(temperaturaIdeale);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        pianta.setFrequenzaIrrigazione(frequenzaIrrigazione);

        try {
            float prezzo = Float.parseFloat(prezzoStr);
            pianta.setPrezzo(prezzo);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        try {
            int disponibilita = Integer.parseInt(disponibilitaStr);
            pianta.setDisponibilita(disponibilita);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        try {
            Timestamp dataInserimento = Timestamp.valueOf(dataInserimentoStr);
            pianta.setData_inserimento(dataInserimento);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        try {
            pianteDAO.aggiungiPianta(pianta);
            response.sendRedirect("inserimento_successo.html");

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("inserimento_errore.html?errore=" + e.getMessage());
        }
    }
}