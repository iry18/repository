<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registrazione Effettuata</title>
    <link rel="stylesheet" href="registrati.css"> <%-- Or a more generic style sheet --%>
</head>
<body>
    <div class="container">
        <div class="left-section">
            <div class="logo-container">
                <img src="<%= request.getContextPath() %>/images/logo.png" alt="Logo">
                <div class="logo-text">
                    <h1>LA</h1>
                    <h2>TECA</h2>
                    <h3>DEL</h3>
                    <h4>GIARDINIERE</h4>
                </div>
            </div>
            <p class="tagline">COMFORT WITH PLANT-BASED<br>INGREDIENTS AND LOTS OF LOVE</p>
        </div>
        <div class="right-section navigation-section">
            <div class="top-decoration">
                <img src="<%= request.getContextPath() %>/images/edera.png" alt="Decorazione Edera">
            </div>
            <div style="text-align: center; padding: 50px;">
                <h2>🥳 Registrazione Effettuata con Successo!</h2>
                <p>Grazie per esserti registrato a La Teca del Giardiniere. Ora puoi effettuare il login.</p>
                <p><a href="Login.jsp">Vai alla pagina di Login</a></p>
            </div>
        </div>
    </div>
</body>
</html>