<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Il Mio Carrello - La Teca del Giardiniere</title>
    <link rel="stylesheet" href="carrello.css">
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
                    <li><a href="${pageContext.request.contextPath}/homepage.jsp">HOME</a></li>
                    <li><a href="${pageContext.request.contextPath}/catalogo-piante?tipo=interno">PIANTE INTERNO</a></li>
                    <li><a href="${pageContext.request.contextPath}/catalogo-piante?tipo=esterno">PIANTE ESTERNO</a></li>
                    <li><a href="${pageContext.request.contextPath}/catalogo-accessori">ACCESSORI</a></li>
                    <li><a href="${pageContext.request.contextPath}/Carrello.jsp">CARRELLO</a></li>
                    <li><a href="${pageContext.request.contextPath}/userlogged/MyAccount.jsp">Account</a></li>
                </ul>
            </nav>
        </div>
    </header>

    <div class="cart-container">
        <h1>Il Mio Carrello</h1>
        <c:if test="${not empty requestScope.messaggio}">
            <div class="message ${requestScope.tipoMessaggio}">
                <c:out value="${requestScope.messaggio}"/>
            </div>
        </c:if>

        <c:choose>
            <c:when test="${empty requestScope.carrello || requestScope.carrello.isEmpty()}">
                <div class="empty-cart-message">
                    <p>Il tuo carrello è vuoto. Inizia ad esplorare i nostri prodotti!</p>
                    <a href="${pageContext.request.contextPath}/homepage.jsp" class="btn">Vai allo Shopping</a>
                </div>
            </c:when>
            <c:otherwise>
                <table class="cart-table">
                    <thead>
                        <tr>
                            <th>Prodotto</th>
                            <th>Prezzo Unitario</th>
                            <th>Quantità</th>
                            <th>Subtotale</th>
                            <th>Azioni</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="entry" items="${requestScope.carrello}">
                            <c:set var="riga" value="${entry.value}" />
                            <tr>
                                <td>
                                    <c:out value="${riga.nomeProdotto}" />
                                    <br>
                                    <small>(ID: ${riga.idProdotto})</small>
                                </td>
                                <td><fmt:formatNumber value="${riga.prezzoUnitario}" type="currency" currencySymbol="€" minFractionDigits="2"/></td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/aggiornaQuantitaCarrello" method="post" style="display:inline-flex; align-items:center;">
                                        <input type="hidden" name="idProdotto" value="${riga.idProdotto}">
                                        <input type="hidden" name="tipoProdotto" value="${riga.tipoProdotto}">
                                        <input type="number" name="quantita" value="${riga.quantita}" min="0" class="quantity-input" onchange="this.form.submit()">
                                    </form>
                                </td>
                                <td>
                                    <c:set var="subtotale" value="${riga.prezzoUnitario * riga.quantita}" />
                                    <fmt:formatNumber value="${subtotale}" type="currency" currencySymbol="€" minFractionDigits="2"/>
                                </td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/AggiornaQuantitaCarrello" method="post">
                                        <input type="hidden" name="idProdotto" value="${riga.idProdotto}">
                                        <input type="hidden" name="tipoProdotto" value="${riga.tipoProdotto}">
                                        <input type="hidden" name="quantita" value="0">
                                        <button type="submit" class="btn remove-btn" onclick="return confirm('Sei sicuro di voler rimuovere l\'articolo dal carrello?');">Rimuovi</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                    <tfoot>
                        <tr>
                            <td colspan="3" style="text-align: right;">Totale Carrello:</td>
                            <td><strong><fmt:formatNumber value="${requestScope.totaleCarrello}" type="currency" currencySymbol="€" minFractionDigits="2"/></strong></td>
                            <td></td>
                        </tr>
                    </tfoot>
                </table>

                <div class="cart-actions">
                    <a href="${pageContext.request.contextPath}/homepage.jsp" class="btn">Continua lo Shopping</a>
                    <a href="${pageContext.request.contextPath}/processaCheckout" class="btn">Procedi al Checkout</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    <footer>
        <p>&copy; 2025 La Teca del Giardiniere</p>
    </footer>
</body>
</html>