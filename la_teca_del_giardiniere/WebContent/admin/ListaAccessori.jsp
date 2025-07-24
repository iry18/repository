<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="it"> 
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"> 
    <title>Lista Accessori - La Teca del Giardiniere</title>
     
    <style>
        body {
            font-family: 'Montserrat', sans-serif;
            background-color: #f4f4f4;
            color: #333;
            margin: 0;
            padding: 0;
        }
        .container {
            max-width: 1200px;
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
            background-color: #558b2f;
            color: white;
        }
        .button.add-button:hover {
            background-color: #386a1a;
        }
        .button.edit-button {
            background-color: #ffc107;
            color: #333;
            margin-right: 5px;
        }
        .button.edit-button:hover {
            background-color: #e0a800;
        }
        .button.delete-button {
            background-color: #dc3545;
            color: white;
        }
        .button.delete-button:hover {
            background-color: #c82333;
        }
        .button.print-button {
            background-color: #007bff;
            color: white;
        }
        .button.print-button:hover {
            background-color: #0056b3;
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
            white-space: nowrap;
        }
        .delete-form {
            display: inline-block; 
            margin: 0;
            padding: 0;
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
        /* Stili per stampa */
        @media print {
            .print-button-container,
            .add-button-container,
            .actions,
            .back-link {
                display: none !important;
            }
            body {
                background: white;
                color: black;
            }
            .container {
                box-shadow: none;
                border: none;
                padding: 0;
                margin: 0;
            }
            table {
                page-break-inside: avoid;
                font-size: 12px;
            }
            th, td {
                border: 1px solid black !important;
            }
        }
    </style>
</head>
<body>
    <div class="container"> 
        <h1>Gestione Accessori</h1>

        <div class="print-button-container" style="text-align: right; margin-bottom: 15px;">
            <button onclick="window.print()" class="button print-button">🖨️ Stampa Lista</button>
        </div>
        

       <c:if test="${not empty sessionScope.messaggio}">
            <div class="message ${sessionScope.tipoMessaggio}">
                <p>${sessionScope.messaggio}</p>
            </div>
            <c:remove var="messaggio" scope="session"/>
            <c:remove var="tipoMessaggio" scope="session"/>
        </c:if>

        <c:choose>
            <c:when test="${not empty listaAccessori}">
                <table class="data-table"> 
                    <thead>
                        <tr>
                            <th>Accessorio_id</th>
                            <th>Nome</th>
                            <th>Prezzo</th>
                            <th>Disponibilità</th>
                            <th>Descrizione Breve</th>
                            <th>Descrizione Dettagliata</th>
                            <th>Dimensioni</th>
                            <th>Immagine (URL)</th>
                            <th>Categoria</th>
                            <th>Data Inserimento</th>
                            <th>Azioni</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="accessorio" items="${listaAccessori}">
                            <tr>
                                <td><c:out value="${accessorio.accessorio_id}"/></td>
                                <td><c:out value="${accessorio.nome}"/></td>
                                <td><fmt:formatNumber value="${accessorio.prezzo}" type="currency" currencySymbol="€"/></td>
                                <td><c:out value="${accessorio.disponibilita}"/></td>
                                <td><c:out value="${accessorio.descrizioneBreve}"/></td>
                                <td><c:out value="${accessorio.descrizioneDettagliata}"/></td>
                                <td><c:out value="${accessorio.dimensioni}"/></td>
                                <td><c:out value="${accessorio.immagine}"/></td>
                                <td><c:out value="${accessorio.categoria}"/></td>
                                <td><fmt:formatDate value="${accessorio.dataInserimento}" pattern="dd/MM/yyyy HH:mm"/></td>
                                <td class="actions">
                                    <a href="${pageContext.request.contextPath}/admin/ModificaAccessorioServlet?id=${accessorio.accessorio_id}" class="button edit-button">Modifica</a>
                                    <form action="${pageContext.request.contextPath}/admin/ListaAccessoriServlet" method="get" class="delete-form" onsubmit="return confirm('Sei sicuro di voler eliminare l\\'accessorio ${accessorio.nome}?');">
                                        <input type="hidden" name="action" value="delete"> 
                                        <input type="hidden" name="id" value="${accessorio.accessorio_id}">
                                        <button type="submit" class="button delete-button">Elimina</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <p>Nessun accessorio trovato nel database.</p>
            </c:otherwise>
        </c:choose>
        
		 <p>Debug: ${fn:length(listaAccessori)} accessori caricati.</p>
 
        <div class="back-link">
            <a href="${pageContext.request.contextPath}/admin/AdminHome.jsp">Torna alla Dashboard Amministratore</a>
        </div>
    </div>
</body>
</html>
