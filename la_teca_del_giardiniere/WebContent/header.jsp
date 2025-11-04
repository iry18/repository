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
    
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" integrity="sha512-SnH5WK+bZxgPHs44uWIX+LLMDJTM7jQyE+H1aB0T1iM5Wp7mB4q3F3pC/x5/381f/C/w2K5TqG5x/43p+H5/Q==" crossorigin="anonymous" referrerpolicy="no-referrer" />

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
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
    
    <a href="${pageContext.request.contextPath}/userlogged/MyAccount.jsp" class="nav-icon-link account-link" aria-label="Il mio Account">
    <i class="bi bi-flower1"></i> <span>ACCOUNT</span>
   </a>
    
    <a href="${pageContext.request.contextPath}/Carrello.jsp" class="nav-icon-link cart-link" aria-label="Carrello">
    <i class="bi bi-basket"></i>  <c:if test="${numeroArticoliCarrello > 0}">
        <span class="cart-badge">${numeroArticoliCarrello}</span>
    </c:if>
	</a>
    
    <form action="${pageContext.request.contextPath}/catalogo" method="GET" class="search-form-header"> 
        <input type="text" name="query" placeholder="Cerca Piante e Accessori..." value="${param.query != null ? param.query : ''}">
        <button type="submit" aria-label="Cerca">
            <i class="bi bi-search"></i>
        </button>
    </form>
</div>
</header>