<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> <%-- Importa per la formattazione della data --%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="forminserimentopiante.css">
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
        <c:if test="${not empty erroriInserimento}">
            <div class="error-messages"> <%-- Aggiunta una classe per stilizzare --%>
                <p>Si sono verificati i seguenti errori:</p>
                <ul>
                    <c:forEach var="errore" items="${erroriInserimento}">
                        <li>${errore}</li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>

        <form action="pianteServlet" method="post">
            <%-- Campo HIDDEN per l'ID in modalità modifica --%>
            <c:if test="${modalita == 'modifica' and not empty pianta.id}">
                <input type="hidden" name="id" value="${pianta.id}">
            </c:if>

            <div class="form-group">
                <label for="nomeComune">Nome Comune:</label>
                <input type="text" id="nomeComune" name="nomeComune" required
                       value="${pianta.nomeComune}"> <%-- Pre-popola --%>
            </div>
            <div class="form-group">
                <label>Tipo di Pianta:</label><br>
                <input type="radio" id="tipoInterno" name="tipo" value="0"
                       <c:if test="${pianta.tipo == false || empty pianta.tipo}">checked</c:if>> <%-- Pre-popola (default interno) --%>
                <label for="tipoInterno">Interno</label>

                <input type="radio" id="tipoEsterno" name="tipo" value="1"
                       <c:if test="${pianta.tipo == true}">checked</c:if>> <%-- Pre-popola --%>
                <label for="tipoEsterno">Esterno</label>
            </div>
            <div class="form-group">
                <label for="NomeScientificoBotanico">Nome Scientifico:</label>
                <input type="text" id="NomeScientificoBotanico" name="NomeScientificoBotanico"
                       value="${pianta.nomeScientificoBotanico}"> <%-- Pre-popola --%>
            </div>
            <div class="form-group">
                <label for="Categoria">Categoria:</label>
                <input type="text" id="Categoria" name="Categoria"
                       value="${pianta.categoria}"> <%-- Pre-popola --%>
            </div>
            <div class="form-group">
                <label for="DescrizioneBreve">Descrizione Breve:</label>
                <input type="text" id="DescrizioneBreve" name="DescrizioneBreve"
                       value="${pianta.descrizioneBreve}"> <%-- Pre-popola --%>
            </div>
            <div class="form-group">
                <label for="DescrizioneDettagliata">Descrizione Dettagliata:</label>
                <textarea id="DescrizioneDettagliata" name="DescrizioneDettagliata"><c:out value="${pianta.descrizioneDettagliata}"/></textarea> <%-- Pre-popola --%>
            </div>
            <div class="form-group">
                <label for="EsposizioneLuminosa">Esposizione:</label>
                <input type="text" id="EsposizioneLuminosa" name="EsposizioneLuminosa"
                       value="${pianta.esposizioneLuminosa}"> <%-- Pre-popola --%>
            </div>
            <div class="form-group">
                <label for="TipoDiTerreno">Tipo di Terreno:</label>
                <input type="text" id="TipoDiTerreno" name="TipoDiTerreno"
                       value="${pianta.tipoDiTerreno}"> <%-- Pre-popola --%>
            </div>
            <div class="form-group">
                <label for="TemperaturaIdeale">Temperatura Ideale:</label>
                <input type="text" id="TemperaturaIdeale" name="TemperaturaIdeale"
                       value="${pianta.temperaturaIdeale}"> <%-- Pre-popola --%>
            </div>
            <div class="form-group">
                <label for="FrequenzaIrrigazione">Frequenza Irrigazione:</label>
                <input type="text" id="FrequenzaIrrigazione" name="FrequenzaIrrigazione"
                       value="${pianta.frequenzaIrrigazione}"> <%-- Pre-popola --%>
            </div>
            <div class="form-group">
                <label for="Prezzo">Prezzo:</label>
                <input type="text" id="Prezzo" name="Prezzo" required
                       value="<fmt:formatNumber value="${pianta.prezzo}" pattern="0.00"/>"> <%-- Pre-popola con formattazione --%>
            </div>
            <div class="form-group">
                <label for="Disponibilita">Disponibilità:</label>
                <input type="text" id="Disponibilita" name="Disponibilita"
                       value="${pianta.disponibilita}"> <%-- Pre-popola --%>
            </div>
            <div class="form-group">
                <label for="data_inserimento">Data Inserimento (yyyy-MM-dd HH:mm:ss):</label>
                <input type="text" id="data_inserimento" name="data_inserimento"
                       value="<fmt:formatDate value="${pianta.data_inserimento}" pattern="yyyy-MM-dd HH:mm:ss"/>"> <%-- Pre-popola con formattazione --%>
            </div>
            <button type="submit">
                <c:choose>
                    <c:when test="${modalita == 'modifica'}">Aggiorna Pianta</c:when>
                    <c:otherwise>Inserisci Pianta</c:otherwise>
                </c:choose>
            </button>
        </form>
        <div class="back-link">
            <a href="listapianteServlet">Torna all'elenco piante</a> <%-- Link modificato per tornare alla lista --%>
        </div>
    </div>
</body>
</html>