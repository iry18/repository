<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

    <!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Area Amministratore - La Teca del Giardiniere</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/homepage.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;700&display=swap" rel="stylesheet">
    <style>
       
        .admin-dashboard {
            max-width: 960px;
            margin: 40px auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            text-align: center;
        }

        .admin-dashboard h1 {
            color: #2e7d32;
            font-size: 2.8em;
            margin-bottom: 20px;
        }

        .admin-dashboard p {
            color: #555;
            font-size: 1.1em;
            margin-bottom: 30px;
        }

        .admin-menu-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 25px;
            margin-top: 30px;
        }

        .admin-menu-item {
            background-color: #e0f2e0; /* Sfondo chiaro per gli item */
            padding: 25px;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.08);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }

        .admin-menu-item:hover {
            transform: translateY(-5px);
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.15);
        }

        .admin-menu-item h3 {
            color: #388e3c;
            font-size: 1.6em;
            margin-bottom: 15px;
        }

        .admin-menu-item a {
            display: block;
            background-color: #558b2f; /* Pulsante verde scuro */
            color: white;
            padding: 12px 20px;
            border-radius: 5px;
            text-decoration: none;
            font-weight: bold;
            font-size: 1.1em;
            transition: background-color 0.3s ease;
        }

        .admin-menu-item a:hover {
            background-color: #386a1a; /* Verde più scuro al hover */
        }

        /* Stili per eventuali messaggi di errore/successo */
        .message {
            margin-top: 20px;
            padding: 15px;
            border-radius: 5px;
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
	            <a href="${pageContext.request.contextPath}/admin/ListaUtentiServlet">Gestione Utenti</a>
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