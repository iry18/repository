<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<%@ page import="java.util.List" %>
<%@ page import="la_teca_del_giardiniere.classes.Utente" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>La Teca del Giardiniere - Gestione Utenti</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/MyAccount.css"> 
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;700&display=swap">
    <style>
       
        .admin-page-container {
            max-width: 1200px; /* Larghezza maggiore per la tabella */
            margin: 50px auto;
            padding: 30px;
            background-color: #f5f5f5;
            border-radius: 8px;
            box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
            overflow-x: auto; /* Permette lo scroll orizzontale se la tabella è troppo larga */
        }
        .admin-page-container h1 {
            text-align: center;
            color: #6C7D47;
            margin-bottom: 40px;
            font-size: 2.8em;
            font-weight: 700;
            border-bottom: 3px solid #DAF7A6;
            padding-bottom: 15px;
            display: inline-block;
            margin-left: auto;
            margin-right: auto;
        }
        .admin-page-container .data-table { 
             border-radius: 8px; 
             overflow: hidden; 
             border: 1px solid #e0e0e0; 
        }
        /* Stili per le icone di modifica/eliminazione */
        .action-icon {
            margin: 0 5px;
            font-size: 1.2em; /* Aumenta la dimensione delle icone */
            color: #007979; /* Colore link */
            text-decoration: none;
        }
        .action-icon.delete {
            color: #dc3545; 
        }
        .action-icon:hover {
            opacity: 0.8;
        }
        .back-link {
            text-align: center;
            margin-top: 30px;
        }
        .back-link a {
            display: inline-block;
            background-color: #8D9F6D; /* Verde simile ai bottoni */
            color: white;
            padding: 10px 20px;
            border-radius: 5px;
            text-decoration: none;
            font-weight: 600;
            transition: background-color 0.3s ease, transform 0.2s ease;
        }
        .back-link a:hover {
            background-color: #6C7D47;
            transform: translateY(-2px);
        }

        /* Responsive Table */
        @media (max-width: 768px) {
            .admin-page-container {
                padding: 15px;
            }
            .data-table th, .data-table td {
                padding: 10px;
            }
            .data-table thead {
                display: none;
            }
            .data-table tbody, .data-table tr, .data-table td {
                display: block;
                width: 100%;
            }
            .data-table tr {
                margin-bottom: 15px;
                border: 1px solid #e0e0e0;
                border-radius: 8px;
            }
            .data-table td {
                text-align: right;
                padding-left: 50%; /* Spazio per l'etichetta */
                position: relative;
            }
            .data-table td:before {
                content: attr(data-label);
                position: absolute;
                left: 10px;
                width: calc(50% - 20px); /* Larghezza dell'etichetta */
                text-align: left;
                font-weight: bold;
                color: #555;
            }
            .action-icon {
                font-size: 1.5em; /* Icone più grandi su mobile */
            }
        }
    </style>
</head>
<body>
   
   <div class="admin-page-container">
        <h1>Elenco Utenti Registrati</h1>

        <%-- Messaggi di stato  --%>
        <c:if test="${not empty requestScope.message}">
            <div class="message <c:out value='${requestScope.messageType}'/>">
                <c:out value="${requestScope.message}"/>
            </div>
        </c:if>

        <c:choose>
            <c:when test="${empty listaUtenti}">
                <p style="text-align: center; color: #777; font-style: italic;">Nessun utente registrato trovato.</p>
            </c:when>
            <c:otherwise>
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>ID Utente</th> 
                            <th>Nome</th>
                            <th>Cognome</th>
                            <th>Email</th>
                            <th>Admin</th>
                            <th>Indirizzo</th>
                            <th>Città</th>
                            <th>CAP</th>
                            <th>Provincia</th> 
                            <th>Telefono</th>
                            <th>Data Registrazione</th>
                            <th>Azioni</th> 
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="utente" items="${listaUtenti}">
                            <tr>
                                <td data-label="ID Utente"><c:out value="${utente.id}"/></td>
                                <td data-label="Nome"><c:out value="${utente.nome}"/></td>
                                <td data-label="Cognome"><c:out value="${utente.cognome}"/></td>
                                <td data-label="Email"><c:out value="${utente.email}"/></td>
                                <td data-label="Admin">
                                    <c:choose>
                                        <c:when test="${utente.isAdmin}">Sì</c:when>
                                        <c:otherwise>No</c:otherwise>
                                    </c:choose>
                                </td>
                                <td data-label="Indirizzo"><c:out value="${utente.indirizzo}"/></td>
                                <td data-label="Città"><c:out value="${utente.citta}"/></td>
                                <td data-label="CAP"><c:out value="${utente.CAP}"/></td>
                                <td data-label="Provincia"><c:out value="${utente.provincia}"/></td>
                                <td data-label="Telefono"><c:out value="${utente.telefono}"/></td>
                                <td data-label="Data Registrazione">
                                    <fmt:formatDate value="${utente.data_registrazione}" pattern="dd/MM/yyyy HH:mm"/>
                                </td>
                                <td data-label="Azioni">
                                    <%-- Esempio di link per modifica ed eliminazione. Dovrai creare le Servlet relative. --%>
                                    <a href="${pageContext.request.contextPath}/AdminServlet?action=modificaUtente&idUtente=${utente.id}" title="Modifica Utente" class="action-icon">✏️</a>
                                    <a href="${pageContext.request.contextPath}/AdminServlet?action=eliminaUtente&idUtente=${utente.id}" title="Elimina Utente" class="action-icon delete" onclick="return confirm('Sei sicuro di voler eliminare ${utente.email}?');">🗑️</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>

        <div class="back-link">
            <a href="${pageContext.request.contextPath}/homepage.jsp">Torna alla Home</a>
        </div>
    </div>

</body>
</html>