<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="la_teca_del_giardiniere.classes.Utente" %>

<%
    // Ottieni la sessione, senza crearne una nuova se non esiste
    HttpSession currentSession = request.getSession(false);
    Utente loggedInUser = null;

    // Se la sessione non esiste, l'utente non è loggato.
    // Reindirizziamo immediatamente alla pagina di login.
    if (currentSession == null) {
        String contextPath = request.getContextPath();
        response.sendRedirect(contextPath + "/Login.jsp");
        return;
    }

    // Se la sessione esiste, prova a recuperare l'utente
    Object userObj = currentSession.getAttribute("currentUser");

    if (userObj instanceof Utente) {
        loggedInUser = (Utente) userObj;
    }

    // Se l'utente non è nella sessione o non è un amministratore, nega l'accesso.
    if (loggedInUser == null || !loggedInUser.isAdmin()) {
        String contextPath = request.getContextPath();
        
        // Creiamo una sessione (o ne usiamo una esistente) per impostare il messaggio di errore
        HttpSession sessionWithMsg = request.getSession(true);
        sessionWithMsg.setAttribute("messaggioErroreLogin", "Accesso negato: non hai i permessi di amministratore.");
        sessionWithMsg.setAttribute("tipoMessaggio", "error");
        
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
            font-family: 'Montserrat', sans-serif;
            background-color: #f0f4ef;
            color: #666;
            margin: 0;
            padding: 20px;
        }
        
        h1 {
            font-family: 'Montserrat', sans-serif;
            text-align: center;
            color: #3b6b1d; /* Verde scuro */
            margin-bottom: 5px;
            font-weight: 700;
            font-size: 2.5em;
        }
        
        p {
            text-align: center;
            margin-top: 0;
            color: #666;
            font-family: 'Montserrat', sans-serif;
            font-weight: 400;
        }
        
        /* Container principale per le card */
        .container {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
            max-width: 1200px;
            margin: 40px auto;
        }
        
        /* Stile per ogni riquadro (card) */
        .card {
            background-color: #f7fff7;
            border: 1px solid #d4e3d4;
            border-radius: 8px;
            padding: 20px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            text-align: center;
            transition: transform 0.2s, box-shadow 0.2s;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            min-height: 180px;
        }
        
        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
        }
        
        .card h3 {
            font-family: 'Montserrat', sans-serif;
            color: #3b6b1d; /* Verde scuro */
            margin-top: 0;
            font-size: 1.5em;
            font-weight: 700;
        }
        
        .card p {
            font-size: 0.9em;
            color: #777;
            flex-grow: 1;
            font-weight: 400;
            text-align: center;
        }
        
        /* Stile del pulsante generico */
        .button {
            display: block;
            width: 80%;
            margin: 15px auto 0;
            padding: 10px 20px;
            background-color: #558b2f;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            transition: background-color 0.3s;
            font-weight: 700;
            font-family: 'Montserrat', sans-serif;
        }
        
        .button:hover {
            background-color: #588157;
        }
        
        /* Stile per il raggruppamento dei pulsanti */
        .button-group {
            display: flex;
            justify-content: center;
            gap: 10px;
            margin-top: 15px;
        }
        
        /* Imposta la larghezza dei pulsanti all'interno del gruppo */
        .button-group .button {
            width: auto;
            flex-grow: 0;
            margin: 0;
        }
        
        /* Media query per schermi più piccoli (responsive design) */
        @media (max-width: 768px) {
            .container {
                grid-template-columns: 1fr;
            }
        }
        
        @media (max-width: 480px) {
            .button-group {
                flex-direction: column;
            }
        }
        
        /* Stili per la barra di navigazione, il footer e i messaggi */
        header {
            background-color: #dcedc8;
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
            font-family: 'Montserrat', sans-serif;
        }
        
        footer {
            text-align: center;
            margin-top: 40px;
            padding: 20px;
            color: #666;
            border-top: 1px solid #ddd;
        }
        
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
                <li><a href="<%= request.getContextPath() %>/homepage">HOME</a></li>
                <li><a href="<%= request.getContextPath() %>/LoginServlet?action=logout">LOGOUT</a></li>
            </ul>
        </nav>
    </header>

    <div class="content-wrapper">
        <h1 style="text-align: center;">Benvenuto nell'Area Amministratore</h1>
        <p style="text-align: center;">Qui puoi gestire tutti gli aspetti del tuo negozio "La Teca del Giardiniere".</p>

        <c:if test="${not empty requestScope.messaggioErroreLogin}">
            <div class="message ${requestScope.tipoMessaggio}">
                <p>${requestScope.messaggioErroreLogin}</p>
            </div>
        </c:if>

        <div class="container">
            <div class="card">
                <h3>Gestione Piante</h3>
                <p>Aggiungi, modifica o elimina piante dal catalogo.</p>
                <div class="button-group">
                    <a href="ListaPiante.jsp" class="button">Lista Piante</a>
                    <a href="FormInserimentoPiante.jsp" class="button">Aggiungi Pianta</a>
                </div>
            </div>

            <div class="card">
                <h3>Gestione Accessori</h3>
                <p>Controlla e aggiorna gli accessori disponibili.</p>
                <div class="button-group">
                    <a href="ListaAccessoriServlet" class="button">Lista Accessori</a>
                    <a href="FormInserimentoAccessori.jsp" class="button">Aggiungi Accessorio</a>
                </div>
            </div>

            <div class="card">
                <h3>Gestione Utenti</h3>
                <p>Visualizza e gestisci gli account degli utenti.</p>
                <div class="button-group">
                    <a href="ListaUtentiServlet" class="button">Gestione Utenti</a>
                </div>
            </div>

            <div class="card">
                <h3>Gestione Ordini</h3>
                <p>Tieni traccia ed elabora gli ordini dei clienti.</p>
                <div class="button-group">
                    <a href="ListaOrdini.jsp" class="button">Gestione Ordini</a>
                </div>
            </div>
        </div>
    </div>

    <footer>
        <p>&copy; 2025 La Teca del Giardiniere - Area Amministratore</p>
    </footer>
</body>
</html>