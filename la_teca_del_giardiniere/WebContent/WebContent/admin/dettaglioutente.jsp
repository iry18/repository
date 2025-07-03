<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dettagli Utente e Ordini - Area Amministratore</title>
    <link rel="stylesheet" href="dettaglioutente.css"> </head>
<body>
    <div class="container">
        <h1>Dettagli Utente</h1>

        <c:if test="${not empty messaggio}">
            <div class="${tipoMessaggio}">${messaggio}</div> </c:if>

        <c:if test="${not empty utente}">
            <p><strong>ID:</strong> ${utente.getId()}</p>
            <p><strong>Username:</strong> ${utente.getUsername()}</p>
            <p><strong>Email:</strong> ${utente.getEmail()}</p>
            <p><strong>Data Registrazione:</strong> ${utente.getDataRegistrazione()}</p>
            <h2>Ordini Utente</h2>
            <c:if test="${empty listaOrdini}">
                <p>Nessun ordine effettuato da questo utente.</p>
            </c:if>
            <c:if test="${not empty listaOrdini}">
                <table>
                    <thead>
                        <tr>
                            <th>ID Ordine</th>
                            <th>Data Ordine</th>
                            <th>Importo Totale</th>
                            <th>Stato</th>
                            <th>Azioni</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="ordine" items="${listaOrdini}">
                            <tr>
                                <td>${ordine.getId()}</td>
                                <td>${ordine.getDataOrdine()}</td>
                                <td>${ordine.getImportoTotale()} €</td>
                                <td>${ordine.getStato()}</td>
                                <td><a href="dettaglioordineServlet?id=${ordine.getId()}">Dettagli</a></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>
        </c:if>

        <div class="back-link">
            <a href="utentiServlet">Torna alla Lista Utenti</a>
            <a href="adminHome.html">Torna all'Area Amministratore</a>
        </div>
    </div>
</body>
</html>