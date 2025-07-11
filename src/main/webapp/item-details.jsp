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
    <!-- Lightbox CSS -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/lightbox2/2.11.3/css/lightbox.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <style>
        .item-image {
            max-height: 400px;
            object-fit: contain;
        }
        .bid-card {
            background-color: #f8f9fa;
            border-radius: 10px;
            padding: 20px;
        }
        .countdown {
            font-size: 1.2rem;
            color: #dc3545;
            font-weight: bold;
        }
        .bid-history-item {
            padding: 10px 0;
            border-bottom: 1px solid #eee;
        }
        .bid-history-item:last-child {
            border-bottom: none;
        }
        .winning-bid {
            background-color: rgba(40, 167, 69, 0.1);
        }
        .bid-btn {
            background-color: #3a5a78;
            border-color: #3a5a78;
        }
        .bid-btn:hover {
            background-color: #2d4861;
            border-color: #2d4861;
        }
        .watchlist-btn {
            background-color: #ffc107;
            border-color: #ffc107;
            color: #212529;
        }
        .watchlist-btn:hover {
            background-color: #e0a800;
            border-color: #d39e00;
            color: #212529;
        }
        .seller-info {
            background-color: #fff;
            border-radius: 10px;
            padding: 20px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }
        .similar-item-card {
            transition: transform 0.3s;
            height: 100%;
        }
        .similar-item-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
        }
        .similar-item-img {
            height: 150px;
            object-fit: cover;
        }
    </style>
