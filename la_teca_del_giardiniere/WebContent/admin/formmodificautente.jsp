<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Modifica Utente - La Teca del Giardiniere</title>
    <style>
        /* Basic styles, similar to your other admin pages */
        body {
            font-family: 'Montserrat', sans-serif;
            background-color: #f4f4f4;
            color: #333;
            margin: 0;
            padding: 0;
        }
        .container {
            max-width: 800px;
            margin: 40px auto;
            padding: 20px 30px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }
        h1 {
            color: #2e7d32;
            text-align: center;
            margin-bottom: 25px;
        }
        .form-group {
            margin-bottom: 15px;
        }
        .form-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
            color: #555;
        }
        .form-group input[type="text"],
        .form-group input[type="email"],
        .form-group input[type="password"],
        .form-group input[type="number"] {
            width: calc(100% - 22px); /* Account for padding and border */
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box; /* Include padding in width */
        }
        .form-group input[type="checkbox"] {
            margin-right: 10px;
        }
        .form-actions {
            text-align: right;
            margin-top: 20px;
        }
        .button {
            display: inline-block;
            padding: 10px 20px;
            border-radius: 5px;
            text-decoration: none;
            font-weight: bold;
            font-size: 1em;
            transition: background-color 0.3s ease;
            cursor: pointer;
            border: none;
            margin-left: 10px;
        }
        .button.submit-button {
            background-color: #558b2f; /* Green */
            color: white;
        }
        .button.submit-button:hover {
            background-color: #386a1a;
        }
        .button.cancel-button {
            background-color: #ccc;
            color: #333;
        }
        .button.cancel-button:hover {
            background-color: #bbb;
        }
        .message {
            margin-bottom: 20px;
            padding: 15px;
            border-radius: 5px;
            font-weight: bold;
        }
        .message.error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Modifica Utente #${utente.id}</h1>

        <c:if test="${not empty requestScope.messaggio}">
            <div class="message ${requestScope.tipoMessaggio}">
                <p>${requestScope.messaggio}</p>
            </div>
        </c:if>

        <c:if test="${empty utente}">
            <p>Impossibile caricare i dati dell'utente per la modifica.</p>
            <div class="form-actions" style="text-align: center;">
                 <a href="${pageContext.request.contextPath}/admin/ListaUtentiServlet" class="button cancel-button">Torna alla Lista Utenti</a>
            </div>
        </c:if>

        <c:if test="${not empty utente}">
            <form action="${pageContext.request.contextPath}/admin/ModificaUtenteServlet" method="post">
                <input type="hidden" name="id" value="${utente.id}">

                <div class="form-group">
                    <label for="nome">Nome:</label>
                    <input type="text" id="nome" name="nome" value="${utente.nome}" required>
                </div>
                <div class="form-group">
                    <label for="cognome">Cognome:</label>
                    <input type="text" id="cognome" name="cognome" value="${utente.cognome}" required>
                </div>
                <div class="form-group">
                    <label for="email">Email:</label>
                    <input type="email" id="email" name="email" value="${utente.email}" required>
                </div>
                <%--
                    IMPORTANT: Password field handling
                    It's common to leave the password field blank on edit forms.
                    If left blank, the existing password hash is retained.
                    If a new value is entered, it should be hashed by the servlet.
                --%>
                <div class="form-group">
                    <label for="password">Nuova Password (lasciare vuoto per mantenere quella attuale):</label>
                    <input type="password" id="password" name="password">
                </div>
                <div class="form-group">
                    <label for="indirizzo">Indirizzo:</label>
                    <input type="text" id="indirizzo" name="indirizzo" value="${utente.indirizzo}">
                </div>
                <div class="form-group">
                    <label for="citta">Città:</label>
                    <input type="text" id="citta" name="citta" value="${utente.citta}">
                </div>
                <div class="form-group">
                    <label for="provincia">Provincia:</label>
                    <input type="text" id="provincia" name="provincia" value="${utente.provincia}">
                </div>
                <div class="form-group">
                    <label for="CAP">CAP:</label>
                    <input type="number" id="CAP" name="CAP" value="${utente.CAP}">
                </div>
                <div class="form-group">
                    <label for="telefono">Telefono:</label>
                    <input type="text" id="telefono" name="telefono" value="${utente.telefono}">
                </div>

                <div class="form-group">
                    <label>Ruoli:</label><br>
                    <input type="checkbox" id="isAdmin" name="isAdmin" value="true" <c:if test="${utente.isAdmin()}">checked</c:if>>
                    <label for="isAdmin">Amministratore</label><br>
                    <input type="checkbox" id="isVenditore" name="isVenditore" value="true" <c:if test="${utente.isVenditore()}">checked</c:if>>
                    <label for="isVenditore">Venditore</label>
                </div>

                <div class="form-actions">
                    <button type="submit" class="button submit-button">Salva Modifiche</button>
                    <a href="${pageContext.request.contextPath}/admin/ListaUtentiServlet" class="button cancel-button">Annulla</a>
                </div>
            </form>
        </c:if>
    </div>
</body>
</html>