<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Accedi - La Teca del Giardiniere</title>
    <link rel="stylesheet" href="login.css"> <%-- Link per login --%>
     <%-- Link per tornare alla home --%>
</head>
<body>
    <div class="login-container"> <%-- Contenitore per il form di login --%>
        <h1>Accedi al tuo account</h1>

        <form action="LoginServlet" method="post">
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required>
            </div>
            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required>
            </div>
            <button type="submit">Accedi</button>

            <c:if test="${not empty erroreLogin}">
                <div style="color: red; margin-top: 15px;">
                    <p>${erroreLogin}</p>
                </div>
            </c:if>

            <c:if test="${param.registrazioneSuccesso}">
                <p style="color: green; margin-top: 15px;">Registrazione avvenuta con successo! Puoi effettuare il login.</p>
            </c:if>
        </form>

        <div class="back-link" style="margin-top: 20px; text-align: center;">
            <a href="index.jsp">Torna alla Home</a> <%-- Link per tornare alla home --%>
        </div>
    </div>
</body>
</html>