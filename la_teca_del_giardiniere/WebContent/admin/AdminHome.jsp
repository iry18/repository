<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="la_teca_del_giardiniere.classes.Utente" %>

<% HttpSession currentSession = request.getSession(false);
   Utente loggedInUser = null;
   
    if (currentSession != null) {
        Object userObj = currentSession.getAttribute("currentUser"); // Get as Object first

        if (userObj instanceof Utente) {
            loggedInUser = (Utente) userObj; // Cast safely
        } else {
    }
        if (loggedInUser == null || !loggedInUser.isAdmin()) {
            String contextPath = request.getContextPath();
            if (currentSession != null) { 
                currentSession.setAttribute("messaggioErroreLogin", "Accesso negato: non hai i permessi di amministratore.");
                currentSession.setAttribute("tipoMessaggio", "error");
            }
            response.sendRedirect(contextPath + "/Login.jsp");
            return; 
        }
    
    %>
    
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Area Amministratore - La Teca del Giardiniere</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;700&display=swap" rel="stylesheet">
   
    <style>
        body {
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
            background-color: #f0f4ef;
            color: #333;
            margin: 0;
            padding: 20px;
        }

        h1 {
            text-align: center;
            color: #3b6b1d; /* Verde scuro */
            margin-bottom: 5px;
        }

        p {
            text-align: center;
            margin-top: 0;
            color: #666;
        }

        .container {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
            max-width: 1200px;
            margin: 0 auto;
        }

        .card {
            background-color: #f7fff7;
            border: 1px solid #d4e3d4;
            border-radius: 8px;
            padding: 20px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            text-align: center;
        }
        
        .card h2 {
            color: #558b2f;
            margin-top: 0;
            font-size: 1.5em;
        }

        .card p {
            font-size: 0.9em;
            color: #777;
        }

        .card a.button {
            display: block;
            width: 80%;
            margin: 15px auto 0;
            padding: 10px 20px;
            background-color: #558b2f;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            transition: background-color 0.3s;
        }

        .card a.button:hover {
            background-color: #3b6b1d;
        }
        
        .card .button-group {
            margin-top: 20px;
        }
        
        .full-width {
            grid-column: 1 / -1;
        }

        @media (max-width: 768px) {
            .container {
                grid-template-columns: 1fr;
            }
        }
        /* Stili per la barra di navigazione e il footer */
        header {
            background-color: #558b2f;
            color: white;
            padding: 10px 20px;
            text-align: right;
        }
        header nav ul {
            list-style: none;
            margin: 0;
            padding: 0;
        }
        header nav li {
            display: inline-block;
            margin-left: 20px;
        }
        header nav a {
            color: white;
            text-decoration: none;
            font-weight: bold;
        }
        footer {
            text-align: center;
            margin-top: 40px;
            padding: 20px;
            color: #666;
            border-top: 1px solid #ddd;
        }
        /* Stili per i messaggi */
        .message {
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 5px;
            text-align: center;
            font-weight: bold;
        }
        .message.success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        .message.error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
    </style>

</head>
<body>
    <header>
        <nav>
            <ul>
                <li><a href="homepage.jsp">HOME SITO</a></li>
                <li><a href="<%= request.getContextPath() %>/LoginServlet?action=logout">LOGOUT</a></li>
            </ul>
        </nav>
    </header>

    <section class="admin-dashboard">
        <h1>Benvenuto nell'Area Amministratore</h1>
        <p>Qui puoi gestire tutti gli aspetti del tuo negozio "La Teca del Giardiniere".</p>

        <%
           String message = (String) request.getAttribute("message");
            if (message != null && !message.isEmpty()) {
                String messageType = (String) request.getAttribute("messageType"); // "success" o "error"
        %>
            <div class="message <%= (messageType != null ? messageType : "") %>">
                <%= message %>
            </div>
        <%
            }
    }
        %>

        <div class="admin-menu-grid">
            <div class="admin-menu-item">
                <h3>Gestione Piante</h3>
                <p>Aggiungi, modifica o elimina piante dal catalogo.</p>
       		 <a href="ListaPiante.jsp">Lista Piante</a>
       		 <a href="FormInserimentoPiante.jsp">Aggiungi Pianta</a>  
         </div>

            <div class="admin-menu-item">
                <h3>Gestione Accessori</h3>
                <p>Controlla e aggiorna gli accessori disponibili.</p>
                <a href="ListaAccessori.jsp">Lista Accessori</a>
                <a href="FormInserimentoAccessori.jsp">Aggiungi Accessorio</a>
            </div>

            <div class="admin-menu-item">
                <h3>Gestione Utenti</h3>
                <p>Visualizza e gestisci gli account degli utenti.</p>
	            <a href="ListaUtentiServlet">Gestione Utenti</a>
	            </div>
            </div>

            <div class="admin-menu-item">
                <h3>Gestione Ordini</h3>
                <p>Tieni traccia e elabora gli ordini dei clienti.</p>
                <a href="gestione-ordini.jsp"> Gestione Ordini</a>
	        </div>
	       
	    </section>

    <footer>
        <p>&copy; 2025 La Teca del Giardiniere - Area Amministratore</p>
    </footer>

</body>
</html>