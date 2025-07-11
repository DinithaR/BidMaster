<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Browse Items - BidMaster</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <!-- Custom CSS -->
    <style>
        .page-header {
            background-color: #3a5a78;
            color: white;
            padding: 2rem 0;
            margin-bottom: 2rem;
        }
        .filter-section {
            background-color: #f8f9fa;
            padding: 20px;
            border-radius: 10px;
            margin-bottom: 20px;
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
            font-size: 0.8rem;
            color: #dc3545;
        }
        .bid-btn {
            background-color: #3a5a78;
            border-color: #3a5a78;
        }
        .bid-btn:hover {
            background-color: #2d4861;
            border-color: #2d4861;
        }
        .pagination-container {
            margin-top: 30px;
            margin-bottom: 50px;
        }
        .page-link {
            color: #3a5a78;
        }
        .page-item.active .page-link {
            background-color: #3a5a78;
            border-color: #3a5a78;
        }
    </style>
</head>
<body>
    <!-- Include Header -->
    <jsp:include page="includes/header.jsp" />
    
    <!-- Page Header -->
    <div class="page-header">
        <div class="container">
            <h1>
                <c:choose>
                    <c:when test="${not empty selectedCategory}">
                        ${selectedCategory.categoryName}
                    </c:when>
                    <c:when test="${not empty searchTerm}">
                        Search Results for "${searchTerm}"
                    </c:when>
                    <c:otherwise>
                        Browse All Items
                    </c:otherwise>
                </c:choose>
            </h1>
            <p class="lead mb-0">
                <c:choose>
                    <c:when test="${not empty selectedCategory}">
                        Browse all items in the ${selectedCategory.categoryName} category
                    </c:when>
                    <c:when test="${not empty searchTerm}">
                        Found ${totalItems} items matching your search
                    </c:when>
                    <c:otherwise>
                        Find great deals on thousands of items
                    </c:otherwise>
                </c:choose>
            </p>
        </div>
    </div>
    
    <div class="container mb-5">
        <!-- Filter Section -->
        <div class="filter-section">
            <form action="BrowseItemsServlet" method="get" id="filterForm">
                <div class="row g-3">
                    <div class="col-md-3">
                        <label for="categoryId" class="form-label">Category</label>
                        <select class="form-select" id="categoryId" name="categoryId">
                            <option value="">All Categories</option>
                            <c:forEach var="category" items="${categories}">
                                <option value="${category.categoryId}" ${selectedCategory.categoryId == category.categoryId ? 'selected' : ''}>
                                    ${category.categoryName}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <label for="sortBy" class="form-label">Sort By</label>
                        <select class="form-select" id="sortBy" name="sortBy">
                            <option value="endingSoon" ${sortBy == 'endingSoon' ? 'selected' : ''}>Ending Soon</option>
                            <option value="priceAsc" ${sortBy == 'priceAsc' ? 'selected' : ''}>Price: Low to High</option>
                            <option value="priceDesc" ${sortBy == 'priceDesc' ? 'selected' : ''}>Price: High to Low</option>
                            <option value="newest" ${sortBy == 'newest' ? 'selected' : ''}>Newest First</option>
                        </select>
                    </div>
                    <div class="col-md-2">
                        <label for="priceMin" class="form-label">Min Price</label>
                        <input type="number" class="form-control" id="priceMin" name="priceMin" min="0" step="0.01" value="${priceMin}">
                    </div>
                    <div class="col-md-2">
                        <label for="priceMax" class="form-label">Max Price</label>
                        <input type="number" class="form-control" id="priceMax" name="priceMax" min="0" step="0.01" value="${priceMax}">
                    </div>
                    <div class="col-md-2 d-flex align-items-end">
                        <button type="submit" class="btn bid-btn text-white w-100">Apply Filters</button>
                    </div>
                </div>
                
                <!-- Keep search term if present -->
                <c:if test="${not empty searchTerm}">
                    <input type="hidden" name="search" value="${searchTerm}">
                </c:if>
            </form>
        </div>
        
        <!-- Results Count -->
        <div class="d-flex justify-content-between align-items-center mb-4">
            <p class="mb-0">Showing ${(currentPage-1) * 20 + 1} - ${endItem} of ${totalItems} items</p>
            <div>
                <a href="HomeServlet" class="btn btn-outline-secondary">
                    <i class="fas fa-home me-2"></i>Back to Home
                </a>
            </div>
        </div>
        
        <!-- Items Grid -->
        <div class="row">
            <c:choose>
                <c:when test="${not empty items}">
                    <c:forEach var="item" items="${items}">
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
                </c:when>
                <c:otherwise>
                    <div class="col-12 text-center py-5">
                        <i class="fas fa-search fa-4x text-muted mb-3"></i>
                        <h3>No items found</h3>
                        <p class="text-muted">Try adjusting your search or filter criteria</p>
                        <a href="BrowseItemsServlet" class="btn bid-btn text-white mt-3">View All Items</a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
        
        <!-- Pagination -->
        <c:if test="${totalPages > 1}">
            <div class="pagination-container d-flex justify-content-center">
                <nav aria-label="Page navigation">
                    <ul class="pagination">
                        <!-- Previous button -->
                        <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                            <a class="page-link" href="#" onclick="navigateToPage(${currentPage - 1})" aria-label="Previous">
                                <span aria-hidden="true">&laquo;</span>
                            </a>
                        </li>
                        
                        <!-- Page numbers -->
                        <c:forEach begin="1" end="${totalPages}" var="pageNum">
                            <c:choose>
                                <c:when test="${pageNum == currentPage}">
                                    <li class="page-item active"><a class="page-link" href="#">${pageNum}</a></li>
                                </c:when>
                                <c:when test="${pageNum <= 3 || pageNum >= totalPages - 2 || Math.abs(pageNum - currentPage) <= 1}">
                                    <li class="page-item"><a class="page-link" href="#" onclick="navigateToPage(${pageNum})">${pageNum}</a></li>
                                </c:when>
                                <c:when test="${pageNum == 4 && currentPage > 4}">
                                    <li class="page-item disabled"><a class="page-link" href="#">...</a></li>
                                </c:when>
                                <c:when test="${pageNum == totalPages - 3 && currentPage < totalPages - 3}">
                                    <li class="page-item disabled"><a class="page-link" href="#">...</a></li>
                                </c:when>
                            </c:choose>
                        </c:forEach>
                        
                        <!-- Next button -->
                        <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                            <a class="page-link" href="#" onclick="navigateToPage(${currentPage + 1})" aria-label="Next">
                                <span aria-hidden="true">&raquo;</span>
                            </a>
                        </li>
                    </ul>
                </nav>
            </div>
        </c:if>
    </div>
    
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
        
        // Navigation function for pagination
        function navigateToPage(pageNumber) {
            const form = document.getElementById('filterForm');
            const pageInput = document.createElement('input');
            pageInput.type = 'hidden';
            pageInput.name = 'page';
            pageInput.value = pageNumber;
            form.appendChild(pageInput);
            form.submit();
            return false;
        }
    </script>
</body>
</html>
