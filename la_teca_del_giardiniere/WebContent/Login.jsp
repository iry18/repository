<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - La Teca del Giardiniere</title>
    <link rel="stylesheet" href="Login.css">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;500;600&display=swap" rel="stylesheet">
</head>
<body>
    <div class="container">
       <div class="left-section">
            <div class="top-decoration">
                <img src="<%= request.getContextPath() %>/images/edera.png" alt="Decorazione Edera">
            </div>

            <div class="login-box">

                <%-- Messaggio di Registrazione Avvenuta con Successo --%>
                <c:if test="${param.registration == 'success'}">
                    <p style="color: green; text-align: center; font-weight: bold;">
                        Registrazione avvenuta con successo! Ora puoi accedere con le tue credenziali.
                    </p>
                </c:if>

                <%-- Messaggio Email Già Registrata --%>
                <c:if test="${param.error == 'email_already_registered'}">
                    <p style="color: red; text-align: center; font-weight: bold;">
                        Questa email è già registrata. Per favore, effettua il login.
                    </p>
                </c:if>

                <%-- Messaggi di Errore Generici o di Login Fallito --%>
                <c:if test="${not empty errorMessage}">
                    <p style="color: red; text-align: center;">${errorMessage}</p>
                </c:if>
                <c:if test="${param.error == 'invalid_credentials'}">
                    <p style="color: red; text-align: center;">Credenziali non valide. Riprova.</p>
                </c:if>
                <c:if test="${param.error == 'server_error'}">
                    <p style="color: red; text-align: center;">Si è verificato un errore del server. Riprova più tardi.</p>
                </c:if>
                <c:if test="${not empty sessionScope.errorMessage}">
                    <p style="color: red; text-align: center;">${sessionScope.errorMessage}</p>
                    <c:remove var="errorMessage" scope="session"/>
                </c:if>


                <form action="<%= request.getContextPath() %>/LoginServlet" method="post">
                    <%-- IMPORTANTE: Assicurati che 'name' sia "email" qui per corrispondere alla Servlet --%>
                    <input type="text" id="email" name="email" placeholder="Email" required>
                    <input type="password" id="password" name="password" placeholder="Password" required>

                    <button type="submit">LOGIN</button>
                </form>

                <div class="register-link">
                    <p>Non hai un account? <a href="Registra.jsp">Registrati qui</a></p>
                </div>
            </div>
        </div>


       <div class="right-section">
            <div class="logo-container">
                <img src="<%= request.getContextPath() %>/images/logo.png" alt="Logo" class="logo-img">
                <div class="logo-text">
                    <h1>LA</h1>
                    <h2>TECA</h2>
                    <h3>DEL</h3>
                    <h4>GIARDINIERE</h4>
                </div>
            </div>
            <p class="tagline">COMFORT WITH PLANT-BASED<br>INGREDIENTS AND LOTS OF LOVE</p>
        </div>
    </div>
</body>
</html>