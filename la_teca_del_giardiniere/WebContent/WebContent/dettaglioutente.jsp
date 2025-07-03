<%--
    Pagina per visualizzare i dettagli di un singolo utente.
    Può essere usata per:
    - Visualizzare il profilo dell'utente loggato.
    - Permettere a un amministratore di visualizzare i dettagli di un utente specifico (e potenzialmente modificarli o eliminarli).
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%> <%-- Cambiato a UTF-8 per migliore compatibilità --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> <%-- AGGIUNTA QUESTA LINEA PER FORMATTARE LE DATE --%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dettagli Utente - ${utente.nome} ${utente.cognome}</title>
    <%-- Assicurati che il percorso del CSS sia corretto rispetto alla posizione di questa JSP --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/dettaglioutente.css">
</head>
<body>
    <div class="container">
        <h1>Dettagli Utente</h1>

        <%-- Messaggi di successo/errore (opzionale, se li gestisci tramite request attributes) --%>
        <c:if test="${not empty requestScope.messaggio}">
            <p class="message ${requestScope.tipoMessaggio}">${requestScope.messaggio}</p>
        </c:if>

        <c:if test="${not empty utente}">
            <div class="user-details">
                <p><strong>ID:</strong> ${utente.id}</p>
                <p><strong>Nome:</strong> ${utente.nome}</p>
                <p><strong>Cognome:</strong> ${utente.cognome}</p>
                <p><strong>Email:</strong> ${utente.email}</p>
                <p><strong>Amministratore:</strong> <c:choose><c:when test="${utente.admin}">Sì</c:when><c:otherwise>No</c:otherwise></c:choose></p> <%-- Usato utente.admin per proprietà booleana --%>
                <p><strong>Indirizzo:</strong> ${utente.indirizzo}</p>
                <p><strong>Città:</strong> ${utente.citta}</p>
                <p><strong>CAP:</strong> ${utente.CAP}</p>
                <p><strong>Telefono:</strong> ${utente.telefono}</p>
                <p><strong>Provincia:</strong> ${utente.provincia}</p> <%-- Aggiunta visualizzazione provincia --%>
                <p><strong>Data Registrazione:</strong> <fmt:formatDate value="${utente.data_registrazione}" pattern="dd/MM/yyyy HH:mm:ss"/></p>
            </div>

            <div class="action-buttons">
                <%-- Assicurati che 'utenteServlet' o 'ModificaUtenteServlet' gestisca l'azione di modifica --%>
                <a href="${pageContext.request.contextPath}/ModificaUtenteServlet?id=${utente.id}" class="button edit-button">Modifica Profilo</a>

                <%-- Solo se l'utente loggato è un amministratore può eliminare altri utenti --%>
                <%-- Assumiamo che l'utente loggato sia nell'attributo di sessione "loggedInUser" e che abbia un metodo isAdmin() --%>
                <c:if test="${sessionScope.loggedInUser != null && sessionScope.loggedInUser.admin}"> <%-- Controllo corretto per admin --%>
                    <a href="${pageContext.request.contextPath}/EliminaUtenteServlet?id=${utente.id}" class="button delete-button" onclick="return confirm('Sei sicuro di voler eliminare questo utente?');">Elimina Utente</a>
                </c:if>
            </div>

            <%-- Sezione per mostrare gli ordini dell'utente (solo se sono stati passati dal servlet) --%>
            <c:if test="${not empty ordiniUtente}">
                <h3>Ordini Effettuati</h3>
                <table border="1">
                    <thead>
                        <tr>
                            <th>ID Ordine</th>
                            <th>Data Ordine</th>
                            <th>Stato</th>
                            <th>Prezzo Totale</th>
                            <th>Dettagli</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="ordine" items="${ordiniUtente}">
                            <tr>
                                <td>${ordine.id}</td>
                                <td><fmt:formatDate value="${ordine.dataOrdine}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                                <td>${ordine.stato}</td>
                                <td>${ordine.prezzoTotale} €</td>
                                <td><a href="${pageContext.request.contextPath}/DettaglioOrdineServlet?id=${ordine.id}">Vedi Dettagli</a></td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty ordiniUtente}">
                            <tr><td colspan="5">Nessun ordine trovato per questo utente.</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </c:if>
            <c:if test="${empty ordiniUtente && utente != null}">
                <p>Nessun ordine trovato per questo utente.</p>
            </c:if>

        </c:if>
        <c:if test="${empty utente}">
            <p>Nessun utente trovato o selezionato. Assicurati di aver fornito un ID utente valido.</p>
        </c:if>

        <div class="back-link">
            <a href="${pageContext.request.contextPath}/homepage.jsp">Torna alla Home</a>
            <%-- Questo link dovrebbe reindirizzare a una servlet che lista gli utenti, se presente --%>
            <a href="${pageContext.request.contextPath}/VisualizzaUtentiServlet">Torna all'elenco Utenti</a>
        </div>
    </div>
</body>
</html>