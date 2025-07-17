<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>La Teca del Giardiniere - Piante da Interno</title>
    <link rel="stylesheet" href="interno.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:wght@400;700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;700&display=swap" rel="stylesheet">

    <%-- Rimosso il blocco <style> da qui, gli stili per il carrello sono ora in interno.css --%>
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
                    <img src="${pageContext.request.contextPath}/images/logointerni.png" alt="Piante da appartamento">
                </div>
            </div>

            <section class="plants-grid">
                <div class="plant-card">
                    <img src="${pageContext.request.contextPath}/images/linguadellasuocera.png" alt="Sansevieria (Lingua di suocera)">
                    <h3>Sansevieria</h3>
                    <p>(Lingua di suocera)</p>
                    <div class="price-info">
                        <span class="price">€19.99</span>
                        <span class="tax-info">IVA inclusa</span>
                    </div>
                    <div class="add-to-cart-container">
    <button class="add-to-cart-btn" data-product-id="[ID_PRODOTTO]">
        <img src="${pageContext.request.contextPath}/images/cart-icon.png" alt="Aggiungi al Carrello"/>
        <span>Aggiungi al Carrello</span>
    </button>
</div>
                </div>

                <div class="plant-card">
                    <img src="${pageContext.request.contextPath}/images/zanzibar.png" alt="Zamioculcas zamiifolia (Pianta di Zanzibar)">
                    <h3>Zamioculcas zamiifolia</h3>
                    <p>(Pianta di Zanzibar)</p>
                    <div class="price-info">
                        <span class="price">€24.50</span>
                        <span class="tax-info">IVA inclusa</span>
                    </div>
                    <div class="add-to-cart-container">
    <button class="add-to-cart-btn" data-product-id="[ID_PRODOTTO]">
        <img src="${pageContext.request.contextPath}/images/cart-icon.png" alt="Aggiungi al Carrello"/>
        <span>Aggiungi al Carrello</span>
    </button>
</div>
                </div>

                <div class="plant-card">
                    <img src="${pageContext.request.contextPath}/images/pothos.png" alt="Pothos (Epipremnum aureum)">
                    <h3>Pothos</h3>
                    <p>(Epipremnum aureum)</p>
                    <div class="price-info">
                        <span class="price">€12.75</span>
                        <span class="tax-info">IVA inclusa</span>
                    </div>
                    <div class="add-to-cart-container">
    <button class="add-to-cart-btn" data-product-id="[ID_PRODOTTO]">
        <img src="${pageContext.request.contextPath}/images/cart-icon.png" alt="Aggiungi al Carrello"/>
        <span>Aggiungi al Carrello</span>
    </button>
</div>
                </div>

                <div class="plant-card">
                    <img src="${pageContext.request.contextPath}/images/falangio.png" alt="Chlorophytum comosum (Falangio)">
                    <h3>Chlorophytum comosum</h3>
                    <p>(Falangio)</p>
                    <div class="price-info">
                        <span class="price">€15.20</span>
                        <span class="tax-info">IVA inclusa</span>
                    </div>
                    <div class="add-to-cart-container">
    <button class="add-to-cart-btn" data-product-id="[ID_PRODOTTO]">
        <img src="${pageContext.request.contextPath}/images/cart-icon.png" alt="Aggiungi al Carrello"/>
        <span>Aggiungi al Carrello</span>
    </button>
</div>
                </div>

                <div class="plant-card">
                    <img src="${pageContext.request.contextPath}/images/ceramica.png" alt="Hoya carnosa (Fiore di cera)">
                    <h3>Hoya carnosa</h3>
                    <p>(Fiore di cera)</p>
                    <div class="price-info">
                        <span class="price">€28.00</span>
                        <span class="tax-info">IVA inclusa</span>
                    </div>
                    <div class="add-to-cart-container">
    <button class="add-to-cart-btn" data-product-id="[ID_PRODOTTO]">
        <img src="${pageContext.request.contextPath}/images/cart-icon.png" alt="Aggiungi al Carrello"/>
        <span>Aggiungi al Carrello</span>
    </button>
