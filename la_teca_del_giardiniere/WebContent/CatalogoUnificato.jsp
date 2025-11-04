<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>La Teca del Giardiniere - <c:out value="${titoloCatalogo}"/></title> 
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Piantedainterni.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:wght@400;700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>

<body>
    <jsp:include page="/header.jsp" />
    
    <main class="content-wrapper">
        <aside class="sidebar-left">
            <h2 class="sidebar-title">Categorie Principali</h2>
            <div class="sidebar-item">
                <a href="${pageContext.request.contextPath}/catalogo-piante?tipo=interno">
                    <img src="${pageContext.request.contextPath}/images/soggiorno.png" alt="Piante da Interno">
                    <p>Piante da Interno</p>
                </a>
            </div>
            <div class="sidebar-item">
                <a href="${pageContext.request.contextPath}/catalogo-piante?tipo=esterno">
                    <img src="${pageContext.request.contextPath}/images/prato.png" alt="Piante da Esterno">
                    <p>Piante da Esterno</p>
                </a>
            </div>
             <div class="sidebar-item">
                <a href="${pageContext.request.contextPath}/catalogo-accessori">
                    <img src="${pageContext.request.contextPath}/images/paletti.png" alt="Accessori">
                    <p>Accessori</p>
                </a>
            </div>
        </aside>

        <section class="main-content">
            <div class="category-header-box">
                <div class="category-text-content">
                    <h2><c:out value="${titoloCatalogo}"/></h2>
                    <p><c:out value="${descrizioneCatalogo}"/></p>
                </div>
                <div class="category-image-content">
                    <img src="${pageContext.request.contextPath}/images/<c:out value="${immagineCatalogo}"/>" alt="Immagine di categoria">
                </div>
            </div>

            <section class="plants-grid">
                <c:choose>
                    <c:when test="${not empty listaProdotti}">
                        <c:forEach var="prodotto" items="${listaProdotti}">
                            <div class="plant-card">
                                
                                <c:set var="className" value="${prodotto.getClass().getName()}" />
                                
                                <c:choose>
                                    <c:when test="${className.endsWith('Piante')}">
                                        <a href="${pageContext.request.contextPath}/dettagliPianta?id=${prodotto.id}" class="plant-card-link">
                                            <div class="plant-info">
                                                <img src="${pageContext.request.contextPath}/images/<c:out value="${prodotto.immagine}"/>" alt="${prodotto.nomeComune}">
                                                <h3><c:out value="${prodotto.nomeComune}"/></h3>
                                            </div>
                                        </a>
                                        <div class="price-section">
                                            <span>€<fmt:formatNumber value="${prodotto.prezzo}" pattern="0.00"/></span>
                                            <small>IVA inclusa</small>
                                        </div>
                                        <form action="${pageContext.request.contextPath}/aggiungiAlCarrello" method="post" class="add-to-cart-form">
                                            <input type="hidden" name="idProdotto" value="${prodotto.id}">
                                            <input type="hidden" name="tipoProdotto" value="pianta">
                                            <button type="submit" class="add-to-cart-btn">
                                                <i class="fas fa-shopping-cart"></i> <span>Aggiungi al Carrello</span>
                                            </button>
                                        </form>
                                    </c:when>
                                    
                                    <c:when test="${className.endsWith('Accessori')}">
                                        <a href="${pageContext.request.contextPath}/dettagliAccessorio?id=${prodotto.accessorio_id}" class="plant-card-link"> 
                                            <div class="plant-info">
                                                <img src="${pageContext.request.contextPath}/images/<c:out value="${prodotto.immagine}"/>" alt="${prodotto.nome}">
                                                <h3><c:out value="${prodotto.nome}"/></h3>
                                            </div>
                                        </a>
                                        <div class="price-section">
                                            <span>€<fmt:formatNumber value="${prodotto.prezzo}" pattern="0.00"/></span>
                                            <small>IVA inclusa</small>
                                        </div>
                                        <form action="${pageContext.request.contextPath}/aggiungiAlCarrello" method="post" class="add-to-cart-form">
                                            <input type="hidden" name="idProdotto" value="${prodotto.accessorio_id}">
                                            <input type="hidden" name="tipoProdotto" value="accessorio">
                                            <button type="submit" class="add-to-cart-btn">
                                                <i class="fas fa-shopping-cart"></i> <span>Aggiungi al Carrello</span>
                                            </button>
                                        </form>
                                    </c:when>
                                </c:choose>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <p class="no-results">Nessun risultato trovato per la tua selezione.</p>
                    </c:otherwise>
                </c:choose>
            </section>
        </section>
    </main>
    
    <jsp:include page="/footer.jsp" />

</body>
</html>