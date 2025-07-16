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
        <div class="header-container"> <%-- Contenitore per logo e nav nell'header --%>
            <div class="logo-area">
                <a href="homepage.jsp">
                    <img src="${pageContext.request.contextPath}/images/logo.png" alt="La Teca del Giardiniere Logo" class="site-logo">
                </a>
            </div>
            <nav>
                   <ul>
                    <li><a href="homepage.jsp">HOME</a></li>
                    <li><a href="Piantedainterno.jsp">PIANTE INTERNO</a></li>
                    <li><a href="Piantedaesterno.jsp">PIANTE ESTERNO</a></li>
                    <li><a href="Accessori.jsp">ACCESSORI</a></li>
                    <li><a href="Carrello.jsp">CARRELLO</a></li>
                    <li><a href="MyAccount.jsp">Account</a></li>
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
                    <img src="${pageContext.request.contextPath}/images/margherite.png" alt="Matricaria Chamomilla">
                    <h3>Matricaria chamomilla</h3>
                    <p>(camomilla)</p>
                    <div class="price-section">
                        <span>€28.00</span>
                        <small>IVA inclusa</small>
                    </div>
                    <button class="add-to-cart-btn">Aggiungi al Carrello</button>
                </div>
                <div class="plant-card">
                    <img src="${pageContext.request.contextPath}/images/calendula.png" alt="Calendula Officinalis">
                    <h3>Calendula officinalis</h3>
                    <p>(Calendula)</p>
                    <div class="price-section">
                        <span>€35.99</span>
                        <small>IVA inclusa</small>
                    </div>
                    <button class="add-to-cart-btn">Aggiungi al Carrello</button>
                </div>
                <div class="plant-card">
                    <img src="${pageContext.request.contextPath}/images/pothos.png" alt="Pothos (Epipremnum aureum)"> <%-- Ho corretto l'alt, ma l'immagine Pothos è da interno di solito --%>
                    <h3>Pothos</h3>
                    <p>(Epipremnum aureum)</p>
                    <div class="price-section">
                        <span>€18.50</span>
                        <small>IVA inclusa</small>
                    </div>
                    <button class="add-to-cart-btn">Aggiungi al Carrello</button>
                </div>
                <div class="plant-card">
                    <img src="${pageContext.request.contextPath}/images/fico.png" alt="Opuntia (Fico d'India nano)">
                    <h3>Opuntia</h3>
                    <p>(Fico d'India nano)</p>
                    <div class="price-section">
                        <span>€29.95</span>
                        <small>IVA inclusa</small>
                    </div>
                    <button class="add-to-cart-btn">Aggiungi al Carrello</button>
                </div>
                <div class="plant-card">
                    <img src="${pageContext.request.contextPath}/images/erica.png" alt="Erica (Calluna vulgaris)">
                    <h3>Erica</h3>
                    <p>(Calluna vulgaris)</p>
                    <div class="price-section">
                        <span>€10.99</span>
                        <small>IVA inclusa</small>
                    </div>
                    <button class="add-to-cart-btn">Aggiungi al Carrello</button>
                </div>
                <%-- Assicurati che i percorsi delle immagini siano corretti per tutte le tue immagini di piante --%>
            </section>
        </section>
    </main>

    <footer>
        <p>&copy; 2025 La Teca del Giardiniere</p>
    </footer>

</body>
</html>