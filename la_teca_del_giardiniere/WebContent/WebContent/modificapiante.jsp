<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Modifica Pianta - La Teca del Giardiniere</title>
    <link rel="stylesheet" href="forminserimentopiante.css"> </head>
<body>
    <div class="container">
        <h1>Modifica Pianta</h1>

        <c:if test="${not empty erroriModifica}">
            <div style="color: red;">
                <ul>
                    <c:forEach var="errore" items="${erroriModifica}">
                        <li>${errore}</li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>
      

        <form action="aggiornapianteServlet" method="post">
            <input type="hidden" name="id" value="${pianta.getId()}"> <div class="form-group">
                <label for="nomeComune">Nome Comune:</label>
                <input type="text" id="nomeComune" name="nomeComune" value="${pianta.getNomeComune()}" required>
            </div>
            <div class="form-group">
                <label>Tipo di Pianta:</label><br>
                <input type="radio" id="tipoInterno" name="tipo" value="0" ${not pianta.isTipo() ? 'checked' : ''}>
                <label for="tipoInterno">Interno</label>
                <input type="radio" id="tipoEsterno" name="tipo" value="1" ${pianta.isTipo() ? 'checked' : ''}>
                <label for="tipoEsterno">Esterno</label>
            </div>
            <div class="form-group">
                <label for="NomeScientificoBotanico">Nome Scientifico:</label>
                <input type="text" id="NomeScientificoBotanico" name="NomeScientificoBotanico" value="${pianta.getNomeScientificoBotanico()}">
            </div>
            <div class="form-group">
                <label for="Categoria">Categoria:</label>
                <input type="text" id="Categoria" name="Categoria" value="${pianta.getCategoria()}">
            </div>
            <div class="form-group">
                <label for="DescrizioneBreve">Descrizione Breve:</label>
                <input type="text" id="DescrizioneBreve" name="DescrizioneBreve" value="${pianta.getDescrizioneBreve()}">
            </div>
            <div class="form-group">
                <label for="DescrizioneDettagliata">Descrizione Dettagliata:</label>
                <textarea id="DescrizioneDettagliata" name="DescrizioneDettagliata">${pianta.getDescrizioneDettagliata()}</textarea>
            </div>
            <div class="form-group">
                <label for="EsposizioneLuminosa">Esposizione:</label>
                <input type="text" id="EsposizioneLuminosa" name="EsposizioneLuminosa" value="${pianta.getEsposizioneLuminosa()}">
            </div>
            <div class="form-group">
                <label for="TipoDiTerreno">Tipo di Terreno:</label>
                <input type="text" id="TipoDiTerreno" name="TipoDiTerreno" value="${pianta.getTipoDiTerreno()}">
            </div>
            <div class="form-group">
                <label for="TemperaturaIdeale">Temperatura Ideale:</label>
                <input type="text" id="TemperaturaIdeale" name="TemperaturaIdeale" value="${pianta.getTemperaturaIdeale()}">
            </div>
            <div class="form-group">
                <label for="FrequenzaIrrigazione">Frequenza Irrigazione:</label>
                <input type="text" id="FrequenzaIrrigazione" name="FrequenzaIrrigazione" value="${pianta.getFrequenzaIrrigazione()}">
            </div>
            <div class="form-group">
                <label for="Prezzo">Prezzo:</label>
                <input type="text" id="Prezzo" name="Prezzo" value="${pianta.getPrezzo()}">
            </div>
            <div class="form-group">
                <label for="Disponibilita">Disponibilità:</label>
                <input type="text" id="Disponibilita" name="Disponibilita" value="${pianta.getDisponibilita()}">
            </div>
            <div class="form-group">
                <label for="data_inserimento">Data Inserimento (yyyy-mm-dd hh:mm:ss):</label>
                <input type="text" id="data_inserimento" name="data_inserimento" value="${pianta.getData_inserimento()}">
            </div>
            <button type="submit">Aggiorna Pianta</button>
        </form>

        <div class="back-link">
            <a href="listapianteServlet">Torna all'Elenco Piante</a>
        </div>
    </div>
</body>
</html>