<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://xmlns.jcp.org/jsp/jstl/core" %>


<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Accedi - La Teca del Giardiniere</title>
    
    <link rel="login1.css">
    
</head>
<body>

    <div class="container">
        <div class="left-section">
            <div class="logo-container">
               <img src="${pageContext.request.contextPath}/images/logo.png" alt="Logo">
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
               <img src="${pageContext.request.contextPath}/images/edera.png" alt="Decorazione Edera">
            </div>
            
            <div class="login-form-container">
                <h1>Accedi al tuo account</h1>
                
                <form action="${pageContext.request.contextPath}/LoginServlet" method="post">
                    <div class="form-group">
                        <label for="email">Email:</label>
                        <input type="email" id="email" name="email" required value="${param.email}">
                    </div>
                    <div class="form-group">
                        <label for="password">Password:</label>
                        <input type="password" id="password" name="password" required>
                    </div>
                    <button type="submit">Accedi</button>

                    <%-- Gestione messaggi di errore/successo tramite parametri URL --%>
                    <c:if test="${not empty param.error}">
                        <div class="error-message">
                            <c:choose>
                                <c:when test="${param.error eq 'invalid_credentials'}">
                                     <p>Email e/o password non validi. Riprova.</p>
                                </c:when>
                                <c:when test="${param.error eq 'not_authenticated'}">
                                    <p>Devi effettuare l'accesso per accedere a questa risorsa.</p>
                                </c:when>
                                <c:when test="${param.error eq 'server_error'}">
                                    <p>Si è verificato un errore del server. Riprova più tardi.</p>
                                </c:when>
                                <c:when test="${param.error eq 'email_already_registered'}"> <%-- Nuovo messaggio da RegistrazioneServlet --%>
                                    <p>Questa email è già registrata. Prova ad accedere o usa un'altra email per la registrazione.</p>
                                </c:when>
                                <c:otherwise>
                                    <p>Si è verificato un errore sconosciuto.</p>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </c:if>

                    <c:if test="${not empty param.message}">
                        <div class="success-message">
                            <c:choose>
                                <c:when test="${param.message eq 'logged_out'}">
                                    <p>Logout effettuato con successo.</p>
                                </c:when>
                                <c:when test="${param.message eq 'registration_success'}"> <%-- Nuovo messaggio da RegistrazioneServlet --%>
                                    <p>Registrazione avvenuta con successo! Ora puoi effettuare il login.</p>
                                </c:when>
                                <c:otherwise>
                                    <p>Operazione completata.</p>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </c:if>
                </form>

                <div class="back-link">
                    <a href="${pageContext.request.contextPath}/homepage.jsp">Torna alla Home</a> <%-- Percorso corretto --%>
                </div>
                <div class="register-link">
                    <a href="${pageContext.request.contextPath}/registrati.jsp">Non hai un account? Registrati!</a> <%-- Percorso corretto --%>
                </div>
            </div>
            
        </div>
    </div>
</body>
</html>