<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<html>
<head>
    <title>Registrazione</title>
</head>
<body>
    <h1>Registrazione</h1>

    <c:if test="${not empty erroreRegistrazione}">
    <div style="color: red;">
        <p>${erroreRegistrazione}</p>
    </div>
	</c:if>

    <c:if test="${not empty erroreGenerico}">
        <div style="color: red;">
            <p>${erroreGenerico}</p>
        </div>
    </c:if>

    <form action="RegistrazioneServlet" method="post">
        Nome: <input type="text" name="nome"><br>
        Cognome: <input type="text" name="cognome"><br>
        Email: <input type="email" name="email"><br>
        Password: <input type="password" name="password"><br>
        Indirizzo: <input type="text" name="indirizzo"><br>
        Città: <input type="text" name="citta"><br>
        CAP: <input type="text" name="cap"><br>
        Telefono: <input type="text" name="telefono"><br>
        Provincia: <input type="text" name="provincia"><br>
        <input type="submit" value="Registrati">
    </form>

    <c:if test="${param.registrazioneSuccesso}">
        <p style="color: green;">Registrazione avvenuta con successo! Puoi effettuare il login.</p>
    </c:if>
</body>
</html>