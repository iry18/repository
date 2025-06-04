<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

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
            <div class="error-messages message error">
                <p>Si sono verificati i seguenti errori:</p>
                <ul>
                    <c:forEach var="errore" items="${erroriInserimento}">
                        <li>${errore}</li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/admin/pianteServlet" method="post">
            <%-- Campo HIDDEN per l'ID in modalità modifica --%>
            <c:if test="${modalita == 'modifica' and not empty pianta.id}">
                <input type="hidden" name="id" value="${pianta.id}">
                <input type="hidden" name="action" value="aggiorna"> <%-- Specifica l'azione per la modifica --%>
            </c:if>
            <c:if test="${modalita == 'inserisci'}">
                <input type="hidden" name="action" value="inserisci"> <%-- Specifica l'azione per l'inserimento --%>
            </c:if>

            <div class="form-group">
                <label for="nomeComune">Nome Comune:</label>
                <input type="text" id="nomeComune" name="nomeComune" required
                       value="<c:if test="${modalita == 'modifica'}">${pianta.nomeComune}</c:if>">
            </div>
            <div class="form-group">
                <label>Tipo di Pianta:</label><br>
                <input type="radio" id="tipoInterno" name="tipo" value="0"
                       <c:if test="${(modalita == 'modifica' && (pianta.tipo == false || empty pianta.tipo)) || modalita == 'inserisci'}">checked</c:if>> <%-- Checked di default per 'Interno' in inserimento o se modifica e è interno --%>
                <label for="tipoInterno">Interno</label>

                <input type="radio" id="tipoEsterno" name="tipo" value="1"
                       <c:if test="${modalita == 'modifica' && pianta.tipo == true}">checked</c:if>>
                <label for="tipoEsterno">Esterno</label>
            </div>
            <div class="form-group">
                <label for="NomeScientificoBotanico">Nome Scientifico:</label>
                <input type="text" id="NomeScientificoBotanico" name="NomeScientificoBotanico"
                       value="<c:if test="${modalita == 'modifica'}">${pianta.nomeScientificoBotanico}</c:if>">
            </div>
            <div class="form-group">
                <label for="Categoria">Categoria:</label>
                <input type="text" id="Categoria" name="Categoria"
                       value="<c:if test="${modalita == 'modifica'}">${pianta.categoria}</c:if>">
            </div>
            <div class="form-group">
                <label for="DescrizioneBreve">Descrizione Breve:</label>
                <input type="text" id="DescrizioneBreve" name="DescrizioneBreve"
                       value="<c:if test="${modalita == 'modifica'}">${pianta.descrizioneBreve}</c:if>">
            </div>
            <div class="form-group">
                <label for="DescrizioneDettagliata">Descrizione Dettagliata:</label>
                <textarea id="DescrizioneDettagliata" name="DescrizioneDettagliata"><c:if test="${modalita == 'modifica'}"><c:out value="${pianta.descrizioneDettagliata}"/></c:if></textarea>
            </div>
            <div class="form-group">
                <label for="EsposizioneLuminosa">Esposizione:</label>
                <input type="text" id="EsposizioneLuminosa" name="EsposizioneLuminosa"
                       value="<c:if test="${modalita == 'modifica'}">${pianta.esposizioneLuminosa}</c:if>">
            </div>
            <div class="form-group">
                <label for="TipoDiTerreno">Tipo di Terreno:</label>
                <input type="text" id="TipoDiTerreno" name="TipoDiTerreno"
                       value="<c:if test="${modalita == 'modifica'}">${pianta.tipoDiTerreno}</c:if>">
            </div>
            <div class="form-group">
                <label for="TemperaturaIdeale">Temperatura Ideale:</label>
                <input type="text" id="TemperaturaIdeale" name="TemperaturaIdeale"
                       value="<c:if test="${modalita == 'modifica'}">${pianta.temperaturaIdeale}</c:if>">
            </div>
            <div class="form-group">
                <label for="FrequenzaIrrigazione">Frequenza Irrigazione:</label>
                <input type="text" id="FrequenzaIrrigazione" name="FrequenzaIrrigazione"
                       value="<c:if test="${modalita == 'modifica'}">${pianta.frequenzaIrrigazione}</c:if>">
            </div>
            <div class="form-group">
                <label for="Prezzo">Prezzo:</label>
                <input type="text" id="Prezzo" name="Prezzo" required
                       value="<c:if test="${modalita == 'modifica'}"><fmt:formatNumber value="${pianta.prezzo}" pattern="0.00"/></c:if>">
            </div>
            <div class="form-group">
                <label for="Disponibilita">Disponibilità:</label>
                <input type="text" id="Disponibilita" name="Disponibilita"
                       value="<c:if test="${modalita == 'modifica'}">${pianta.disponibilita}</c:if>">
            </div>
            <div class="form-group">
                <label for="data_inserimento">Data Inserimento (yyyy-MM-dd HH:mm:ss):</label>
                <input type="text" id="data_inserimento" name="data_inserimento"
                       value="<c:if test="${modalita == 'modifica'}"><fmt:formatDate value="${pianta.data_inserimento}" pattern="yyyy-MM-dd HH:mm:ss"/></c:if>">
            </div>

            <%-- INIZIO NUOVO CAMPO IMMAGINE --%>
            <div class="form-group">
                <label for="immagine">URL Immagine:</label>
                <input type="text" id="immagine" name="immagine"
                       value="<c:if test="${modalita == 'modifica'}">${pianta.immagine}</c:if>">
                <small class="form-text text-muted">Inserisci l'URL completo o il percorso relativo dell'immagine.</small>
            </div>
            <%-- FINE NUOVO CAMPO IMMAGINE --%>

            <div class="form-actions"> <%-- Nuovo div per i pulsanti --%>
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
            <a href="${pageContext.request.contextPath}/admin/listapianteServlet">Torna all'elenco piante</a>
        </div>
    </div>
</body>
</html>