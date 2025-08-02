<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>La Teca del Giardiniere</title>
    <link rel="stylesheet" href="homepage.css">
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:wght@400;700&family=Montserrat:wght@400;700&display=swap" rel="stylesheet">
</head>
<body>
    <header>
        <div class="header-container">
            <div class="logo-area">
                <a href="<%= request.getContextPath() %>/homepage">
                    <img src="<%= request.getContextPath() %>/images/logo_small.png" alt="La Teca del Giardiniere Logo" class="site-logo">
                </a>
            </div>
            <nav>
                <ul>
                    <li><a href="<%= request.getContextPath() %>/homepage">HOME</a></li>
                    <li><a href="<%= request.getContextPath() %>/Piantedainterno.jsp">PIANTE INTERNO</a></li>
                    <li><a href="<%= request.getContextPath() %>/Piantedaesterno.jsp">PIANTE ESTERNO</a></li>
                    <li><a href="<%= request.getContextPath() %>/Accessori.jsp">ACCESSORI</a></li>
                    <li><a href="<%= request.getContextPath() %>/Carrello.jsp">CARRELLO</a></li>
                    <li><a href="<%= request.getContextPath() %>/userlogged/MyAccount.jsp">Account</a></li>
                </ul>
            </nav>
        </div>
    </header>

    <div class="main-content-wrapper">
        <div class="brand-section">
            <h1 class="brand-title">LA TECA DEL GIARDINIER_E</h1>
            <p class="brand-slogan">COMFORT WITH PLANT-BASED INGREDIENTS AND LOTS OF LOVE</p>
        </div>

        <section class="highlight-plants-section">
            <div class="plants-grid">
                <c:forEach items="${listaPianteInEvidenza}" var="plant">
                    <div class="plant-highlight-card">
                        <img src="<%= request.getContextPath() %>/images/${plant.immagine}" alt="${plant.nomeComune}">
                        <div class="plant-info">
                            <h3 class="scientific-name">${plant.nomeScientificoBotanico}</h3>
                            <p class="common-name">(${plant.nomeComune})</p>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </section>

        <section class="secret-plants-section">
            <h2 class="secret-title">il segreto delle piante</h2>
            <p>"SEI UN 'KILLER DI PIANTE'? NON PREOCCUPARTI. TI CAPIAMO! <br> MA ABBIAMO UNA BUONA NOTIZIA: ESISTONO PIANTE CHE SOPRAVVIVONO ANCHE AI POLLICI NERI PIÙ OSTEINATI!"</p>
        </section>

        <section class="category-links-section">
            <div class="category-card">
                <a href="Piantedainterno.jsp">
                    <img src="<%= request.getContextPath() %>/images/piante-interno-icon.png" alt="Piante da Interno">
                    <h3>PIANTE DA INTERNO</h3>
                </a>
            </div>
            <div class="category-card">
                <a href="Accessori.jsp">
                    <img src="<%= request.getContextPath() %>/images/accessori-icon.png" alt="Accessori">
                    <h3>ACCESSORI</h3>
                </a>
            </div>
            <div class="category-card">
                <a href="Piantedaesterno.jsp">
                    <img src="<%= request.getContextPath() %>/images/piante-esterno-icon.png" alt="Piante da Esterno">
                    <h3>PIANTE DA ESTERNO</h3>
                </a>
            </div>
        </section>

        <div class="full-width-section pine-loricato">
            <div class="content-section">
                <div class="pine-text-content">
                    <h2>Il pino loricato <span class="scientific-name">(Pinus heldreichii)</span></h2>
                    <p>Questi pini sono resistenti a condizioni ambientali estreme, con inverni rigidi, forti venti e scarsità di suolo.</p>
                    <p>La loro chioma è spesso irregolare e contorta a causa delle avversità, conferendo loro un aspetto scultoreo e affascinante.</p>
                    <p>Sono un simbolo di resilienza e adattamento, e rappresentano un elemento distintivo del paesaggio montano appenninico.</p>
                </div>
                <div class="pine-image-container">
                    <img src="<%= request.getContextPath() %>/images/pino-loricato.png" alt="Pino Loricato">
                </div>
            </div>
        </div>

        <div class="full-width-section modular-vases">
            <div class="content-section">
                <div class="modular-vases-image">
                    <img src="<%= request.getContextPath() %>/images/drenaggio-intelligente.png" alt="Vaso Modulare">
                </div>
                <div class="modular-vases-content">
                    <h2>Innovativo Sistema di Vasi Modulari per un Rinvaso Semplice e un Drenaggio Ottimale</h2>
                    <p>Rivoluziona il modo in cui ti prendi cura delle tue piante con il nostro esclusivo sistema di vasi modulari. Progettati pensando alla salute delle tue piante e alla tua comodità, questi vasi presentano un design intelligente con una base inferiore staccabile.</p>
                    <p>Rinvaso Facile e Senza Stress: Dì addio a rinvasi difficili e disordinati! Grazie alla base inferiore removibile, potrai estrarre delicatamente la tua pianta con il suo pane di terra senza danneggiare le radici o spargere terriccio ovunque. Questo sistema rende il cambio di vaso un'operazione semplice e veloce, ideale per piante in crescita che necessitano di spazio aggiuntivo o per rinnovare il terriccio.</p>
                </div>
            </div>
        </div>
    </div>

    <footer>
        <p>&copy; 2025 La Teca del Giardiniere. Tutti i diritti riservati.</p>
    </footer>
</body>
</html>