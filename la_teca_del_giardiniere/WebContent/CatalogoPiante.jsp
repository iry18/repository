<%-- catalogo.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${requestScope.pageTitle} - La Teca del Giardiniere</title>
    <link rel="stylesheet" href="Piantedainterni.css">
</head>

<body>
    <main class="content-wrapper">
        <aside class="sidebar-left">
            <%-- Sidebar content can also be made dynamic, or stay static if it's generic --%>
            <h2 class="sidebar-title">Consigli per le tue Piante</h2>
            <div class="sidebar-item">
                <p>💡 Scegli la pianta giusta in base all'esposizione solare.</p>
            </div>
            <div class="sidebar-item">
                <p>💧 Non esagerare con l'acqua! Controlla sempre il terriccio.</p>
            </div>
            <div class="sidebar-item">
                <p>🌱 Concima le tue piante regolarmente per una crescita sana.</p>
            </div>
        </aside>

        <section class="main-content">
            <div class="category-header-box">
                <div class="category-text-content">
                    <h2>${requestScope.headerTitle}</h2>
                    <p>${requestScope.headerDescription}</p>
                </div>
                <div class="category-image-content">
                    <img src="${pageContext.request.contextPath}/images/${requestScope.headerImage}" alt="${requestScope.headerImageAlt}">
                </div>
            </div>

            <section class="plants-grid">
                <c:if test="${empty requestScope.listaPiante}">
                    <p class="empty-message">Non sono disponibili piante in questa categoria.</p>
                </c:if>
                <c:forEach var="pianta" items="${requestScope.listaPiante}">
                    <div class="plant-card">
                        <div class="plant-info">
                            <a href="${pageContext.request.contextPath}/dettaglio-prodotto?id=${pianta.id}&type=pianta">
                                <img src="${pageContext.request.contextPath}/images/${pianta.immagine}" alt="${pianta.nomeComune}">
                                <h3><c:out value="${pianta.nomeComune}"/></h3>
                            </a>
                        </div>
                        <div class="price-section">
                            <span>€<fmt:formatNumber value="${pianta.prezzo}" pattern="0.00"/></span>
                            <small>IVA inclusa</small>
                        </div>
                        <form action="${pageContext.request.contextPath}/aggiungiAlCarrello" method="post" class="add-to-cart-form">
                            <input type="hidden" name="productId" value="${pianta.id}">
                            <input type="hidden" name="productType" value="pianta">
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
</body>
</html>