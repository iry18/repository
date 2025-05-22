<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> <%-- Importa per la formattazione della data --%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Elenco Piante - La Teca del Giardiniere</title>
    <link rel="stylesheet" href="listapiante.css"> </head>
<body>
    <div class="container">
        <h1>Elenco delle Piante</h1>

        <c:if test="${not empty messaggio}">
            <div class="${tipoMessaggio}">${messaggio}</div> </c:if>

        <c:if test="${empty listaPiante}">
            <p>Non ci sono piante nel catalogo.</p>
        </c:if>

        <c:if test="${not empty listaPiante}">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nome Comune</th>
                        <th>Tipo</th>
                        <th>Nome Scientifico</th>
                        <th>Categoria</th>
                        <th>Prezzo</th>
                        <th>Disponibilità</th>
                        <th>Azioni</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="pianta" items="${listaPiante}">
                        <tr>
                            <td>${pianta.getId()}</td>
                            <td>${pianta.getNomeComune()}</td>
                            <td><c:if test="${pianta.isTipo()}">Esterno</c:if><c:if test="${not pianta.isTipo()}">Interno</c:if></td>
                            <td>${pianta.getNomeScientificoBotanico()}</td>
                            <td>${pianta.getCategoria()}</td>
                            <td>${pianta.getPrezzo()} €</td>
                            <td>${pianta.getDisponibilita()}</td>
                            <td>
                                <a href="modificapianteServlet?id=${pianta.getId()}">Modifica</a>
                                <a href="eliminapianteServlet?id=${pianta.getId()}" onclick="return confirm('Sei sicuro di voler eliminare questa pianta?')">Elimina</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>

        <div class="back-link">
            <a href="index.html">Torna alla Home</a>
        </div>
    </div>
</body>
</html>