<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>


<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestione Piante - La Teca del Giardiniere</title>
    <%-- Se hai un CSS globale (es. per header/footer o layout base), lascialo: --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/homepage.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;700&display=swap" rel="stylesheet">

    <%-- STILI INCLUSI DA listapiante.css + aggiunte per gestione_piante --%>
    <style>
        /* Stili generali del body e del container */
        body {
            font-family: 'Montserrat', Arial, sans-serif; /* Preferisci Montserrat se importato */
            background-color: #f4f4f4;
            margin: 0;
            padding: 20px;
            display: flex;
            justify-content: center;
            align-items: flex-start; /* Allinea in alto, non al centro verticale */
            min-height: 100vh; /* Assicura che occupi almeno l'altezza della viewport */
        }

        .container {
            background-color: #ffffff;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 1200px; /* Larghezza massima per evitare tabelle troppo larghe */
            box-sizing: border-box; /* Include padding e border nella larghezza */
        }

        h1 {
            text-align: center;
            color: #333;
            margin-bottom: 30px;
        }

        /* Stili per i messaggi di stato (successo/errore) */
        .message { /* Classe generica per messaggi */
            margin-bottom: 20px;
            padding: 15px;
            border-radius: 5px;
            font-weight: bold;
            text-align: center;
        }
        .message.success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        .message.error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        /* Stili per la tabella */
        .plants-table { /* Cambiato da 'table' a '.plants-table' per coerenza con il markup esistente e per specificità */
            width: 100%;
            border-collapse: collapse; /* Rimuove lo spazio tra i bordi delle celle */
            margin-top: 20px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.08);
        }

        .plants-table th, .plants-table td { /* Cambiato da 'table th, table td' */
            padding: 12px 15px;
            text-align: left;
            border-bottom: 1px solid #ddd; /* Bordi inferiori per le righe */
            vertical-align: middle; /* Allinea il contenuto delle celle al centro verticalmente */
        }

        .plants-table th { /* Cambiato da 'table th' */
            background-color: #4CAF50; /* Verde più scuro per l'intestazione */
            color: white;
            font-weight: bold;
            text-transform: uppercase;
            font-size: 0.9em;
        }

        .plants-table tbody tr:nth-child(even) { /* Cambiato da 'table tbody tr:nth-child(even)' */
            background-color: #f9f9f9; /* Sfondo alternato per le righe */
        }

        .plants-table tbody tr:hover { /* Cambiato da 'table tbody tr:hover' */
            background-color: #e2f0e2; /* Colore al passaggio del mouse sulle righe */
        }

        /* Stili per le celle delle azioni (Modifica, Elimina) */
        .plants-table td:last-child { /* Cambiato da 'table td:last-child' */
            white-space: nowrap; /* Evita che i link vadano a capo */
        }

        .plants-table td a { /* Cambiato da 'table td a' */
            text-decoration: none;
            color: #007bff; /* Blu per i link */
            margin-right: 10px;
            transition: color 0.3s ease;
        }

        .plants-table td a:hover { /* Cambiato da 'table td a:hover' */
            color: #0056b3;
            text-decoration: underline;
        }

        .plants-table td a.delete-btn { /* Ho usato .delete-btn come nel tuo markup, anziché .delete-link */
            color: #dc3545; /* Rosso per il link di eliminazione */
        }

        .plants-table td a.delete-btn:hover {
            color: #bd2130;
        }

        /* Stili per l'immagine nella tabella */
        .plants-table img {
            width: 50px;
            height: 50px;
            object-fit: cover;
            border-radius: 4px;
        }

        /* Stili per il messaggio "Non ci sono piante" */
        p.no-plants-message { /* Aggiunta una classe per specificità, se vuoi differenziarlo */
            text-align: center;
            color: #666;
            margin-top: 30px;
            font-style: italic;
        }

        /* Stili per il pulsante "Aggiungi Nuova Pianta" */
        .add-plant-button-container {
            text-align: center;
            margin-top: 30px;
            margin-bottom: 40px;
        }

        .add-plant-button-container a {
            background-color: #558b2f;
            color: white;
            padding: 12px 25px;
            border-radius: 5px;
            text-decoration: none;
            font-weight: bold;
            font-size: 1.1em;
            transition: background-color 0.3s ease;
        }

        .add-plant-button-container a:hover {
            background-color: #386a1a;
        }

        /* Stili per il link di ritorno alla home */
        .back-link {
            text-align: center;
            margin-top: 30px;
        }

        .back-link a {
            display: inline-block;
            background-color: #007bff;
            color: white;
            padding: 10px 20px;
            border-radius: 5px;
            text-decoration: none;
            transition: background-color 0.3s ease;
        }

        .back-link a:hover {
            background-color: #0056b3;
        }

        /* Media Queries per la responsività */
        @media (max-width: 768px) {
            .container {
                padding: 15px;
            }
            .plants-table, .plants-table thead, .plants-table tbody, .plants-table th, .plants-table td, .plants-table tr {
                display: block;
            }
            .plants-table thead tr {
                position: absolute;
                top: -9999px;
                left: -9999px;
            }
            .plants-table tr {
                border: 1px solid #ccc;
                margin-bottom: 10px;
                border-radius: 5px;
            }
            .plants-table td {
                border: none;
                border-bottom: 1px solid #eee;
                position: relative;
                padding-left: 50%;
                text-align: right;
            }
            .plants-table td:before {
                position: absolute;
                top: 6px;
                left: 6px;
                width: 45%;
                padding-right: 10px;
                white-space: nowrap;
                text-align: left;
                font-weight: bold;
            }
            /* Assegna le etichette ai campi per la modalità mobile */
            .plants-table td:nth-of-type(1):before { content: "ID:"; }
            .plants-table td:nth-of-type(2):before { content: "Immagine:"; } /* Aggiunto label per l'immagine */
            .plants-table td:nth-of-type(3):before { content: "Nome Comune:"; }
            .plants-table td:nth-of-type(4):before { content: "Tipo:"; }
            .plants-table td:nth-of-type(5):before { content: "Nome Scientifico:"; }
            .plants-table td:nth-of-type(6):before { content: "Categoria:"; }
            .plants-table td:nth-of-type(7):before { content: "Prezzo:"; }
            .plants-table td:nth-of-type(8):before { content: "Disponibilità:"; }
            .plants-table td:nth-of-type(9):before { content: "Azioni:"; } /* Se l'immagine è il 2°, le azioni sono il 9° */
        }
    </style>
