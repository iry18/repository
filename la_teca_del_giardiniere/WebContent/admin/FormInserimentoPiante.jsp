<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> <%-- Aggiunto per fmt:formatNumber e formatDate --%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/FormInserimentoPiante.css">
    <title>
        <c:choose>
            <c:when test="${modalita == 'modifica'}">Modifica Pianta</c:when>
            <c:otherwise>Inserisci Nuova Pianta</c:otherwise>
        </c:choose>
        - La Teca del Giardiniere
    </title>
</head>

<body>
    <div class="container">
        <h1>
            <c:choose>
                <c:when test="${modalita == 'modifica'}">Modifica Pianta</c:when>
                <c:otherwise>Inserisci una nuova pianta</c:otherwise>
            </c:choose>
        </h1>

        <%-- Visualizzazione dei messaggi di errore --%>
        <c:if test="${not empty erroriModifica}"> <%-- Usa erroriModifica per coerenza con la servlet --%>
            <div class="error-messages message error">
                <p>Si sono verificati i seguenti errori:</p>
                <ul>
                    <c:forEach var="errore" items="${erroriModifica}">
                        <li>${errore}</li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>
        <%-- Visualizzazione dei messaggi generali (successo/errore) --%>
        <c:if test="${not empty messaggio}">
            <div class="message ${tipoMessaggio}">
                <p>${messaggio}</p>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/PianteServlet" method="post"> <%-- L'action punta sempre alla PianteServlet --%>
            <%-- Campo HIDDEN per l'ID in modalità modifica --%>
            <c:if test="${modalita == 'modifica' and not empty pianta.id}">
                <input type="hidden" name="id" value="${pianta.id}">
                <%-- Non è necessario un campo 'action' nascosto, la servlet decide in base all'ID --%>
            </c:if>

            <div class="form-group">
                <label for="nomeComune">Nome Comune:</label>
                <input type="text" id="nomeComune" name="nomeComune" required
                       value="<c:if test="${not empty pianta.nomeComune}">${pianta.nomeComune}</c:if>">
            </div>
            <div class="form-group">
                <label>Tipo di Pianta:</label><br>
                <%-- I valori dei radio button corrispondono alle stringhe "interno" / "esterno" --%>
                <input type="radio" id="tipoInterno" name="tipo" value="interno"
                       <c:if test="${(pianta.tipo == 'interno') || (modalita == 'inserisci' && empty pianta.tipo)}">checked</c:if>>
                <label for="tipoInterno">Interno</label>

                <input type="radio" id="tipoEsterno" name="tipo" value="esterno"
                       <c:if test="${pianta.tipo == 'esterno'}">checked</c:if>>
                <label for="tipoEsterno">Esterno</label>
            </div>
            <div class="form-group">
                <label for="nomeBotanico">Nome Scientifico:</label>
                <input type="text" id="nomeBotanico" name="nomeBotanico"
                       value="<c:if test="${not empty pianta.nomeBotanico}">${pianta.nomeBotanico}</c:if>">
            </div>
            <div class="form-group">
                <label for="categoria">Categoria:</label>
                <input type="text" id="categoria" name="categoria"
                       value="<c:if test="${not empty pianta.categoria}">${pianta.categoria}</c:if>">
            </div>
            <%-- Campo 'descrizione' unificato --%>
            <div class="form-group">
                <label for="descrizione">Descrizione:</label>
                <textarea id="descrizione" name="descrizione"><c:if test="${not empty pianta.descrizione}"><c:out value="${pianta.descrizione}"/></c:if></textarea>
            </div>
            <div class="form-group">
                <label for="esposizioneLuminosa">Esposizione:</label>
                <input type="text" id="esposizioneLuminosa" name="esposizioneLuminosa"
                       value="<c:if test="${not empty pianta.esposizioneLuminosa}">${pianta.esposizioneLuminosa}</c:if>">
            </div>
            <div class="form-group">
                <label for="tipoDiTerreno">Tipo di Terreno:</label>
                <input type="text" id="tipoDiTerreno" name="tipoDiTerreno"
                       value="<c:if test="${not empty pianta.tipoDiTerreno}">${pianta.tipoDiTerreno}</c:if>">
            </div>
            <div class="form-group">
                <label for="temperaturaIdeale">Temperatura Ideale:</label>
                <input type="text" id="temperaturaIdeale" name="temperaturaIdeale"
                       value="<c:if test="${not empty pianta.temperaturaIdeale}">${pianta.temperaturaIdeale}</c:if>">
            </div>
            <div class="form-group">
                <label for="frequenzaIrrigazione">Frequenza Irrigazione:</label>
                <input type="text" id="frequenzaIrrigazione" name="frequenzaIrrigazione"
                       value="<c:if test="${not empty pianta.frequenzaIrrigazione}">${pianta.frequenzaIrrigazione}</c:if>">
            </div>
            <div class="form-group">
                <label for="prezzo">Prezzo:</label>
                <input type="text" id="prezzo" name="prezzo" required
                       value="<c:if test="${not empty pianta.prezzo}"><fmt:formatNumber value="${pianta.prezzo}" pattern="0.00"/></c:if>">
            </div>
            <div class="form-group">
                <label for="quantitaDisponibile">Quantità Disponibile:</label>
                <input type="text" id="quantitaDisponibile" name="quantitaDisponibile"
                       value="<c:if test="${not empty pianta.quantitaDisponibile}">${pianta.quantitaDisponibile}</c:if>">
            </div>

            
            <div class="form-group">
                <label for="urlImmagine">URL Immagine:</label>
                <input type="text" id="urlImmagine" name="urlImmagine"
                       value="<c:if test="${not empty pianta.urlImmagine}">${pianta.urlImmagine}</c:if>">
                <small class="form-text text-muted">Inserisci l'URL completo o il percorso relativo dell'immagine.</small>
            </div>

            <div class="form-actions">
                <c:choose>
                    <c:when test="${modalita == 'modifica'}">
                        <button type="submit" class="button update-button">Aggiorna Pianta</button>
                    </c:when>
                    <c:otherwise>
                        <button type="submit" class="button insert-button">Inserisci Pianta</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </form>
        <div class="back-link">
            <a href="${pageContext.request.contextPath}/ListaPianteServlet">Torna all'elenco piante</a>
        </div>
    </div>
</body>
</html>