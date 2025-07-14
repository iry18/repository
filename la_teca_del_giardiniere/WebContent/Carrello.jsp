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
        /* Stili di base per il carrello se non hai un carrello.css specifico */
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: flex-start; /* Align at the top */
            min-height: 100vh; /* Full viewport height */
            padding-top: 20px; /* Add some space from the top */
        }
        .cart-container {
            width: 80%;
            max-width: 900px; /* Max width for better readability on large screens */
            margin: 20px auto;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 8px;
            background-color: #fff;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1); /* Leggera ombra per un aspetto migliore */
        }
        h1 {
            text-align: center;
            color: #333;
            margin-bottom: 25px;
        }
        .cart-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        .cart-table th, .cart-table td {
            border: 1px solid #ddd;
            padding: 12px; /* Aumentato il padding per migliore leggibilità */
            text-align: left;
            vertical-align: middle; /* Allinea verticalmente il contenuto */
        }
        .cart-table th {
            background-color: #f2f2f2;
            font-weight: bold;
            color: #555;
        }
        .cart-table tbody tr:nth-child(even) {
            background-color: #f9f9f9; /* Colore di sfondo alternato per le righe */
        }
        .cart-total {
            text-align: right;
            margin-top: 20px;
            font-size: 1.3em; /* Leggermente più grande */
            font-weight: bold;
            color: #333;
        }
        .cart-actions {
            text-align: center;
            margin-top: 30px;
            padding-top: 20px;
            border-top: 1px solid #eee; /* Separatore visivo */
        }
        .cart-actions .btn {
            display: inline-block;
            padding: 12px 25px; /* Padding aumentato */
            background-color: #4CAF50; /* Verde, puoi cambiarlo */
            color: white;
            text-decoration: none;
            border-radius: 5px;
            border: none;
            cursor: pointer;
            font-size: 1.1em; /* Testo più grande */
            margin: 8px; /* Margine aumentato */
            transition: background-color 0.3s ease; /* Effetto hover più fluido */
        }
        .cart-actions .btn:hover {
            background-color: #45a049;
        }
        .remove-btn {
            background-color: #f44336; /* Rosso per rimuovi */
            padding: 8px 15px; /* Padding leggermente ridotto per i pulsanti azione nella tabella */
            font-size: 0.9em;
            margin: 0; /* Rimuovi margine per inline-flex */
        }
        .remove-btn:hover {
            background-color: #da190b;
        }
        .quantity-input {
            width: 70px; /* Larghezza leggermente aumentata */
            padding: 8px; /* Padding aumentato */
            border: 1px solid #ccc; /* Bordo leggermente più scuro */
            border-radius: 4px; /* Raggio del bordo leggermente aumentato */
            text-align: center;
            -moz-appearance: textfield; /* Rimuove le frecce per Firefox */
        }
        .quantity-input::-webkit-outer-spin-button,
        .quantity-input::-webkit-inner-spin-button {
            -webkit-appearance: none; /* Rimuove le frecce per Chrome/Safari */
            margin: 0;
        }
        .small-btn { /* Stile per il pulsante "Aggiorna" nella riga del carrello */
            padding: 8px 15px;
            font-size: 0.9em;
            background-color: #007bff; /* Blu per l'aggiornamento */
            margin-left: 5px;
        }
        .small-btn:hover {
            background-color: #0056b3;
        }
        .message {
            padding: 15px; /* Aumentato il padding */
            margin-bottom: 20px; /* Aumentato il margine */
            border-radius: 8px; /* Raggio del bordo aumentato */
            font-size: 1.1em;
            font-weight: bold;
            text-align: center;
        }
        .message.success {
            background-color: #d4edda;
            color: #155724;
            border-color: #c3e6cb;
        }
        .message.error {
            background-color: #f8d7da;
            color: #721c24;
            border-color: #f5c6cb;
        }
        .empty-cart-message {
            text-align: center;
            padding: 80px 20px; /* Padding aumentato per centrare meglio */
            font-size: 1.3em;
            color: #777;
            background-color: #fefefe;
            border-radius: 8px;
            margin-top: 30px;
        }
        .empty-cart-message p {
            margin-bottom: 25px;
        }
    </style>
</head>
<body>

    <div class="cart-container">
        <h1>Il Mio Carrello</h1>

        <%-- Messaggi di Feedback (Successo/Errore) --%>
        <c:if test="${not empty requestScope.messaggio}">
            <div class="message ${requestScope.tipoMessaggio}">
                <c:out value="${requestScope.messaggio}"/>
            </div>
        </c:if>

        <%-- Visualizzazione Carrello --%>
        <c:choose>
            <c:when test="${empty requestScope.articoliCarrello}">
                <div class="empty-cart-message">
                    <p>Il tuo carrello è vuoto. Inizia ad esplorare i nostri prodotti!</p>
                    <a href="homepage.jsp" class="btn">Vai allo Shopping</a>
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
                        <%-- QUESTO È IL BLOCCO CORRETTO PER ITERARE SUGLI ARTICOLI --%>
                        <c:forEach var="item" items="${requestScope.articoliCarrello}">
                            <tr>
                                <td><c:out value="${item.nomeProdotto}" /></td>
                                <td><fmt:formatNumber value="${item.prezzoUnitario}" type="currency" currencySymbol="€" /></td>
                                <td>
                                    <form action="<%= request.getContextPath() %>/aggiornaQuantitaCarrello" method="post" style="display:inline-flex; align-items:center;">
                                        <input type="hidden" name="carrelloId" value="${item.carrelloId}">
                                        <input type="number" name="quantita" value="${item.quantita}" min="0" class="quantity-input" onchange="this.form.submit()">
                                        <%-- Rimosso il pulsante "Aggiorna" per un aggiornamento automatico al cambio, come da codice precedente --%>
                                    </form>
                                </td>
                                <td><fmt:formatNumber value="${item.totaleArticolo}" type="currency" currencySymbol="€" /></td>
                                <td>
                                    <form action="<%= request.getContextPath() %>/rimuoviDalCarrello" method="post">
                                        <input type="hidden" name="carrelloId" value="${item.carrelloId}">
                                        <button type="submit" class="btn remove-btn">Rimuovi</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                    <tfoot>
                        <tr>
                            <td colspan="3" style="text-align: right;">Totale Carrello:</td>
                            <td><strong><fmt:formatNumber value="${requestScope.totaleCarrello}" type="currency" currencySymbol="€" /></strong></td>
                            <td></td>
                        </tr>
                    </tfoot>
                </table>

                <div class="cart-actions">
                    <a href="homepage.jsp" class="btn">Continua lo Shopping</a>
                    <a href="ProcessaCheckoutServlet" class="btn">Procedi al Checkout</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

</body>
</html>