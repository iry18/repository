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
            font-family: Arial, sans-serif;
            background-color: #f0f2f5;
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
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            padding: 30px;
            width: 100%;
            max-width: 1500px;
            text-align: center;
        }
        h1 {
            color: #333;
            margin-bottom: 20px;
            font-size: 2em;
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
            margin-bottom: 20px;
            text-align: right;
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
            transition: background-color 0.3s ease;
            text-align: center;
        }
        .add-button {
            background-color: #28a745;
        }
        .add-button:hover {
            background-color: #218838;
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
            margin-left: 5px;
        }
        .delete-button:hover {
            background-color: #c82333;
        }
        .print-button {
            background-color: #007bff;
        }
        .print-button:hover {
            background-color: #0056b3;
        }
        table {
            width: 90%;
            border-collapse: collapse;
            margin-top: 20px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
            margin-left: auto;
            margin-right: auto;
        }
        thead {
            background-color: #4CAF50;
            color: white;
        }
        th, td {
            padding: 12px 15px;
            text-align: left;
            border-bottom: 1px solid #ddd;
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
        .delete-form {
            display: inline;
        }
        .home-button-container {
            margin-top: 30px;
            text-align: center;
        }
        .home-button {
            display: inline-block;
            padding: 12px 25px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            text-decoration: none;
            font-size: 1em;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }
        .home-button:hover {
            background-color: #0056b3;
        }
        @media print {
            .print-button-container,
            .add-button-container,
            .actions,
            .home-button-container {
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

        **<c:if test="${not empty messaggio}">**
            **<div class="message ${tipoMessaggio}">**
                **<p>${messaggio}</p>**
            **</div>**
        **</c:if>**

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
