<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${item.title} - BidMaster</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <!-- Custom CSS -->
    <style>
        .countdown-timer {
            font-size: 1.2rem;
            font-weight: bold;
        }
        .bid-history {
            max-height: 300px;
            overflow-y: auto;
        }
        .item-image {
            max-height: 400px;
            object-fit: contain;
        }
        .similar-item-card {
            transition: transform 0.3s;
        }
        .similar-item-card:hover {
            transform: translateY(-5px);
        }
    </style>
</head>
<body>
    <!-- Include Header -->
    <jsp:include page="../includes/header.jsp" />
    
    <div class="container py-5 mt-5">
        <!-- Alert Messages -->
        <c:if test="${not empty param.message}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                ${param.message}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>
        
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ${errorMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>
        
        <!-- Item Details -->
        <div class="card shadow-sm mb-4">
            <div class="card-body">
                <div class="row">
                    <!-- Item Image -->
                    <div class="col-md-6 mb-4">
                        <div class="position-relative">
                            <img src="${empty item.imageUrl ? 'assets/images/no-image.png' : item.imageUrl}" 
                                 class="img-fluid rounded item-image" alt="${item.title}">
                            <span class="position-absolute top-0 start-0 m-2 badge bg-${item.status == 'active' ? 'success' : item.status == 'completed' ? 'secondary' : 'primary'}">
                                ${item.status}
                            </span>
                        </div>
                        
                        <!-- Seller Info -->
                        <div class="card mt-3">
                            <div class="card-body">
                                <h5 class="card-title">Seller Information</h5>
                                <div class="d-flex align-items-center">
                                    <div class="flex-shrink-0">
                                        <img src="assets/images/user-avatar.png" alt="Seller" class="rounded-circle" width="50">
                                    </div>
                                    <div class="flex-grow-1 ms-3">
                                        <h6 class="mb-0">${seller.username}</h6>
                                        <div class="small text-muted">
                                            <i class="fas fa-star text-warning"></i> 4.8 (120 ratings)
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Item Details -->
                    <div class="col-md-6">
                        <h2 class="mb-3">${item.title}</h2>
                        
                        <div class="d-flex justify-content-between align-items-center mb-4">
                            <div>
                                <h4 class="text-primary mb-0">
                                    $<fmt:formatNumber value="${item.currentPrice}" pattern="#,##0.00"/>
                                </h4>
                                <small class="text-muted">
                                    Starting price: $<fmt:formatNumber value="${item.startingPrice}" pattern="#,##0.00"/>
                                </small>
                            </div>
                            <div class="text-end">
                                <div class="d-flex align-items-center">
                                    <i class="fas fa-gavel text-primary me-2"></i>
                                    <span class="fw-bold">${bidCount}</span>
                                    <span class="ms-1">bids</span>
                                </div>
                            </div>
                        </div>
                        
                        <!-- Countdown Timer -->
                        <div class="card bg-light mb-4">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <h6 class="mb-0">Auction Ends In:</h6>
                                    </div>
                                    <div class="text-end">
                                        <div class="countdown-timer" data-end="${item.endTime}">
                                            <span class="days">00</span>d 
                                            <span class="hours">00</span>h 
                                            <span class="minutes">00</span>m 
                                            <span class="seconds">00</span>s
                                        </div>
                                        <small class="text-muted">
                                            <fmt:formatDate value="${item.endTime}" pattern="MMM dd, yyyy HH:mm" />
                                        </small>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                        <!-- Bid Form -->
                        <c:if test="${item.status == 'active' && sessionScope.userId != null && sessionScope.userId != item.sellerId}">
                            <div class="card mb-4">
                                <div class="card-body">
                                    <h5 class="card-title">Place Your Bid</h5>
                                    <form action="PlaceBidServlet" method="post" id="bidForm">
                                        <input type="hidden" name="itemId" value="${item.itemId}">
                                        <div class="input-group mb-3">
                                            <span class="input-group-text">$</span>
                                            <input type="number" class="form-control" name="bidAmount" id="bidAmount" 
                                                   min="${item.currentPrice + 1}" step="0.01" 
                                                   placeholder="Enter bid amount" required>
                                            <button class="btn btn-primary" type="submit">
                                                <i class="fas fa-gavel me-2"></i>Place Bid
                                            </button>
                                        </div>
                                        <small class="text-muted">
                                            Minimum bid: $<fmt:formatNumber value="${item.currentPrice + 1}" pattern="#,##0.00"/>
                                        </small>
                                    </form>
                                </div>
                            </div>
                        </c:if>
                        
                        <!-- Action Buttons -->
                        <div class="d-flex gap-2 mb-4">
                            <c:choose>
                                <c:when test="${sessionScope.userId == item.sellerId}">
                                    <c:if test="${item.status == 'pending' || item.status == 'active'}">
                                        <a href="UpdateItemServlet?id=${item.itemId}" class="btn btn-outline-primary">
                                            <i class="fas fa-edit me-2"></i>Edit Item
                                        </a>
                                    </c:if>
                                </c:when>
                                <c:otherwise>
                                    <c:if test="${sessionScope.userId != null}">
                                        <form action="WatchlistServlet" method="post">
                                            <input type="hidden" name="itemId" value="${item.itemId}">
                                            <input type="hidden" name="action" value="${inWatchlist ? 'remove' : 'add'}">
                                            <button type="submit" class="btn btn-outline-${inWatchlist ? 'danger' : 'primary'}">
                                                <i class="fas fa-${inWatchlist ? 'heart' : 'heart'} me-2"></i>
                                                ${inWatchlist ? 'Remove from Watchlist' : 'Add to Watchlist'}
                                            </button>
                                        </form>
                                    </c:if>
                                </c:otherwise>
                            </c:choose>
                            
                            <button type="button" class="btn btn-outline-secondary" data-bs-toggle="modal" data-bs-target="#shareModal">
                                <i class="fas fa-share-alt me-2"></i>Share
                            </button>
                            
                            <c:if test="${sessionScope.role == 'admin'}">
                                <form action="DeleteItemServlet" method="post" onsubmit="return confirm('Are you sure you want to delete this item?');">
                                    <input type="hidden" name="itemId" value="${item.itemId}">
                                    <button type="submit" class="btn btn-outline-danger">
                                        <i class="fas fa-trash-alt me-2"></i>Delete
                                    </button>
                                </form>
                            </c:if>
                        </div>
                        
                        <!-- Item Description -->
                        <div class="mb-4">
                            <h5>Description</h5>
                            <p>${item.description}</p>
                        </div>
                        
                        <!-- Item Details Table -->
                        <div class="table-responsive">
                            <table class="table table-striped">
                                <tbody>
                                    <tr>
                                        <th scope="row">Category</th>
                                        <td>${category.categoryName}</td>
                                    </tr>
                                    <tr>
                                        <th scope="row">Condition</th>
                                        <td>New</td>
                                    </tr>
                                    <tr>
                                        <th scope="row">Location</th>
                                        <td>New York, NY</td>
                                    </tr>
                                    <tr>
                                        <th scope="row">Shipping</th>
                                        <td>Free shipping</td>
                                    </tr>
                                    <tr>
                                        <th scope="row">Returns</th>
                                        <td>Not accepted</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Bid History -->
        <div class="card shadow-sm mb-4">
            <div class="card-header bg-white">
                <h5 class="mb-0">Bid History</h5>
            </div>
            <div class="card-body bid-history">
                <c:choose>
                    <c:when test="${not empty bids}">
                        <div class="table-responsive">
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>Bidder</th>
                                        <th>Amount</th>
                                        <th>Date & Time</th>
                                        <th>Status</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="bid" items="${bids}">
                                        <tr>
                                            <td>
                                                <div class="d-flex align-items-center">
                                                    <img src="assets/images/user-avatar.png" alt="Bidder" class="rounded-circle me-2" width="30">
                                                    ${bid.bidderUsername}
                                                </div>
                                            </td>
                                            <td class="fw-bold">$<fmt:formatNumber value="${bid.bidAmount}" pattern="#,##0.00"/></td>
                                            <td><fmt:formatDate value="${bid.bidTime}" pattern="MMM dd, yyyy HH:mm:ss" /></td>
                                            <td>
                                                <span class="badge bg-${bid.status == 'winning' ? 'success' : bid.status == 'outbid' ? 'warning' : 'primary'}">
                                                    ${bid.status}
                                                </span>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="text-center py-4">
                            <i class="fas fa-gavel fa-3x text-muted mb-3"></i>
                            <p class="mb-0">No bids yet. Be the first to place a bid!</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        
        <!-- Similar Items -->
        <h3 class="mb-4">Similar Items</h3>
        <div class="row g-4">
            <c:forEach var="similarItem" items="${similarItems}" varStatus="loop">
                <c:if test="${loop.index < 4 && similarItem.itemId != item.itemId}">
                    <div class="col-md-3">
                        <div class="card h-100 similar-item-card">
                            <img src="${empty similarItem.imageUrl ? 'assets/images/no-image.png' : similarItem.imageUrl}" 
                                 class="card-img-top" alt="${similarItem.title}" style="height: 180px; object-fit: cover;">
                            <div class="card-body">
                                <h5 class="card-title text-truncate">${similarItem.title}</h5>
                                <div class="d-flex justify-content-between align-items-center">
                                    <span class="fw-bold text-primary">$<fmt:formatNumber value="${similarItem.currentPrice}" pattern="#,##0.00"/></span>
                                    <small class="text-muted countdown" data-end="${similarItem.endTime}"></small>
                                </div>
                            </div>
                            <div class="card-footer bg-white">
                                <a href="ItemDetailsServlet?id=${similarItem.itemId}" class="btn btn-sm btn-primary w-100">View Details</a>
                            </div>
                        </div>
                    </div>
                </c:if>
            </c:forEach>
        </div>
    </div>
    
    <!-- Share Modal -->
    <div class="modal fade" id="shareModal" tabindex="-1" aria-labelledby="shareModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="shareModalLabel">Share This Item</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="d-flex justify-content-center gap-3">
                        <a href="#" class="btn btn-outline-primary">
                            <i class="fab fa-facebook-f"></i>
                        </a>
                        <a href="#" class="btn btn-outline-info">
                            <i class="fab fa-twitter"></i>
                        </a>
                        <a href="#" class="btn btn-outline-success">
                            <i class="fab fa-whatsapp"></i>
                        </a>
                        <a href="#" class="btn btn-outline-secondary">
                            <i class="fas fa-envelope"></i>
                        </a>
                    </div>
                    <div class="mt-3">
                        <label for="shareLink" class="form-label">Copy Link</label>
                        <div class="input-group">
                            <input type="text" class="form-control" id="shareLink" value="${pageContext.request.requestURL}?id=${item.itemId}" readonly>
                            <button class="btn btn-outline-secondary" type="button" onclick="copyShareLink()">
                                <i class="fas fa-copy"></i>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Include Footer -->
    <jsp:include page="../includes/footer.jsp" />
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
        // Countdown timer
        function updateCountdown() {
            const endTime = new Date('${item.endTime}').getTime();
            const now = new Date().getTime();
            const distance = endTime - now;
            
            if (distance < 0) {
                document.querySelector('.countdown-timer').innerHTML = 'Auction ended';
                return;
            }
            
            const days = Math.floor(distance / (1000 * 60 * 60 * 24));
            const hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
            const minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
            const seconds = Math.floor((distance % (1000 * 60)) / 1000);
            
            document.querySelector('.countdown-timer .days').textContent = days.toString().padStart(2, '0');
            document.querySelector('.countdown-timer .hours').textContent = hours.toString().padStart(2, '0');
            document.querySelector('.countdown-timer .minutes').textContent = minutes.toString().padStart(2, '0');
            document.querySelector('.countdown-timer .seconds').textContent = seconds.toString().padStart(2, '0');
        }
        
        // Update countdown every second
        updateCountdown();
        setInterval(updateCountdown, 1000);
        
        // Copy share link
        function copyShareLink() {
            const shareLink = document.getElementById('shareLink');
            shareLink.select();
            document.execCommand('copy');
            alert('Link copied to clipboard!');
        }
        
        // Bid form validation
        document.getElementById('bidForm')?.addEventListener('submit', function(event) {
            const bidAmount = parseFloat(document.getElementById('bidAmount').value);
            const currentPrice = parseFloat('${item.currentPrice}');
            
            if (bidAmount <= currentPrice) {
                event.preventDefault();
                alert('Your bid must be higher than the current price.');
            }
        });
    </script>
</body>
</html>
