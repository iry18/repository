<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

    
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>La Teca del Giardiniere - Piante da Interno</title>
    <%-- Usa request.getContextPath() per il percorso del CSS --%>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/piantedainterni.css"> <%-- AGGIUNTO: /css/ per una migliore organizzazione --%>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;700&display=swap" rel="stylesheet">
</head>

<body>
    <header>
        <nav>
            <ul>
                <%-- Usa request.getContextPath() per tutti i link di navigazione --%>
                <li><a href="homepage.jsp">HOME</a></li>
                <li><a href="piantedainterno.jsp">PIANTE INTERNO</a></li>
                <li><a href="piantedaesterno.jsp">PIANTE ESTERNO</a></li>
                <li><a href="accessori.jsp">ACCESSORI</a></li> 
                <li><a href="about-us.jsp">ABOUT US</a></li>
                <li><a href="my-plants.jsp">MY PLANTS</a></li>
                <% 
                    // Assicurati che l'oggetto Utente sia nella sessione e abbia i ruoli corretti
                    // Questo blocco andrebbe idealmente spostato in un tag file o una taglib personalizzata
                    // per evitare scriptlet e rendere la JSP più pulita.
                    src.com.la_teca_del_giardiniere.classes.Utente utenteLoggato = 
                        (src.com.la_teca_del_giardiniere.classes.Utente) session.getAttribute("loggedInUser");
                    
                    if (utenteLoggato != null && (utenteLoggato.isAdmin() || utenteLoggato.getRuoli().contains("venditore"))) {
                %>
                    <li><a href="<%= request.getContextPath() %>/ListaPianteServlet">GESTIONE PIANTE</a></li>
                    <li><a href="<%= request.getContextPath() %>/aggiungi-pianta.jsp">AGGIUNGI PIANTA</a></li>
                <%
                    }
                    if (utenteLoggato != null) {
                %>
                    <li><a href="<%= request.getContextPath() %>/logoutServlet">LOGOUT</a></li>
                <%
                    } else {
                %>
                    <li><a href="<%= request.getContextPath() %>/login.jsp">LOGIN</a></li>
                <%
                    }
                %>
            </ul>
        </nav>
    </header>

    <section class="hero">
        <div class="hero-content">
            <div class="hero-image">
                <%-- Usa request.getContextPath() per il percorso delle immagini --%>
                <img src="<%= request.getContextPath() %>/images/logointerni.png" alt="Logo Il Segreto delle Piante">
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
            <img src="<%= request.getContextPath() %>/images/sansevieria.png" alt="Sansevieria (Lingua di suocera)">
            <h3>Sansevieria</h3>
            <p>(Lingua di suocera)</p>
        </div>
        <div class="plant-card">
            <img src="<%= request.getContextPath() %>/images/zamioculcas.png" alt="Zamioculcas zamiifolia (Pianta di Zanzibar)">
            <h3>Zamioculcas zamiifolia</h3>
            <p>(Pianta di Zanzibar)</p>
        </div>
        <div class="plant-card">
            <img src="<%= request.getContextPath() %>/images/pothos.png" alt="Pothos (Epipremnum aureum)">
            <h3>Pothos</h3>
            <p>(Epipremnum aureum)</p>
        </div>
        <div class="plant-card">
            <img src="<%= request.getContextPath() %>/images/chlorophytum.png" alt="Chlorophytum comosum (Falangio)">
            <h3>Chlorophytum comosum</h3>
            <p>(Falangio)</p>
        </div>
        <div class="plant-card">
            <img src="<%= request.getContextPath() %>/images/hoya_carnosa.png" alt="Hoya carnosa (Fiore di cera)">
            <h3>Hoya carnosa</h3>
            <p>(Fiore di cera)</p>
        </div>
        <div class="plant-card">
            <img src="<%= request.getContextPath() %>/images/beaucarnea.png" alt="Beaucarnea recurvata (Nolina o Piede di elefante)">
            <h3>Beaucarnea recurvata</h3>
            <p>(Nolina o Piede di elefante)</p>
        </div>
    </section>

    <section class="additional-plants-grid">
        <div class="plant-card">
            <img src="<%= request.getContextPath() %>/images/crassula_ovata.png" alt="Crassula ovata">
            <h3>Crassula ovata</h3>
        </div>
        <div class="plant-card">
            <img src="<%= request.getContextPath() %>/images/spathiphyllum.png" alt="Spathiphyllum (Giglio della pace)">
            <h3>Spathiphyllum</h3>
            <p>(Giglio della pace)</p>
        </div>
        <div class="plant-card">
            <img src="<%= request.getContextPath() %>/images/aspidistra.png" alt="Aspidistra elatior (Pianta di piombo)">
            <h3>Aspidistra elatior</h3>
            <p>(Pianta di piombo)</p>
        </div>
        <div class="plant-card single-row">
            <img src="<%= request.getContextPath() %>/images/peperomia.png" alt="Peperomia">
            <h3>Peperomia</h3>
        </div>
    </section>

    <footer>
        <p>&copy; 2025 La Teca del Giardiniere</p>
    </footer>

</body>
</html>