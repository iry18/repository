<%-- visualizzare i dettagli di un singolo utente. Potrebbe essere usata per:
Visualizzare il profilo dell'utente loggato.
Permettere a un amministratore di visualizzare i dettagli di un utente specifico (e potenzialmente modificarli o eliminarli).--%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dettagli Utente - ${utente.nome} ${utente.cognome}</title>
    <link rel="stylesheet" href="dettaglioutente.css"> 
</head>
<body>
    <div class="container">
        <h1>Dettagli Utente</h1>
        <c:if test="${not empty utente}">
            <div class="user-details">
                <p><strong>ID:</strong> ${utente.id}</p>
                <p><strong>Nome:</strong> ${utente.nome}</p>
                <p><strong>Cognome:</strong> ${utente.cognome}</p>
                <p><strong>Email:</strong> ${utente.email}</p>
                <p><strong>Amministratore:</strong> <c:choose><c:when test="${utente.isAdmin}">Sì</c:when><c:otherwise>No</c:otherwise></c:choose></p>
                <p><strong>Indirizzo:</strong> ${utente.indirizzo}</p>
                <p><strong>Città:</strong> ${utente.citta}</p>
                <p><strong>CAP:</strong> ${utente.CAP}</p>
                <p><strong>Telefono:</strong> ${utente.telefono}</p>
                <p><strong>Data Registrazione:</strong> <fmt:formatDate value="${utente.data_registrazione}" pattern="dd/MM/yyyy HH:mm:ss"/></p>
            </div>

            <div class="action-buttons">
                <a href="utenteServlet?action=modifica&id=${utente.id}" class="button edit-button">Modifica Profilo</a>
                <c:if test="${sessionScope.currentUser.isAdmin}"> <%-- Solo se l'utente loggato è admin --%>
                    <a href="utenteServlet?action=elimina&id=${utente.id}" class="button delete-button" onclick="return confirm('Sei sicuro di voler eliminare questo utente?');">Elimina Utente</a>
                </c:if>
            </div>
        </c:if>
        <c:if test="${empty utente}">
            <p>Nessun utente trovato o selezionato.</p>
        </c:if>
        <div class="back-link">
            <a href="index.jsp">Torna alla Home</a>
            <a href="utentiServlet">Torna all'elenco Utenti</a> <%-- Se esiste una servlet per listare gli utenti --%>
        </div>
    </div>
</body>
</html>