<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

 <!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>La Teca del Giardiniere </title>
    <link rel="stylesheet" href="Registra.css">
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
            <nav class="main-nav">
                <%-- Display error message if present --%>
                <c:if test="${not empty errorMessage}">
                    <p style="color: red; text-align: center;">${errorMessage}</p>
                </c:if>

                <form action="<%= request.getContextPath() %>/RegistrazioneServlet" method="post">
                    <div class="form-group">
                        <label for="nome">Nome</label>
                        <input type="text" id="nome" name="nome" required value="${param.nome}">
                    </div>
                    <div class="form-group">
                        <label for="cognome">Cognome</label>
                        <input type="text" id="cognome" name="cognome" required value="${param.cognome}">
                    </div>
                    <div class="form-group">
                        <label for="username">Username</label>
                        <input type="text" id="username" name="username" required value="${param.username}">
                    </div>
                    <div class="form-group">
                        <label for="email">Email</label>
                        <input type="email" id="email" name="email" required value="${param.email}">
                    </div>
                    <div class="form-group">
                        <label for="password">Password</label>
                        <input type="password" id="password" name="password" required>
                    </div>
                    <div class="form-group">
                        <label for="citta">Città</label>
                        <input type="text" id="citta" name="citta" value="${param.citta}">
                    </div>
                    <div class="address-group">
                        <div class="form-group address-indirizzo">
                            <label for="indirizzo">Indirizzo</label>
                            <input type="text" id="indirizzo" name="indirizzo" value="${param.indirizzo}">
                        </div>
                        <div class="form-group address-cap">
                            <label for="cap">CAP</label>
                            <input type="text" id="cap" name="cap" value="${param.cap}">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="provincia">Provincia</label>
                        <input type="text" id="provincia" name="provincia" value="${param.provincia}">
                    </div>
                    <div class="form-group">
                        <label for="telefono">Telefono</label>
                        <input type="text" id="telefono" name="telefono" value="${param.telefono}">
                    </div>
                    <div class="register-button">
                        <button type="submit">REGISTRATI</button>
                    </div>
                    <div class="login-link">
                    <p>Sei già registrato? <a href="<%= request.getContextPath() %>/Login.jsp">ACCEDI</a></p>
                </div>
                </form>
            </nav>
        </div>
    </div>
</body>