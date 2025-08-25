<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
    <title>Lista Utenti - La Teca del Giardiniere</title>
    
   <style>
	body {
	    font-family: 'Montserrat', sans-serif;
	    background-color: #f0f4f7;
	    display: flex;
	    justify-content: center;
	    align-items: flex-start;
	    min-height: 100vh;
	    margin: 0;
	    padding: 40px 20px;
	    box-sizing: border-box;
	}
	
	.container {
	    background-color: #ffffff;
	    border-radius: 12px;
	    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
	    padding: 30px;
	    width: 100%;
	    max-width: 1200px; /* Aumentato il max-width per contenere la tabella */
	    text-align: center;
	}
	
	h1 {
	    color: #2e7d32;
	    margin-bottom: 20px;
	    font-size: 2.5em;
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
	    display: flex;
	    justify-content: flex-end;
	    align-items: center;
	    margin-bottom: 20px;
	    gap: 10px;
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
	    transition: background-color 0.3s ease, transform 0.2s ease;
	    text-align: center;
	}
	
	.button:hover {
	    transform: scale(1.05);
	}
	
	.print-button {
	    background-color: #007bff;
	}
	
	.print-button:hover {
	    background-color: #0056b3;
	}
	
	.table-responsive {
	    overflow-x: auto; /* Permette lo scorrimento orizzontale */
	}
	
	table {
	    width: 100%;
	    margin: 0 auto;
	    border-collapse: collapse;
	    margin-top: 20px;
	    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
	}
	
	thead {
	    background-color: #4CAF50;
	    color: white;
	}
	
	th, td {
	    padding: 8px 10px; /* Padding ridotto per le celle */
	    text-align: left;
	    border-bottom: 1px solid #ddd;
	    white-space: nowrap; /* Impedisce al testo di andare a capo */
	    font-size: 0.9em;
	}
	
	thead th:first-child { border-top-left-radius: 8px; }
	thead th:last-child { border-top-right-radius: 8px; }
	
	tbody tr:last-child td {
	    border-bottom: none;
	}
	
	tr:hover {
	    background-color: #f2f2f2;
	}
	
	.actions {
	    white-space: nowrap;
	}
	
	.actions a {
	    margin: 0 4px;
	}
	
	.edit-button {
	    background-color: #007bff;
	    padding: 8px 12px;
	}
	
	.edit-button:hover {
	    background-color: #0056b3;
	}
	
	.delete-button {
	    background-color: #dc3545;
	    padding: 8px 12px;
	}
	
	.delete-button:hover {
	    background-color: #c82333;
	}
	
	.home-button-container {
	    margin-top: 30px;
	    text-align: center;
	}
	
	.home-button {
	    display: inline-block;
	    padding: 12px 25px;
	    background-color: #558b2f;
	    color: white;
	    border: none;
	    border-radius: 8px;
	    text-decoration: none;
	    font-size: 1.1em;
	    font-weight: bold;
	    cursor: pointer;
	    transition: background-color 0.3s ease;
	}
	
	.home-button:hover {
	    background-color: #386a1a;
	}
	
	@media print {
	    .action-bar,
	    .actions,
	    .home-button-container {
	        display: none !important;
	    }
	    body {
	        background: white;
	    }
	    .container {
	        box-shadow: none;
	        padding: 0;
	        margin: 0;
	    }
	    table {
	        page-break-inside: auto;
	        font-size: 12px;
	        width: 100%;
	        border: 1px solid black;
	    }
	    th, td {
	        border: 1px solid black !important;
	        padding: 8px;
	    }
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
            <c:remove var="messaggio" scope="session"/>
            <c:remove var="tipoMessaggio" scope="session"/>
        </c:if>


        <c:if test="${empty listaUtenti}">
            <p>Non ci sono utenti registrati nel database.</p>
        </c:if>

        <c:if test="${not empty listaUtenti}">
          <div class="table-responsive">
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
                                <a href="${pageContext.request.contextPath}/admin/EliminaUtenteServlet?action=delete&id=${utente.id}" class="button delete-button" onclick="return confirm('Sei sicuro di voler eliminare questo utente?');">Elimina</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            </div>
        </c:if>

        <div class="back-link">
           <a href="${pageContext.request.contextPath}/admin/AdminHome.jsp">Torna alla Dashboard Amministratore</a>
        </div>
    </div>
</body>
</html>