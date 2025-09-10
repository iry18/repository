<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Il Mio Carrello - La Teca del Giardiniere</title>
    
    <style>
      
    header {
    background-color: white;
    padding: 10px 30px;
    border-bottom: none;
    display: flex;
    justify-content: center;
    align-items: center;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
		}
		
		.header-container {
		    display: flex;
		    justify-content: space-between;
		    align-items: center;
		    width: 100%;
		    max-width: 1200px;
		    padding: 10px 0;
		}
		
		.site-logo {
		    height: 40px;
		    width: auto;
		}
		
		nav {
		    display: flex;
		    flex-grow: 1;
		    justify-content: center;
		    align-items: center;
		}
		
		nav ul {
		    list-style: none;
		    padding: 0;
		    margin: 0;
		    display: flex;
		    align-items: center;
		    gap: 30px;
		}
		
		nav ul li a {
		    text-decoration: none;
		    color: #588157;
		    font-weight: bold;
		    font-size: 0.9em;
		    transition: color 0.3s ease;
		    font-family: 'Cormorant Garamond', serif;
		}
		
		nav ul li a:hover {
		    color: #3a5a40;
		} 
        .cart-container {
            width: 90%;
            max-width: 1000px;
            margin: 40px auto;
            padding: 30px;
            background-color: #fff;
            border-radius: 12px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
        }

        .cart-container h1 {
            font-family: 'Cormorant Garamond', serif;
            font-size: 2.8em;
            font-weight: 400;
            text-align: center;
            color: #3a5a40;
            margin-bottom: 30px;
        }

        /* Tabella del carrello */
        .cart-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            border: 1px solid #e0e0e0;
        }

        .cart-table th, .cart-table td {
            border: 1px solid #e0e0e0;
            padding: 15px;
            text-align: left;
            vertical-align: middle;
        }

        .cart-table th {
            background-color: #f4f8f4;
            font-family: 'Montserrat', sans-serif;
            font-weight: bold;
            color: #588157;
            text-transform: uppercase;
            font-size: 0.9em;
        }

        .cart-table tbody tr:nth-child(even) {
            background-color: #f9fdf9;
        }

        .cart-table tbody tr:hover {
            background-color: #eaf6ea;
        }

        .cart-table td small {
            color: #888;
            font-size: 0.8em;
        }

        /* Totale e azioni */
        .cart-total {
            text-align: right;
            margin-top: 25px;
            font-size: 1.5em;
            font-weight: bold;
            color: #3a5a40;
        }

        .cart-actions {
            text-align: center;
            margin-top: 40px;
            padding-top: 25px;
            border-top: 2px solid #e0f2e0;
        }

        .cart-actions .btn {
            display: inline-block;
            padding: 15px 30px;
            background-color: #588157;
            color: white;
            text-decoration: none;
            border-radius: 50px;
            border: none;
            cursor: pointer;
            font-family: 'Montserrat', sans-serif;
            font-size: 1em;
            font-weight: bold;
            text-transform: uppercase;
            margin: 10px;
            transition: background-color 0.3s ease, transform 0.2s ease;
        }

        .cart-actions .btn:hover {
            background-color: #3a5a40;
            transform: translateY(-2px);
        }

        /* Pulsanti specifici e input */
        .remove-btn {
            background-color: #b33951;
            padding: 10px 20px;
            border-radius: 50px;
            font-size: 0.8em;
        }

        .remove-btn:hover {
            background-color: #8c2a3e;
        }

        .quantity-input {
            width: 60px;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 6px;
            text-align: center;
            font-size: 0.9em;
            font-family: 'Montserrat', sans-serif;
            -moz-appearance: textfield;
        }

        .quantity-input::-webkit-outer-spin-button,
        .quantity-input::-webkit-inner-spin-button {
            -webkit-appearance: none;
            margin: 0;
        }

        /* Messaggi di stato */
        .message {
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 8px;
            font-family: 'Montserrat', sans-serif;
            font-size: 1em;
            font-weight: bold;
            text-align: center;
        }

        .message.success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        .message.error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        .message.warning {
            background-color: #fff3cd;
            color: #856404;
            border: 1px solid #ffeeba;
        }

        /* Carrello vuoto */
        .empty-cart-message {
            text-align: center;
            padding: 80px 20px;
            font-family: 'Montserrat', sans-serif;
            font-size: 1.2em;
            color: #777;
        }

        .empty-cart-message p {
            margin-bottom: 25px;
            font-family: 'Cormorant Garamond', serif;
            font-size: 1.5em;
            color: #588157;
        }
    </style>
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
                    <li><a href="${pageContext.request.contextPath}/visualizzaCarrello">CARRELLO</a></li>
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

        <c:choose><c:when test="${empty requestScope.carrello || requestScope.carrello.isEmpty()}">
                <div class="empty-cart-message">
                    <p>Il tuo carrello è vuoto. Inizia ad esplorare i nostri prodotti!</p>
                    <a href="${pageContext.request.contextPath}/homepage.jsp" class="btn">Vai allo Shopping</a>
                </div>
        </c:when><c:otherwise>
                <table class="cart-table">
                    <thead>
                        <tr>
                            <th>Prodotto</th>
                            <th>Prezzo Unitario</th>
                            <th>Quantità</th>
                            <th>Subtotale</th>
                            <th>Rimuovi</th>
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
                                    <form action="${pageContext.request.contextPath}/aggiornaQuantitaCarrello" method="post" class="update-form">
                                        <input type="hidden" name="idProdotto" value="${riga.idProdotto}">
                                        <input type="hidden" name="tipoProdotto" value="${riga.tipoProdotto}">
                                        <input type="number" name="quantita" value="${riga.quantita}" min="0" class="quantity-input" onchange="this.form.submit()">
                                 	    <button type="submit" class="btn">Aggiorna</button> 
                                    </form>
                                </td>
                                <td>
                                    <c:set var="subtotale" value="${riga.prezzoUnitario * riga.quantita}" />
                                    <fmt:formatNumber value="${subtotale}" type="currency" currencySymbol="€" minFractionDigits="2"/>
                                </td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/aggiornaQuantitaCarrello" method="post" class="remove-form">
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
                    <a href="${pageContext.request.contextPath}/visualizzaCheckout" class="btn">Procedi al Checkout</a>
                </div>
        </c:otherwise></c:choose>
    </div>
    <footer>
        <p>&copy; 2025 La Teca del Giardiniere</p>
    </footer>
</body>
</html>