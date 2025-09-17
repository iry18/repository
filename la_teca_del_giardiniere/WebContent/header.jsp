<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>La Teca del Giardiniere</title>
    <link rel="stylesheet" href="header.css">
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:wght@400;700&family=Montserrat:wght@400;700&display=swap" rel="stylesheet">
</head>
<body>
<header>
    <div class="header-container">
        <div class="logo-area">
            <a href="<%= request.getContextPath() %>/homepage">
                <img src="<%= request.getContextPath() %>/images/logo.png" alt="La Teca del Giardiniere Logo" class="site-logo">
            </a>
        </div>
        
        <nav class="main-nav">
            <ul>
                <li><a href="<%= request.getContextPath() %>/homepage">HOME</a></li>
                <li><a href="${pageContext.request.contextPath}/catalogo-piante?tipo=interno">PIANTE INTERNO</a></li>
                <li><a href="${pageContext.request.contextPath}/catalogo-piante?tipo=esterno">PIANTE ESTERNO</a></li>
                <li><a href="${pageContext.request.contextPath}/catalogo-accessori">ACCESSORI</a></li>
            </ul>
        </nav>
        
        <div class="right-nav">
            <a href="${pageContext.request.contextPath}/visualizzaCarrello" class="nav-link cart-link">
                CARRELLO
                <img src="<%= request.getContextPath() %>/images/cart.svg" alt="Carrello">
            </a>
            <a href="<%= request.getContextPath() %>/userlogged/MyAccount.jsp" class="nav-link account-link"> 
            </a>
            
           <form action="${pageContext.request.contextPath}/catalogo-piante" method="GET" class="search-form">
			    <input type="hidden" name="tipo" value="${param.tipo != null ? param.tipo : ''}">
			    <input type="text" name="query" value="${param.query != null ? param.query : ''}">
			    <button type="submit">
			        <img src="<%= request.getContextPath() %>/images/search-icon.svg" alt="Cerca">
			    </button>
			</form>
        </div>
    </div>
</header>