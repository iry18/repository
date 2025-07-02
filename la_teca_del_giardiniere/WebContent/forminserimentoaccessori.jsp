<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>


<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
   <link rel="stylesheet" href="forminserimentoaccessori.css"> 
   <title>
        <c:choose>
            <c:when test="${modalita == 'modifica'}">Modifica Accessorio</c:when>
            <c:otherwise>Inserisci Nuovo Accessorio</c:otherwise>
        </c:choose>
        - La Teca del Giardiniere
    </title>
</head>

<body>
    <div class="container">
        <h1>
            <c:choose>
                <c:when test="${modalita == 'modifica'}">Modifica Accessorio</c:when>
                <c:otherwise>Inserisci un nuovo accessorio</c:otherwise>
            </c:choose>
        </h1>

        <%-- Visualizzazione dei messaggi di errore/successo --%>
        <c:if test="${not empty requestScope.messaggio}">
            <div class="message ${requestScope.tipoMessaggio}">${requestScope.messaggio}</div>
        </c:if>
        <c:if test="${not empty erroriInserimento}">
            <div class="error-messages message error">
                <p>Si sono verificati i seguenti errori:</p>
                <ul>
                    <c:forEach var="errore" items="${erroriInserimento}">
                        <li>${errore}</li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/admin/accessoriServlet" method="post">
            <%-- Campo HIDDEN per l'ID in modalità modifica --%>
            <c:if test="${modalita == 'modifica' and not empty accessorio.id}">
                <input type="hidden" name="id" value="${accessorio.id}">
                <input type="hidden" name="action" value="aggiorna"> <%-- Specifica l'azione per la modifica --%>
            </c:if>
            <c:if test="${modalita == 'inserisci'}">
                <input type="hidden" name="action" value="inserisci"> <%-- Specifica l'action per l'inserimento --%>
            </c:if>

            <%-- L'ID non dovrebbe essere un campo input editabile in un form di inserimento,
                 generalmente viene generato automaticamente dal DB.
                 Se è per modifica, dovrebbe essere hidden.
                 Lo commento per ora. Se il tuo DB lo richiede inserito manualmente, riabilita. --%>
            <%--
            <div class="form-group">
                <label for="id">ID:</label>
                <input type="number" id="id" name="id" required
                       value="<c:if test="${modalita == 'modifica'}">${accessorio.id}</c:if>">
            </div>
            --%>

            <div class="form-group">
                <label for="nome">Nome:</label>
                <input type="text" id="nome" name="nome" required
                       value="<c:if test="${modalita == 'modifica'}">${accessorio.nome}</c:if>">
            </div>
            <div class="form-group">
                <label for="prezzo">Prezzo:</label>
                <input type="text" id="prezzo" name="prezzo" required <%-- Prezzo quasi sempre richiesto --%>
                       value="<c:if test="${modalita == 'modifica'}"><fmt:formatNumber value="${accessorio.prezzo}" pattern="0.00"/></c:if>">
            </div>
            <div class="form-group">
                <label for="disponibilita">Disponibilità:</label>
                <input type="number" id="disponibilita" name="disponibilita" required <%-- Disponibilità quasi sempre richiesta --%>
                       value="<c:if test="${modalita == 'modifica'}">${accessorio.disponibilita}</c:if>">
            </div>
            <div class="form-group">
                <label for="descrizione">Descrizione:</label>
                <textarea id="descrizione" name="descrizione"><c:if test="${modalita == 'modifica'}"><c:out value="${accessorio.descrizione}"/></c:if></textarea>
            </div>
            <div class="form-group">
                <label for="dimensioni">Dimensioni:</label>
                <input type="text" id="dimensioni" name="dimensioni"
                       value="<c:if test="${modalita == 'modifica'}">${accessorio.dimensioni}</c:if>">
            </div>
            <div class="form-group">
                <label for="data_inserimento">Data Inserimento (yyyy-MM-dd HH:mm:ss):</label>
                <input type="text" id="data_inserimento" name="data_inserimento"
                       value="<c:if test="${modalita == 'modifica'}"><fmt:formatDate value="${accessorio.data_inserimento}" pattern="yyyy-MM-dd HH:mm:ss"/></c:if>">
                <%-- Aggiungere istruzioni o un selettore di data se il campo è manuale, altrimenti impostarlo come readonly o hidden in caso di generazione automatica --%>
            </div>

            <div class="form-actions">
                <c:choose>
                    <c:when test="${modalita == 'modifica'}">
                        <button type="submit" class="button update-button">Aggiorna Accessorio</button>
                    </c:when>
                    <c:otherwise>
                        <button type="submit" class="button insert-button">Inserisci Accessorio</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </form>
        <div class="back-link">
            <a href="${pageContext.request.contextPath}/admin/listaaccessoriServlet">Torna all'elenco accessori</a> <%-- Link corretto alla lista accessori --%>
        </div>
    </div>
</body>
</html>