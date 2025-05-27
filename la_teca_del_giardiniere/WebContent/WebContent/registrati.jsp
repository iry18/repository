<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

    <!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>La Teca del Giardiniere - Registrati</title>
    <link rel="stylesheet" href="registrati.css">
</head>

<body>
    <div class="container">
        <div class="left-section">
            <div class="logo-container">
                <%-- Usa request.getContextPath() per il percorso delle immagini.
                     ATTENZIONE: le immagini in WEB-INF/ non sono direttamente accessibili dal browser.
                     Dovresti spostarle in una cartella accessibile come /images/ o /img/ sotto la radice della webapp.
                     Ho ipotizzato che le sposterai in /images/.
                --%>
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
                <%-- L'action del form punterà alla tua Servlet di registrazione --%>
                <form action="<%= request.getContextPath() %>/RegistrazioneServlet" method="post">
                    <div class="form-group">
                        <label for="nome">Nome</label>
                        <input type="text" id="nome" name="nome" required>
                    </div>
                    <div class="form-group">
                        <label for="cognome">Cognome</label>
                        <input type="text" id="cognome" name="cognome" required>
                    </div>
                    <div class="form-group">
                        <label for="username">Username</label>
                        <input type="text" id="username" name="username" required>
                    </div>
                    <div class="form-group">
                        <label for="email">Email</label>
                        <input type="email" id="email" name="email" required>
                    </div>
                    <div class="form-group">
                        <label for="password">Password</label>
                        <input type="password" id="password" name="password" required>
                    </div>
                    <div class="form-group">
                        <label for="citta">Città</label>
                        <input type="text" id="citta" name="citta">
                    </div>
                    <div class="address-group">
                        <div class="form-group address-indirizzo">
                            <label for="indirizzo">Indirizzo</label>
                            <input type="text" id="indirizzo" name="indirizzo">
                        </div>
                        <div class="form-group address-cap">
                            <label for="cap">CAP</label>
                            <input type="text" id="cap" name="cap">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="provincia">Provincia</label>
                        <input type="text" id="provincia" name="provincia">
                    </div>
                    <div class="form-group">
                        <label for="telefono">Telefono</label>
                        <input type="text" id="telefono" name="telefono">
                    </div>
                    <div class="register-button">
                        <button type="submit">REGISTRATI</button>
                    </div>
                </form>
            </nav>
        </div>
    </div>
</body>
</html>