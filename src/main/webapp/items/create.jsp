<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<%-- This is a simple success/failure page for item creation --%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Item Creation Status</title>
    <meta http-equiv="refresh" content="2;url=${pageContext.request.contextPath}/seller-dashboard.jsp">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <div class="container mt-5">
        <div class="card shadow">
            <div class="card-body text-center p-5">
                <c:choose>
                    <c:when test="${not empty successMessage}">
                        <i class="fas fa-check-circle text-success" style="font-size: 4rem;"></i>
                        <h2 class="mt-3">Success!</h2>
                        <p class="lead">${successMessage}</p>
                    </c:when>
                    <c:when test="${not empty errorMessage}">
                        <i class="fas fa-exclamation-circle text-danger" style="font-size: 4rem;"></i>
                        <h2 class="mt-3">Error</h2>
                        <p class="lead">${errorMessage}</p>
                    </c:when>
                    <c:otherwise>
                        <i class="fas fa-spinner fa-spin text-primary" style="font-size: 4rem;"></i>
                        <h2 class="mt-3">Processing...</h2>
                        <p class="lead">Please wait while we process your request.</p>
                    </c:otherwise>
                </c:choose>
                <p class="mt-4">Redirecting to dashboard in 2 seconds...</p>
                <a href="${pageContext.request.contextPath}/seller-dashboard.jsp" class="btn btn-primary">Return to Dashboard Now</a>
            </div>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/js/all.min.js"></script>
</body>
</html>
