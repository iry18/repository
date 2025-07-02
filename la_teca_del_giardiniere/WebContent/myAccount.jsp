<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>La Teca del Giardiniere - Il Mio Account</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/piantedainterni.css"> <%-- O un CSS più generale per l'utente --%>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;700&display=swap" rel="stylesheet">
    <style>
        /* Stili specifici per la pagina del mio account */
        .user-account-section {
            max-width: 960px;
            margin: 40px auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }

        .user-account-section h1, .user-account-section h2 {
            color: #2e7d32;
            text-align: center;
            margin-bottom: 25px;
        }

        .user-info, .order-list, .cart-section {
            margin-bottom: 40px;
            padding: 20px;
            border: 1px solid #d0e0d0;
            border-radius: 8px;
            background-color: #f9fdf9;
        }

        .user-info h3, .order-list h3, .cart-section h3 {
            color: #388e3c;
            margin-bottom: 15px;
            text-align: left;
        }

        .user-info p {
            margin-bottom: 10px;
            line-height: 1.8;
        }

        .user-info .edit-link {
            display: inline-block;
            background-color: #558b2f;
            color: white;
            padding: 8px 15px;
            border-radius: 5px;
            text-decoration: none;
            font-size: 0.9em;
            margin-top: 10px;
            transition: background-color 0.3s ease;
        }
        .user-info .edit-link:hover {
            background-color: #386a1a;
        }

        /* Stili per tabelle (ordini e carrello) */
        .data-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        .data-table th, .data-table td {
            border: 1px solid #ddd;
            padding: 10px;
            text-align: left;
            font-size: 0.95em;
        }

        .data-table th {
            background-color: #e0f2e0;
            color: #558b2f;
            font-weight: bold;
            text-transform: uppercase;
        }

        .data-table tr:nth-child(even) {
            background-color: #f9fdf9;
        }

        .data-table tr:hover {
            background-color: #eafbea;
        }

        .data-table .action-link {
            color: #558b2f;
            text-decoration: none;
            font-weight: bold;
        }
        .data-table .action-link:hover {
            text-decoration: underline;
        }

        .cart-total {
            text-align: right;
            font-size: 1.1em;
            font-weight: bold;
            margin-top: 15px;
        }

        .cart-actions {
            text-align: right;
            margin-top: 20px;
        }
        .cart-actions button {
            background-color: #558b2f;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 1em;
            font-weight: bold;
            transition: background-color 0.3s ease;
            margin-left: 10px;
        }
        .cart-actions button:hover {
            background-color: #386a1a;
        }
        .cart-actions .empty-cart-btn {
            background-color: #dc3545;
        }
        .cart-actions .empty-cart-btn:hover {
            background-color: #c82333;
        }

        /* Messaggi di stato */
        .message {
            margin-bottom: 20px;
            padding: 15px;
            border-radius: 5px;
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
    </style>
</head>
<body>
    <header>
        <nav>
            <ul>
                <li><a href="<%= request.getContextPath() %>/index.jsp">HOME</a></li>
                <li><a href="piantedainterno.jsp">PIANTE INTERNO</a></li>
                <li><a href="piantedaesterno.jsp">PIANTE ESTERNO</a></li>
                <li><a href="accessori.jsp">ACCESSORI</a></li>
                <li><a href="<%= request.getContextPath() %>/about-us.jsp">ABOUT US</a></li>
                <li><a href="<%= request.getContextPath() %>/my-plants.jsp">MY PLANTS</a></li> <%-- Link a una pagina separata per le piante possedute --%>
                <li><a href="<%= request.getContextPath() %>/myAccount.jsp">IL MIO ACCOUNT</a></li> <%-- Link a questa pagina --%>
                <li><a href="<%= request.getContextPath() %>/LoginServlet?action=logout">LOGOUT</a></li>
            </ul>
        </nav>
    </header>

    <section class="user-account-section">
        <h1>Il Mio Account</h1>

        <%
            Utente currentUser = (Utente) session.getAttribute("currentUser"); // Recupera l'utente dalla sessione
            // Assicurati che l'utente sia loggato. Altrimenti, reindirizza al login.
            if (currentUser == null) {
                response.sendRedirect(request.getContextPath() + "/login.jsp"); // O la tua pagina di login
                return;
            }

            // Recupera messaggi di stato, se presenti
            String message = (String) request.getAttribute("message");
            String messageType = (String) request.getAttribute("messageType");
            if (message != null && !message.isEmpty()) {
        %>
            <div class="message <%= (messageType != null ? messageType : "") %>">
                <%= message %>
            </div>
        <%
            }

            // Recupera gli ordini e il carrello dalla richiesta/sessione
            List<Ordine> ordiniUtente = (List<Ordine>) request.getAttribute("ordiniUtente");
            Map<Integer, ProdottoNelCarrello> carrello = (Map<Integer, ProdottoNelCarrello>) session.getAttribute("carrello");

            if (ordiniUtente == null) ordiniUtente = new java.util.ArrayList<>();
            if (carrello == null) carrello = new java.util.LinkedHashMap<>(); // LinkedHashMap per mantenere l'ordine di inserimento
        %>

        <p style="text-align: center; color: #555; font-size: 1.2em; margin-bottom: 30px;">
            Ciao, **<%= currentUser.getUsername() %>**! Benvenuto nella tua area personale.
        </p>

        <div class="user-info">
            <h3>I Tuoi Dati Personali</h3>
            <p><strong>Nome:</strong> <%= currentUser.getNome() %></p>
            <p><strong>Cognome:</strong> <%= currentUser.getCognome() %></p>
            <p><strong>Username:</strong> <%= currentUser.getUsername() %></p>
            <p><strong>Email:</strong> <%= currentUser.getEmail() %></p>
            <p><strong>Città:</strong> <%= currentUser.getCitta() %></p>
            <p><strong>Indirizzo:</strong> <%= currentUser.getIndirizzo() %></p>
            <p><strong>CAP:</strong> <%= currentUser.getCap() %></p>
            <p><strong>Provincia:</strong> <%= currentUser.getProvincia() %></p>
            <p><strong>Telefono:</strong> <%= currentUser.getTelefono() %></p>
            <a href="<%= request.getContextPath() %>/UtenteServlet?action=modificaDati" class="edit-link">Modifica i Tuoi Dati</a> <%-- Link per modificare le info personali --%>
        </div>

        <div class="cart-section">
            <h3>Il Mio Carrello</h3>
            <% if (carrello.isEmpty()) { %>
                <p style="text-align: center; color: #777;">Il tuo carrello è vuoto. Inizia a fare acquisti!</p>
            <% } else { %>
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Prodotto</th>
                            <th>Quantità</th>
                            <th>Prezzo Unitario</th>
                            <th>Subtotale</th>
                            <th>Azioni</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            double totalCartPrice = 0.0;
                            for (Map.Entry<Integer, ProdottoNelCarrello> entry : carrello.entrySet()) {
                                ProdottoNelCarrello item = entry.getValue();
                                double subtotal = item.getQuantita() * item.getPrezzoUnitario();
                                totalCartPrice += subtotal;
                        %>
                            <tr>
                                <td><%= item.getNomeProdotto() %></td>
                                <td><%= item.getQuantita() %></td>
                                <td>€<%= String.format("%.2f", item.getPrezzoUnitario()) %></td>
                                <td>€<%= String.format("%.2f", subtotal) %></td>
                                <td>
                                    <a href="<%= request.getContextPath() %>/CarrelloServlet?action=rimuovi&idProdotto=<%= item.getIdProdotto() %>" class="action-link">Rimuovi</a>
                                    <%-- Potresti aggiungere link per aumentare/diminuire la quantità --%>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
                <div class="cart-total">
                    Totale Carrello: **€<%= String.format("%.2f", totalCartPrice) %>**
                </div>
                <div class="cart-actions">
                    <button type="button" class="empty-cart-btn" onclick="location.href='<%= request.getContextPath() %>/CarrelloServlet?action=svuota'">Svuota Carrello</button>
                    <button type="button" onclick="location.href='<%= request.getContextPath() %>/CheckoutServlet'">Procedi al Checkout</button>
                </div>
            <% } %>
        </div>

        <div class="order-list">
            <h3>I Miei Ordini</h3>
            <% if (ordiniUtente.isEmpty()) { %>
                <p style="text-align: center; color: #777;">Non hai ancora effettuato nessun ordine.</p>
            <% } else { %>
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>ID Ordine</th>
                            <th>Data</th>
                            <th>Stato</th>
                            <th>Totale</th>
                            <th>Dettagli</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Ordine ordine : ordiniUtente) { %>
                            <tr>
                                <td><%= ordine.getIdOrdine() %></td>
                                <td><%= new java.text.SimpleDateFormat("dd/MM/yyyy").format(ordine.getDataOrdine()) %></td> <%-- Assicurati che getDataOrdine ritorni un Date o Timestamp --%>
                                <td><%= ordine.getStatoOrdine() %></td>
                                <td>€<%= String.format("%.2f", ordine.getTotaleOrdine()) %></td>
                                <td><a href="<%= request.getContextPath() %>/OrdineServlet?action=dettagli&idOrdine=<%= ordine.getIdOrdine() %>" class="action-link">Vedi Dettagli</a></td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } %>
        </div>

    </section>

    <footer>
        <p>&copy; 2025 La Teca del Giardiniere</p>
    </footer>

</body>
</html>