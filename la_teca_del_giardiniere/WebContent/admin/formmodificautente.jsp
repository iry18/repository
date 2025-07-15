<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>
        <c:choose>
            <c:when test="${not empty utenteDaModificare}">Modifica Profilo</c:when>
            <c:otherwise>Registrati</c:otherwise>
        </c:choose>
        - La Teca del Giardiniere
    </title>
    <link rel="stylesheet" href="formmodificautente.css">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
    <div class="register-container">
        <h1>
            <c:choose>
                <c:when test="${not empty utenteDaModificare}">Modifica Profilo</c:when>
                <c:otherwise>Crea un nuovo account</c:otherwise>
            </c:choose>
        </h1>

        <%-- Visualizzazione dei messaggi di errore --%>
        <c:if test="${not empty erroriRegistrazione}">
            <div class="error-messages message error">
                <p>Si sono verificati i seguenti errori:</p>
                <ul>
                    <c:forEach var="errore" items="${erroriRegistrazione}">
                        <li>${errore}</li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>

        <%-- Visualizzazione dei messaggi di successo --%>
        <c:if test="${not empty messaggioSuccesso}">
            <div class="message success">
                <p>${messaggioSuccesso}</p>
            </div>
        </c:if>

        <form action="<c:url value="${not empty utenteDaModificare ? '/admin/ModificaUtenteServlet' : '/RegistrazioneServlet'}"/>" method="post">

            <%-- Campo ID nascosto per la modifica --%>
            <c:if test="${not empty utenteDaModificare}">
                <input type="hidden" name="id" value="${utenteDaModificare.id}">
            </c:if>

            <div class="form-group">
                <label for="nome">Nome:</label>
                <input type="text" id="nome" name="nome" value="<c:out value="${utenteDaModificare.nome}"/>" required>
            </div>
            <div class="form-group">
                <label for="cognome">Cognome:</label>
                <input type="text" id="cognome" name="cognome" value="<c:out value="${utenteDaModificare.cognome}"/>" required>
            </div>
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" value="<c:out value="${utenteDaModificare.email}"/>" required>
            </div>
            <div class="form-group">
                <label for="password">Password: <c:if test="${not empty utenteDaModificare}">(Lascia vuoto per non modificare)</c:if></label>
                <input type="password" id="password" name="password" <c:if test="${empty utenteDaModificare}">required</c:if>>
            </div>
            <div class="form-group">
                <label for="indirizzo">Indirizzo:</label>
                <input type="text" id="indirizzo" name="indirizzo" value="<c:out value="${utenteDaModificare.indirizzo}"/>" required>
            </div>
            <div class="form-group">
                <label for="citta">Città:</label>
                <input type="text" id="citta" name="citta" value="<c:out value="${utenteDaModificare.citta}"/>" required>
            </div>
            <div class="form-group">
                <label for="cap">CAP:</label>
                <input type="text" id="cap" name="CAP" value="<c:out value="${utenteDaModificare.CAP}"/>" required pattern="\d{5}" title="Il CAP deve essere di 5 cifre numeriche">
            </div>
            <div class="form-group">
                <label for="telefono">Telefono:</label>
                <input type="tel" id="telefono" name="telefono" value="<c:out value="${utenteDaModificare.telefono}"/>" required pattern="\d{10}" title="Il numero di telefono deve essere di 10 cifre numeriche">
            </div>

            <%-- Controllo ruolo per amministratori (opzionale) --%>
            <%-- Qui assumiamo che sessionScope.currentUser.isAdmin sia disponibile per l'utente loggato --%>
            <c:if test="${sessionScope.currentUser != null && sessionScope.currentUser.isAdmin}">
                <div class="form-group">
                    <label for="isAdmin">Amministratore:</label>
                    <input type="checkbox" id="isAdmin" name="isAdmin" <c:if test="${utenteDaModificare.isAdmin}">checked</c:if>>
                </div>
            </c:if>

            <div class="form-actions">
                <c:choose>
                    <c:when test="${not empty utenteDaModificare}">
                        <button type="submit" class="button update-button">Salva Modifiche</button>
                    </c:when>
                    <c:otherwise>
                        <button type="submit" class="button register-button">Registrati</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </form>

        <div class="back-link">
            <a href="${pageContext.request.contextPath}/index.jsp">Torna alla Home</a>
            <%-- Questo link appare solo se l'utente loggato è un amministratore --%>
            <c:if test="${sessionScope.currentUser != null && sessionScope.currentUser.isAdmin}">
                <a href="${pageContext.request.contextPath}/admin/utentiServlet" class="button-back-list">Torna all'elenco Utenti</a>
            </c:if>
        </div>
    </div>
</body>
</html>