<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> <%-- Importa per la formattazione della data --%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Elenco Utenti Registrati</title>
    <link rel="stylesheet" href="listapiante.css"> 
    
</head>
<body>
    <div class="container"> <%-- WRAPPA IL CONTENUTO IN UN CONTAINER --%>
        <h1>Elenco Utenti Registrati</h1>
        <table>
            <thead>
                <tr>
                    <th>Nome</th>
                    <th>Cognome</th>
                    <th>Email</th>
                    <th>Admin</th>
                    <th>Indirizzo</th>
                    <th>Città</th>
                    <th>CAP</th>
                    <th>Telefono</th>
                    <th>Data Registrazione</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="utente" items="${listaUtenti}">
                    <tr>
                        <td><c:out value="${utente.nome}"/></td>
                        <td><c:out value="${utente.cognome}"/></td>
                        <td><c:out value="${utente.email}"/></td>
                        <td><c:out value="${utente.isAdmin}"/></td>
                        <td><c:out value="${utente.indirizzo}"/></td>
                        <td><c:out value="${utente.citta}"/></td>
                        <td><c:out value="${utente.CAP}"/></td>
                        <td><c:out value="${utente.telefono}"/></td>
                        <td><c:out value="${utente.data_registrazione}"/></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <br>
        <div class="back-link">
            <a href="index.html">Torna alla Home</a>
        </div>
    </div> <%-- CHIUDI IL CONTAINER --%>
</body>
</html>