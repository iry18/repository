<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

    <!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Area Venditore - La Teca del Giardiniere</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/homepage.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;700&display=swap" rel="stylesheet">
    <style>
        /* Stili simili a adminHome.jsp ma con colori o enfasi diversi se vuoi */
        .seller-dashboard {
            max-width: 960px;
            margin: 40px auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            text-align: center;
        }

        .seller-dashboard h1 {
            color: #2e7d32;
            font-size: 2.8em;
            margin-bottom: 20px;
        }

        .seller-dashboard p {
            color: #555;
            font-size: 1.1em;
            margin-bottom: 30px;
        }

        .seller-menu-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 25px;
            margin-top: 30px;
        }

        .seller-menu-item {
            background-color: #f0fff0; /* Sfondo chiaro leggermente diverso */
            padding: 25px;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.08);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }

        .seller-menu-item:hover {
            transform: translateY(-5px);
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.15);
        }

        .seller-menu-item h3 {
            color: #388e3c;
            font-size: 1.6em;
            margin-bottom: 15px;
        }

        .seller-menu-item a {
            display: block;
            background-color: #66bb6a; /* Verde più chiaro per i bottoni */
            color: white;
            padding: 12px 20px;
            border-radius: 5px;
            text-decoration: none;
            font-weight: bold;
            font-size: 1.1em;
            transition: background-color 0.3s ease;
        }

        .seller-menu-item a:hover {
            background-color: #43a047; /* Verde più scuro al hover */
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
                <li><a href="<%= request.getContextPath() %>/VenditoreHome.jsp">HOME VENDITORE</a></li>
                <li><a href="<%= request.getContextPath() %>/homepage.jsp">HOME SITO</a></li>
                <li><a href="<%= request.getContextPath() %>/LoginServlet?action=logout">LOGOUT</a></li>
            </ul>
        </nav>
    </header>

    <section class="seller-dashboard">
        <h1>Benvenuto, Venditore!</h1>
        <p>Qui puoi gestire i prodotti e gli ordini del negozio.</p>

        <%
            String message = (String) request.getAttribute("message");
            if (message != null && !message.isEmpty()) {
                String messageType = (String) request.getAttribute("messageType");
        %>
            <div class="message <%= (messageType != null ? messageType : "") %>">
                <%= message %>
            </div>
        <%
            }
        %>

        <div class="seller-menu-grid">
            <div class="seller-menu-item">
                <h3>Gestione Piante</h3>
                <p>Visualizza e modifica le piante del catalogo.</p>
                <a href="<%= request.getContextPath() %>/PianteServlet?action=list">Gestisci Piante</a>
            </div>

            <div class="seller-menu-item">
                <h3>Gestione Accessori</h3>
                <p>Visualizza e modifica gli accessori disponibili.</p>
                <a href="<%= request.getContextPath() %>/AccessoriServlet?action=list">Gestisci Accessori</a> 
            </div>

            <div class="seller-menu-item">
                <h3>Gestione Ordini</h3>
                <p>Visualizza e aggiorna lo stato degli ordini.</p>
                <a href="<%= request.getContextPath() %>/OrdineServlet?action=listAll">Gestisci Ordini</a>
            </div>

           
        </div>
    </section>

    <footer>
        <p>&copy; 2025 La Teca del Giardiniere - Area Venditore</p>
    </footer>

</body>
</html>