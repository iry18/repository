<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Elenco delle Piante - La Teca del Giardiniere (Admin)</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        /* Stili CSS integrati per replicare la struttura */
        body {
            font-family: Arial, sans-serif;
            background-color: #f0f2f5;
            display: flex;
            justify-content: center;
            align-items: flex-start; /* Allineato in alto, non al centro verticale */
            min-height: 100vh;
            margin: 0;
            padding: 40px 20px; /* Spazio sopra e sotto il contenitore */
            box-sizing: border-box;
        }
        .container {
            background-color: #ffffff;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            padding: 30px;
            width: 100%;
            max-width: 1200px; /* Larghezza massima adatta a più colonne */
            text-align: center; /* Per centrare il titolo e il messaggio */
        }
        h1 {
            color: #333;
            margin-bottom: 20px;
            font-size: 2em;
        }
        .message {
            padding: 12px;
            margin-bottom: 20px;
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
        .action-bar {
            margin-bottom: 20px;
            text-align: right; /* Allinea il bottone a destra */
        }
        .button {
            display: inline-block;
            padding: 10px 18px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            text-decoration: none;
            color: white;
            font-weight: bold;
            transition: background-color 0.3s ease;
            text-align: center;
        }
        .add-button {
            background-color: #28a745; /* Verde per aggiungi */
        }
        .add-button:hover {
            background-color: #218838;
        }
        .edit-button {
            background-color: #007bff; /* Blu per modifica */
            padding: 8px 12px; /* Rendi i bottoni nella tabella più piccoli */
        }
        .edit-button:hover {
            background-color: #0056b3;
        }
        .delete-button {
            background-color: #dc3545; /* Rosso per elimina */
            padding: 8px 12px;
            margin-left: 5px; /* Spazio tra modifica ed elimina */
        }
        .delete-button:hover {
            background-color: #c82333;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
        }
        thead {
            background-color: #4CAF50; /* Verde per l'intestazione */
            color: white;
        }
        th, td {
            padding: 12px 15px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        thead th:first-child { border-top-left-radius: 8px; }
        thead th:last-child { border-top-right-radius: 8px; }

        tbody tr:last-child td {
            border-bottom: none; /* Rimuovi bordo dall'ultima riga del corpo */
        }
        tr:hover {
            background-color: #f2f2f2;
        }
        .actions {
            white-space: nowrap; /* Previene il wrapping dei bottoni */
        }
        .delete-form {
            display: inline; /* Per allineare il bottone elimina con il link modifica */
        }
        .home-button-container {
            margin-top: 30px; /* Spazio sopra il bottone */
            text-align: center;
        }
        .home-button {
            display: inline-block;
            padding: 12px 25px;
            background-color: #007bff; /* Blu per il bottone */
            color: white;
            border: none;
            border-radius: 5px;
            text-decoration: none;
            font-size: 1em;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }
        .home-button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Elenco delle Piante</h1>
        
        <%-- Visualizzazione messaggi di successo/errore dalla request/session --%>
        <c:if test="${not empty requestScope.messaggio}">
            <div class="message ${requestScope.tipoMessaggio}">
                <p>${requestScope.messaggio}</p>
            </div>
        </c:if>
        <c:if test="${not empty sessionScope.messaggio}">
            <div class="message ${sessionScope.tipoMessaggio}">
                <p>${sessionScope.messaggio}</p>
            </div>
            <c:remove var="messaggio" scope="session"/>
            <c:remove var="tipoMessaggio" scope="session"/>
        </c:if>


        <c:if test="${empty listaPiante}">
            <p class="no-plants-message">Non ci sono piante nel catalogo.</p>
        </c:if>
        <c:if test="${not empty listaPiante}">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>NOME COMUNE</th>
                        <th>TIPO</th>
                        <th>NOME SCIENTIFICO</th>
                        <th>CATEGORIA</th>
                        <th>PREZZO</th>
                        <th>DISPONIBILITÀ</th>
                        <th>AZIONI</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="pianta" items="${listaPiante}">
                        <tr>
                            <td><c:out value="${pianta.id}"/></td>
                            <td><c:out value="${pianta.nomeComune}"/></td>
                            <td><c:out value="${pianta.tipo}"/></td>
                            <td><em><c:out value="${pianta.nomeBotanico}"/></em></td>
                            <td><c:out value="${pianta.categoria}"/></td>
                            <td><fmt:formatNumber value="${pianta.prezzo}" type="currency" currencySymbol="€"/></td>
                            <td>
                                <c:choose>
                                    <c:when test="${pianta.quantitaDisponibile != null && pianta.quantitaDisponibile > 0}">
                                        <c:out value="${pianta.quantitaDisponibile}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <span style="color: red; font-weight: bold;">Esaurito</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="actions">
                                <a href="${pageContext.request.contextPath}/admin/ModificaPiantaServlet?id=${pianta.id}" class="button edit-button">Modifica</a>
                                <form action="${pageContext.request.contextPath}/admin/EliminaPiantaServlet" method="post" class="delete-form" onsubmit="return confirm('Sei sicuro di voler eliminare la pianta ${pianta.nomeComune}?');">
                                    <input type="hidden" name="id" value="${pianta.id}">
                                    <button type="submit" class="button delete-button">Elimina</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>

        <div class="home-button-container">
            <a href="${pageContext.request.contextPath}/admin/AdminHome.jsp" class="home-button">Torna alla Dashboard Admin</a>
        </div>
    </div>
</body>
</html>