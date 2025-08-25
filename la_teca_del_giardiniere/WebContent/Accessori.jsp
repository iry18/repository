<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>La Teca del Giardiniere - Accessori</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/accessori.css"> 
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:wght@400;700&display=swap" rel="stylesheet">
</head>

<body>
    <header>
        <div class="header-container">
            <div class="logo-area">
                <a href="${pageContext.request.contextPath}/homepage.jsp">
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

    <section class="hero">
        <div class="hero-logo">
            <img src="${pageContext.request.contextPath}/images/accessori.png" alt="Vaso Sorridente"> <%-- Immagine del vaso sorridente --%>
        </div>
        <div class="hero-text-content">
            <h2>La teca del giardiniere</h2>
         <p>Scopri la nostra selezione di accessori essenziali e innovativi. </p>
         <p>Strumenti intelligenti, vasi di design e kit completi per rendere la cura delle tue piante un vero piacere, anche per i meno esperti."</p>
            </div>
    </section>

    <%-- Messaggi di stato dalla Servlet (successo/errore/warning) --%>
    <c:if test="${not empty requestScope.messaggio}">
        <div class="message ${requestScope.tipoMessaggio}">
            <c:out value="${requestScope.messaggio}"/>
        </div>
    </c:if>

    <main class="main-content"> <%-- main-content ora non è più in flex con sidebar --%>
        <section class="products-grid-section"> <%-- Contenitore principale per la griglia --%>
            <c:choose>
                <c:when test="${empty requestScope.listaAccessori}">
                    <p class="no-products-message">Nessun accessorio disponibile al momento. Torna presto!</p>
                </c:when>
                <c:otherwise>
                    <div class="products-grid">
                        <c:forEach var="accessorio" items="${requestScope.listaAccessori}">
                            <div class="product-card">
                                <img src="${pageContext.request.contextPath}/images/<c:out value='${accessorio.immagine}'/>" alt="<c:out value='${accessorio.nome}'/>">
                                <h3><c:out value="${accessorio.nome}"/></h3>
                                <p class="short-description"><c:out value="${accessorio.descrizioneBreve}"/></p>
                                <div class="price-info">
                                    <span class="price">€ <fmt:formatNumber value="${accessorio.prezzo}" pattern="0.00"/></span>
                                    <span class="tax-info">IVA inclusa</span>
                                </div>
                                <div class="add-to-cart-container">
                                    <form action="${pageContext.request.contextPath}/aggiungiAlCarrello" method="post">
								        <input type="hidden" name="idProdotto" value="${accessorio.accessorio_id}">
								        <input type="hidden" name="tipoProdotto" value="ACCESSORIO">
								        <input type="hidden" name="quantita" value="1">
								        <button type="submit" class="add-to-cart-btn">
								        <span>Aggiungi al Carrello</span>
                                    </button>
                                   </form>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </section>

</main>


    <footer>
        <p>&copy; 2025 La Teca del Giardiniere. Tutti i diritti riservati.</p>
    </footer>

</body>
</html>