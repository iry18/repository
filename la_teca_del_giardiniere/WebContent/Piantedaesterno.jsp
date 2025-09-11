<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">

</head>

<body>
    <jsp:include page="/header.jsp" />

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
        </aside>

		  <section class="main-content">
		    <div class="category-header-box">
		        <div class="category-text-content">
		            <h2>piante da esterno</h2>
		            <p>Scopri la nostra selezione di piante facilissime da curare e con qualità uniche per i piccoli e grandi ambienti e la propria salute</p>
		        </div>
		        <div class="category-image-content">
		            <img src="${pageContext.request.contextPath}/images/logoesterno.png" alt="Pianta in Vaso Esterno">
		        </div>
			</div>
		
            <section class="plants-grid">
                <c:forEach var="pianta" items="${listaPiante}">
                    <div class="plant-card">
                        <div class="plant-info">
                            <img src="${pageContext.request.contextPath}/images/${pianta.immagine}" alt="${pianta.nomeComune}">
                            <h3><c:out value="${pianta.nomeComune}"/></h3>
                        </div>
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
       </section>
    </main>

    <jsp:include page="/footer.jsp" />
</body>
</html>