<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Errore 404 - Pagina non trovata</title>
</head>
	<style>
	body {
	    font-family: 'Montserrat', sans-serif;
	    background-color: #f4f8f4;
	    color: #333;
	    display: flex;
	    justify-content: center;
	    align-items: center;
	    height: 100vh;
	    margin: 0;
	    text-align: center;
	}
	
	.not-found-container {
	    padding: 20px;
	    background-color: #ffffff;
	    border-radius: 12px;
	    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
	    max-width: 500px;
	}
	
	.not-found-image {
	    max-width: 200px;
	    height: auto;
	    margin-bottom: 20px;
	}
	
	h1 {
	    font-family: 'Cormorant Garamond', serif;
	    font-size: 2.5em;
	    color: #3a5a40;
	    margin: 0 0 10px 0;
	}
	
	p {
	    font-size: 1em;
	    color: #588157;
	    margin-bottom: 25px;
	}
	
	.home-button {
	    display: inline-block;
	    padding: 12px 25px;
	    background-color: #588157;
	    color: white;
	    text-decoration: none;
	    font-weight: bold;
	    border-radius: 5px;
	    transition: background-color 0.3s ease;
	}
	
	.home-button:hover {
	    background-color: #3a5a40;
	}
	</style>

<body>
    <div class="not-found-container">
        <img src="${pageContext.request.contextPath}/images/404.png" alt="Pianta secca, simbolo di pagina non trovata" class="not-found-image">
        <h1>Ops! Questa pagina è secca.</h1>
        <p>Sembra che tu abbia perso la strada. Non temere, torna a coltivare il tuo giardino dalla nostra homepage.</p>
        <a href="${pageContext.request.contextPath}/homepage.jsp" class="home-button">Torna alla Home</a>
    </div>
</body>
</html>