</head>
<body>
    <!-- Include Header -->
    <jsp:include page="includes/header.jsp" />
    
    <div class="container my-5">
        <!-- Alert Messages -->
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="fas fa-check-circle me-2"></i>${successMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>
        
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-circle me-2"></i>${errorMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>
        
        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb" class="mb-4">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="HomeServlet">Home</a></li>
                <li class="breadcrumb-item"><a href="CategoryServlet?id=${item.categoryId}">${item.categoryName}</a></li>
                <li class="breadcrumb-item active" aria-current="page">${item.title}</li>
            </ol>
        </nav>
        
        <div class="row">
            <!-- Item Details -->
            <div class="col-lg-8 mb-4">
                <div class="card shadow-sm">
                    <div class="card-body">
                        <h1 class="card-title mb-3">${item.title}</h1>
                        <div class="mb-4">
                            <a href="${empty item.imageUrl ? 'assets/images/no-image.png' : item.imageUrl}" 
                               data-lightbox="item-image" data-title="${item.title}">
                                <img src="${empty item.imageUrl ? 'assets/images/no-image.png' : item.imageUrl}" 
                                     class="img-fluid item-image rounded" alt="${item.title}">
                            </a>
                        </div>
                        
                        <div class="row mb-4">
                            <div class="col-md-6">
                                <h5>Item Details</h5>
                                <ul class="list-group list-group-flush">
                                    <li class="list-group-item d-flex justify-content-between">
                                        <span>Category:</span>
                                        <span class="fw-bold">${item.categoryName}</span>
                                    </li>
                                    <li class="list-group-item d-flex justify-content-between">
                                        <span>Condition:</span>
                                        <span class="fw-bold">${item.condition != null ? item.condition : 'Not specified'}</span>
                                    </li>
                                    <li class="list-group-item d-flex justify-content-between">
                                        <span>Location:</span>
                                        <span class="fw-bold">${item.location != null ? item.location : 'Not specified'}</span>
                                    </li>
                                    <li class="list-group-item d-flex justify-content-between">
                                        <span>Seller:</span>
                                        <span class="fw-bold">${item.sellerUsername}</span>
                                    </li>
                                </ul>
                            </div>
                            <div class="col-md-6">
                                <h5>Auction Details</h5>
                                <ul class="list-group list-group-flush">
                                    <li class="list-group-item d-flex justify-content-between">
                                        <span>Starting Price:</span>
                                        <span class="fw-bold">
                                            <c:choose>
                                                <c:when test="${not empty item.startingPrice}">
                                                    $<fmt:formatNumber value="${item.startingPrice}" pattern="#,##0.00"/>
                                                </c:when>
                                                <c:otherwise>
                                                    $0.00
                                                </c:otherwise>
                                            </c:choose>
                                        </span>
                                    </li>
                                    <li class="list-group-item d-flex justify-content-between">
                                        <span>Current Bid:</span>
                                        <span class="fw-bold text-primary">
                                            <c:choose>
                                                <c:when test="${not empty item.currentPrice}">
                                                    $<fmt:formatNumber value="${item.currentPrice}" pattern="#,##0.00"/>
                                                </c:when>
                                                <c:otherwise>
                                                    $0.00
                                                </c:otherwise>
                                            </c:choose>
                                        </span>
                                    </li>
                                    <li class="list-group-item d-flex justify-content-between">
                                        <span>Bid Count:</span>
                                        <span class="fw-bold">${item.bidCount}</span>
                                    </li>
                                    <li class="list-group-item d-flex justify-content-between">
                                        <span>Time Left:</span>
                                        <span class="countdown" data-end="${item.endTime}">
                                            ${item.endTime.toLocalDate()} ${item.endTime.toLocalTime().toString().substring(0, 5)}
                                        </span>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        
                        <h5>Description</h5>
                        <p class="card-text">${item.description}</p>
                    </div>
                </div>
                
<!-- Bid History -->
<div class="card shadow-sm mt-4">
    <div class="card-header bg-white">
        <h5 class="mb-0">Bid History (${item.bidCount} bids)</h5>
    </div>
    <div class="card-body">
        <c:choose>
            <c:when test="${not empty bidHistory}">
                <div class="bid-history">
                    <c:forEach var="bidItem" items="${bidHistory}" varStatus="status">
                        <div class="bid-history-item ${status.index == 0 ? 'winning-bid' : ''}">
                            <div class="d-flex justify-content-between align-items-center">
                                <div>
                                    <span class="fw-bold">${bidItem.bidderUsername}</span>
                                    <c:if test="${status.index == 0}">
                                        <span class="badge bg-success ms-2">Highest Bid</span>
                                    </c:if>
                                </div>
                                <div class="text-end">
                                    <div class="fw-bold">
                                        $${bidItem.bidAmount}
                                    </div>
                                    <small class="text-muted">
                                        <c:if test="${bidItem.bidTime != null}">
                                            ${bidItem.bidTime}
                                        </c:if>
                                    </small>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <p class="text-center text-muted">No bids yet. Be the first to bid!</p>
            </c:otherwise>
        </c:choose>
    </div>
</div>


            
            <!-- Bid Section and Seller Info -->
            <div class="col-lg-4">
                <!-- Bid Card -->
                <div class="bid-card mb-4 shadow-sm">
                    <h4 class="mb-3">Place Your Bid</h4>
                    <div class="mb-3">
                        <div class="d-flex justify-content-between align-items-center mb-2">
                            <span>Current Bid:</span>
                            <span class="fw-bold text-primary fs-5">
                                <c:choose>
                                    <c:when test="${not empty item.currentPrice}">
                                        $<fmt:formatNumber value="${item.currentPrice}" pattern="#,##0.00"/>
                                    </c:when>
                                    <c:otherwise>
                                        $0.00
                                    </c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <span>Minimum Bid:</span>
                            <span class="fw-bold">
                                <c:choose>
                                    <c:when test="${not empty item.currentPrice}">
                                        $<fmt:formatNumber value="${item.currentPrice + 1}" pattern="#,##0.00"/>
                                    </c:when>
                                    <c:otherwise>
                                        $1.00
                                    </c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                    </div>
                    
                    <c:choose>
                        <c:when test="${item.status == 'active' && not empty sessionScope.userId && sessionScope.userId != item.sellerId}">
                            <form action="PlaceBidServlet" method="post">
                                <input type="hidden" name="itemId" value="${item.itemId}">
                                <div class="mb-3">
                                    <label for="bidAmount" class="form-label">Your Bid Amount ($)</label>
                                    <input type="number" class="form-control form-control-lg" id="bidAmount" name="bidAmount" 
                                           min="${not empty item.currentPrice ? item.currentPrice + 1 : 1}" step="0.01" required>
                                    <div class="form-text">Enter an amount greater than the minimum bid</div>
                                </div>
                                <div class="d-grid gap-2">
                                    <button type="submit" class="btn btn-lg bid-btn text-white">
                                        <i class="fas fa-gavel me-2"></i>Place Bid
                                    </button>
                                    
                                    <c:choose>
                                        <c:when test="${isInWatchlist}">
                                            <a href="WatchlistServlet?action=remove&itemId=${item.itemId}" class="btn btn-outline-secondary">
                                                <i class="fas fa-heart me-2"></i>Remove from Watchlist
                                            </a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="WatchlistServlet?action=add&itemId=${item.itemId}" class="btn watchlist-btn">
                                                <i class="far fa-heart me-2"></i>Add to Watchlist
                                            </a>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </form>
                        </c:when>
                        <c:when test="${empty sessionScope.userId}">
                            <div class="alert alert-info mb-3">
                                <i class="fas fa-info-circle me-2"></i>You need to <a href="login.jsp" class="alert-link">login</a> to place a bid.
                            </div>
                            <div class="d-grid">
                                <a href="login.jsp" class="btn btn-lg bid-btn text-white">
                                    <i class="fas fa-sign-in-alt me-2"></i>Login to Bid
                                </a>
                            </div>
                        </c:when>
                        <c:when test="${sessionScope.userId == item.sellerId}">
                            <div class="alert alert-warning mb-3">
                                <i class="fas fa-exclamation-triangle me-2"></i>You cannot bid on your own item.
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="alert alert-secondary mb-3">
                                <i class="fas fa-clock me-2"></i>This auction has ended.
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
                
                <!-- Seller Info -->
                <div class="seller-info mb-4">
                    <h4 class="mb-3">Seller Information</h4>
                    <c:if test="${not empty sellerInfo}">
                        <div class="d-flex align-items-center mb-3">
                            <img src="${empty sellerInfo.profileImage ? 'assets/images/default-avatar.png' : sellerInfo.profileImage}" 
                                 alt="${sellerInfo.username}" class="rounded-circle me-3" width="60" height="60">
                            <div>
                                <h5 class="mb-1">${sellerInfo.username}</h5>
                                <div class="text-muted">Member since <fmt:formatDate value="${sellerInfo.registrationDate}" pattern="MMM yyyy" /></div>
                            </div>
                        </div>
                        <div class="mb-3">
                            <div class="d-flex justify-content-between mb-2">
                                <span>Rating:</span>
                                <span class="fw-bold">
                                    <c:set var="rating" value="${sellerInfo.rating > 0 ? sellerInfo.rating : 0}" />
                                    <c:forEach begin="1" end="5" var="i">
                                        <i class="fas fa-star ${i <= rating ? 'text-warning' : 'text-muted'}"></i>
                                    </c:forEach>
                                    (${sellerInfo.ratingCount > 0 ? sellerInfo.ratingCount : 0})
                                </span>
                            </div>
                            <div class="d-flex justify-content-between">
                                <span>Items Sold:</span>
                                <span class="fw-bold">${sellerInfo.itemsSold > 0 ? sellerInfo.itemsSold : 0}</span>
                            </div>
                        </div>
                        <div class="d-grid">
                            <a href="SellerProfileServlet?id=${item.sellerId}" class="btn btn-outline-primary">
                                <i class="fas fa-user me-2"></i>View Seller Profile
                            </a>
                        </div>
                    </c:if>
                </div>
                
                <!-- Shipping Info -->
                <div class="card shadow-sm mb-4">
                    <div class="card-header bg-white">
                        <h5 class="mb-0">Shipping & Payment</h5>
                    </div>
                    <div class="card-body">
                        <div class="mb-3">
                            <h6><i class="fas fa-truck me-2"></i>Shipping</h6>
                            <p class="mb-0">${not empty item.shippingInfo ? item.shippingInfo : 'Contact seller for shipping details.'}</p>
                        </div>
                        <div>
                            <h6><i class="fas fa-credit-card me-2"></i>Payment Methods</h6>
                            <p class="mb-0">${not empty item.paymentMethods ? item.paymentMethods : 'Contact seller for payment options.'}</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Similar Items -->
        <c:if test="${not empty similarItems}">
            <section class="mt-5">
                <h3 class="mb-4">Similar Items</h3>
                <div class="row">
                    <c:forEach var="similarItem" items="${similarItems}">
                        <div class="col-lg-3 col-md-4 col-sm-6 mb-4">
                            <div class="card similar-item-card">
                                <img src="${empty similarItem.imageUrl ? 'assets/images/no-image.png' : similarItem.imageUrl}" 
                                     class="card-img-top similar-item-img" alt="${similarItem.title}">
                                <div class="card-body">
                                    <h5 class="card-title text-truncate">${similarItem.title}</h5>
                                    <div class="d-flex justify-content-between align-items-center mb-2">
                                        <span>Current Bid:</span>
                                        <span class="fw-bold">
                                            <c:choose>
                                                <c:when test="${not empty similarItem.currentPrice}">
                                                    $<fmt:formatNumber value="${similarItem.currentPrice}" pattern="#,##0.00"/>
                                                </c:when>
                                                <c:otherwise>
                                                    $0.00
                                                </c:otherwise>
                                            </c:choose>
                                        </span>
                                    </div>
                                    <div class="d-flex justify-content-between align-items-center mb-3">
                                        <span>Ends in:</span>
                                        <span class="countdown" data-end="${similarItem.endTime}">
                                            ${similarItem.endTime.toLocalDate()} ${similarItem.endTime.toLocalTime().toString().substring(0, 5)}
                                        </span>
                                    </div>
                                    <div class="d-grid">
                                        <a href="ItemDetailsServlet?id=${similarItem.itemId}" class="btn btn-sm bid-btn text-white">
                                            <i class="fas fa-gavel me-2"></i>Bid Now
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </section>
        </c:if>
    </div>
    
    <!-- Include Footer -->
    <jsp:include page="includes/footer.jsp" />
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <!-- Lightbox JS -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/lightbox2/2.11.3/js/lightbox.min.js"></script>
    
    <script>
        // Countdown timer for auctions
        function updateCountdowns() {
            document.querySelectorAll('.countdown').forEach(function(element) {
                const endTime = new Date(element.getAttribute('data-end')).getTime();
                const now = new Date().getTime();
                const distance = endTime - now;
                
                if (distance < 0) {
                    element.innerHTML = "Auction Ended";
                    element.classList.add('text-danger');
                } else {
                    const days = Math.floor(distance / (1000 * 60 * 60 * 24));
                    const hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
                    const minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
                    const seconds = Math.floor((distance % (1000 * 60)) / 1000);
                    
                    let countdownText = "";
                    if (days > 0) {
                        countdownText = days + "d " + hours + "h " + minutes + "m " + seconds + "s";
                    } else if (hours > 0) {
                        countdownText = hours + "h " + minutes + "m " + seconds + "s";
                    } else if (minutes > 0) {
                        countdownText = minutes + "m " + seconds + "s";
                    } else {
                        countdownText = seconds + "s";
                        element.classList.add('text-danger');
                    }
                    
                    element.innerHTML = countdownText;
                }
            });
        }
        
        // Update countdowns immediately and then every second
        updateCountdowns();
        setInterval(updateCountdowns, 1000);
        
        // Initialize lightbox
        lightbox.option({
            'resizeDuration': 200,
            'wrapAround': true
        });
    </script>
</body>
</html>
