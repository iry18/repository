<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dettagli Pianta - ${pianta.nomeComune}</title>
    <link rel="stylesheet" href="paginepiante.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>

<body>
    
    <section class="main-content">
    
    <div class="container-dettagli-pianta">
        <c:choose><c:when test="${pianta != null}">
            <section class="immagine-principale">
                <img src="${pageContext.request.contextPath}/images/${pianta.immagine}" alt="${pianta.nomeComune}">
            </section>

            <section class="dettagli-testuali">
                <h1 class="nome-pianta">${pianta.nomeComune}</h1>
                <p class="nome-scientifico">${pianta.nomeScientificoBotanico}</p>
                <p class="descrizione-breve">${pianta.descrizioneBreve}</p>
                <div class="prezzo-prodotto">
                    <span class="prezzo">€<fmt:formatNumber value="${pianta.prezzo}" pattern="0.00"/></span>
                    <small>IVA inclusa</small>
                </div>

                <form action="${pageContext.request.contextPath}/aggiungiAlCarrello" method="post" class="add-to-cart-form">
                    <input type="hidden" name="idProdotto" value="${pianta.id}">
                    <input type="hidden" name="tipoProdotto" value="pianta">
                    <button type="submit" class="add-to-cart-btn">
                        <i class="fas fa-shopping-cart"></i> Aggiungi al Carrello
                    </button>
                </form>
            </section>

            <section class="caratteristiche-principali">
                <h2>Cure e Caratteristiche</h2>
                <div class="info-list">
                    <div class="info-item">
                        <div class="icon-circle">
                            <i class="fas fa-seedling"></i>
                        </div>
                        <div class="text-container">
                            <h3 class="info-title">DIFFICOLTÀ</h3>
                            <p class="info-details">${pianta.difficolta}</p>
                        </div>
                    </div>

                    <div class="info-item">
                        <div class="icon-circle">
                            <i class="fas fa-sun"></i>
                        </div>
                        <div class="text-container">
                            <h3 class="info-title">ILLUMINAZIONE</h3>
                            <p class="info-details">${pianta.esposizioneLuminosa}</p>
                        </div>
                    </div>

                    <div class="info-item">
                        <div class="icon-circle">
                            <i class="fas fa-thermometer-half"></i>
                        </div>
                        <div class="text-container">
                            <h3 class="info-title">TEMPERATURA IDEALE</h3>
                            <p class="info-details">${pianta.temperaturaIdeale}</p>
                        </div>
                    </div>
                    
                    <div class="info-item">
                        <div class="icon-circle">
                            <i class="fas fa-hand-holding-water"></i>
                        </div>
                        <div class="text-container">
                            <h3 class="info-title">IRRIGAZIONE</h3>
                            <p class="info-details">${pianta.frequenzaIrrigazione}</p>
                        </div>
                    </div>

                    <div class="info-item">
                        <div class="icon-circle">
                            <i class="fas fa-search-location"></i>
                        </div>
                        <div class="text-container">
                            <h3 class="info-title">TERRENO</h3>
                            <p class="info-details">${pianta.tipoDiTerreno}</p>
                        </div>
                    </div>

                    <div class="info-item">
                        <div class="icon-circle">
                            <i class="fas fa-cut"></i>
                        </div>
                        <div class="text-container">
                            <h3 class="info-title">POTATURA</h3>
                            <p class="info-details">${pianta.potatura}</p>
                        </div>
                    </div>

                    <div class="info-item">
                        <div class="icon-circle">
                            <i class="fas fa-spray-can"></i>
                        </div>
                        <div class="text-container">
                            <h3 class="info-title">CONCIMAZIONE</h3>
                            <p class="info-details">${pianta.concimazione}</p>
                        </div>
                    </div>
                    
                    <div class="info-item">
                        <div class="icon-circle">
                            <i class="fas fa-tools"></i>
                        </div>
                        <div class="text-container">
                            <h3 class="info-title">ATTREZZI UTILI</h3>
                            <p class="info-details">${pianta.attrezziUtili}</p>
                        </div>
                    </div>
                    
                    <div class="info-item">
                        <div class="icon-circle">
                            <i class="fas fa-calendar-alt"></i>
                        </div>
                        <div class="text-container">
                            <h3 class="info-title">FREQUENZA LAVORAZIONE</h3>
                            <p class="info-details">${pianta.frequenzaLavorazione}</p>
                        </div>
                    </div>
                </div>
            </section>

            <section class="descrizione-estesa">
                <h2>Approfondimento</h2>
                <p>${pianta.descrizioneDettagliata}</p>
            </section>
        </c:when><c:otherwise>
            <div class="no-plant-found">
                <h1>Pianta non trovata</h1>
                <p>Siamo spiacenti, la pianta che stai cercando non esiste o non è più disponibile.</p>
            </div>
        </c:otherwise></c:choose>

        <section class="consigli-utili">
            <img src="${pageContext.request.contextPath}/images/consigli.png" alt="Consigli Utili per la Cura delle Piante">
        </section>
    </div>
 </section>
 
</body>

</html>