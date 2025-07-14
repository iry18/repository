<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 

<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="la_teca_del_giardiniere.classes.Utente" %>
<%@ page import="la_teca_del_giardiniere.classes.Carrello" %>
<%@ page import="la_teca_del_giardiniere.classes.Ordine" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>La Teca del Giardiniere - Il Mio Account</title>
    
    <link rel="stylesheet" href="MyAccount.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;700&display=swap">
</head>
<body>
    <header>
        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/homepage.jsp">HOME</a></li>
                <li><a href="${pageContext.request.contextPath}/Piantedainterno.jsp">PIANTE INTERNO</a></li>
                <li><a href="${pageContext.request.contextPath}/Piantedaesterno.jsp">PIANTE ESTERNO</a></li>
                <li><a href="${pageContext.request.contextPath}/Accessori.jsp">ACCESSORI</a></li>
                <li><a href="${pageContext.request.contextPath}/Carrello.jsp">CARRELLO</a></li>
                <li><a href="${pageContext.request.contextPath}/MyAccount.jsp">MENU</a></li>
               
                <c:choose>
                    <c:when test="${sessionScope.currentUser != null}">
                        <li style="margin-left: 20px;"><a href="${pageContext.request.contextPath}/MyAccount.jsp">IL MIO ACCOUNT</a></li>
                        <li><a href="${pageContext.request.contextPath}/LogoutServlet">LOGOUT</a></li>
                    </c:when>
                    <c:otherwise>
                        <li><a href="${pageContext.request.contextPath}/Login.jsp">LOGIN</a></li>
                        <li><a href="${pageContext.request.contextPath}/Registra.jsp">REGISTRATI</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </nav>
    </header>

    <section class="user-account-section">
        <h1>Il Mio Account</h1>

      <%
            // Retrieve currentUser from session
            Utente currentUser = (Utente) session.getAttribute("currentUser");

            // If the user is not logged in, redirect. This must be done BEFORE any content is written.
            if (currentUser == null) {
                response.sendRedirect(request.getContextPath() + "/Login.jsp?error=not_authenticated");
                return; // Stop processing this JSP further
            }

            // Retrieve status messages from the request (e.g., after a data update)
            String message = (String) request.getAttribute("message");
            String messageType = (String) request.getAttribute("messageType");

            // Retrieve orders and cart. These should be set by a Servlet
            // (e.g., MyAccountServlet) that prepares the data before forwarding to this JSP.
            List<Ordine> ordiniUtente = (List<Ordine>) request.getAttribute("ordiniUtente");
            // Corrected type for carrello: It should be Map<Integer, Carrello> as defined in your imports
            Map<Integer, Carrello> carrello = (Map<Integer, Carrello>) session.getAttribute("carrello");

            // Initialize lists if null to prevent NullPointerExceptions in JSTL loops
            if (ordiniUtente == null) ordiniUtente = new java.util.ArrayList<>();
            if (carrello == null) carrello = new java.util.LinkedHashMap<>(); // LinkedHashMap to maintain insertion order
        %>

        <%-- Display status messages --%>
        <c:if test="${not empty requestScope.message}">
            <div class="message <c:out value='${requestScope.messageType}'/>">
                <c:out value="${requestScope.message}"/>
            </div>
        </c:if>

        <p style="text-align: center; color: #555; font-size: 1.2em; margin-bottom: 30px;">
            Ciao, **<%= currentUser.getNome() %> <%= currentUser.getCognome() %>**! Benvenuto nella tua area personale.
        </p>

        <div class="user-info">
            <h3>I Tuoi Dati Personali</h3>
            <p><strong>Nome:</strong> <%= currentUser.getNome() %></p>
            <p><strong>Cognome:</strong> <%= currentUser.getCognome() %></p>
            <p><strong>Email:</strong> <%= currentUser.getEmail() %></p>
            <p><strong>Città:</strong> <%= currentUser.getCitta() %></p>
            <p><strong>Indirizzo:</strong> <%= currentUser.getIndirizzo() %></p>
            <p><strong>CAP:</strong> <%= currentUser.getCAP() %></p>
            <p><strong>Provincia:</strong> <%= currentUser.getProvincia() %></p>
            <p><strong>Telefono:</strong> <%= currentUser.getTelefono() %></p>
            <a href="${pageContext.request.contextPath}/UtenteServlet?action=modificaDati" class="edit-link">Modifica i Tuoi Dati</a>
        </div>

        <div class="cart-section">
            <h3>Il Mio Carrello</h3>
           <c:choose>
                <c:when test="${empty sessionScope.carrello || sessionScope.carrello.size() == 0}">
                    <p style="text-align: center; color: #777;">Il tuo carrello è vuoto. Inizia a fare acquisti!</p>
                </c:when>
                <c:otherwise>
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
                            <c:set var="totalCartPrice" value="0.0" />
                            <c:forEach var="itemEntry" items="${sessionScope.carrello}">
                                <c:set var="item" value="${itemEntry.value}" />
                                <c:set var="subtotal" value="${item.quantita * item.prezzoUnitario}" />
                                <c:set var="totalCartPrice" value="${totalCartPrice + subtotal}" />
                                <tr>
                                    <td><c:out value="${item.nomeProdotto}"/></td>
                                    <td><c:out value="${item.quantita}"/></td>
                                    <td>€<fmt:formatNumber value="${item.prezzoUnitario}" pattern="0.00"/></td>
                                    <td>€<fmt:formatNumber value="${subtotal}" pattern="0.00"/></td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/CarrelloServlet?action=rimuovi&idProdotto=${item.idProdotto}" class="action-link">Rimuovi</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <div class="cart-total">
                        Totale Carrello: **€<fmt:formatNumber value="${totalCartPrice}" pattern="0.00"/>**
                    </div>
                    <div class="cart-actions">
                        <button type="button" class="empty-cart-btn" onclick="location.href='${pageContext.request.contextPath}/CarrelloServlet?action=svuota'">Svuota Carrello</button>
                        <button type="button" onclick="location.href='${pageContext.request.contextPath}/CheckoutServlet'">Procedi al Checkout</button>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="order-list">
            <h3>I Miei Ordini</h3>
            <c:choose>
                <c:when test="${empty requestScope.ordiniUtente || requestScope.ordiniUtente.size() == 0}">
                    <p style="text-align: center; color: #777;">Non hai ancora effettuato nessun ordine.</p>
                </c:when>
                <c:otherwise>
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
                           <c:forEach var="ordine" items="${requestScope.ordiniUtente}">
                                <tr>
                                    <td><c:out value="${ordine.idOrdine}"/></td>
                                    <td><fmt:formatDate value="${ordine.dataOrdine}" pattern="dd/MM/yyyy"/></td>
                                    <td><c:out value="${ordine.statoOrdine}"/></td>
                                    <td>€<fmt:formatNumber value="${ordine.totaleOrdine}" pattern="0.00"/></td>
                                    <td><a href="${pageContext.request.contextPath}/OrdineServlet?action=dettagli&idOrdine=${ordine.idOrdine}" class="action-link">Vedi Dettagli</a></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>

    </section>

    <footer>
        <p>&copy; 2025 La Teca del Giardiniere</p>
    </footer>

</body>
</html>