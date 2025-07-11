<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BidMaster - Online Auction Platform</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <!-- Custom CSS -->
    <style>
        .hero-section {
            background: linear-gradient(rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.7)), url('assets/images/hero-bg.jpg');
            background-size: cover;
            background-position: center;
            color: white;
            padding: 100px 0;
            margin-bottom: 40px;
        }
        .category-card {
            transition: transform 0.3s;
            margin-bottom: 20px;
            height: 100%;
        }
        .category-card:hover {
            transform: translateY(-5px);
        }
        .item-card {
            transition: transform 0.3s;
            margin-bottom: 30px;
            height: 100%;
        }
        .item-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
        }
        .item-img {
            height: 200px;
            object-fit: cover;
        }
        .countdown {
            color: #dc3545;
            font-weight: bold;
        }
        .bid-btn {
            background-color: #3a5a78;
            border-color: #3a5a78;
        }
        .bid-btn:hover {
            background-color: #2d4861;
            border-color: #2d4861;
        }
        .featured-badge {
            position: absolute;
            top: 10px;
            right: 10px;
            background-color: #ffc107;
            color: #212529;
            font-size: 0.8rem;
            padding: 5px 10px;
            border-radius: 20px;
        }
        .search-section {
            background-color: #f8f9fa;
            padding: 30px 0;
            margin-bottom: 40px;
        }
    </style>
