<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%-- Non sono necessari taglib specifici per funzioni in questo caso, a meno che non ti servano --%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Catalogo delle Piante - La Teca del Giardiniere</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #e9ecef;
            color: #333;
            margin: 0;
            padding: 20px;
            display: flex;
            flex-direction: column;
            align-items: center;
        }
        .container {
            width: 100%;
            max-width: 1200px;
            background-color: #fff;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            text-align: center;
        }
        h1 {
            color: #28a745; /* Verde giardiniere */
            margin-bottom: 25px;
            font-size: 2.5em;
        }
        .plant-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
            gap: 25px;
            justify-content: center;
            margin-top: 30px;
        }
        .plant-card {
            background-color: #f8f9fa;
            border: 1px solid #dee2e6;
            border-radius: 8px;
            padding: 20px;
            text-align: left;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
            transition: transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
        }
        .plant-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);
        }
        .plant-card img {
            max-width: 100%;
            height: 200px; /* Altezza fissa per le immagini */
            object-fit: cover; /* Mantiene le proporzioni e taglia se necessario */
            border-radius: 5px;
            margin-bottom: 15px;
            display: block; /* Rimuove lo spazio extra sotto l'immagine */
            margin-left: auto;
            margin-right: auto;
        }
        .plant-card h2 {
            color: #333;
            font-size: 1.5em;
            margin-top: 0;
            margin-bottom: 10px;
        }
        .plant-card h3 {
            color: #555;
            font-style: italic;
            font-size: 1em;
            margin-bottom: 10px;
        }
        .plant-card p {
            font-size: 0.9em;
            line-height: 1.6;
            color: #666;
            margin-bottom: 8px;
        }
        .plant-card .price {
            font-size: 1.4em;
            font-weight: bold;
            color: #007bff;
            margin-top: 15px;
            margin-bottom: 10px;
        }
        .plant-card .availability {
            font-size: 0.9em;
            font-weight: bold;
            color: #28a745; /* Verde per disponibile */
            margin-bottom: 5px;
        }
        .plant-card .out-of-stock {
            color: #dc3545; /* Rosso per esaurito */
        }
        .no-plants-message {
            font-size: 1.2em;
            color: #777;
            margin-top: 50px;
        }
        .error-message {
            padding: 15px;
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
            border-radius: 5px;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Il Nostro Catalogo di Piante</h1>

        <c:if test="${not empty requestScope.messaggioErrore}">
            <div class="error-message">
                <p>${requestScope.messaggioErrore}</p>
            </div>
        </c:if>

        <c:if test="${empty listaPiante}">
            <p class="no-plants-message">Al momento non ci sono piante disponibili nel catalogo.</p>
        </c:if>

        <c:if test="${not empty listaPiante}">
            <div class="plant-grid">
                <c:forEach var="pianta" items="${listaPiante}">
                    <div class="plant-card">
                        <%-- Se hai un percorso per l'immagine nel tuo oggetto Piante --%>
                        <c:if test="${not empty pianta.immagine}">
                            <img src="${pageContext.request.contextPath}/images/${pianta.immagine}" alt="${pianta.nomeComune}">
                        </c:if>
                        <%-- Altrimenti, usa un'immagine segnaposto --%>
                        <c:if test="${empty pianta.immagine}">
                            <img src="${pageContext.request.contextPath}/images/placeholder.png" alt="Immagine non disponibile">
                        </c:if>

                        <h2><c:out value="${pianta.nomeComune}"/></h2>
                        <h3><em><c:out value="${pianta.nomeScientificoBotanico}"/></em></h3>
                        <p><strong>Tipo:</strong> <c:out value="${pianta.tipo}"/></p>
                        <p><strong>Categoria:</strong> <c:out value="${pianta.categoria}"/></p>
                        <p><strong>Descrizione:</strong> <c:out value="${pianta.descrizioneBreve}"/></p>
                        
                        <div class="price">
                            <fmt:formatNumber value="${pianta.prezzo}" type="currency" currencySymbol="€"/>
                        </div>
                        
                        <div class="availability">
                            <c:choose>
                                <c:when test="${pianta.disponibilita != null && pianta.disponibilita > 0}">
                                    Disponibilità: <c:out value="${pianta.disponibilita}"/> unità
                                </c:when>
                                <c:otherwise>
                                    <span class="out-of-stock">Esaurito</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <%-- Puoi aggiungere un link per i dettagli della pianta o per aggiungerla al carrello qui --%>
                        <%-- <a href="${pageContext.request.contextPath}/dettagliPianta?id=${pianta.id}" class="details-button">Dettagli</a> --%>
                    </div>
                </c:forEach>
            </div>
        </c:if>
    </div>
</body>
</html>