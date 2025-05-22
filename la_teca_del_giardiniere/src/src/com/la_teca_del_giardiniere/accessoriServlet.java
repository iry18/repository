package src.com.la_teca_del_giardiniere;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import src.com.la_teca_del_giardiniere.classes.accessori;
import src.com.la_teca_del_giardiniere.dao.*;

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

        java.util.ArrayList<String> errori = new java.util.ArrayList<>();
        accessori accessorio = new accessori();

        // 2. Validazione lato server
        if (nome == null || nome.trim().isEmpty()) {
            errori.add("Il Nome è obbligatorio.");
        } else if (nome.trim().length() > 255) {
            errori.add("Il Nome non può superare i 255 caratteri.");
        }

        if (prezzoStr == null || prezzoStr.trim().isEmpty()) {
            errori.add("Il Prezzo è obbligatorio.");
        } else {
            try {
                accessorio.setPrezzo(new java.math.BigDecimal(prezzoStr));
                if (accessorio.isPrezzo().compareTo(java.math.BigDecimal.ZERO) < 0) {
                    errori.add("Il Prezzo non può essere negativo.");
                }
            } catch (NumberFormatException e) {
                errori.add("Il formato del Prezzo non è valido.");
            }
        }

        if (disponibilitaStr != null && !disponibilitaStr.trim().isEmpty()) {
            try {
                int disponibilita = Integer.parseInt(disponibilitaStr);
                if (disponibilita < 0) {
                    errori.add("La Disponibilità non può essere negativa.");
                }
                accessorio.setDisponibilita(disponibilita);
            } catch (NumberFormatException e) {
                errori.add("Il formato della Disponibilità non è valido.");
            }
        }

        if (descrizione != null && descrizione.trim().length() > 1000) {
            errori.add("La Descrizione non può superare i 1000 caratteri.");
        }

        if (dimensioni != null && dimensioni.trim().length() > 255) {
            errori.add("Le Dimensioni non possono superare i 255 caratteri.");
        }

        if (dataInserimentoStr != null && !dataInserimentoStr.trim().isEmpty()) {
            try {
                accessorio.setData_inserimento(Timestamp.valueOf(dataInserimentoStr));
            } catch (IllegalArgumentException e) {
                errori.add("Il formato della Data Inserimento non è valido (yyyy-mm-dd hh:mm:ss).");
            }
        } else {
            accessorio.setData_inserimento(new Timestamp(System.currentTimeMillis())); // Imposta la data corrente se non fornita
        }

        // Gestione dell'ID
        if (!isIdAutogenerato() && (idStr == null || idStr.trim().isEmpty())) {
            errori.add("L'ID è obbligatorio."); // Solo se non è autogenerato
        } else if (!isIdAutogenerato() && idStr != null && !idStr.trim().isEmpty()) {
            try {
                accessorio.setId(Integer.parseInt(idStr));
            } catch (NumberFormatException e) {
                errori.add("L'ID deve essere un numero intero valido.");
            }
        }

        // 3. Se ci sono errori, rimanda l'utente al form
        if (!errori.isEmpty()) {
            request.setAttribute("erroriInserimento", errori);
            request.getRequestDispatcher("forminserimentoaccessori.jsp").forward(request, response);
            return; // Importante per non proseguire con l'inserimento
        }

        // 4. Chiama il metodo del DAO per salvare l'accessorio nel database
        try {
            if (accessorioDAO != null) {
                accessorioDAO.aggiungiAccessori(accessorio);
                response.sendRedirect("inserimento_successo.html");
            } else {
                response.sendRedirect("inserimento_errore.html?errore=Errore nella creazione del DAO");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("inserimento_errore.html?errore=" + e.getMessage());
        }
    }

    // Metodo di esempio per determinare se l'ID è autogenerato
    private boolean isIdAutogenerato() {
        // Implementa la tua logica qui. Potrebbe essere una configurazione,
        // un controllo sul nome della colonna nel database, ecc.
        return true; // Esempio: supponiamo che l'ID sia autogenerato
    }
}