</head>
<body>
    <header>
        <nav>
            <ul>
                <li><a href="adminHome.jsp">HOME ADMIN</a></li>
                <li><a href="homepage.jsp">HOME SITO</a></li>
                <li><a href="${pageContext.request.contextPath}/LoginServlet?action=logout">LOGOUT</a></li>
            </ul>
        </nav>
    </header>

    <section class="admin-section">
        <h1>Gestione Piante</h1>

        <%-- Visualizzazione dei messaggi dalla Servlet --%>
        <c:if test="${not empty messaggio}">
            <div class="message ${tipoMessaggio}">
                ${messaggio}
            </div>
        </c:if>

        <div class="add-plant-button-container">
            <a href="${pageContext.request.contextPath}/admin/pianteServlet?action=mostraFormInserimento">Aggiungi Nuova Pianta</a>
        </div>

        <h2>Elenco Piante</h2>
        <c:choose>
            <c:when test="${empty listaPiante}">
                <p class="no-plants-message">Nessuna pianta trovata nel database.</p>
            </c:when>
            <c:otherwise>
                <table class="plants-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Immagine</th> <%-- Colonna Immagine --%>
                            <th>Nome Comune</th>
                            <th>Tipo</th>
                            <th>Nome Scientifico</th>
                            <th>Categoria</th>
                            <th>Prezzo</th>
                            <th>Disponibilità</th>
                            <th>Azioni</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="p" items="${listaPiante}">
                            <tr>
                                <td>${p.id}</td>
                                <td>
                                    <c:if test="${not empty p.immagine}">
                                        <img src="${pageContext.request.contextPath}/images/${p.immagine}" alt="${p.nomeComune}">
                                    </c:if>
                                    <c:if test="${empty p.immagine}">
                                        N/D
                                    </c:if>
                                </td>
                                <td>${p.nomeComune}</td>
                                <td>${p.tipo ? 'Esterno' : 'Interno'}</td>
                                <td>${p.nomeScientificoBotanico}</td>
                                <td>${p.categoria}</td>
                                <td><fmt:formatNumber value="${p.prezzo}" type="currency" currencySymbol="€" minFractionDigits="2"/></td>
                                <td>${p.disponibilita}</td>
                                <td class="actions">
                                    <a href="${pageContext.request.contextPath}/admin/pianteServlet?action=mostraFormModifica&id=${p.id}" class="edit-btn">Modifica</a>
                                    <a href="${pageContext.request.contextPath}/admin/eliminapianteServlet?id=${p.id}" class="delete-btn" onclick="return confirm('Sei sicuro di voler eliminare questa pianta?');">Elimina</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </section>

    <footer>
        <p>&copy; 2025 La Teca del Giardiniere - Gestione Piante</p>
    </footer>

</body>
</html>