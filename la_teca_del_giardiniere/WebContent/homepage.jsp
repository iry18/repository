<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>La Teca del Giardiniere</title>
    <link rel="stylesheet" href="homepage.css">
</head>
<body>
    <header>
        <div class="header-container">
            <div class="logo-area">
                <a href="homepage.jsp">
                    <img src="<%= request.getContextPath() %>/images/logo.png" alt="La Teca del Giardiniere Logo" class="site-logo">
                    </a>
            </div>
            <nav>
                <ul>
                    <li><a href="homepage.jsp">HOME</a></li>
                    <li><a href="Piantedainterno.jsp">PIANTE INTERNO</a></li>
                    <li><a href="Piantedaesterno.jsp">PIANTE ESTERNO</a></li>
                    <li><a href="Accessori.jsp">ACCESSORI</a></li>
                    <li><a href="MyAccount.jsp">MyAccount</a></li> <li><a href="Carrello.jsp">CARRELLO</a></li>
                    <li><a href="Login.jsp">LOGIN</a></li> 
                </ul>
            </nav>
        </div>
    </header>

    <section class="hero">
        <div class="hero-left">
            <div class="logo-container">
                <h1>LA</h1>
                <h2>TECA</h2>
                <h3>DEL</h3>
                <h4>GIARDINIERE</h4>
            </div>
            <p class="tagline">COMFORT WITH PLANT-BASED<br>INGREDIENTS AND LOTS OF LOVE</p>
        </div>
        <div class="hero-right">
            <img src="<%= request.getContextPath() %>/images/presentazione.png" alt="Selezione di piante">
        </div>
    </section>

    <section class="featured-plants">
        <h2>Le Nostre Piante di Stagione </h2>
        <div class="plants-grid">
            <div class="plant-card">
                <img src="<%= request.getContextPath() %>/images/calendula.png" alt="Calendula officinalis">
                <h3>Calendula officinalis</h3>
                <p>(Calendula)</p>
                <p class="price">€12.50 <span class="vat">+ IVA 22%</span></p>
                <a href="calendula.jsp" class="view-details-button">Aggiungi al carrello</a>
            </div>

            <div class="plant-card">
                <img src="<%= request.getContextPath() %>/images/margherite.png" alt="Matricaria chamomilla">
                <h3>Matricaria chamomilla</h3>
                <p>(Camomilla)</p>
                <p class="price">€9.99 <span class="vat">+ IVA 22%</span></p>
                <a href="chamomilla.jsp" class="view-details-button">Aggiungi al carrello</a>
            </div>

            <div class="plant-card">
                <img src="<%= request.getContextPath() %>/images/sassifreaga.png" alt="Sempervivum">
                <h3>Sempervivum</h3>
                <p>(Sassifraga)</p>
                <p class="price">€15.00 <span class="vat">+ IVA 22%</span></p>
                <a href="product-page-sempervivum.jsp" class="view-details-button">Aggiungi al carrello</a>
            </div>

            <div class="plant-card">
                <img src="<%= request.getContextPath() %>/images/fico.png" alt="Opuntia">
                <h3>Opuntia</h3>
                <p>(Fico d'India nano)</p>
                <p class="price">€18.75 <span class="vat">+ IVA 22%</span></p>
                <a href="product-page-opuntia.jsp" class="view-details-button">Aggiungi al carrello</a>
            </div>
        </div>
        <div class="secret-text">
            <h2>il segreto delle piante</h2>
            <p>"SEI UN 'KILLER DI PIANTE'? NON PREOCCUPARTI, TI CAPIAMO!</p>
            <p>MA ABBIAMO UNA BUONA NOTIZIA: ESISTONO PIANTE CHE SOPRAVVIVONO ANCHE AI POLLICI NERI PIÙ OSTINATI."</p>
            <p>SCOPRI LA NOSTRA SELEZIONE DI PIANTE INDISTRUTTIBILI E TRASFORMA LA TUA CASA IN UNA GIUNGLA URBANA... SENZA IL MINIMO SFORZO!"</p>
        </div>
    </section>

    <section class="plant-details">
        <div class="plant-details-top">
            <div class="plant-category">
                <a href="Piantedainterno.jsp">
                    <img src="<%= request.getContextPath() %>/images/logointerni.png" alt="Piante da Interno">
                    <h3>PIANTE<br>DA<br>INTERNO</h3>
                </a>
            </div>
            <div class="accessories">
                <a href="Accessori.jsp">
                    <img src="<%= request.getContextPath() %>/images/logoaccessori.png" alt="Accessori">
                    <h3>ACCESSORI</h3>
                </a>
            </div>
            <div class="plant-category">
                <a href="Piantedaesterno.jsp">
                    <img src="<%= request.getContextPath() %>/images/logoesterno.png" alt="Piante da Esterno">
                    <h3>PIANTE<br>DA<br>ESTERNO</h3>
                </a>
            </div>
        </div>

        <div class="pine-loricato">
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

        <div class="featured-plants-grid-secondary plants-grid">
            <div class="plant-card">
                <img src="<%= request.getContextPath() %>/images/calendario.png" alt="Vaso-Calendario">
                <h3>Vaso Calendario</h3>
                <p class="price">€25.00 <span class="vat">+ IVA 22%</span></p>
             <a href="product-page-opuntia.jsp" class="view-details-button">Aggiungi al carrello</a>
            </div>
            <div class="plant-card">
                <img src="<%= request.getContextPath() %>/images/contagocce.png" alt="Contagocce">
                <h3>Contagocce</h3>
                <p class="price">€8.99 <span class="vat">+ IVA 22%</span></p>
            <a href="product-page-opuntia.jsp" class="view-details-button">Aggiungi al carrello</a>
            </div>
            <div class="plant-card">
                <img src="<%= request.getContextPath() %>/images/setregalo.png" alt="Set Regalo">
                <h3>Set Regalo</h3>
                <p class="price">€35.00 <span class="vat">+ IVA 22%</span></p>
           <a href="product-page-opuntia.jsp" class="view-details-button">Aggiungi al carrello</a>
            </div>
            <div class="plant-card">
                <img src="<%= request.getContextPath() %>/images/set professionale.png" alt="Set Professionale">
                <h3>Set Professionale</h3>
                <p class="price">€59.99 <span class="vat">+ IVA 22%</span></p>
                <a href="product-page-opuntia.jsp" class="view-details-button">Aggiungi al carrello</a>
            </div>
            
        </div>

        <div class="modular-vases">
            <div class="modular-vases-image">
                <img src="<%= request.getContextPath() %>/images/drenaggio-intelligente.png" alt="Vaso Modulare">
            </div>
            <div class="modular-vases-content">
                <h2>Innovativo Sistema di Vasi Modulari per un Rinvaso Semplice e un Drenaggio Ottimale</h2>
                <p>Rivoluziona il modo in cui ti prendi cura delle tue piante con il nostro esclusivo sistema di vasi modulari. Progettati pensando alla salute delle tue piante e alla tua comodità, questi vasi presentano un design intelligente con una base inferiore staccabile.</p>
                <p>Rinvaso Facile e Senza Stress: Dì addio a rinvasi difficili e disordinati! Grazie alla base inferiore removibile, potrai estrarre delicatamente la tua pianta con il suo pane di terra senza danneggiare le radici o spargere terriccio ovunque. Questo sistema rende il cambio di vaso un'operazione semplice e veloce, ideale per piante in crescita che necessitano di spazio aggiuntivo o per rinnovare il terriccio.</p>
            </div>
        </div>
    </section>

    <footer>
        <p>&copy; 2025 La Teca del Giardiniere. Tutti i diritti riservati.</p>
    </footer>
</body>
</html>