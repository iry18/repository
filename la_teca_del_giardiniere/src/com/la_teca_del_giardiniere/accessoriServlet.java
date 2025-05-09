package com.la_teca_del_giardiniere;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.la_teca_del_giardiniere.classes.accessori;
import com.la_teca_del_giardiniere.dao.accessoriDAO;

@WebServlet("/accessoriServlet")
public class accessoriServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private accessoriDAO accessorioDAO;
	
    public accessoriServlet() {
        super();
        try {
            accessorioDAO = new accessoriDAO();
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestisci l'errore di creazione del DAO (es. log, pagina di errore)
        }
    }
        
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Recupera i parametri dal form
        String idStr = request.getParameter("id");
        String nome = request.getParameter("nome");
        String prezzoStr = request.getParameter("prezzo");
        String disponibilitaStr = request.getParameter("disponibilita");
        String descrizione = request.getParameter("descrizione");
        String dimensioni = request.getParameter("dimensioni");
        String dataInserimentoStr = request.getParameter("data_inserimento");

        // 2. Crea un oggetto 'accessori' e popola i suoi attributi
        accessori accessorio = new accessori();
        try {
            accessorio.setId(Integer.parseInt(idStr));
        } catch (NumberFormatException e) {
            e.printStackTrace(); // Gestisci l'errore se l'ID non è un numero valido
        }
        accessorio.setNome(nome);
        try {
            accessorio.setPrezzo(new java.math.BigDecimal(prezzoStr)); // Usa BigDecimal per il prezzo
        } catch (NumberFormatException e) {
            e.printStackTrace(); // Gestisci l'errore se il prezzo non è un numero valido
        }
        try {
            accessorio.setDisponibilita(Integer.parseInt(disponibilitaStr));
        } catch (NumberFormatException e) {
            e.printStackTrace(); // Gestisci l'errore se la disponibilità non è un numero valido
        }
        accessorio.setDescrizione(descrizione);
        accessorio.setDimensioni(dimensioni);
        try {
            accessorio.setData_inserimento(Timestamp.valueOf(dataInserimentoStr));
        } catch (IllegalArgumentException e) {
            e.printStackTrace(); // Gestisci l'errore se il formato della data non è valido
        }

        // 3. Chiama il metodo del DAO per salvare l'accessorio nel database
        try {
            if (accessorioDAO != null) {
                accessorioDAO.aggiungiAccessori(accessorio);
                response.sendRedirect("inserimento_successo.html");
            } else {
                // Gestisci il caso in cui accessorioDAO è null (errore nella creazione)
                response.sendRedirect("inserimento_errore.html?errore=Errore nella creazione del DAO");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("inserimento_errore.html?errore=" + e.getMessage());
        }
    }
}
