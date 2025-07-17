<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/ListaUtenti.css"> 
    <title>Lista Utenti - La Teca del Giardiniere</title>
    <style>
        /* Basic CSS for the table - you can put this in ListaUtenti.css */
        body {
            font-family: 'Montserrat', sans-serif;
            background-color: #f4f4f4;
            color: #333;
            margin: 0;
            padding: 0;
        }
        .container {
            max-width: 1500px;
            margin: 20px auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }
        h1 {
            color: #2e7d32;
            text-align: center;
            margin-bottom: 25px;
        }
        .message {
            margin-bottom: 20px;
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
        .add-button-container {
            text-align: right;
            margin-bottom: 20px;
        }
        .button {
            display: inline-block;
            padding: 10px 18px;
            border-radius: 5px;
            text-decoration: none;
            font-weight: bold;
            font-size: 0.9em;
            transition: background-color 0.3s ease;
            cursor: pointer;
            border: none;
        }
        .button.add-button {
            background-color: #558b2f; /* Green */
            color: white;
        }
        .button.add-button:hover {
            background-color: #386a1a;
        }
        .button.edit-button {
            background-color: #ffc107; /* Yellow */
            color: #333;
            margin-right: 5px;
        }
        .button.edit-button:hover {
            background-color: #e0a800;
        }
        .button.delete-button {
            background-color: #dc3545; /* Red */
            color: white;
        }
        .button.delete-button:hover {
            background-color: #c82333;
        }
        .data-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        .data-table th, .data-table td {
            border: 1px solid #ddd;
            padding: 10px;
            text-align: left;
            vertical-align: middle;
        }
        .data-table th {
            background-color: #f2f2f2;
            color: #555;
            font-weight: bold;
        }
        .data-table tbody tr:nth-child(even) {
            background-color: #f9f9f9;
        }
        .data-table tbody tr:hover {
            background-color: #f1f1f1;
        }
        .actions {
            white-space: nowrap; /* Keep buttons on one line */
        }
        .back-link {
            text-align: center;
            margin-top: 30px;
        }
        .back-link a {
            color: #558b2f;
            text-decoration: none;
            font-weight: bold;
        }
        .back-link a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Gestione Utenti</h1>

        <%-- Visualizzazione dei messaggi (successo/errore dal redirect) --%>
        <c:if test="${not empty sessionScope.messaggio}">
            <div class="message ${sessionScope.tipoMessaggio}">
                <p>${sessionScope.messaggio}</p>
            </div>
            <%-- Clear the session attributes after displaying --%>
            <c:remove var="messaggio" scope="session"/>
            <c:remove var="tipoMessaggio" scope="session"/>
        </c:if>


        <c:if test="${empty listaUtenti}">
            <p>Non ci sono utenti registrati nel database.</p>
        </c:if>

        <c:if test="${not empty listaUtenti}">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nome</th>
                        <th>Cognome</th>
                        <th>Email</th>
                        <th>Telefono</th>
                        <th>Città</th>
                        <th>Provincia</th>
                        <th>CAP</th>
                        <th>Admin</th>
                        <th>Venditore</th>
                        <th>Data Registrazione</th>
                        <th>Azioni</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="utente" items="${listaUtenti}">
                        <tr>
                            <td>${utente.id}</td>
                            <td>${utente.nome}</td>
                            <td>${utente.cognome}</td>
                            <td>${utente.email}</td>
                            <td>${utente.telefono}</td>
                            <td>${utente.citta}</td>
                            <td>${utente.provincia}</td>
                            <td>${utente.CAP}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${utente.isAdmin()}">Sì</c:when>
                                    <c:otherwise>No</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${utente.isVenditore()}">Sì</c:when>
                                    <c:otherwise>No</c:otherwise>
                                </c:choose>
                            </td>
                            <td><fmt:formatDate value="${utente.data_registrazione}" pattern="dd/MM/yyyy HH:mm"/></td>
                            <td class="actions">
                                <a href="${pageContext.request.contextPath}/admin/ModificaUtenteServlet?id=${utente.id}" class="button edit-button">Modifica</a>
                                <a href="${pageContext.request.contextPath}/admin/ListaUtentiServlet?action=delete&id=${utente.id}" class="button delete-button" onclick="return confirm('Sei sicuro di voler eliminare questo utente?');">Elimina</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>

        <div class="back-link">
           <a href="${pageContext.request.contextPath}/admin/AdminHome.jsp">Torna alla Dashboard Amministratore</a>
        </div>
    </div>
</body>
</html>