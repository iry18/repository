<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> <%-- Importa per la formattazione della data --%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestione Utenti - Area Amministratore</title>
    <link rel="stylesheet" href="admin.css"> </head>
<body>
    <div class="container">
        <h1>Gestione Utenti</h1>

        <c:if test="${not empty messaggio}">
            <div class="${tipoMessaggio}">${messaggio}</div> </c:if>

        <c:if test="${empty listaUtenti}">
            <p>Non ci sono utenti registrati.</p>
        </c:if>

        <c:if test="${not empty listaUtenti}">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Username</th>
                        <th>Email</th>
                        <th>Data Registrazione</th>
                        <th>Azioni</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="utente" items="${listaUtenti}">
                        <tr>
                            <td>${utente.getId()}</td>
                            <td>${utente.getUsername()}</td>
                            <td>${utente.getEmail()}</td>
                            <td>${utente.getDataRegistrazione()}</td>
                            <td>
                                <a href="dettaglioutenteServlet?id=${utente.getId()}">Dettagli e Ordini</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>

        <div class="back-link">
            <a href="adminHome.html">Torna all'Area Amministratore</a>
        </div>
    </div>
</body>
</html>