</head>
<body>
    <!-- Include Header -->
    <jsp:include page="includes/header.jsp" />
    
    <!-- Hero Section -->
    <section class="hero-section text-center">
        <div class="container">
            <h1 class="display-4 mb-4">Find Unique Items at Great Prices</h1>
            <p class="lead mb-5">Join thousands of users buying and selling on BidMaster</p>
            <div class="d-flex justify-content-center">
                <a href="#featured-items" class="btn btn-primary btn-lg me-3">
                    <i class="fas fa-gavel me-2"></i>Start Bidding
                </a>
                <c:if test="${empty sessionScope.userId}">
                    <a href="register.jsp" class="btn btn-outline-light btn-lg">
                        <i class="fas fa-user-plus me-2"></i>Register Now
                    </a>
                </c:if>
                <c:if test="${not empty sessionScope.userId && sessionScope.role != 'seller'}">
                    <a href="BecomeSellerServlet" class="btn btn-outline-light btn-lg">
                        <i class="fas fa-store me-2"></i>Become a Seller
                    </a>
                </c:if>
            </div>
        </div>
    </section>
    
    <!-- Search Section -->
    <section class="search-section">
        <div class="container">
            <form action="SearchServlet" method="get">
                <div class="row justify-content-center">
                    <div class="col-md-8">
                        <div class="input-group">
                            <input type="text" class="form-control form-control-lg" name="query" placeholder="Search for items...">
                            <select class="form-select form-select-lg" name="categoryId" style="max-width: 200px;">
                                <option value="">All Categories</option>
                                <c:forEach var="category" items="${categories}">
                                    <option value="${category.categoryId}">${category.categoryName}</option>
                                </c:forEach>
                            </select>
                            <button class="btn btn-primary btn-lg" type="submit">
                                <i class="fas fa-search"></i>
                            </button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </section>
    
    <!-- Categories Section -->
    <section class="mb-5">
        <div class="container">
            <h2 class="text-center mb-4">Browse Categories</h2>
            <div class="row">
                <c:forEach var="category" items="${categories}">
                    <div class="col-md-3 col-sm-6">
                        <div class="card category-card">
                            <div class="card-body text-center">
                                <i class="fas fa-${category.icon} fa-3x mb-3 text-primary"></i>
                                <h5 class="card-title">${category.categoryName}</h5>
                                <p class="card-text text-muted">${category.itemCount} items</p>
                                <a href="CategoryServlet?id=${category.categoryId}" class="btn btn-outline-primary">Browse</a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </section>
    
    <!-- Featured Items Section -->
    <section id="featured-items" class="mb-5">
        <div class="container">
            <h2 class="text-center mb-4">Featured Items</h2>
            <div class="row">
                <c:forEach var="item" items="${featuredItems}">
                    <div class="col-lg-3 col-md-4 col-sm-6">
                        <div class="card item-card">
                            <span class="featured-badge">Featured</span>
                            <img src="${empty item.imageUrl ? 'assets/images/no-image.png' : item.imageUrl}" 
                                 class="card-img-top item-img" alt="${item.title}">
                            <div class="card-body">
                                <h5 class="card-title">${item.title}</h5>
                                <p class="card-text text-truncate">${item.description}</p>
                                <div class="d-flex justify-content-between align-items-center mb-2">
                                    <span class="fw-bold">Current Bid:</span>
                                    <span class="text-primary">$<fmt:formatNumber value="${item.currentPrice}" pattern="#,##0.00"/></span>
                                </div>
                                <div class="d-flex justify-content-between align-items-center mb-3">
                                    <span class="fw-bold">Ends in:</span>
                                    <span class="countdown" data-end="${item.endTime}">
                                        ${item.endTime.toLocalDate()} ${item.endTime.toLocalTime().toString().substring(0, 5)}
                                    </span>
                                </div>
                                <div class="d-grid">
                                    <a href="ItemDetailsServlet?id=${item.itemId}" class="btn bid-btn text-white">
                                        <i class="fas fa-gavel me-2"></i>Bid Now
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <div class="text-center mt-4">
                <a href="BrowseItemsServlet" class="btn btn-outline-primary btn-lg">View All Items</a>
            </div>
        </div>
    </section>
    
    <!-- New Arrivals Section -->
    <section class="mb-5">
        <div class="container">
            <h2 class="text-center mb-4">New Arrivals</h2>
            <div class="row">
                <c:forEach var="item" items="${newItems}">
                    <div class="col-lg-3 col-md-4 col-sm-6">
                        <div class="card item-card">
                            <img src="${empty item.imageUrl ? 'assets/images/no-image.png' : item.imageUrl}" 
                                 class="card-img-top item-img" alt="${item.title}">
                            <div class="card-body">
                                <h5 class="card-title">${item.title}</h5>
                                <p class="card-text text-truncate">${item.description}</p>
                                <div class="d-flex justify-content-between align-items-center mb-2">
                                    <span class="fw-bold">Current Bid:</span>
                                    <span class="text-primary">$<fmt:formatNumber value="${item.currentPrice}" pattern="#,##0.00"/></span>
                                </div>
                                <div class="d-flex justify-content-between align-items-center mb-3">
                                    <span class="fw-bold">Ends in:</span>
                                    <span class="countdown" data-end="${item.endTime}">
                                        ${item.endTime.toLocalDate()} ${item.endTime.toLocalTime().toString().substring(0, 5)}
                                    </span>
                                </div>
                                <div class="d-grid">
                                    <a href="ItemDetailsServlet?id=${item.itemId}" class="btn bid-btn text-white">
                                        <i class="fas fa-gavel me-2"></i>Bid Now
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </section>
    
    <!-- How It Works Section -->
    <section class="mb-5 bg-light py-5">
        <div class="container">
            <h2 class="text-center mb-5">How It Works</h2>
            <div class="row text-center">
                <div class="col-md-4 mb-4 mb-md-0">
                    <div class="bg-white p-4 rounded shadow-sm h-100">
                        <i class="fas fa-search fa-3x text-primary mb-3"></i>
                        <h4>Find Items</h4>
                        <p class="text-muted">Browse through thousands of items or search for something specific.</p>
                    </div>
                </div>
                <div class="col-md-4 mb-4 mb-md-0">
                    <div class="bg-white p-4 rounded shadow-sm h-100">
                        <i class="fas fa-gavel fa-3x text-primary mb-3"></i>
                        <h4>Place Bids</h4>
                        <p class="text-muted">Bid on items you're interested in and keep track of your activity.</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="bg-white p-4 rounded shadow-sm h-100">
                        <i class="fas fa-trophy fa-3x text-primary mb-3"></i>
                        <h4>Win & Pay</h4>
                        <p class="text-muted">If you win, complete the payment and receive your item.</p>
                    </div>
                </div>
            </div>
        </div>
    </section>
    
    <!-- Include Footer -->
    <jsp:include page="includes/footer.jsp" />
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
        // Countdown timer for auctions
        function updateCountdowns() {
            document.querySelectorAll('.countdown').forEach(function(element) {
                const endTime = new Date(element.getAttribute('data-end')).getTime();
                const now = new Date().getTime();
                const distance = endTime - now;
                
                if (distance < 0) {
                    element.innerHTML = "Ended";
                } else {
                    const days = Math.floor(distance / (1000 * 60 * 60 * 24));
                    const hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
                    const minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
                    const seconds = Math.floor((distance % (1000 * 60)) / 1000);
                    
                    let countdownText = "";
                    if (days > 0) {
                        countdownText = days + "d " + hours + "h";
                    } else if (hours > 0) {
                        countdownText = hours + "h " + minutes + "m";
                    } else if (minutes > 0) {
                        countdownText = minutes + "m " + seconds + "s";
                    } else {
                        countdownText = seconds + "s";
                    }
                    
                    element.innerHTML = countdownText;
                }
            });
        }
        
        // Update countdowns immediately and then every second
        updateCountdowns();
        setInterval(updateCountdowns, 1000);
    </script>
</body>
</html>
