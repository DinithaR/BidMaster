<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle} - BidMaster</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="assets/css/style.css">
</head>
<body>
    <!-- Include Header -->
    <jsp:include page="../includes/header.jsp" />
    
    <div class="container py-5 my-5">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h1>${pageTitle}</h1>
            
            <!-- Search Form -->
            <form action="ItemListServlet" method="get" class="d-flex">
                <input type="hidden" name="action" value="search">
                <input type="text" name="searchTerm" class="form-control me-2" placeholder="Search items..." value="${searchTerm}">
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-search"></i>
                </button>
            </form>
        </div>
        
        <!-- Filters -->
        <div class="card mb-4">
            <div class="card-body">
                <div class="row">
                    <div class="col-md-4 mb-3 mb-md-0">
                        <label class="form-label">Category</label>
                        <select class="form-select" id="categoryFilter">
                            <option value="">All Categories</option>
                            <option value="1" ${categoryId == 1 ? 'selected' : ''}>Electronics</option>
                            <option value="2" ${categoryId == 2 ? 'selected' : ''}>Collectibles</option>
                            <option value="3" ${categoryId == 3 ? 'selected' : ''}>Fashion</option>
                            <option value="4" ${categoryId == 4 ? 'selected' : ''}>Home & Garden</option>
                        </select>
                    </div>
                    <div class="col-md-4 mb-3 mb-md-0">
                        <label class="form-label">Sort By</label>
                        <select class="form-select" id="sortFilter">
                            <option value="endTime">Ending Soon</option>
                            <option value="priceAsc">Price: Low to High</option>
                            <option value="priceDesc">Price: High to Low</option>
                            <option value="newest">Newest</option>
                        </select>
                    </div>
                    <div class="col-md-4">
                        <label class="form-label">Status</label>
                        <select class="form-select" id="statusFilter">
                            <option value="">All Status</option>
                            <option value="active">Active</option>
                            <option value="ending">Ending Soon</option>
                            <option value="completed">Completed</option>
                        </select>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Items Grid -->
        <div class="row g-4">
            <c:choose>
                <c:when test="${not empty items}">
                    <c:forEach var="item" items="${items}">
                        <div class="col-md-4 col-lg-3">
                            <div class="card h-100 item-card">
                                <div class="position-relative">
                                    <img src="${empty item.imageUrl ? 'assets/images/no-image.png' : item.imageUrl}" 
                                         class="card-img-top" alt="${item.title}">
                                    <span class="position-absolute top-0 start-0 badge bg-${item.status == 'active' ? 'success' : item.status == 'completed' ? 'secondary' : 'primary'}">
                                        ${item.status}
                                    </span>
                                </div>
                                <div class="card-body">
                                    <h5 class="card-title text-truncate">${item.title}</h5>
                                    <p class="card-text text-truncate">${item.description}</p>
                                    <div class="d-flex justify-content-between align-items-center">
                                        <span class="fw-bold text-primary">$<fmt:formatNumber value="${item.currentPrice}" pattern="#,##0.00"/></span>
                                        <small class="text-muted">
                                            <i class="fas fa-gavel me-1"></i>
                                            <span id="bidCount-${item.itemId}">0</span> bids
                                        </small>
                                    </div>
                                    <div class="mt-2 small">
                                        <i class="fas fa-clock text-warning me-1"></i>
                                        <span class="countdown" data-end="${item.endTime}">
                                            <fmt:formatDate value="${item.endTime}" pattern="MMM dd, yyyy HH:mm" />
                                        </span>
                                    </div>
                                </div>
                                <div class="card-footer bg-white">
                                    <a href="ItemDetailsServlet?id=${item.itemId}" class="btn btn-sm btn-primary w-100">View Details</a>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="col-12">
                        <div class="alert alert-info text-center" role="alert">
                            <i class="fas fa-info-circle me-2"></i>
                            No items found. Try adjusting your search criteria.
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
        
        <!-- Pagination -->
        <c:if test="${not empty items && items.size() > 12}">
            <nav class="mt-5">
                <ul class="pagination justify-content-center">
                    <li class="page-item disabled">
                        <a class="page-link" href="#" tabindex="-1" aria-disabled="true">Previous</a>
                    </li>
                    <li class="page-item active"><a class="page-link" href="#">1</a></li>
                    <li class="page-item"><a class="page-link" href="#">2</a></li>
                    <li class="page-item"><a class="page-link" href="#">3</a></li>
                    <li class="page-item">
                        <a class="page-link" href="#">Next</a>
                    </li>
                </ul>
            </nav>
        </c:if>
    </div>
    
    <!-- Include Footer -->
    <jsp:include page="../includes/footer.jsp" />
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    <!-- Custom JS -->
    <script src="assets/js/script.js"></script>
    
    <script>
        // Filter functionality
        document.getElementById('categoryFilter').addEventListener('change', function() {
            if (this.value) {
                window.location.href = 'ItemListServlet?action=listByCategory&categoryId=' + this.value;
            }
        });
        
        // Countdown timer
        document.querySelectorAll('.countdown').forEach(function(element) {
            updateCountdown(element);
            setInterval(function() {
                updateCountdown(element);
            }, 1000);
        });
        
        function updateCountdown(element) {
            const endTime = new Date(element.getAttribute('data-end')).getTime();
            const now = new Date().getTime();
            const distance = endTime - now;
            
            if (distance < 0) {
                element.innerHTML = 'Auction ended';
                element.classList.add('text-danger');
                return;
            }
            
            const days = Math.floor(distance / (1000 * 60 * 60 * 24));
            const hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
            const minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
            const seconds = Math.floor((distance % (1000 * 60)) / 1000);
            
            let countdownText = '';
            if (days > 0) {
                countdownText = days + 'd ' + hours + 'h';
            } else if (hours > 0) {
                countdownText = hours + 'h ' + minutes + 'm';
            } else if (minutes > 0) {
                countdownText = minutes + 'm ' + seconds + 's';
            } else {
                countdownText = seconds + 's';
                element.classList.add('text-danger');
            }
            
            element.innerHTML = countdownText;
        }
    </script>
</body>
</html>
