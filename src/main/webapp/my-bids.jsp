<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Bids - BidMaster</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <!-- DataTables CSS -->
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.4/css/dataTables.bootstrap5.min.css">
    <!-- Custom CSS -->
    <style>
        body {
            background-color: #f8f9fa;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }
        .page-header {
            background-color: #3a5a78;
            color: white;
            padding: 2rem 0;
            margin-bottom: 2rem;
        }
        .item-img {
            width: 60px;
            height: 60px;
            object-fit: cover;
            border-radius: 4px;
        }
        .badge-winning {
            background-color: #28a745;
        }
        .badge-outbid {
            background-color: #dc3545;
        }
        .badge-active {
            background-color: #17a2b8;
        }
        .badge-completed {
            background-color: #6c757d;
        }
        .countdown {
            font-size: 0.8rem;
            color: #dc3545;
        }
        .action-btn {
            background-color: #3a5a78;
            border-color: #3a5a78;
        }
        .action-btn:hover {
            background-color: #2d4861;
            border-color: #2d4861;
        }
    </style>
</head>
<body>
    <!-- Include Header -->
    <jsp:include page="includes/header.jsp" />
    
    <!-- Page Header -->
    <div class="page-header">
        <div class="container">
            <h1>My Bids</h1>
            <p class="lead mb-0">Track all your bidding activity</p>
        </div>
    </div>
    
    <div class="container mb-5">
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
        
        <!-- Bids Tabs -->
        <ul class="nav nav-tabs mb-4" id="myBidsTabs" role="tablist">
            <li class="nav-item" role="presentation">
                <button class="nav-link active" id="active-bids-tab" data-bs-toggle="tab" data-bs-target="#active-bids" 
                        type="button" role="tab" aria-controls="active-bids" aria-selected="true">
                    <i class="fas fa-gavel me-2"></i>Active Bids
                </button>
            </li>
            <li class="nav-item" role="presentation">
                <button class="nav-link" id="winning-bids-tab" data-bs-toggle="tab" data-bs-target="#winning-bids" 
                        type="button" role="tab" aria-controls="winning-bids" aria-selected="false">
                    <i class="fas fa-trophy me-2"></i>Winning Bids
                </button>
            </li>
            <li class="nav-item" role="presentation">
                <button class="nav-link" id="outbid-bids-tab" data-bs-toggle="tab" data-bs-target="#outbid-bids" 
                        type="button" role="tab" aria-controls="outbid-bids" aria-selected="false">
                    <i class="fas fa-times-circle me-2"></i>Outbid
                </button>
            </li>
            <li class="nav-item" role="presentation">
                <button class="nav-link" id="completed-bids-tab" data-bs-toggle="tab" data-bs-target="#completed-bids" 
                        type="button" role="tab" aria-controls="completed-bids" aria-selected="false">
                    <i class="fas fa-check-circle me-2"></i>Completed
                </button>
            </li>
        </ul>
        
        <!-- Tab Content -->
        <div class="tab-content" id="myBidsTabContent">
            <!-- Active Bids Tab -->
            <div class="tab-pane fade show active" id="active-bids" role="tabpanel" aria-labelledby="active-bids-tab">
                <div class="card shadow-sm">
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${not empty activeBids}">
                                <div class="table-responsive">
                                    <table class="table table-hover" id="activeBidsTable">
                                        <thead>
                                            <tr>
                                                <th>Item</th>
                                                <th>Your Bid</th>
                                                <th>Current Price</th>
                                                <th>Status</th>
                                                <th>End Time</th>
                                                <th>Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="bid" items="${activeBids}">
                                                <tr>
                                                    <td>
                                                        <div class="d-flex align-items-center">
                                                            <img src="${empty bid.itemImageUrl ? 'assets/images/no-image.png' : bid.itemImageUrl}" 
                                                                 alt="${bid.itemTitle}" class="item-img me-3">
                                                            <div>
                                                                <div class="fw-bold">${bid.itemTitle}</div>
                                                                <small class="text-muted">Seller: ${bid.sellerUsername}</small>
                                                            </div>
                                                        </div>
                                                    </td>
                                                    <td>$<fmt:formatNumber value="${bid.bidAmount}" pattern="#,##0.00"/></td>
                                                    <td>$<fmt:formatNumber value="${bid.currentPrice}" pattern="#,##0.00"/></td>
                                                    <td>
                                                        <span class="badge ${bid.status == 'winning' ? 'badge-winning' : 'badge-active'}">
                                                            ${bid.status == 'winning' ? 'Highest Bid' : 'Active'}
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <span class="countdown" data-end="${bid.endTime}">
                                                            ${bid.endTime.toLocalDate()} ${bid.endTime.toLocalTime().toString().substring(0, 5)}
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <div class="btn-group btn-group-sm">
                                                            <a href="ItemDetailsServlet?id=${bid.itemId}" class="btn action-btn text-white" title="View Item">
                                                                <i class="fas fa-eye"></i>
                                                            </a>
                                                            <c:if test="${bid.status != 'winning'}">
                                                                <button type="button" class="btn btn-warning" 
                                                                        onclick="showBidModal(${bid.itemId}, '${bid.itemTitle}', ${bid.currentPrice})" title="Place New Bid">
                                                                    <i class="fas fa-gavel"></i>
                                                                </button>
                                                            </c:if>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="text-center py-5">
                                    <i class="fas fa-gavel fa-4x text-muted mb-3"></i>
                                    <h5>You don't have any active bids</h5>
                                    <p class="text-muted">Start bidding on items you're interested in!</p>
                                    <a href="BrowseItemsServlet" class="btn action-btn text-white mt-3">
                                        <i class="fas fa-search me-2"></i>Browse Items
                                    </a>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
            
            <!-- Winning Bids Tab -->
            <div class="tab-pane fade" id="winning-bids" role="tabpanel" aria-labelledby="winning-bids-tab">
                <div class="card shadow-sm">
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${not empty winningBids}">
                                <div class="table-responsive">
                                    <table class="table table-hover" id="winningBidsTable">
                                        <thead>
                                            <tr>
                                                <th>Item</th>
                                                <th>Your Bid</th>
                                                <th>End Time</th>
                                                <th>Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="bid" items="${winningBids}">
                                                <tr>
                                                    <td>
                                                        <div class="d-flex align-items-center">
                                                            <img src="${empty bid.itemImageUrl ? 'assets/images/no-image.png' : bid.itemImageUrl}" 
                                                                 alt="${bid.itemTitle}" class="item-img me-3">
                                                            <div>
                                                                <div class="fw-bold">${bid.itemTitle}</div>
                                                                <small class="text-muted">Seller: ${bid.sellerUsername}</small>
                                                            </div>
                                                        </div>
                                                    </td>
                                                    <td>$<fmt:formatNumber value="${bid.bidAmount}" pattern="#,##0.00"/></td>
                                                    <td>
                                                        <span class="countdown" data-end="${bid.endTime}">
                                                            ${bid.endTime.toLocalDate()} ${bid.endTime.toLocalTime().toString().substring(0, 5)}
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <a href="ItemDetailsServlet?id=${bid.itemId}" class="btn btn-sm action-btn text-white">
                                                            <i class="fas fa-eye me-1"></i>View Item
                                                        </a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="text-center py-5">
                                    <i class="fas fa-trophy fa-4x text-muted mb-3"></i>
                                    <h5>You don't have any winning bids</h5>
                                    <p class="text-muted">Keep bidding to win auctions!</p>
                                    <a href="BrowseItemsServlet" class="btn action-btn text-white mt-3">
                                        <i class="fas fa-search me-2"></i>Browse Items
                                    </a>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
            
            <!-- Outbid Bids Tab -->
            <div class="tab-pane fade" id="outbid-bids" role="tabpanel" aria-labelledby="outbid-bids-tab">
                <div class="card shadow-sm">
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${not empty outbidBids}">
                                <div class="table-responsive">
                                    <table class="table table-hover" id="outbidBidsTable">
                                        <thead>
                                            <tr>
                                                <th>Item</th>
                                                <th>Your Bid</th>
                                                <th>Current Price</th>
                                                <th>End Time</th>
                                                <th>Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="bid" items="${outbidBids}">
                                                <tr>
                                                    <td>
                                                        <div class="d-flex align-items-center">
                                                            <img src="${empty bid.itemImageUrl ? 'assets/images/no-image.png' : bid.itemImageUrl}" 
                                                                 alt="${bid.itemTitle}" class="item-img me-3">
                                                            <div>
                                                                <div class="fw-bold">${bid.itemTitle}</div>
                                                                <small class="text-muted">Seller: ${bid.sellerUsername}</small>
                                                            </div>
                                                        </div>
                                                    </td>
                                                    <td>$<fmt:formatNumber value="${bid.bidAmount}" pattern="#,##0.00"/></td>
                                                    <td>$<fmt:formatNumber value="${bid.currentPrice}" pattern="#,##0.00"/></td>
                                                    <td>
                                                        <span class="countdown" data-end="${bid.endTime}">
                                                            ${bid.endTime.toLocalDate()} ${bid.endTime.toLocalTime().toString().substring(0, 5)}
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <div class="btn-group btn-group-sm">
                                                            <a href="ItemDetailsServlet?id=${bid.itemId}" class="btn action-btn text-white" title="View Item">
                                                                <i class="fas fa-eye"></i>
                                                            </a>
                                                            <button type="button" class="btn btn-warning" 
                                                                    onclick="showBidModal(${bid.itemId}, '${bid.itemTitle}', ${bid.currentPrice})" title="Place New Bid">
                                                                <i class="fas fa-gavel"></i>
                                                            </button>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="text-center py-5">
                                    <i class="fas fa-times-circle fa-4x text-muted mb-3"></i>
                                    <h5>You don't have any outbid bids</h5>
                                    <p class="text-muted">When you get outbid on an item, it will appear here.</p>
                                    <a href="BrowseItemsServlet" class="btn action-btn text-white mt-3">
                                        <i class="fas fa-search me-2"></i>Browse Items
                                    </a>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
            
            <!-- Completed Bids Tab -->
            <div class="tab-pane fade" id="completed-bids" role="tabpanel" aria-labelledby="completed-bids-tab">
                <div class="card shadow-sm">
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${not empty completedBids}">
                                <div class="table-responsive">
                                    <table class="table table-hover" id="completedBidsTable">
                                        <thead>
                                            <tr>
                                                <th>Item</th>
                                                <th>Your Bid</th>
                                                <th>Final Price</th>
                                                <th>Result</th>
                                                <th>End Date</th>
                                                <th>Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="bid" items="${completedBids}">
                                                <tr>
                                                    <td>
                                                        <div class="d-flex align-items-center">
                                                            <img src="${empty bid.itemImageUrl ? 'assets/images/no-image.png' : bid.itemImageUrl}" 
                                                                 alt="${bid.itemTitle}" class="item-img me-3">
                                                            <div>
                                                                <div class="fw-bold">${bid.itemTitle}</div>
                                                                <small class="text-muted">Seller: ${bid.sellerUsername}</small>
                                                            </div>
                                                        </div>
                                                    </td>
                                                    <td>$<fmt:formatNumber value="${bid.bidAmount}" pattern="#,##0.00"/></td>
                                                    <td>$<fmt:formatNumber value="${bid.finalPrice}" pattern="#,##0.00"/></td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${bid.won}">
                                                                <span class="badge badge-winning">Won</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge badge-outbid">Lost</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <fmt:formatDate value="${bid.endDate}" pattern="MMM dd, yyyy" />
                                                    </td>
                                                    <td>
                                                        <a href="ItemDetailsServlet?id=${bid.itemId}" class="btn btn-sm action-btn text-white">
                                                            <i class="fas fa-eye me-1"></i>View Item
                                                        </a>
                                                        <c:if test="${bid.won}">
                                                            <a href="PaymentServlet?id=${bid.transactionId}" class="btn btn-sm btn-success">
                                                                <i class="fas fa-credit-card me-1"></i>Pay Now
                                                            </a>
                                                        </c:if>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="text-center py-5">
                                    <i class="fas fa-check-circle fa-4x text-muted mb-3"></i>
                                    <h5>You don't have any completed bids</h5>
                                    <p class="text-muted">When auctions you've bid on end, they'll appear here.</p>
                                    <a href="BrowseItemsServlet" class="btn action-btn text-white mt-3">
                                        <i class="fas fa-search me-2"></i>Browse Items
                                    </a>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- New Bid Modal -->
    <div class="modal fade" id="bidModal" tabindex="-1" aria-labelledby="bidModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="bidModalLabel">Place a Bid</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form action="PlaceBidServlet" method="post">
                    <div class="modal-body">
                        <input type="hidden" id="modalItemId" name="itemId">
                        <div class="mb-3">
                            <h5 id="modalItemTitle"></h5>
                            <p class="text-muted">Current price: $<span id="modalCurrentPrice"></span></p>
                        </div>
                        <div class="mb-3">
                            <label for="modalBidAmount" class="form-label">Your Bid Amount ($)</label>
                            <input type="number" class="form-control" id="modalBidAmount" name="bidAmount" step="0.01" required>
                            <div class="form-text">Enter an amount greater than the current price</div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn action-btn text-white">Place Bid</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    
    <!-- Include Footer -->
    <jsp:include page="includes/footer.jsp" />
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <!-- DataTables JS -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.4/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.4/js/dataTables.bootstrap5.min.js"></script>
    
    <script>
        // Initialize DataTables
        $(document).ready(function() {
            $('#activeBidsTable').DataTable({
                order: [[4, 'asc']], // Sort by end time ascending
                language: {
                    search: "_INPUT_",
                    searchPlaceholder: "Search bids..."
                }
            });
            
            $('#winningBidsTable').DataTable({
                order: [[2, 'asc']], // Sort by end time ascending
                language: {
                    search: "_INPUT_",
                    searchPlaceholder: "Search bids..."
                }
            });
            
            $('#outbidBidsTable').DataTable({
                order: [[3, 'asc']], // Sort by end time ascending
                language: {
                    search: "_INPUT_",
                    searchPlaceholder: "Search bids..."
                }
            });
            
            $('#completedBidsTable').DataTable({
                order: [[4, 'desc']], // Sort by end date descending
                language: {
                    search: "_INPUT_",
                    searchPlaceholder: "Search bids..."
                }
            });
        });
        
        // Countdown timer for auctions
        function updateCountdowns() {
            document.querySelectorAll('.countdown').forEach(function(element) {
                const endTime = new Date(element.getAttribute('data-end')).getTime();
                const now = new Date().getTime();
                const distance = endTime - now;
                
                if (distance < 0) {
                    element.innerHTML = "Ended";
                    element.classList.add('text-danger');
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
                        element.classList.add('text-danger');
                    }
                    
                    element.innerHTML = countdownText;
                }
            });
        }
        
        // Update countdowns immediately and then every second
        updateCountdowns();
        setInterval(updateCountdowns, 1000);
        
        // Show bid modal with item details
        function showBidModal(itemId, itemTitle, currentPrice) {
            document.getElementById('modalItemId').value = itemId;
            document.getElementById('modalItemTitle').textContent = itemTitle;
            document.getElementById('modalCurrentPrice').textContent = currentPrice.toFixed(2);
            document.getElementById('modalBidAmount').min = (currentPrice + 0.01).toFixed(2);
            document.getElementById('modalBidAmount').value = (currentPrice + 1).toFixed(2);
            
            const bidModal = new bootstrap.Modal(document.getElementById('bidModal'));
            bidModal.show();
        }
    </script>
</body>
</html>
