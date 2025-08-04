<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="FormInserimentoPiante.css">
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

        <!-- Messaggi di errore -->
        <c:if test="${not empty erroriModifica}">
            <div class="error-messages message error">
                <p>Si sono verificati i seguenti errori:</p>
                <ul>
                    <c:forEach var="errore" items="${erroriModifica}">
                        <li>${errore}</li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>

        <!-- Messaggi generali -->
        <c:if test="${not empty messaggio}">
            <div class="message ${tipoMessaggio}">
                <p>${messaggio}</p>
            </div>
        </c:if>

        <form action="<c:choose>
                         <c:when test='${modalita == "modifica"}'>${pageContext.request.contextPath}/admin/ModificaPiantaServlet</c:when>
                         <c:otherwise>${pageContext.request.contextPath}/admin/AggiungiPiantaServlet</c:otherwise>
                     </c:choose>"
              method="post">

            <c:if test="${modalita == 'modifica'}">
                <input type="hidden" name="id" value="${pianta.id}" />
                <input type="hidden" name="action" value="update" />
            </c:if>
            <c:if test="${modalita != 'modifica'}">
                <input type="hidden" name="action" value="add" />
            </c:if>

            <div class="form-group">
                <label for="nomeComune">Nome Comune:</label>
                <input type="text" id="nomeComune" name="nomeComune" 
                       value="${pianta.nomeComune != null ? pianta.nomeComune : ''}" required>
            </div>

            <div class="form-group">
                <label>Tipo di Pianta:</label><br>
                <input type="radio" id="tipoInterno" name="tipo" value="interno"
                       <c:if test="${pianta.tipo == null || pianta.tipo == 'interno'}">checked</c:if>>
                <label for="tipoInterno">Interno</label>

                <input type="radio" id="tipoEsterno" name="tipo" value="esterno"
                       <c:if test="${pianta.tipo == 'esterno'}">checked</c:if>>
                <label for="tipoEsterno">Esterno</label>
            </div>

            <div class="form-group">
                <label for="nomeScientificoBotanico">Nome Scientifico:</label>
                <input type="text" id="nomeScientificoBotanico" name="nomeScientificoBotanico"
                       value="${pianta.nomeScientificoBotanico != null ? pianta.nomeScientificoBotanico : ''}">
            </div>

            <div class="form-group">
                <label for="categoria">Categoria:</label>
                <input type="text" id="categoria" name="categoria"
                       value="${pianta.categoria != null ? pianta.categoria : ''}">
            </div>

            <div class="form-group">
                <label for="descrizioneBreve">Descrizione Breve:</label>
                <textarea id="descrizioneBreve" name="descrizioneBreve">${pianta.descrizioneBreve != null ? pianta.descrizioneBreve : ''}</textarea>
            </div>

            <div class="form-group">
                <label for="descrizioneDettagliata">Descrizione Dettagliata:</label>
                <textarea id="descrizioneDettagliata" name="descrizioneDettagliata">${pianta.descrizioneDettagliata != null ? pianta.descrizioneDettagliata : ''}</textarea>
            </div>

            <div class="form-group">
                <label for="esposizioneLuminosa">Esposizione Luminosa:</label>
                <input type="text" id="esposizioneLuminosa" name="esposizioneLuminosa"
                       value="${pianta.esposizioneLuminosa != null ? pianta.esposizioneLuminosa : ''}">
            </div>

            <div class="form-group">
                <label for="tipoDiTerreno">Tipo di Terreno:</label>
                <input type="text" id="tipoDiTerreno" name="tipoDiTerreno"
                       value="${pianta.tipoDiTerreno != null ? pianta.tipoDiTerreno : ''}">
            </div>

            <div class="form-group">
                <label for="temperaturaIdeale">Temperatura Ideale:</label>
                <input type="text" id="temperaturaIdeale" name="temperaturaIdeale"
                       value="${pianta.temperaturaIdeale != null ? pianta.temperaturaIdeale : ''}">
            </div>

            <div class="form-group">
                <label for="frequenzaIrrigazione">Frequenza Irrigazione:</label>
                <input type="text" id="frequenzaIrrigazione" name="frequenzaIrrigazione"
                       value="${pianta.frequenzaIrrigazione != null ? pianta.frequenzaIrrigazione : ''}">
            </div>

            <div class="form-group">
                <label for="prezzo">Prezzo (€):</label>
                <input type="number" step="0.01" id="prezzo" name="prezzo" required
                       value="${pianta.prezzo != null ? pianta.prezzo : ''}">
            </div>

            <div class="form-group">
                <label for="disponibilita">Quantità Disponibile:</label>
                <input type="number" id="disponibilita" name="disponibilita"
                       value="${pianta.disponibilita != null ? pianta.disponibilita : ''}">
            </div>

            <div class="form-group">
                <label for="immagine">URL Immagine:</label>
                <input type="text" id="immagine" name="immagine"
                       value="${pianta.immagine != null ? pianta.immagine : ''}">
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
           <a href="${pageContext.request.contextPath}/admin/ListaPianteServlet?action=list">Torna all'elenco piante</a>
        </div>
    </div>
</body>
</html>
