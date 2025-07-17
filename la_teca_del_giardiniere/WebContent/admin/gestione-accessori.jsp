<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestione Accessori - La Teca del Giardiniere</title>
    <%-- Puoi mantenere il tuo CSS globale se lo usi per header/footer --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/homepage.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;700&display=swap" rel="stylesheet">

    <%-- STILI SPECIFICI PER LA PAGINA DI GESTIONE ACCESSORI --%>
    <style>
        body {
            font-family: 'Montserrat', Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 20px;
            display: flex;
            flex-direction: column; /* Cambiato per allineare header, section e footer in colonna */
            align-items: center;
            min-height: 100vh;
        }

        .container {
            background-color: #ffffff;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 1200px;
            box-sizing: border-box;
            margin-bottom: 20px; /* Spazio prima del footer se presente */
        }

        h1, h2 {
            text-align: center;
            color: #333;
            margin-bottom: 30px;
        }

        /* Stili per i messaggi di stato (successo/errore) */
        .message {
            margin-bottom: 20px;
            padding: 15px;
            border-radius: 5px;
            font-weight: bold;
            text-align: center;
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

        /* Stili per la tabella */
        .accessories-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.08);
        }

        .accessories-table th, .accessories-table td {
            padding: 12px 15px;
            text-align: left;
            border-bottom: 1px solid #ddd;
            vertical-align: middle;
        }

        .accessories-table th {
            background-color: #4CAF50;
            color: white;
            font-weight: bold;
            text-transform: uppercase;
            font-size: 0.9em;
        }

        .accessories-table tbody tr:nth-child(even) {
            background-color: #f9f9f9;
        }

        .accessories-table tbody tr:hover {
            background-color: #e2f0e2;
        }

        /* Stili per le celle delle azioni (Modifica, Elimina) */
        .accessories-table td:last-child {
            white-space: nowrap;
        }

        .accessories-table td a {
            text-decoration: none;
            color: #007bff;
            margin-right: 10px;
            transition: color 0.3s ease;
        }

        .accessories-table td a:hover {
            color: #0056b3;
            text-decoration: underline;
        }

        .accessories-table td a.delete-btn {
            color: #dc3545;
        }

        .accessories-table td a.delete-btn:hover {
            color: #bd2130;
        }

        /* Stili per il messaggio "Nessun accessorio" */
        p.no-accessories-message {
            text-align: center;
            color: #666;
            margin-top: 30px;
            font-style: italic;
        }

        /* Stili per il pulsante "Aggiungi Nuovo Accessorio" */
        .add-accessory-button-container {
            text-align: center;
            margin-top: 30px;
            margin-bottom: 40px;
        }

        .add-accessory-button-container a {
            background-color: #558b2f;
            color: white;
            padding: 12px 25px;
            border-radius: 5px;
            text-decoration: none;
            font-weight: bold;
            font-size: 1.1em;
            transition: background-color 0.3s ease;
        }

        .add-accessory-button-container a:hover {
            background-color: #386a1a;
        }

        /* Stili per il link di ritorno alla home admin */
        .back-to-admin-home {
            text-align: center;
            margin-top: 30px;
        }
        .back-to-admin-home a {
            display: inline-block;
            background-color: #007bff;
            color: white;
            padding: 10px 20px;
            border-radius: 5px;
            text-decoration: none;
            transition: background-color 0.3s ease;
        }
        .back-to-admin-home a:hover {
            background-color: #0056b3;
        }

        /* Media Queries per la responsività */
        @media (max-width: 768px) {
            .container {
                padding: 15px;
            }
            .accessories-table, .accessories-table thead, .accessories-table tbody, .accessories-table th, .accessories-table td, .accessories-table tr {
                display: block;
            }
            .accessories-table thead tr {
                position: absolute;
                top: -9999px;
                left: -9999px;
            }
            .accessories-table tr {
                border: 1px solid #ccc;
                margin-bottom: 10px;
                border-radius: 5px;
            }
            .accessories-table td {
                border: none;
                border-bottom: 1px solid #eee;
                position: relative;
                padding-left: 50%;
                text-align: right;
            }
            .accessories-table td:before {
                position: absolute;
                top: 6px;
                left: 6px;
                width: 45%;
                padding-right: 10px;
                white-space: nowrap;
                text-align: left;
                font-weight: bold;
            }
            /* Assegna le etichette ai campi per la modalità mobile */
            .accessories-table td:nth-of-type(1):before { content: "ID:"; }
            .accessories-table td:nth-of-type(2):before { content: "Nome:"; }
            .accessories-table td:nth-of-type(3):before { content: "Prezzo:"; }
            .accessories-table td:nth-of-type(4):before { content: "Disponibilità:"; }
            .accessories-table td:nth-of-type(5):before { content: "Dimensioni:"; }
            .accessories-table td:nth-of-type(6):before { content: "Data Inserimento:"; }
            .accessories-table td:nth-of-type(7):before { content: "Azioni:"; }
        }
    </style>
</head>
<body>
    <header>
        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/adminHome.jsp">HOME ADMIN</a></li>
                <li><a href="${pageContext.request.contextPath}/homepage.jsp">HOME SITO</a></li>
                <li><a href="${pageContext.request.contextPath}/LoginServlet?action=logout">LOGOUT</a></li>
            </ul>
        </nav>
    </header>

    <section class="admin-section">
        <div class="container">
            <h1>Gestione Accessori</h1>

            <%-- Visualizzazione dei messaggi dalla Servlet --%>
            <c:if test="${not empty messaggio}">
                <div class="message ${tipoMessaggio}">
                    ${messaggio}
                </div>
            </c:if>

            <div class="add-accessory-button-container">
                <a href="${pageContext.request.contextPath}/admin/accessoriServlet?action=mostraFormInserimento">Aggiungi Nuovo Accessorio</a>
            </div>

            <h2>Elenco Accessori</h2>
            <c:choose>
                <c:when test="${empty listaAccessori}">
                    <p class="no-accessories-message">Nessun accessorio trovato nel database.</p>
                </c:when>
                <c:otherwise>
                    <table class="accessories-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Nome</th>
                                <th>Prezzo</th>
                                <th>Disponibilità</th>
                                <th>Dimensioni</th>
                                <th>Data Inserimento</th>
                                <th>Azioni</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="acc" items="${listaAccessori}">
                                <tr>
                                    <td>${acc.id}</td>
                                    <td>${acc.nome}</td>
                                    <td><fmt:formatNumber value="${acc.prezzo}" type="currency" currencySymbol="€" minFractionDigits="2"/></td>
                                    <td>${acc.disponibilita}</td>
                                    <td>${acc.dimensioni}</td>
                                    <td><fmt:formatDate value="${acc.data_inserimento}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                    <td class="actions">
                                        <a href="${pageContext.request.contextPath}/admin/accessoriServlet?action=mostraFormModifica&id=${acc.id}" class="edit-btn">Modifica</a>
                                        <a href="${pageContext.request.contextPath}/admin/accessoriServlet?action=elimina&id=${acc.id}" class="delete-btn" onclick="return confirm('Sei sicuro di voler eliminare questo accessorio?');">Elimina</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>

            <div class="back-to-admin-home">
                <a href="${pageContext.request.contextPath}/adminHome.jsp">Torna alla Home Admin</a>
            </div>
        </div>
    </section>

    <footer>
        <p>&copy; 2025 La Teca del Giardiniere - Gestione Accessori</p>
    </footer>

</body>
</html>