<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error - BidMaster</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <!-- Custom CSS -->
    <style>
        body {
            background-color: #f8f9fa;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }
        .error-container {
            flex: 1;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 2rem;
        }
        .error-card {
            max-width: 600px;
            width: 100%;
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
        }
        .error-header {
            background-color: #dc3545;
            color: white;
            padding: 1.5rem;
            text-align: center;
        }
        .error-icon {
            font-size: 4rem;
            margin-bottom: 1rem;
        }
        .error-details {
            background-color: #f8d7da;
            color: #721c24;
            padding: 1rem;
            border-radius: 5px;
            margin-top: 1rem;
            white-space: pre-wrap;
            overflow-x: auto;
        }
        .btn-home {
            background-color: #3a5a78;
            border-color: #3a5a78;
        }
        .btn-home:hover {
            background-color: #2d4861;
            border-color: #2d4861;
        }
    </style>
</head>
<body>
    <!-- Include Header -->
    <jsp:include page="includes/header.jsp" />
    
    <div class="error-container">
        <div class="error-card">
            <div class="error-header">
                <div class="error-icon">
                    <i class="fas fa-exclamation-triangle"></i>
                </div>
                <h1 class="display-5">Oops! Something went wrong</h1>
                <p class="lead">We apologize for the inconvenience</p>
            </div>
            <div class="card-body bg-white p-4">
                <h5 class="card-title">Error Details</h5>
                
                <c:choose>
                    <c:when test="${not empty errorMessage}">
                        <p>${errorMessage}</p>
                    </c:when>
                    <c:when test="${not empty pageContext.exception}">
                        <p>${pageContext.exception.message}</p>
                        <div class="error-details">
                            <c:forEach var="stackTraceElement" items="${pageContext.exception.stackTrace}">
                                ${stackTraceElement}
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:when test="${not empty requestScope['jakarta.servlet.error.status_code']}">
                        <p>Error Code: ${requestScope['jakarta.servlet.error.status_code']}</p>
                        <p>Error Message: ${requestScope['jakarta.servlet.error.message']}</p>
                    </c:when>
                    <c:otherwise>
                        <p>An unknown error occurred. Please try again later.</p>
                    </c:otherwise>
                </c:choose>
                
                <div class="mt-4 text-center">
                    <a href="index.jsp" class="btn btn-primary btn-home">
                        <i class="fas fa-home me-2"></i>Return to Home
                    </a>
                </div>
                
                <div class="mt-3 text-center">
                    <small class="text-muted">
                        If the problem persists, please contact our support team at support@bidmaster.com
                    </small>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Include Footer -->
    <jsp:include page="includes/footer.jsp" />
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
