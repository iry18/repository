<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/ListaAccessori.css"> <%-- Adjust CSS path as needed --%>
    <title>Lista Accessori - La Teca del Giardiniere</title>
</head>
<body>
    <div class="container">
        <h1>Gestione Accessori</h1>

        <%-- Visualizzazione dei messaggi generali (successo/errore dal redirect) --%>
        <c:if test="${not empty messaggio}">
            <div class="message ${tipoMessaggio}">
                <p>${messaggio}</p>
            </div>
        </c:if>

        <div class="add-button-container">
            <a href="${pageContext.request.contextPath}/admin/AggiungiAccessorioServlet" class="button add-button">Aggiungi Nuovo Accessorio</a>
        </div>

        <c:if test="${empty listaAccessori}">
            <p>Non ci sono accessori nel catalogo.</p>
        </c:if>

        <c:if test="${not empty listaAccessori}">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Immagine</th>
                        <th>Nome</th>
                        <th>Categoria</th>
                        <th>Prezzo</th>
                        <th>Disponibilità</th>
                        <th>Data Inserimento</th>
                        <th>Azioni</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="accessorio" items="${listaAccessori}">
                        <tr>
                            <td>${accessorio.id}</td>
                            <td>
                                <c:if test="${not empty accessorio.immagine}">
                                    <img src="${pageContext.request.contextPath}/${accessorio.immagine}" alt="${accessorio.nome}" style="width: 50px; height: auto;">
                                </c:if>
                                <c:if test="${empty accessorio.immagine}">
                                    N/A
                                </c:if>
                            </td>
                            <td>${accessorio.nome}</td>
                            <td>${accessorio.categoria}</td>
                            <td><fmt:formatNumber value="${accessorio.prezzo}" type="currency" currencySymbol="€" minFractionDigits="2" maxFractionDigits="2"/></td>
                            <td>${accessorio.disponibilita}</td>
                            <td><fmt:formatDate value="${accessorio.dataInserimento}" pattern="dd/MM/yyyy HH:mm"/></td>
                            <td class="actions">
                                <a href="${pageContext.request.contextPath}/admin/ModificaAccessorioServlet?id=${accessorio.id}" class="button edit-button">Modifica</a>
                                <a href="${pageContext.request.contextPath}/admin/ListaAccessoriServlet?action=delete&id=${accessorio.id}" class="button delete-button" onclick="return confirm('Sei sicuro di voler eliminare questo accessorio?');">Elimina</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
    </div>
</body>
</html>