</div>
                </div>

                <div class="plant-card">
                    <img src="${pageContext.request.contextPath}/images/piededielefante.png" alt="Beaucarnea recurvata (Nolina o Piede di elefante)">
                    <h3>Beaucarnea recurvata</h3>
                    <p>(Nolina o Piede di elefante)</p>
                    <div class="price-info">
                        <span class="price">€35.99</span>
                        <span class="tax-info">IVA inclusa</span>
                    </div>
                    <div class="add-to-cart-container">
    <button class="add-to-cart-btn" data-product-id="[ID_PRODOTTO]">
        <img src="${pageContext.request.contextPath}/images/cart-icon.png" alt="Aggiungi al Carrello"/>
        <span>Aggiungi al Carrello</span>
    </button>
</div>
                </div>
            </section>

            <section class="additional-plants-grid">
                <div class="plant-card">
                    <img src="${pageContext.request.contextPath}/images/crassula_ovata.png" alt="Crassula ovata">
                    <h3>Crassula ovata</h3>
                    <p>(Albero di Giada)</p>
                    <div class="price-info">
                        <span class="price">€18.50</span>
                        <span class="tax-info">IVA inclusa</span>
                    </div>
                    <div class="add-to-cart-container">
    <button class="add-to-cart-btn" data-product-id="[ID_PRODOTTO]">
        <img src="${pageContext.request.contextPath}/images/cart-icon.png" alt="Aggiungi al Carrello"/>
        <span>Aggiungi al Carrello</span>
    </button>
</div>
                </div>

                <div class="plant-card">
                    <img src="${pageContext.request.contextPath}/images/gigliodellapace.png" alt="Spathiphyllum (Giglio della pace)">
                    <h3>Spathiphyllum</h3>
                    <p>(Giglio della pace)</p>
                    <div class="price-info">
                        <span class="price">€22.00</span>
                        <span class="tax-info">IVA inclusa</span>
                    </div>
                    <div class="add-to-cart-container">
    <button class="add-to-cart-btn" data-product-id="[ID_PRODOTTO]">
        <img src="${pageContext.request.contextPath}/images/cart-icon.png" alt="Aggiungi al Carrello"/>
        <span>Aggiungi al Carrello</span>
    </button>
</div>
                </div>

                <div class="plant-card">
                    <img src="${pageContext.request.contextPath}/images/piantadipiombo.png" alt="Aspidistra elatior (Pianta di piombo)">
                    <h3>Aspidistra elatior</h3>
                    <p>(Pianta di piombo)</p>
                    <div class="price-info">
                        <span class="price">€29.95</span>
                        <span class="tax-info">IVA inclusa</span>
                    </div>
                    <div class="add-to-cart-container">
    <button class="add-to-cart-btn" data-product-id="[ID_PRODOTTO]">
        <img src="${pageContext.request.contextPath}/images/cart-icon.png" alt="Aggiungi al Carrello"/>
        <span>Aggiungi al Carrello</span>
    </button>
</div>
                </div>

                <div class="plant-card">
                    <img src="${pageContext.request.contextPath}/images/peperonia.png" alt="Peperomia">
                    <h3>Peperomia</h3>
                    <p>(Pianta dei soldi)</p>
                    <div class="price-info">
                        <span class="price">€10.99</span>
                        <span class="tax-info">IVA inclusa</span>
                    </div>
                    <div class="add-to-cart-container">
    <button class="add-to-cart-btn" data-product-id="[ID_PRODOTTO]">
        <img src="${pageContext.request.contextPath}/images/cart-icon.png" alt="Aggiungi al Carrello"/>
        <span>Aggiungi al Carrello</span>
    </button>
</div>
                </div>
            </section>
        </section>
    </main>

    <footer>
        <p>&copy; 2025 La Teca del Giardiniere</p>
    </footer>

</body>
</html>