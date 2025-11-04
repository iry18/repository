<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
<footer class="site-footer">
    <div class="footer-container">
        <link rel="stylesheet" href="footer.css">
        
        <div class="footer-section footer-logo">
            <a href="<%= request.getContextPath() %>/homepage">
                <img src="<%= request.getContextPath() %>/images/mascotte.png" alt="La Teca del Giardiniere" class="site-logo">
            </a>
        </div>
        
        <div class="footer-section footer-links">
            <h3>Esplora</h3>
            <ul>
                <li><a href="${pageContext.request.contextPath}/home">Home</a></li>
                <li><a href="${pageContext.request.contextPath}/catalogo-piante?tipo=interno">Piante da Interno</a></li>
                <li><a href="${pageContext.request.contextPath}/catalogo-piante?tipo=esterno">Piante da Esterno</a></li>
                <li><a href="${pageContext.request.contextPath}/catalogo-accessori">Accessori</a></li>
                <li><a href="${pageContext.request.contextPath}/MyAccount.jsp">Account</a></li>
            </ul>
        </div>

        <div class="footer-section footer-info">
            <h3>Contattaci</h3>
            <p><i class="fas fa-map-marker-alt"></i> Via del Giardino, 10 - 00100 Italy</p>
            <p><i class="fas fa-phone"></i> +39 06 1234 5678</p>
            <p><i class="fas fa-envelope"></i> info@latecadelgiardiniere.it</p>
            
            <div class="social-icons">
                <a href="#" aria-label="Facebook"><i class="fab fa-facebook-f"></i></a>
                <a href="#" aria-label="Instagram"><i class="fab fa-instagram"></i></a>
                <a href="#" aria-label="Pinterest"><i class="fab fa-pinterest-p"></i></a>
            </div>
        </div>
    
    <div class="footer-bottom">
        <p>&copy; 2025 La Teca del Giardiniere. Tutti i diritti riservati. | <a href="${pageContext.request.contextPath}/privacy">Privacy Policy</a></p>
    </div>
</footer>