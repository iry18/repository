<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%-- Necessario per i tag JSTL --%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>La Teca del Giardiniere - Piante da Interno</title>
    <%-- Usa pageContext.request.contextPath per il percorso del CSS --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/piantedainterni.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;700&display=swap" rel="stylesheet">
</head>

<body>
    <header>
        <nav>
            <ul>
                <%-- Usa pageContext.request.contextPath per tutti i link di navigazione --%>
                <li><a href="${pageContext.request.contextPath}/homepage.jsp">HOME</a></li>
                <li><a href="${pageContext.request.contextPath}/piantedainterno.jsp">PIANTE INTERNO</a></li>
                <li><a href="${pageContext.request.contextPath}/piantedaesterno.jsp">PIANTE ESTERNO</a></li>
                <li><a href="${pageContext.request.contextPath}/accessori.jsp">ACCESSORI</a></li>
                <li><a href="${pageContext.request.contextPath}/about-us.jsp">ABOUT US</a></li>
                <li><a href="${pageContext.request.contextPath}/my-plants.jsp">MY PLANTS</a></li>

                <%-- JSTL per la gestione della visibilità dei link --%>
                <%-- Assumiamo che l'utente loggato sia salvato in sessione come "loggedInUser" --%>
                <%-- E che l'oggetto Utente abbia una property 'admin' (getter isAdmin()) e un metodo getRuoli() --%>
                <c:set var="utenteLoggato" value="${sessionScope.loggedInUser}"/>

                <c:if test="${utenteLoggato != null && (utenteLoggato.admin || utenteLoggato.ruoli.contains('venditore'))}">
                    <li><a href="${pageContext.request.contextPath}/ListaPianteServlet">GESTIONE PIANTE</a></li>
                    <li><a href="${pageContext.request.contextPath}/aggiungi-pianta.jsp">AGGIUNGI PIANTA</a></li>
                </c:if>

                <c:if test="${utenteLoggato != null}">
                    <li><a href="${pageContext.request.contextPath}/LogoutServlet">LOGOUT</a></li>
                </c:if>
                <c:if test="${utenteLoggato == null}">
                    <li><a href="${pageContext.request.contextPath}/login.jsp">LOGIN</a></li>
                </c:if>
            </ul>
        </nav>
    </header>

    <section class="hero">
        <div class="hero-content">
            <div class="hero-image">
                <%-- Usa pageContext.request.contextPath() per il percorso delle immagini --%>
                <img src="${pageContext.request.contextPath}/images/logointerni.png" alt="Logo Il Segreto delle Piante">
            </div>
            <div class="hero-text">
                <h1>il segreto delle piante</h1>
                <p>"Non tutti nasciamo con il pollice verde, e va benissimo così! A volte la vita è frenetica e prendersi cura delle piante può sembrare un'impresa. Ma non temere, abbiamo la soluzione perfetta per te!</p>
                <p>Scopri la nostra selezione di piante facili da amare e impossibili da uccidere. Belle, resistenti e a bassa manutenzione, sono l'ideale per chi vuole un tocco di verde in casa senza stress."</p>
            </div>
        </div>
    </section>

    <section class="plants-grid">
        <div class="plant-card">
            <img src="${pageContext.request.contextPath}/images/sansevieria.png" alt="Sansevieria (Lingua di suocera)">
            <h3>Sansevieria</h3>
            <p>(Lingua di suocera)</p>
        </div>
        <div class="plant-card">
            <img src="${pageContext.request.contextPath}/images/zamioculcas.png" alt="Zamioculcas zamiifolia (Pianta di Zanzibar)">
            <h3>Zamioculcas zamiifolia</h3>
            <p>(Pianta di Zanzibar)</p>
        </div>
        <div class="plant-card">
            <img src="${pageContext.request.contextPath}/images/pothos.png" alt="Pothos (Epipremnum aureum)">
            <h3>Pothos</h3>
            <p>(Epipremnum aureum)</p>
        </div>
        <div class="plant-card">
            <img src="${pageContext.request.contextPath}/images/chlorophytum.png" alt="Chlorophytum comosum (Falangio)">
            <h3>Chlorophytum comosum</h3>
            <p>(Falangio)</p>
        </div>
        <div class="plant-card">
            <img src="${pageContext.request.contextPath}/images/hoya_carnosa.png" alt="Hoya carnosa (Fiore di cera)">
            <h3>Hoya carnosa</h3>
            <p>(Fiore di cera)</p>
        </div>
        <div class="plant-card">
            <img src="${pageContext.request.contextPath}/images/beaucarnea.png" alt="Beaucarnea recurvata (Nolina o Piede di elefante)">
            <h3>Beaucarnea recurvata</h3>
            <p>(Nolina o Piede di elefante)</p>
        </div>
    </section>

    <section class="additional-plants-grid">
        <div class="plant-card">
            <img src="${pageContext.request.contextPath}/images/crassula_ovata.png" alt="Crassula ovata">
            <h3>Crassula ovata</h3>
        </div>
        <div class="plant-card">
            <img src="${pageContext.request.contextPath}/images/spathiphyllum.png" alt="Spathiphyllum (Giglio della pace)">
            <h3>Spathiphyllum</h3>
            <p>(Giglio della pace)</p>
        </div>
        <div class="plant-card">
            <img src="${pageContext.request.contextPath}/images/aspidistra.png" alt="Aspidistra elatior (Pianta di piombo)">
            <h3>Aspidistra elatior</h3>
            <p>(Pianta di piombo)</p>
        </div>
        <div class="plant-card single-row">
            <img src="${pageContext.request.contextPath}/images/peperomia.png" alt="Peperomia">
            <h3>Peperomia</h3>
        </div>
    </section>

    <footer>
        <p>&copy; 2025 La Teca del Giardiniere</p>
    </footer>

</body>
</html>