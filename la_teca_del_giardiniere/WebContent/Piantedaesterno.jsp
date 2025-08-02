<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>La Teca del Giardiniere - Piante da Esterno</title>
    <link rel="stylesheet" href="Piantedainterni.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:wght@400;700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;700&display=swap" rel="stylesheet">
</head>

<body>
    <header>
        <div class="header-container"> 
            <div class="logo-area">
                <a href="homepage.jsp">
                    <img src="${pageContext.request.contextPath}/images/accessori.png" alt="La Teca del Giardiniere Logo" class="site-logo">
                </a>
            </div>
            <nav>
                   <ul>
                    <li><a href="homepage.jsp">HOME</a></li>
                    <li><a href="Piantedainterno.jsp">PIANTE INTERNO</a></li>
                    <li><a href="Piantedaesterno.jsp">PIANTE ESTERNO</a></li>
                    <li><a href="Accessori.jsp">ACCESSORI</a></li>
                    <li><a href="Carrello.jsp">CARRELLO</a></li>
                    <li><a href="userlogged/MyAccount.jsp">Account</a></li>
                </ul>
            </nav>
        </div>
    </header>

    <main class="content-wrapper"> 
        <aside class="sidebar-left"> 
            <h2 class="sidebar-title">Per i tuoi esterni</h2>
            <div class="sidebar-item">
                <img src="${pageContext.request.contextPath}/images/prato.png" alt="Giardini Adorabili">
                <p>Adorabili giardini</p>
            </div>
            <div class="sidebar-item">
                <img src="${pageContext.request.contextPath}/images/balcone.png" alt="Terrazzi Colorati">
                <p>Colorati terrazzi</p>
            </div>
            <%-- Aggiungi altri sidebar-item se necessario, con immagini e testi pertinenti --%>
        </aside>

        <section class="main-content"> <%-- Colonna principale destra --%>
            <div class="category-header-box"> <%-- Riquadro centrale sotto l'header --%>
                <div class="category-text-content">
                    <h2>piante da esterno</h2>
                    <p>Scopri la nostra selezione di piante facilissime da curare e con qualità uniche per i piccoli e grandi ambienti e la propria salute</p>
                </div>
                <div class="category-image-content">
                    <img src="${pageContext.request.contextPath}/images/logoesterno.png" alt="Pianta in Vaso Esterno"> <%-- Immagine specifica per esterno, come da screenshot --%>
                </div>
                
            </div>

            <section class="plants-grid"> 
                <div class="plant-card">
                
                <form action="${pageContext.request.contextPath}/aggiungiAlCarrello" method="post" class="add-to-cart-form">
                        <input type="hidden" name="idProdotto" value="109"> <%-- ID univoco dell'Erica nel tuo DB --%>
                        <input type="hidden" name="tipoProdotto" value="PIANTA">
                        <input type="hidden" name="quantita" value="1">
                        <button type="submit" class="add-to-cart-btn">Aggiungi al Carrello</button>
                    </form>
                    </div>
                
            </section>
        </section>
    </main>

    <footer>
        <p>&copy; 2025 La Teca del Giardiniere</p>
    </footer>

</body>
</html>