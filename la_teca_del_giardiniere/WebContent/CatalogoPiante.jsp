<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>La Teca del Giardiniere - Piante</title>
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
                    <img src="${pageContext.request.contextPath}/images/logo.png" alt="La Teca del Giardiniere Logo" class="site-logo">
                </a>
            </div>
            <nav>
                <ul>
                    <li><a href="homepage.jsp">HOME</a></li>
                    <li><a href="${pageContext.request.contextPath}/catalogo-piante?tipo=interno">Piante da interno</a></li>
                    <li><a href="${pageContext.request.contextPath}/catalogo-piante?tipo=esterno">Piante da esterno</a></li>
                    <li><a href="${pageContext.request.contextPath}/catalogo-accessori">ACCESSORI</a></li>
                    <li><a href="${pageContext.request.contextPath}/visualizzaCarrello">CARRELLO</a></li>
                    <li><a href="userlogged/MyAccount.jsp">Account</a></li>
                </ul>
            </nav>
        </div>
    </header>

    <main class="content-wrapper">
        <aside class="sidebar-left">
            <h2 class="sidebar-title">Ogni pianta ha il suo scopo</h2>
            <div class="sidebar-item">
                <img src="${pageContext.request.contextPath}/images/soggiorno.png" alt="Piante per il soggiorno">
                <p>piante per il soggiorno</p>
            </div>
            <div class="sidebar-item">
                <img src="${pageContext.request.contextPath}/images/bagno.png" alt="Piante per il bagno">
                <p>piante per il bagno</p>
            </div>
            <div class="sidebar-item">
                <img src="${pageContext.request.contextPath}/images/cucina.png" alt="Piante per la cucina">
                <p>piante per la cucina</p>
            </div>
        </aside>

        <section class="main-content">
            <div class="category-header-box">
                <div class="category-text-content">
                    <h2>piante da appartamento</h2>
                    <p>Scopri la nostra selezione di piante facilissime da curare e con qualità uniche per la casa e la propria salute</p>
                </div>
                <div class="category-image-content">
                    <img src="${pageContext.request.contextPath}/images/presentazionepiante.png" alt="Piante da appartamento">
                </div>
            </div>

            <section class="plants-grid">
                <c:forEach var="pianta" items="${listaPiante}">
                    <div class="plant-card">
                        <a href="${pageContext.request.contextPath}/dettagliPianta?id=${pianta.id}" class="plant-card-link">
                            <div class="plant-info">
                                <img src="${pageContext.request.contextPath}/images/${pianta.immagine}" alt="${pianta.nomeComune}">
                                <h3><c:out value="${pianta.nomeComune}"/></h3>
                            </div>
                        </a>
                        <div class="price-section">
                            <span>€<c:out value="${pianta.prezzo}"/></span>
                            <small>IVA inclusa</small>
                        </div>
                        <form action="${pageContext.request.contextPath}/aggiungiAlCarrello" method="post" class="add-to-cart-form">
                            <input type="hidden" name="idProdotto" value="${pianta.id}">
                            <input type="hidden" name="tipoProdotto" value="pianta">
                            <button type="submit" class="add-to-cart-btn">
                                <img src="${pageContext.request.contextPath}/images/cart-icon.png" alt="Aggiungi al Carrello"/>
                                <span>Aggiungi al Carrello</span>
                            </button>
                        </form>
                    </div>
                </c:forEach>
            </section>
        </section>
    </main>
    
    <footer>
        <p>&copy; 2025 La Teca del Giardiniere</p>
    </footer>
</body>
</html>