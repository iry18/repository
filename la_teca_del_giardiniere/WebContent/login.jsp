<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Accedi - La Teca del Giardiniere</title>
    <%-- Link per login.css --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/login.css">
    
    <%-- Stili minimi per un buon aspetto predefinito, se login.css non è ancora completo --%>
    <style>
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; display: flex; justify-content: center; align-items: center; min-height: 100vh; background-color: #e0f2f1; margin: 0; }
        .login-container {
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.1);
            width: 100%;
            max-width: 400px;
            box-sizing: border-box;
            text-align: center;
        }
        h1 {
            color: #263238;
            margin-bottom: 25px;
            font-size: 1.8em;
        }
        .form-group {
            margin-bottom: 18px;
            text-align: left;
        }
        label {
            display: block;
            margin-bottom: 8px;
            color: #546e7a;
            font-weight: bold;
        }
        input[type="email"],
        input[type="password"] {
            width: calc(100% - 20px); /* Ajusta il padding */
            padding: 12px 10px;
            border: 1px solid #b0bec5;
            border-radius: 6px;
            box-sizing: border-box;
            font-size: 1em;
            transition: border-color 0.3s ease;
        }
        input[type="email"]:focus,
        input[type="password"]:focus {
            border-color: #00796b;
            outline: none;
            box-shadow: 0 0 0 3px rgba(0, 121, 107, 0.2);
        }
        button {
            width: 100%;
            padding: 12px 20px;
            background-color: #00796b;
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-size: 1.1em;
            font-weight: bold;
            transition: background-color 0.3s ease, transform 0.2s ease;
            margin-top: 15px;
        }
        button:hover {
            background-color: #004d40;
            transform: translateY(-2px);
        }
        .error-message {
            color: #d32f2f;
            margin-top: 15px;
            font-size: 0.95em;
            background-color: #ffebee;
            border: 1px solid #ef9a9a;
            padding: 10px;
            border-radius: 5px;
        }
        .success-message {
            color: #388e3c;
            margin-top: 15px;
            font-size: 0.95em;
            background-color: #e8f5e9;
            border: 1px solid #a5d6a7;
            padding: 10px;
            border-radius: 5px;
        }
        .back-link {
            margin-top: 25px;
            text-align: center;
        }
        .back-link a, .register-link a {
            color: #00796b;
            text-decoration: none;
            font-weight: bold;
            transition: color 0.3s ease;
        }
        .back-link a:hover, .register-link a:hover {
            color: #004d40;
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="login-container">
        <h1>Accedi al tuo account</h1>

        <form action="${pageContext.request.contextPath}/LoginServlet" method="post">
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required>
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
                            <p>Email o password non validi. Riprova.</p>
                        </c:when>
                        <c:when test="${param.error eq 'not_authenticated'}">
                            <p>Devi effettuare l'accesso per accedere a questa risorsa.</p>
                        </c:when>
                        <c:when test="${param.error eq 'server_error'}">
                            <p>Si è verificato un errore del server. Riprova più tardi.</p>
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
                        <c:when test="${param.message eq 'registration_success'}">
                            <p>Registrazione avvenuta con successo! Ora puoi effettuare il login.</p>
                        </c:when>
                        <c:otherwise>
                            <p>Operazione completata.</p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>

            <%-- Se preferisci passare l'errore come attributo di richiesta invece che parametro URL --%>
            <c:if test="${not empty erroreLogin}">
                <div class="error-message">
                    <p>${erroreLogin}</p>
                </div>
            </c:if>

        </form>

        <div class="back-link">
            <a href="${pageContext.request.contextPath}/homepage.jsp">Torna alla Home</a>
        </div>
        <div class="register-link" style="margin-top: 15px;">
            <a href="${pageContext.request.contextPath}/registrati.jsp">Non hai un account? Registrati!</a>
        </div>
    </div>
</body>
</html>