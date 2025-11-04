<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
   <link rel="stylesheet" href="FormInserimentoAccessori.css"> 
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

        <form action="${pageContext.request.contextPath}/admin/AggiungiAccessorioServlet" method="post">
            
            <c:if test="${modalita == 'modifica' and not empty accessorio.accessorio_id}">
                <input type="hidden" name="id" value="${accessorio.accessorio_id}">
                <input type="hidden" name="action" value="aggiorna"> 
            </c:if>
            <c:if test="${modalita == 'inserisci'}">
                <input type="hidden" name="action" value="inserisci"> 
            </c:if>

            <div class="form-group">
                <label for="nome">Nome:</label>
                <input type="text" id="nome" name="nome" required
                       value="<c:if test="${modalita == 'modifica'}">${accessorio.nome}</c:if>">
            </div>
            <div class="form-group">
                <label for="prezzo">Prezzo:</label>
                <input type="text" id="prezzo" name="prezzo" required 
                       value="<c:if test="${modalita == 'modifica'}"><fmt:formatNumber value="${accessorio.prezzo}" pattern="0.00"/></c:if>">
            </div>
            <div class="form-group">
                <label for="disponibilita">Disponibilità:</label>
                <input type="number" id="disponibilita" name="disponibilita" required
                       value="<c:if test="${modalita == 'modifica'}">${accessorio.disponibilita}</c:if>">
            </div>
            <div class="form-group">
                <label for="descrizione">DescrizioneBreve:</label>
                <textarea id="descrizione" name="descrizione"><c:if test="${modalita == 'modifica'}"><c:out value="${accessorio.descrizione}"/></c:if></textarea>
            </div>
            
            <div class="form-group">
                <label for="descrizione">DescrizioneDettagliata:</label>
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
            <a href="${pageContext.request.contextPath}/admin/ListaAccessoriServlet">Torna all'elenco accessori</a> <%-- Link corretto alla lista accessori --%>
        </div>
    </div>
</body>
</html>