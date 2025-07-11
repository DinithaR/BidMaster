<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<header class="fixed-top">
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <a class="navbar-brand d-flex align-items-center" href="index.jsp">
                <img src="assets/images/logo.png" alt="BidMaster Logo" height="40" class="me-2">
                <span>BidMaster</span>
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarMain" 
                    aria-controls="navbarMain" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarMain">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link" href="index.jsp">Home</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="ItemListServlet?action=listActive">Browse Auctions</a>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="categoriesDropdown" role="button" 
                           data-bs-toggle="dropdown" aria-expanded="false">
                            Categories
                        </a>
                        <ul class="dropdown-menu" aria-labelledby="categoriesDropdown">
                            <li><a class="dropdown-item" href="ItemListServlet?action=listByCategory&categoryId=1">Electronics</a></li>
                            <li><a class="dropdown-item" href="ItemListServlet?action=listByCategory&categoryId=2">Collectibles</a></li>
                            <li><a class="dropdown-item" href="ItemListServlet?action=listByCategory&categoryId=3">Fashion</a></li>
                            <li><a class="dropdown-item" href="ItemListServlet?action=listByCategory&categoryId=4">Home & Garden</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="ItemListServlet">All Categories</a></li>
                        </ul>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="ItemListServlet?action=listEndingSoon">Ending Soon</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="about.jsp">About Us</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="contact.jsp">Contact</a>
                    </li>
                </ul>
                
                <form class="d-flex me-2" action="ItemListServlet" method="get">
                    <input type="hidden" name="action" value="search">
                    <input class="form-control me-2" type="search" name="searchTerm" placeholder="Search items..." aria-label="Search">
                    <button class="btn btn-outline-light" type="submit">
                        <i class="fas fa-search"></i>
                    </button>
                </form>
                
                <div class="d-flex align-items-center">
                    <c:choose>
                        <c:when test="${sessionScope.userId != null}">
                            <!-- Logged in user -->
                            <div class="dropdown">
                                <a class="btn btn-outline-light dropdown-toggle" href="#" role="button" id="userDropdown" 
                                   data-bs-toggle="dropdown" aria-expanded="false">
                                    <i class="fas fa-user-circle me-1"></i>
                                    ${sessionScope.username}
                                </a>
                                <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
                                    <li><a class="dropdown-item" href="ProfileServlet">
                                        <i class="fas fa-user me-2"></i>My Profile
                                    </a></li>
                                    <li><a class="dropdown-item" href="WatchlistServlet">
                                        <i class="fas fa-heart me-2"></i>My Watchlist
                                    </a></li>
                                    <li><a class="dropdown-item" href="BidHistoryServlet">
                                        <i class="fas fa-gavel me-2"></i>My Bids
                                    </a></li>
                                    <c:if test="${sessionScope.role == 'seller' || sessionScope.role == 'admin'}">
                                        <li><a class="dropdown-item" href="MyItemsServlet">
                                            <i class="fas fa-box me-2"></i>My Items
                                        </a></li>
                                        <li><a class="dropdown-item" href="CreateItemServlet">
                                            <i class="fas fa-plus-circle me-2"></i>Create Auction
                                        </a></li>
                                    </c:if>
                                    <c:if test="${sessionScope.role == 'admin'}">
                                        <li><hr class="dropdown-divider"></li>
                                        <li><a class="dropdown-item" href="admin/dashboard.jsp">
                                            <i class="fas fa-tachometer-alt me-2"></i>Admin Dashboard
                                        </a></li>
                                    </c:if>
                                    <li><hr class="dropdown-divider"></li>
                                    <li><a class="dropdown-item" href="LogoutServlet">
                                        <i class="fas fa-sign-out-alt me-2"></i>Logout
                                    </a></li>
                                </ul>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <!-- Guest user -->
                            <a href="LoginServlet" class="btn btn-outline-light me-2">
                                <i class="fas fa-sign-in-alt me-1"></i> Login
                            </a>
                            <a href="RegisterServlet" class="btn btn-primary">
                                <i class="fas fa-user-plus me-1"></i> Register
                            </a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </nav>
</header>
