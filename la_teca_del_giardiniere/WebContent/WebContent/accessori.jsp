<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

    <!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>La Teca del Giardiniere - Accessori</title>
    <%-- Utilizza il CSS fornito, assicurandoti che il percorso sia corretto --%>
    <link rel="stylesheet" href="piantedainterni.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;700&display=swap" rel="stylesheet">
</head>

<body>
    <header>
        <nav>
            <ul>
                <%-- Assicurati che tutti i link puntino ai rispettivi JSP --%>
                <li><a href="homepage.jsp">HOME</a></li>
                <li><a href="piantedainterno.jsp">PIANTE INTERNO</a></li>
                <li><a href="piantedaesterno.jsp">PIANTE ESTERNO</a></li>
                <li><a href="accessori.jsp">ACCESSORI</a></li> 
                <li><a href="<%= request.getContextPath() %>/about-us.jsp">ABOUT US</a></li>
                <li><a href="<%= request.getContextPath() %>/my-plants.jsp">MY PLANTS</a></li>
            </ul>
        </nav>
    </header>

    <section class="hero">
        <div class="hero-content">
            <div class="hero-image">
                <%-- Immagine del vaso sorridente per la sezione hero degli accessori --%>
                <img src="<%= request.getContextPath() %>/images/vaso-sorridente.png" alt="Logo Accessori Piante"> <%-- Assicurati di avere questa immagine --%>
            </div>
            <div class="hero-text">
                <h1>il segreto delle piante</h1> <%-- Il titolo rimane uguale per coerenza con la pagina "il segreto delle piante" --%>
                <p>"Non tutti nasciamo con il pollice verde, e va benissimo così! A volte la vita è frenetica e prendersi cura delle piante può sembrare un'impresa. Ma non temere, abbiamo la soluzione perfetta per te!</p>
                <p>Scopri la nostra selezione di piante facili da amare e impossibili da uccidere. Belle, resistenti e a bassa manutenzione, sono l'ideale per chi vuole un tocco di verde in casa senza stress."</p>
            </div>
        </div>
    </section>

    <section class="plants-grid"> <%-- Usiamo plants-grid e plant-card per coerenza con il CSS esistente --%>
        <div class="plant-card">
            <img src="<%= request.getContextPath() %>/images/vaso-calendario.png" alt="Vaso Calendario">
            <h3>vaso-calendario</h3>
            <%-- Puoi aggiungere una breve descrizione qui se vuoi --%>
        </div>
        <div class="plant-card">
            <img src="<%= request.getContextPath() %>/images/contagocce.png" alt="Contagocce per Piante">
            <h3>contagocce</h3>
        </div>
        <div class="plant-card">
            <img src="<%= request.getContextPath() %>/images/set-regalo.png" alt="Set Regalo per Piante">
            <h3>set regalo</h3>
        </div>
    </section>

    <section class="additional-plants-grid"> <%-- Usiamo additional-plants-grid per la seconda riga --%>
        <div class="plant-card">
            <img src="<%= request.getContextPath() %>/images/set-professionale.png" alt="Set Professionale da Giardinaggio">
            <h3>set professionale</h3>
        </div>
        <div class="plant-card">
            <img src="<%= request.getContextPath() %>/images/vaso-intelligente.png" alt="Vaso Intelligente">
            <h3>vaso intelligente</h3>
        </div>
        <div class="plant-card">
            <img src="<%= request.getContextPath() %>/images/drenaggio-intelligente.png" alt="Sistema di Drenaggio Intelligente">
            <h3>drenaggio intelligente</h3>
        </div>
    </section>

    <footer>
        <p>&copy; 2025 La Teca del Giardiniere</p> <%-- Correggi il nome del copyright per coerenza --%>
    </footer>

</body>
</html>