<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lista Piante - La Teca del Giardiniere</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/ListaPiante.css"> 
</head>
<body>
    <div class="container">
        <h1>Elenco Piante</h1>

        <%-- Visualizzazione messaggi di successo/errore dalla sessione (dopo redirect) --%>
        <c:if test="${not empty sessionScope.messaggio}">
            <div class="message ${sessionScope.tipoMessaggio}">
                <p>${sessionScope.messaggio}</p>
            </div>
            <c:remove var="messaggio" scope="session"/>
            <c:remove var="tipoMessaggio" scope="session"/>
        </c:if>

        <div class="action-bar">
            <a href="${pageContext.request.contextPath}/PianteServlet" class="button add-button">Aggiungi Nuova Pianta</a>
        </div>

        <c:if test="${empty listaPiante}">
            <p>Nessuna pianta disponibile nel catalogo.</p>
        </c:if>
        <c:if test="${not empty listaPiante}">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nome Comune</th>
                        <th>Tipo</th>
                        <th>Prezzo</th>
                        <th>Disponibilità</th>
                        <th>Azioni</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="pianta" items="${listaPiante}">
                        <tr>
                            <td>${pianta.id}</td>
                            <td>${pianta.nomeComune}</td>
                            <td>${pianta.tipo}</td>
                            <td><fmt:formatNumber value="${pianta.prezzo}" type="currency" currencySymbol="€"/></td>
                            <td>${pianta.quantitaDisponibile}</td>
                            <td class="actions">
                                <a href="${pageContext.request.contextPath}/PianteServlet?id=${pianta.id}" class="button edit-button">Modifica</a>
                                <a href="${pageContext.request.contextPath}/EliminaPiantaServlet?id=${pianta.id}"
                                   class="button delete-button"
                                   onclick="return confirm('Sei sicuro di voler eliminare la pianta ${pianta.nomeComune}?');">Elimina</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
        <div class="back-link">
            <a href="${pageContext.request.contextPath}/AdminHome.jsp">Torna alla Dashboard Admin</a>
        </div>
    </div>
</body>
</html>