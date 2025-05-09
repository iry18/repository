package com.la_teca_del_giardiniere;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.la_teca_del_giardiniere.classes.registrazione;
import com.la_teca_del_giardiniere.dao.UtenteDAO;

@WebServlet("/RegistrazioneServlet")
public class RegistrazioneServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UtenteDAO utenteDao;

    public RegistrazioneServlet() {
        super();
        try {
            utenteDao = new UtenteDAO();
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestisci l'errore di creazione del DAO (es. log, pagina di errore)
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String isAdminStr = request.getParameter("isAdmin");
        String indirizzo = request.getParameter("indirizzo");
        String citta = request.getParameter("citta");
        String CAPStr = request.getParameter("cap");
        String telefonoStr = request.getParameter("telefono");
        String dataRegistrazioneStr = request.getParameter("data_registrazione");

        registrazione utente = new registrazione();
        utente.setNome(nome);
        utente.setCognome(cognome);
        utente.setEmail(email);
        utente.setPassword(password); // **IMPORTANTE: DA HASH PRIMA DI SALVARE NEL DB!**

        // Conversione per isAdmin
        boolean isAdmin = false;
        if (isAdminStr != null && isAdminStr.equalsIgnoreCase("true")) {
            isAdmin = true;
        }
        utente.setAdmin(isAdmin);

        utente.setIndirizzo(indirizzo);
        utente.setCitta(citta);

        // Conversione per CAP
        try {
            if (CAPStr != null && !CAPStr.isEmpty()) {
                utente.setCAP(Integer.parseInt(CAPStr));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            // aggiungere un messaggio di errore all'utente
        }

        // Conversione per telefono
        try {
            if (telefonoStr != null && !telefonoStr.isEmpty()) {
                utente.setTelefono(Integer.parseInt(telefonoStr));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            // Potresti voler aggiungere un messaggio di errore all'utente
        }

        // Conversione per data_registrazione
        try {
            if (dataRegistrazioneStr != null && !dataRegistrazioneStr.isEmpty()) {
                utente.setData_registrazione(Timestamp.valueOf(dataRegistrazioneStr));
            } else {
                utente.setData_registrazione(new Timestamp(System.currentTimeMillis())); // Imposta la data/ora corrente se non fornita
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            // Potresti voler aggiungere un messaggio di errore all'utente
        }

        try {
            if (utenteDao != null) {
                utenteDao.aggiungiUtente(utente);
                response.sendRedirect("inserimento_successo.html");
            } else {
                response.sendRedirect("inserimento_errore.html?errore=Errore nella creazione del DAO");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("inserimento_errore.html?errore=" + e.getMessage());
        }
    }
}