<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">

<footer class="site-footer">
    <link rel="stylesheet" href="footer.css">
    
    <div class="footer-center">
    
        <div class="footer-logo-left">
            <a href="<%= request.getContextPath() %>/homepage">
                <img src="<%= request.getContextPath() %>/images/mascotte.png" alt="La Teca del Giardiniere" class="site-logo">
            </a>
        </div>

        <div class="footer-service-links">
            <ul>
                <a href="mailto:info@latecadelgiardiniere.it" aria-label="Email"><i class="fas fa-envelope"></i></a>
                <li><a href="${pageContext.request.contextPath}/termini-e-condizioni">Termini di Servizio</a></li>
                <li><a href="${pageContext.request.contextPath}/contatti">Contatti</a></li>
                <li><a href="${pageContext.request.contextPath}/faq">FAQ</a></li>
                <li><a href="${pageContext.request.contextPath}/chi-siamo">CHI SIAMO</a></li>
                <li><a href="${pageContext.request.contextPath}/registrazione">REGISTRATI</a></li>
            </ul>
        </div>
        
        <div class="footer-social-section">
            <div class="social-icons-circular">
                <a href="#" aria-label="Facebook"><i class="fab fa-facebook-f"></i></a>
                <a href="#" aria-label="Instagram"><i class="fab fa-instagram"></i></a>
                <a href="#" aria-label="Pinterest"><i class="fab fa-pinterest-p"></i></a>
                <a href="#" aria-label="Whatsapp"><i class="fab fa-whatsapp"></i></a>
            </div>
            
            <a href="#" class="language-button">
                Italiano
            </a>
        </div>
        
        <div class="footer-copyright-center">
            <p>&copy; 2025 La Teca del Giardiniere. Tutti i diritti riservati.</p>
        </div>
    </div>
</footer>