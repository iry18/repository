<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>La Teca del Giardiniere</title>
    
    <style>
        /* Stili di base per l'header */
	header {
	    background-color: #dcedc8; /* Sfondo verde chiaro */
	    padding: 10px 30px;
	    border-bottom: 1px solid #ddd;
	    display: flex;
	    align-items: center;
	    justify-content: center;
	}
	
	.header-container {
	    display: flex;
	    justify-content: space-between;
	    align-items: center;
	    width: 100%;
	    max-width: 1200px;
	    padding: 10px 0;
	}
	
	/* Area del logo e testo */
	.logo-area {
	    display: flex;
	    align-items: center;
	    gap: 15px; /* Spazio tra logo e testo */
	}
	
	.site-logo {
	    height: 40px; /* Altezza del logo */
	    width: auto;
	}
	/* Navigazione */
	nav ul {
	    list-style: none;
	    padding: 0;
	    margin: 0;
	    display: flex;
	    align-items: center;
	}
	
	nav ul li {
	    display: inline;
	    margin: 0 15px;
	}
	
	nav ul li a {
	    text-decoration: none;
	    color: #588157;
	    font-weight: normal;
	    font-size: 0.95em;
	    transition: color 0.3s ease;
	    font-family: 'Cormorant Garamond', serif;
	}
	
	nav ul li a:hover {
	    color: #3a5a40;
	}
    </style>
    
    <header>
    <div class="header-container">
        <div class="logo-area">
            <a href="homepage.jsp">
                <img src="${pageContext.request.contextPath}/images/logo.png" alt="La Teca del Giardiniere Logo" class="site-logo">
            </a>
            
        </div>
        <nav>
            <ul>
                <li><a href="homepage.jsp">HOME</a></li>
                <li><a href="${pageContext.request.contextPath}/catalogo-piante?tipo=interno">PIANTE INTERNO</a></li>
                <li><a href="${pageContext.request.contextPath}/catalogo-piante?tipo=esterno">PIANTE ESTERNO</a></li>
                <li><a href="${pageContext.request.contextPath}/catalogo-accessori">ACCESSORI</a></li>
                <li><a href="Carrello.jsp">CARRELLO</a></li>
                <li><a href="userlogged/MyAccount.jsp">Account</a></li>
            </ul>
        </nav>
    </div>
	</header>

</html>