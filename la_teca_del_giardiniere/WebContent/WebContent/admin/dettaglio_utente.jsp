<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dettagli Utente</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"> <%-- Assicurati che il percorso sia corretto --%>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .container { max-width: 900px; margin: auto; background: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
        h1, h2 { color: #333; border-bottom: 1px solid #eee; padding-bottom: 10px; margin-bottom: 20px; }
        .user-details, .order-list { margin-bottom: 30px; }
        .user-details p, .order-item p { margin: 5px 0; }
        .order-item { border: 1px solid #ddd; padding: 15px; margin-bottom: 15px; border-radius: 5px; }
        .order-item h3 { margin-top: 0; color: #555; }
        .order-details-table { width: 100%; border-collapse: collapse; margin-top: 10px; }
        .order-details-table th, .order-details-table td { border: 1px solid #eee; padding: 8px; text-align: left; }
        .order-details-table th { background-color: #f2f2f2; }
        .no-data { color: #888; font-style: italic; }
        .button-back { display: inline-block; padding: 10px 15px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px; margin-top: 20px; }
        .button-back:hover { background-color: #0056b3; }
        .message.error { color: red; }
        .message.success { color: green; }
    </style>
</head>
<body>
    <div class="container">
        <h1>Dettagli Utente</h1>

        <c:if test="${not empty requestScope.messaggio}">
            <div class="message ${requestScope.tipoMessaggio}">${requestScope.messaggio}</div>
        </c:if>

        <div class="user-details">
            <h2>Informazioni Utente</h2>
            <c:choose>
                <c:when test="${not empty requestScope.utente}">
                    <p><strong>Nome:</strong> ${requestScope.utente.nome}</p>
                    <p><strong>Cognome:</strong> ${requestScope.utente.cognome}</p>
                    <p><strong>Email:</strong> ${requestScope.utente.email}</p>
                    <p><strong>Indirizzo:</strong> ${requestScope.utente.indirizzo}, ${requestScope.utente.citta}, ${requestScope.utente.CAP} (${requestScope.utente.provincia})</p>
                    <p><strong>Telefono:</strong> ${requestScope.utente.telefono}</p>
                    <p><strong>Data Registrazione:</strong> <fmt:formatDate value="${requestScope.utente.data_registrazione}" pattern="dd/MM/yyyy HH:mm:ss" /></p>
                    <p><strong>Ruoli:</strong>
                        <c:if test="${not empty requestScope.utente.ruoli}">
                            <c:forEach var="ruolo" items="${requestScope.utente.ruoli}" varStatus="loop">
                                ${ruolo}<c:if test="${!loop.last}">, </c:if>
                            </c:forEach>
                        </c:if>
                        <c:if test="${empty requestScope.utente.ruoli}">
                            Nessun ruolo assegnato.
                        </c:if>
                    </p>
                </c:when>
                <c:otherwise>
                    <p class="no-data">Nessun dettaglio utente disponibile.</p>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="order-list">
            <h2>Ordini dell'Utente</h2>
            <c:choose>
                <c:when test="${not empty requestScope.ordiniUtente}">
                    <c:forEach var="ordine" items="${requestScope.ordiniUtente}">
                        <div class="order-item">
                            <h3>Ordine #${ordine.ordine_id} - <fmt:formatDate value="${ordine.data_ordine}" pattern="dd/MM/yyyy HH:mm" /></h3>
                            <p><strong>Totale:</strong> <fmt:formatNumber value="${ordine.tot_ordine}" type="currency" currencySymbol="€" maxFractionDigits="2"/></p>
                            <p><strong>Stato:</strong> ${ordine.stato_ordine}</p>
                            <p><strong>Metodo Pagamento:</strong> ${ordine.metodo_pagamento}</p>
                            <p><strong>Spedizione a:</strong> ${ordine.citta_spedizione}, ${ordine.paese_spedizione} (${ordine.CAP_spedizione})</p>
                            <p><strong>Note:</strong> ${ordine.note}</p>

                            <h4>Dettagli Ordine:</h4>
                            <c:choose>
                                <c:when test="${not empty ordine.dettagliOrdine}">
                                    <table class="order-details-table">
                                        <thead>
                                            <tr>
                                                <th>Prodotto</th>
                                                <th>Quantità</th>
                                                <th>Prezzo Unitario</th>
                                                <th>Subtotale</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="dettaglio" items="${ordine.dettagliOrdine}">
                                                <tr>
                                                    <td>${dettaglio.nomeProdotto}</td>
                                                    <td>${dettaglio.quantita}</td>
                                                    <td><fmt:formatNumber value="${dettaglio.prezzo_unitario}" type="currency" currencySymbol="€" maxFractionDigits="2"/></td>
                                                    <td><fmt:formatNumber value="${dettaglio.prezzo_unitario * dettaglio.quantita}" type="currency" currencySymbol="€" maxFractionDigits="2"/></td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </c:when>
                                <c:otherwise>
                                    <p class="no-data">Nessun dettaglio per questo ordine.</p>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p class="no-data">Questo utente non ha ordini.</p>
                </c:otherwise>
            </c:choose>
        </div>

        <a href="${pageContext.request.contextPath}/admin/utentiServlet" class="button-back">Torna alla Lista Utenti</a>
    </div>
</body>
</html>