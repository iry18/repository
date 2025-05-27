<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dettagli Utente - <c:out value="${utente.nome}"/> <c:out value="${utente.cognome}"/></title>
    <link rel="stylesheet" href="dettaglioutente.css">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;600;700&display=swap" rel="stylesheet">
</head>

<body>
    <div class="container">
        <h1>Dettagli Utente: <c:out value="${utente.nome}"/> <c:out value="${utente.cognome}"/></h1>

        <c:if test="${not empty messaggio}">
            <div class="message <c:out value="${tipoMessaggio}"/>">
                <p><c:out value="${messaggio}"/></p>
            </div>
        </c:if>

        <c:if test="${not empty utente}">
            <div class="user-details-section">
                <h2>Informazioni Utente</h2>
                <p><strong>ID Utente:</strong> <c:out value="${utente.id}"/></p>
                <p><strong>Nome:</strong> <c:out value="${utente.nome}"/></p>
                <p><strong>Cognome:</strong> <c:out value="${utente.cognome}"/></p>
                <p><strong>Email:</strong> <c:out value="${utente.email}"/></p>
                <p><strong>Amministratore:</strong>
                    <c:choose>
                        <c:when test="${utente.isAdmin}">Sì</c:when> <%-- Assicurati che la classe registrazione abbia un metodo getIsAdmin() --%>
                        <c:otherwise>No</c:otherwise>
                    </c:choose>
                </p>
                <p><strong>Indirizzo:</strong> <c:out value="${utente.indirizzo}"/></p>
                <p><strong>Città:</strong> <c:out value="${utente.citta}"/></p>
                <p><strong>CAP:</strong> <c:out value="${utente.CAP}"/></p>
                <p><strong>Telefono:</strong> <c:out value="${utente.telefono}"/></p>
                <p><strong>Data Registrazione:</strong> <fmt:formatDate value="${utente.data_registrazione}" pattern="dd/MM/yyyy HH:mm:ss"/></p>
            </div>

            <div class="action-buttons">
                <%-- Link per modificare l'utente. Assicurati che esista una servlet per la modifica --%>
                <a href="${pageContext.request.contextPath}/admin/modificautenteServlet?id=<c:out value="${utente.id}"/>" class="button edit-button">Modifica Profilo</a>
                <%-- Link per eliminare l'utente (solo per admin, con conferma) --%>
                <a href="${pageContext.request.contextPath}/admin/eliminautenteServlet?id=<c:out value="${utente.id}"/>" class="button delete-button" onclick="return confirm('Sei sicuro di voler eliminare questo utente?');">Elimina Utente</a>
            </div>

            <div class="orders-section">
                <h2>Ordini Effettuati</h2>
                <c:if test="${not empty ordiniUtente}">
                    <table>
                        <thead>
                            <tr>
                                <th>ID Ordine</th>
                                <th>Data Ordine</th>
                                <th>Totale</th>
                                <th>Stato</th>
                                <th>Dettagli</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="ordine" items="${ordiniUtente}">
                                <tr>
                                    <td><c:out value="${ordine.id}" /></td>
                                    <td><fmt:formatDate value="${ordine.dataOrdine}" pattern="dd/MM/yyyy HH:mm"/></td>
                                    <td><fmt:formatNumber value="${ordine.totale}" type="currency" currencySymbol="€"/></td>
                                    <td><c:out value="${ordine.stato}" /></td>
                                    <td><a href="${pageContext.request.contextPath}/admin/dettaglioordineServlet?id=<c:out value="${ordine.id}"/>">Visualizza</a></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:if>
                <c:if test="${empty ordiniUtente}">
                    <p>Nessun ordine trovato per questo utente.</p>
                </c:if>
            </div>
        </c:if>
        <c:if test="${empty utente && empty messaggio}">
            <p>Nessun utente trovato o selezionato per la visualizzazione.</p>
        </c:if>

        <div class="back-links">
            <a href="${pageContext.request.contextPath}/index.jsp" class="button back-button">Torna alla Home</a>
            <a href="${pageContext.request.contextPath}/admin/utentiServlet" class="button back-button">Torna all'Elenco Utenti</a>
        </div>
    </div>
</body>
</html>