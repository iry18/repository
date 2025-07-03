<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Checkout - La Teca del Giardiniere</title><link rel="stylesheet" type="homepage.css">
    <link rel="stylesheet" type="login1.css">
    
    <style>
        /* Stili temporanei o specifici per questa pagina */
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            color: #333;
            margin: 0;
            padding: 0;
        }
        .container {
            max-width: 960px;
            margin: 20px auto;
            background: #fff;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }
        h1, h2 {
            color: #4CAF50;
            text-align: center;
            margin-bottom: 20px;
        }
        .section {
            margin-bottom: 30px;
            border: 1px solid #ddd;
            padding: 20px;
            border-radius: 5px;
            background-color: #f9f9f9;
        }
        .form-group {
            margin-bottom: 15px;
        }
        .form-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        .form-group input[type="text"],
        .form-group input[type="email"],
        .form-group input[type="tel"],
        .form-group textarea,
        .form-group select {
            width: calc(100% - 20px);
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }
        .form-group input[type="radio"] {
            margin-right: 5px;
        }
        .button-container {
            text-align: center;
            margin-top: 30px;
        }
        .btn {
            background-color: #4CAF50;
            color: white;
            padding: 12px 25px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 1.1em;
            text-decoration: none;
            transition: background-color 0.3s ease;
        }
        .btn:hover {
            background-color: #45a049;
        }
        .order-summary {
            list-style: none;
            padding: 0;
            margin-bottom: 20px;
        }
        .order-summary li {
            padding: 8px 0;
            border-bottom: 1px dashed #eee;
            display: flex;
            justify-content: space-between;
        }
        .order-summary li:last-child {
            border-bottom: none;
        }
        .order-summary .total {
            font-weight: bold;
            font-size: 1.2em;
            color: #4CAF50;
            border-top: 2px solid #4CAF50;
            padding-top: 10px;
            margin-top: 10px;
        }
        .error-message {
            color: red;
            text-align: center;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Checkout</h1>

        <c:if test="${not empty errorMessage}">
            <p class="error-message">${errorMessage}</p>
        </c:if>

        <form action="ProcessaCheckoutServlet" method="post">

            <div class="section">
                <h2>Riepilogo Ordine</h2>
                <ul class="order-summary">
                    <c:choose>
                        <c:when test="${not empty articoliCarrello}">
                            <c:forEach var="item" items="${articoliCarrello}">
                                <li>
                                    <span>${item.nomeProdotto} (x${item.quantita})</span>
                                    <span><fmt:formatNumber value="${item.totaleArticolo}" type="currency" currencySymbol="€" /></span>
                                </li>
                            </c:forEach>
                            <li class="total">
                                <span>Totale Carrello:</span>
                                <span><fmt:formatNumber value="${totaleCarrello}" type="currency" currencySymbol="€" /></span>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li>Il tuo carrello non contiene articoli . <a href="homepage.jsp">Torna allo shopping</a></li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>

            <div class="section">
                <h2>Dettagli di Spedizione</h2>
                <div class="form-group">
                    <label for="nome">Nome:</label>
                    <input type="text" id="nome" name="nome" value="${utenteCorrente.nome}" required>
                </div>
                <div class="form-group">
                    <label for="cognome">Cognome:</label>
                    <input type="text" id="cognome" name="cognome" value="${utenteCorrente.cognome}" required>
                </div>
                <div class="form-group">
                    <label for="indirizzo">Indirizzo:</label>
                    <input type="text" id="indirizzo" name="indirizzo" value="${utenteCorrente.indirizzo}" required>
                </div>
                <div class="form-group">
                    <label for="citta">Città:</label>
                    <input type="text" id="citta" name="citta" value="${utenteCorrente.citta}" required>
                </div>
                <div class="form-group">
                    <label for="cap">CAP:</label>
                    <input type="text" id="cap" name="cap" value="${utenteCorrente.cap}" required pattern="[0-9]{5}" title="Inserisci un CAP valido di 5 cifre.">
                </div>
                <div class="form-group">
                    <label for="paese">Paese:</label>
                    <input type="text" id="paese" name="paese" value="${utenteCorrente.paese}" required>
                </div>
                <div class="form-group">
                    <label for="telefono">Telefono:</label>
                    <input type="tel" id="telefono" name="telefono" value="${utenteCorrente.telefono}" pattern="[0-9]{10}" title="Inserisci un numero di telefono valido di 10 cifre.">
                </div>
                <div class="form-group">
                    <label for="email">Email:</label>
                    <input type="email" id="email" name="email" value="${utenteCorrente.email}" required>
                </div>
                <div class="form-group">
                    <label for="note">Note sull'ordine (opzionale):</label>
                    <textarea id="note" name="note" rows="4"></textarea>
                </div>
            </div>

            <div class="section">
                <h2>Metodo di Pagamento</h2>
                <div class="form-group">
                    <label>
                        <input type="radio" name="metodoPagamento" value="Contrassegno" checked> Pagamento alla consegna (Contrassegno)
                    </label>
                </div>
                <div class="form-group">
                    <label>
                        <input type="radio" name="metodoPagamento" value="Bonifico Bancario"> Bonifico Bancario
                    </label>
                </div>
                <%-- Puoi aggiungere altri metodi di pagamento qui, es. carta di credito se prevedi integrazioni --%>
                <%-- <div class="form-group">
                    <label>
                        <input type="radio" name="metodoPagamento" value="Carta di Credito" disabled> Carta di Credito (Prossimamente)
                    </label>
                </div> --%>
            </div>

            <div class="button-container">
                <button type="submit" class="btn">Conferma Ordine</button>
            </div>
        </form>
    </div>
> 
</body>
</html>