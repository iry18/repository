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
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    
</head>

<body>
    <jsp:include page="/header.jsp" />

    <c:if test="${not empty requestScope.messaggio}">
        <div class="message ${requestScope.tipoMessaggio}">
            <c:out value="${requestScope.messaggio}"/>
        </div>
    </c:if>
    
    <main class="content-wrapper">
        <aside class="sidebar-left">
            <h2 class="sidebar-title">Ogni pianta ha il suo scopo</h2>
            <div class="sidebar-item">
                <img src="${pageContext.request.contextPath}/images/stikers.png" alt="Piante per la cucina">
                <p> Accessori carini ed utili </p>
            </div>
            <div class="sidebar-item">
                <img src="${pageContext.request.contextPath}/images/paletti.png" alt="Piante per la cucina">
                <p> coltiva la tua creatività </p>
            </div>
            <div class="sidebar-item">
                <img src="${pageContext.request.contextPath}/images/flebo.png" alt="Piante per la cucina">
                <p> fatti ispirare </p>
            </div>
        </aside>

        <section class="main-content">
        
           <div class="category-header-box">
                <div class="category-text-content">
                    <h2><c:out value="${requestScope.titoloCatalogo}"/></h2>
                    <p><c:out value="${requestScope.descrizioneCatalogo}"/></p>
                </div>
                <div class="category-image-content">
                    <img src="${pageContext.request.contextPath}/images/<c:out value='${requestScope.immagineCatalogo}'/>" 
                         alt="Immagine di presentazione per gli accessori">
                </div>
            </div>
            <section class="products-grid-section">
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
                                                <i class="fas fa-shopping-cart"></i> Aggiungi al Carrello
                                            </button>
                                        </form>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </section>
        </section>
    </main>

    <jsp:include page="/footer.jsp" />

</body>
</html>