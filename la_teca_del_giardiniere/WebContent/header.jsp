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
                    <li><a href="<%= request.getContextPath() %>/homepage">HOME La Teca del Giardiniere </a></li>
                    <li><a href="${pageContext.request.contextPath}/catalogo-piante?tipo=interno">PIANTE DA INTERNI</a></li>
                    <li><a href="${pageContext.request.contextPath}/catalogo-piante?tipo=esterno">PIANTE DA ESTERNO</a></li>
                    <li><a href="${pageContext.request.contextPath}/catalogo-accessori">ACCESSORI</a></li>
                    <li>
					    <a href="${pageContext.request.contextPath}/visualizzaCarrello" class="cart-icon">CARRELLO 
					        <span class="cart-badge">${numeroArticoliCarrello}</span>
					    </a>
					 </li>
                    <li><a href="<%= request.getContextPath() %>/userlogged/MyAccount.jsp">Account</a></li>
                </ul>
            </nav>
        </div>
    </header>
