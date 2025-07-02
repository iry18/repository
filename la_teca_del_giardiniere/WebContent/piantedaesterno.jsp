<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>


<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>La Teca del Giardiniere - Piante da Esterno</title>
    <link rel="stylesheet" href="piantedaesterno.css">
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
                <li><a href="<%= request.getContextPath() %>/about-us.jsp">ABOUT US</a></li>
                <li><a href="<%= request.getContextPath() %>/my-plants.jsp">MY PLANTS</a></li>
            </ul>
        </nav>
    </header>

    <section class="hero">
        <div class="hero-content">
            <div class="hero-image">
                <%-- Usa request.getContextPath() per il percorso delle immagini --%>
                <img src="<%= request.getContextPath() %>/images/logoesterno.png" alt="Logo Piante da Esterno"> <%-- Ho cambiato l'immagine del logo per riflettere le piante da esterno --%>
            </div>
            <div class="hero-text">
                <h1>Il Giardino dei Tuoi Sogni</h1> <%-- Titolo modificato per le piante da esterno --%>
                <p>"Trasforma il tuo balcone, terrazzo o giardino in un'oasi verde con la nostra selezione di piante da esterno resistenti e splendide!</p>
                <p>Che tu sia un esperto giardiniere o alle prime armi, abbiamo le specie perfette per ogni spazio e clima. Dal colore vibrante dei fiori alla maestosità degli arbusti, scopri come rendere ogni angolo del tuo esterno un capolavoro naturale."</p>
            </div>
        </div>
    </section>

    <section class="plants-grid">
        <%-- Inserisci qui le tue piante da esterno. Ecco alcuni esempi: --%>
        <div class="plant-card">
            <img src="<%= request.getContextPath() %>/images/rosa.png" alt="Rosa">
            <h3>Rosa</h3>
            <p>(La regina del giardino)</p>
        </div>
        <div class="plant-card">
            <img src="<%= request.getContextPath() %>/images/lavanda.png" alt="Lavanda">
            <h3>Lavanda</h3>
            <p>(Profumo di Provenza)</p>
        </div>
        <div class="plant-card">
            <img src="<%= request.getContextPath() %>/images/bougainvillea.png" alt="Bougainvillea">
            <h3>Bougainvillea</h3>
            <p>(Esplosione di colori)</p>
        </div>
        <div class="plant-card">
            <img src="<%= request.getContextPath() %>/images/gelsomino.png" alt="Gelsomino">
            <h3>Gelsomino</h3>
            <p>(Profumo inebriante)</p>
        </div>
        <div class="plant-card">
            <img src="<%= request.getContextPath() %>/images/oleandro.png" alt="Oleandro">
            <h3>Oleandro</h3>
            <p>(Fioritura mediterranea)</p>
        </div>
        <div class="plant-card">
            <img src="<%= request.getContextPath() %>/images/ortensia.png" alt="Ortensia">
            <h3>Ortensia</h3>
            <p>(Eleganza senza tempo)</p>
        </div>
    </section>

    <section class="additional-plants-grid">
        <%-- Inserisci qui altre piante da esterno, se ne hai: --%>
        <div class="plant-card">
            <img src="<%= request.getContextPath() %>/images/agave.png" alt="Agave">
            <h3>Agave</h3>
        </div>
        <div class="plant-card">
            <img src="<%= request.getContextPath() %>/images/cactus_esterno.png" alt="Cactus da Esterno">
            <h3>Cactus da Esterno</h3>
            <p>(Resistenti e affascinanti)</p>
        </div>
        <div class="plant-card">
            <img src="<%= request.getContextPath() %>/images/ulivo.png" alt="Ulivo">
            <h3>Ulivo</h3>
            <p>(Simbolo di pace e longevità)</p>
        </div>
        <div class="plant-card single-row">
            <img src="<%= request.getContextPath() %>/images/cycas.png" alt="Cycas">
            <h3>Cycas</h3>
        </div>
    </section>

    <footer>
        <p>&copy; 2025 La Teca del Giardiniere</p>
    </footer>

</body>
</html>