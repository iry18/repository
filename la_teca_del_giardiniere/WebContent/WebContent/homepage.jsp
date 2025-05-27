<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>La Teca del Giardiniere</title>
    <link rel="stylesheet" href="homepage.css">
</head>
<body>
    <header>
        <nav>
            <ul>
                <li><a href="homepage.jsp">HOME</a></li>
                <li><a href="piantedainterno.jsp">PIANTE INTERNO</a></li>
                <li><a href="piantedaesterno.jsp">PIANTE ESTERNO</a></li>
                <li><a href="accessori.jsp">ACCESSORI</a></li> 
                <li><a href="<%= request.getContextPath() %>/about-us.jsp">ABOUT US</a></li>
                <li><a href="<%= request.getContextPath() %>/menu.jsp">MENU</a></li>
            </ul>
        </nav>
    </header>

    <section class="hero">
        <div class="hero-left">
            <div class="logo-container">
                <h1>LA</h1>
                <h2>TECA</h2>
                <h3>DEL</h3>
                <h4>GIARDINIERE</h4>
            </div>
            <p class="tagline">COMFORT WITH PLANT-BASED<br>INGREDIENTS AND LOTS OF LOVE</p>
        </div>
        <div class="hero-right">
            <img src="<%= request.getContextPath() %>/images/presentazionepiante.png" alt="Selezione di piante">
        </div>
    </section>

    <section class="featured-plants">
        <h2></h2>
        <div class="plants-grid">
            <div class="plant-card">
                <img src="<%= request.getContextPath() %>/images/sansevieria.png" alt="Sansevieria">
                <h3>Sansevieria</h3>
                <p>(Lingua di suocera)</p>
            </div>
            <div class="plant-card">
                <img src="<%= request.getContextPath() %>/images/zamioculcas.png" alt="Zamioculcas">
                <h3>Zamioculcas zamiifolia</h3>
                <p>(Pianta di Zanzibar)</p>
            </div>
            <div class="plant-card">
                <img src="<%= request.getContextPath() %>/images/pothos.png" alt="Pothos">
                <h3>Pothos</h3>
                <p>(Epipremnum aureum)</p>
            </div>
            <div class="plant-card">
                <img src="<%= request.getContextPath() %>/images/falangio.png" alt="Chlorophytum">
                <h3>Chlorophytum comosum</h3>
                <p>(Falangio)</p>
            </div>
        </div>
        <div class="secret-text">
            <h2>il segreto delle piante</h2>
            <p>"SEI UN 'KILLER DI PIANTE'? NON PREOCCUPARTI, TI CAPIAMO!</p>
            <p>MA ABBIAMO UNA BUONA NOTIZIA: ESISTONO PIANTE CHE SOPRAVVIVONO ANCHE AI POLLICI NERI PIÙ OSTAANTI."</p>
            <p>SCOPRI LA NOSTRA SELEZIONE DI PIANTE INDISTRUTTIBILI E TRASFORMA LA TUA CASA IN UNA GIUNGLA URBANA... SENZA IL MINIMO SFORZO!"</p>
        </div>
    </section>

    <section class="plant-details">
        <div class="plant-details-top">
            <div class="plant-category">
                <a href="piantedainterno.jsp"> <%-- Link alla pagina Piante Interno --%>
                    <img src="<%= request.getContextPath() %>/images/pianta-interno-icon.png" alt="Piante da Interno">
                    <h3>PIANTE<br>DA<br>INTERNO</h3>
                </a>
            </div>
            <div class="accessories">
                <a href="accessori.jsp"> <%-- Link alla pagina Accessori --%>
                    <h3>ACCESSORI</h3>
                </a>
            </div>
            <div class="plant-category">
                <a href="piantedaesterno.jsp"> <%-- Link alla pagina Piante Esterno --%>
                    <img src="<%= request.getContextPath() %>/images/pianta-esterno-icon.png" alt="Piante da Esterno">
                    <h3>PIANTE<br>DA<br>ESTERNO</h3>
                </a>
            </div>
        </div>
        <div class="pine-loricato">
            <h2>Il pino loricato <span class="scientific-name">(Pinus heldreichii)</span></h2>
            <p>Questi pini sono resistenti a condizioni ambientali estreme, con inverni rigidi, forti venti e scarsità di suolo.</p>
            <p>La loro chioma è spesso irregolare e contorta a causa delle avversità, conferendo loro un aspetto scultoreo e affascinante.</p>
            <p>Sono un simbolo di resilienza e adattamento, e rappresentano un elemento distintivo del paesaggio montano appenninico.</p>
            <img src="<%= request.getContextPath() %>/images/pino-loricato-removebg-preview.png" alt="Pino Loricato">
        </div>
        <div class="featured-plants-repeat">
            <div class="plants-grid">
                <div class="plant-card">
                    <img src="<%= request.getContextPath() %>/images/linguadellasuocera.png" alt="Sansevieria">
                    <h3>Sansevieria</h3>
                    <p>(Lingua di suocera)</p>
                </div>
                <div class="plant-card">
                    <img src="<%= request.getContextPath() %>/images/zamioculcas-small.png" alt="Zamioculcas">
                    <h3>Zamioculcas zamiifolia</h3>
                    <p>(Pianta di Zanzibar)</p>
                </div>
                <div class="plant-card">
                    <img src="<%= request.getContextPath() %>/images/pothos-small.png" alt="Pothos">
                    <h3>Pothos</h3>
                    <p>(Epipremnum aureum)</p>
                </div>
                <div class="plant-card">
                    <img src="<%= request.getContextPath() %>/images/chlorophytum-small.png" alt="Chlorophytum">
                    <h3>Chlorophytum comosum</h3>
                    <p>(Falangio)</p>
                </div>
            </div>
        </div>
    </section>

    <footer>
        <p>&copy; 2025 La Teca del Giardiniere. Tutti i diritti riservati.</p>
    </footer>
</body>
</html>