<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>La Teca del Giardiniere - Piante da Interni</title>
    <link rel="stylesheet" href="Piantedainterni.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:wght@400;700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>

<body>
    <jsp:include page="/header.jsp" />

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
		 <div class="category-header-box">
		                <div class="category-text-content">
		                    <h2><c:out value="${requestScope.titoloCatalogo}"/></h2>
		                    <p><c:out value="${requestScope.descrizioneCatalogo}"/></p>
		                </div>
		                <div class="category-image-content">
		                    <img src="${pageContext.request.contextPath}/images/presentazionepiante.png"  alt="Immagine di presentazione per gli accessori">
		                </div>
		            </div>
            <section class="plants-grid">
                <c:forEach var="pianta" items="${listaPiante}">
                    <div class="plant-card">
                        <a href="${pageContext.request.contextPath}/dettagliPianta?id=${pianta.id}">
                            <div class="plant-info">
                                <img src="${pageContext.request.contextPath}/images/${pianta.immagine}" alt="${pianta.nomeComune}">
                                <h3><c:out value="${pianta.nomeComune}"/></h3>
                            </div>
                        </a>
                        <div class="price-section">
                            <span>€<fmt:formatNumber value="${pianta.prezzo}" pattern="0.00"/></span>
                            <small>IVA inclusa</small>
                        </div>
                        <form action="${pageContext.request.contextPath}/aggiungiAlCarrello" method="post" class="add-to-cart-form">
                            <input type="hidden" name="idProdotto" value="${pianta.id}">
                            <input type="hidden" name="tipoProdotto" value="pianta">
                            <button type="submit" class="add-to-cart-btn">
                                <i class="fas fa-shopping-cart"></i> Aggiungi al Carrello
                            </button>
                        </form>
                    </div>
                </c:forEach>
            </section>
           </div>
        </section>
    </main>

    <jsp:include page="/footer.jsp" />
</body>
</html>