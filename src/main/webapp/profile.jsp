<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${user.username} - Profile - BidMaster</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <!-- DataTables CSS -->
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.4/css/dataTables.bootstrap5.min.css">
    <!-- Custom CSS -->
    <style>
        .profile-header {
            background-color: #3a5a78;
            color: white;
            padding: 2rem 0;
            margin-bottom: 2rem;
        }
        .profile-avatar {
            width: 120px;
            height: 120px;
            border-radius: 50%;
            object-fit: cover;
            border: 4px solid white;
        }
        .nav-pills .nav-link.active {
            background-color: #3a5a78;
        }
        .nav-pills .nav-link {
            color: #3a5a78;
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
        .action-btn {
            background-color: #3a5a78;
            border-color: #3a5a78;
        }
        .action-btn:hover {
            background-color: #2d4861;
            border-color: #2d4861;
        }
        .item-img {
            width: 60px;
            height: 60px;
            object-fit: cover;
            border-radius: 4px;
        }
    </style>
</head>
<body>
    <!-- Include Header -->
    <jsp:include page="includes/header.jsp" />
    
    <!-- Profile Header -->
    <div class="profile-header">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-md-2 text-center">
                    <img src="${empty user.profileImage ? 'assets/images/default-avatar.png' : user.profileImage}" 
                         alt="${user.username}" class="profile-avatar">
                </div>
                <div class="col-md-10">
                    <h1>@${user.username}</h1>
                    <p class="lead mb-0">${user.email}</p>
                    <p class="mb-0">${user.role} | Member since <fmt:formatDate value="${user.registrationDate}" pattern="MMM yyyy" /></p>
                </div>
            </div>
        </div>
    </div>
    
    <div class="container mb-5">
        <!-- Alert Messages -->
        <c:if test="${not empty sessionScope.successMessage}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="fas fa-check-circle me-2"></i>${sessionScope.successMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <% session.removeAttribute("successMessage"); %>
        </c:if>
        
        <c:if test="${not empty sessionScope.errorMessage}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-circle me-2"></i>${sessionScope.errorMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <% session.removeAttribute("errorMessage"); %>
        </c:if>
        
        <!-- Profile Tabs -->
        <ul class="nav nav-pills mb-4" id="profileTab" role="tablist">
            <li class="nav-item" role="presentation">
                <button class="nav-link active" id="bids-tab" data-bs-toggle="pill" data-bs-target="#bids" 
                        type="button" role="tab" aria-controls="bids" aria-selected="true">
                    <i class="fas fa-gavel me-2"></i>My Bids
                </button>
            </li>
            <li class="nav-item" role="presentation">
                <button class="nav-link" id="watchlist-tab" data-bs-toggle="pill" data-bs-target="#watchlist" 
                        type="button" role="tab" aria-controls="watchlist" aria-selected="false">
                    <i class="fas fa-heart me-2"></i>Watchlist
                </button>
            </li>
            <c:if test="${user.role == 'seller' || user.role == 'admin'}">
                <li class="nav-item" role="presentation">
                    <button class="nav-link" id="listings-tab" data-bs-toggle="pill" data-bs-target="#listings" 
                            type="button" role="tab" aria-controls="listings" aria-selected="false">
                        <i class="fas fa-box me-2"></i>My Listings
                    </button>
                </li>
            </c:if>
            <li class="nav-item" role="presentation">
                <button class="nav-link" id="transactions-tab" data-bs-toggle="pill" data-bs-target="#transactions" 
                        type="button" role="tab" aria-controls="transactions" aria-selected="false">
                    <i class="fas fa-exchange-alt me-2"></i>Transactions
                </button>
            </li>
            <li class="nav-item" role="presentation">
                <button class="nav-link" id="settings-tab" data-bs-toggle="pill" data-bs-target="#settings" 
                        type="button" role="tab" aria-controls="settings" aria-selected="false">
                    <i class="fas fa-cog me-2"></i>Settings
                </button>
            </li>
        </ul>
        
        <!-- Tab Content -->
        <div class="tab-content" id="profileTabContent">
            <!-- My Bids Tab -->
            <div class="tab-pane fade show active" id="bids" role="tabpanel" aria-labelledby="bids-tab">
                <div class="card shadow-sm">
                    <div class="card-header bg-white">
                        <h5 class="card-title mb-0">My Bids</h5>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${not empty userBids}">
                                <div class="table-responsive">
                                    <table class="table table-hover" id="userBidsTable">
                                        <thead>
                                            <tr>
                                                <th>Item</th>
                                                <th>Bid Amount</th>
                                                <th>Current Price</th>
                                                <th>Status</th>
                                                <th>End Time</th>
                                                <th>Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="bid" items="${userBids}">
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
                                                    <td>$${bid.bidAmount}</td>
                                                    <td>$${bid.currentPrice}</td>
                                                    <td>
                                                        <span class="badge ${bid.status == 'winning' ? 'badge-winning' : (bid.status == 'outbid' ? 'badge-outbid' : 'badge-active')}">
                                                            ${bid.status}
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <c:if test="${not empty bid.endTime}">
                                                            <span class="countdown" data-end="${bid.endTime}">
                                                                ${bid.endTime}
                                                            </span>
                                                        </c:if>
                                                    </td>
                                                    <td>
                                                        <div class="btn-group btn-group-sm">
                                                            <a href="ItemDetailsServlet?id=${bid.itemId}" class="btn action-btn text-white" title="View Item">
                                                                <i class="fas fa-eye"></i>
                                                            </a>
                                                            <c:if test="${bid.status != 'winning' && bid.itemStatus == 'active'}">
                                                                <button type="button" class="btn btn-warning" 
                                                                        onclick="showEditBidModal(${bid.bidId}, '${bid.itemTitle}', ${bid.bidAmount})" title="Edit Bid">
                                                                    <i class="fas fa-edit"></i>
                                                                </button>
                                                                <button type="button" class="btn btn-danger" 
                                                                        onclick="showDeleteBidModal(${bid.bidId}, '${bid.itemTitle}')" title="Delete Bid">
                                                                    <i class="fas fa-trash"></i>
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
                                    <h5>You haven't placed any bids yet</h5>
                                    <p class="text-muted">Start bidding on items you're interested in!</p>
                                    <a href="BrowseItemsServlet" class="btn action-btn text-white mt-3">
                                        <i class="fas fa-search me-2"></i>Browse Active Auctions
                                    </a>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
            
            <!-- Watchlist Tab -->
            <div class="tab-pane fade" id="watchlist" role="tabpanel" aria-labelledby="watchlist-tab">
                <div class="card shadow-sm">
                    <div class="card-header bg-white">
                        <h5 class="card-title mb-0">My Watchlist</h5>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${not empty watchlist}">
                                <div class="table-responsive">
                                    <table class="table table-hover" id="watchlistTable">
                                        <thead>
                                            <tr>
                                                <th>Item</th>
                                                <th>Current Price</th>
                                                <th>End Time</th>
                                                <th>Status</th>
                                                <th>Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="item" items="${watchlist}">
                                                <tr>
                                                    <td>
                                                        <div class="d-flex align-items-center">
                                                            <img src="${empty item.itemImageUrl ? 'assets/images/no-image.png' : item.itemImageUrl}" 
                                                                 alt="${item.itemTitle}" class="item-img me-3">
                                                            <div>
                                                                <div class="fw-bold">${item.itemTitle}</div>
                                                                <small class="text-muted">Seller: ${item.sellerUsername}</small>
                                                            </div>
                                                        </div>
                                                    </td>
                                                    <td>$${item.currentPrice}</td>
                                                    <td>
                                                        <c:if test="${not empty item.endTime}">
                                                            <span class="countdown" data-end="${item.endTime}">
                                                                ${item.endTime}
                                                            </span>
                                                        </c:if>
                                                    </td>
                                                    <td>
                                                        <span class="badge ${item.status == 'active' ? 'badge-active' : 'badge-outbid'}">
                                                            ${item.status}
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <div class="btn-group btn-group-sm">
                                                            <a href="ItemDetailsServlet?id=${item.itemId}" class="btn action-btn text-white" title="View Item">
                                                                <i class="fas fa-eye"></i>
                                                            </a>
                                                            <a href="WatchlistServlet?action=remove&itemId=${item.itemId}" class="btn btn-danger" title="Remove from Watchlist">
                                                                <i class="fas fa-trash"></i>
                                                            </a>
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
                                    <i class="fas fa-heart fa-4x text-muted mb-3"></i>
                                    <h5>Your watchlist is empty</h5>
                                    <p class="text-muted">Save items you're interested in to your watchlist!</p>
                                    <a href="BrowseItemsServlet" class="btn action-btn text-white mt-3">
                                        <i class="fas fa-search me-2"></i>Browse Active Auctions
                                    </a>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
            
            <!-- My Listings Tab -->
            <div class="tab-pane fade" id="listings" role="tabpanel" aria-labelledby="listings-tab">
                <div class="card shadow-sm">
                    <div class="card-header bg-white d-flex justify-content-between align-items-center">
                        <h5 class="card-title mb-0">My Listings</h5>
                        <a href="seller-dashboard.jsp" class="btn btn-sm action-btn text-white">
                            <i class="fas fa-tachometer-alt me-2"></i>Go to Seller Dashboard
                        </a>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${not empty listings}">
                                <div class="table-responsive">
                                    <table class="table table-hover" id="listingsTable">
                                        <thead>
                                            <tr>
                                                <th>Item</th>
                                                <th>Current Price</th>
                                                <th>Bids</th>
                                                <th>End Time</th>
                                                <th>Status</th>
                                                <th>Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="item" items="${listings}">
                                                <tr>
                                                    <td>
                                                        <div class="d-flex align-items-center">
                                                            <img src="${empty item.imageUrl ? 'assets/images/no-image.png' : item.imageUrl}" 
                                                                 alt="${item.title}" class="item-img me-3">
                                                            <div class="fw-bold">${item.title}</div>
                                                        </div>
                                                    </td>
                                                    <td>$${item.currentPrice}</td>
                                                    <td>${item.bidCount}</td>
                                                    <td>
                                                        <c:if test="${not empty item.endTime}">
                                                            <span class="countdown" data-end="${item.endTime}">
                                                                ${item.endTime}
                                                            </span>
                                                        </c:if>
                                                    </td>
                                                    <td>
                                                        <span class="badge ${item.status == 'active' ? 'badge-active' : (item.status == 'pending' ? 'badge-outbid' : 'badge-winning')}">
                                                            ${item.status}
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <div class="btn-group btn-group-sm">
                                                            <a href="ItemDetailsServlet?id=${item.itemId}" class="btn action-btn text-white" title="View">
                                                                <i class="fas fa-eye"></i>
                                                            </a>
                                                            <c:if test="${item.status != 'completed'}">
                                                                <a href="EditItemServlet?id=${item.itemId}" class="btn btn-warning" title="Edit">
                                                                    <i class="fas fa-edit"></i>
                                                                </a>
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
                                    <i class="fas fa-box fa-4x text-muted mb-3"></i>
                                    <h5>You don't have any listings yet</h5>
                                    <p class="text-muted">Start selling by creating your first auction!</p>
                                    <a href="create-item.jsp" class="btn action-btn text-white mt-3">
                                        <i class="fas fa-plus-circle me-2"></i>Create New Auction
                                    </a>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
            
            <!-- Transactions Tab -->
            <div class="tab-pane fade" id="transactions" role="tabpanel" aria-labelledby="transactions-tab">
                <div class="card shadow-sm">
                    <div class="card-header bg-white">
                        <h5 class="card-title mb-0">My Transactions</h5>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${not empty transactions}">
                                <div class="table-responsive">
                                    <table class="table table-hover" id="transactionsTable">
                                        <thead>
                                            <tr>
                                                <th>Item</th>
                                                <th>Role</th>
                                                <th>Amount</th>
                                                <th>Date</th>
                                                <th>Status</th>
                                                <th>Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="transaction" items="${transactions}">
                                                <tr>
                                                    <td>${transaction.itemTitle}</td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${transaction.sellerId == user.userId}">
                                                                <span class="badge bg-info">Seller</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge bg-primary">Buyer</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>$${transaction.amount}</td>
                                                    <td>
                                                        <c:if test="${not empty transaction.transactionDate}">
                                                            <fmt:formatDate value="${transaction.transactionDate}" pattern="MMM dd, yyyy" />
                                                        </c:if>
                                                    </td>
                                                    <td>
                                                        <span class="badge ${transaction.status == 'completed' ? 'badge-winning' : (transaction.status == 'pending' ? 'badge-active' : 'badge-outbid')}">
                                                            ${transaction.status}
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <a href="TransactionDetailsServlet?id=${transaction.transactionId}" class="btn btn-sm action-btn text-white">
                                                            <i class="fas fa-eye me-1"></i>Details
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
                                    <i class="fas fa-exchange-alt fa-4x text-muted mb-3"></i>
                                    <h5>You don't have any transactions yet</h5>
                                    <p class="text-muted">Your transaction history will appear here</p>
                                    <a href="BrowseItemsServlet" class="btn action-btn text-white mt-3">
                                        <i class="fas fa-search me-2"></i>Browse Active Auctions
                                    </a>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
            
            <!-- Settings Tab -->
            <div class="tab-pane fade" id="settings" role="tabpanel" aria-labelledby="settings-tab">
                <div class="card shadow-sm">
                    <div class="card-header bg-white">
                        <h5 class="card-title mb-0">Account Settings</h5>
                    </div>
                    <div class="card-body">
                        <form action="UpdateProfileServlet" method="post" enctype="multipart/form-data">
                            <div class="row mb-4">
                                <div class="col-md-4 text-center">
                                    <div class="mb-3">
                                        <img src="${empty user.profileImage ? 'assets/images/default-avatar.png' : user.profileImage}" 
                                             alt="${user.username}" class="rounded-circle mb-3" width="150" height="150" id="profilePreview">
                                        <div class="d-grid">
                                            <label for="profileImage" class="btn btn-outline-primary">
                                                <i class="fas fa-upload me-2"></i>Change Photo
                                            </label>
                                            <input type="file" id="profileImage" name="profileImage" accept="image/*" class="d-none" onchange="previewImage(this)">
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-8">
                                    <div class="mb-3">
                                        <label for="username" class="form-label">Username</label>
                                        <input type="text" class="form-control" id="username" name="username" value="${user.username}" readonly>
                                        <div class="form-text">Username cannot be changed</div>
                                    </div>
                                    <div class="mb-3">
                                        <label for="email" class="form-label">Email Address</label>
                                        <input type="email" class="form-control" id="email" name="email" value="${user.email}" required>
                                    </div>
                                    <div class="mb-3">
                                        <label for="fullName" class="form-label">Full Name</label>
                                        <input type="text" class="form-control" id="fullName" name="fullName" value="${user.fullName}" required>
                                    </div>
                                    <div class="mb-3">
                                        <label for="contactNo" class="form-label">Contact Number</label>
                                        <input type="text" class="form-control" id="contactNo" name="contactNo" value="${user.contactNo}">
                                    </div>
                                </div>
                            </div>
                            
                            <hr class="my-4">
                            
                            <h5 class="mb-3">Change Password</h5>
                            <div class="row">
                                <div class="col-md-4 mb-3">
                                    <label for="currentPassword" class="form-label">Current Password</label>
                                    <input type="password" class="form-control" id="currentPassword" name="currentPassword">
                                </div>
                                <div class="col-md-4 mb-3">
                                    <label for="newPassword" class="form-label">New Password</label>
                                    <input type="password" class="form-control" id="newPassword" name="newPassword">
                                </div>
                                <div class="col-md-4 mb-3">
                                    <label for="confirmPassword" class="form-label">Confirm New Password</label>
                                    <input type="password" class="form-control" id="confirmPassword" name="confirmPassword">
                                </div>
                            </div>
                            <div class="form-text mb-3">Leave password fields empty if you don't want to change your password</div>
                            
                            <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                <button type="reset" class="btn btn-outline-secondary">
                                    <i class="fas fa-undo me-2"></i>Reset
                                </button>
                                <button type="submit" class="btn action-btn text-white">
                                    <i class="fas fa-save me-2"></i>Save Changes
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Edit Bid Modal -->
    <div class="modal fade" id="editBidModal" tabindex="-1" aria-labelledby="editBidModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="editBidModalLabel">Edit Bid</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form action="UserBidsServlet" method="post">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" id="editBidId" name="bidId">
                    <div class="modal-body">
                        <div class="mb-3">
                            <h6 id="editItemTitle"></h6>
                        </div>
                        <div class="mb-3">
                            <label for="editBidAmount" class="form-label">New Bid Amount ($)</label>
                            <input type="number" class="form-control" id="editBidAmount" name="bidAmount" step="0.01" required>
                            <div class="form-text">Enter an amount higher than the current price</div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn action-btn text-white">Update Bid</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    
    <!-- Delete Bid Modal -->
    <div class="modal fade" id="deleteBidModal" tabindex="-1" aria-labelledby="deleteBidModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="deleteBidModalLabel">Confirm Delete</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <p>Are you sure you want to delete your bid on <span id="deleteItemTitle" class="fw-bold"></span>?</p>
                    <p class="text-danger">This action cannot be undone.</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <form action="UserBidsServlet" method="post">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" id="deleteBidId" name="bidId">
                        <button type="submit" class="btn btn-danger">Delete</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Include Footer -->
    <jsp:include page="includes/footer.jsp" />
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <!-- DataTables JS -->
    <script src="https://cdn.datatables.net/1.13.4/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.4/js/dataTables.bootstrap5.min.js"></script>
    
    <script>
        // Initialize DataTables
        $(document).ready(function() {
            $('#userBidsTable').DataTable({
                order: [[4, 'asc']], // Sort by end time
                language: {
                    search: "_INPUT_",
                    searchPlaceholder: "Search bids..."
                }
            });
            
            $('#watchlistTable').DataTable({
                order: [[2, 'asc']], // Sort by end time
                language: {
                    search: "_INPUT_",
                    searchPlaceholder: "Search watchlist..."
                }
            });
            
            $('#listingsTable').DataTable({
                order: [[3, 'asc']], // Sort by end time
                language: {
                    search: "_INPUT_",
                    searchPlaceholder: "Search listings..."
                }
            });
            
            $('#transactionsTable').DataTable({
                order: [[3, 'desc']], // Sort by date (newest first)
                language: {
                    search: "_INPUT_",
                    searchPlaceholder: "Search transactions..."
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
        
        // Show edit bid modal
        function showEditBidModal(bidId, itemTitle, bidAmount) {
            document.getElementById('editBidId').value = bidId;
            document.getElementById('editItemTitle').textContent = itemTitle;
            document.getElementById('editBidAmount').value = bidAmount;
            document.getElementById('editBidAmount').min = bidAmount + 0.01;
            
            const editModal = new bootstrap.Modal(document.getElementById('editBidModal'));
            editModal.show();
        }
        
        // Show delete bid modal
        function showDeleteBidModal(bidId, itemTitle) {
            document.getElementById('deleteBidId').value = bidId;
            document.getElementById('deleteItemTitle').textContent = itemTitle;
            
            const deleteModal = new bootstrap.Modal(document.getElementById('deleteBidModal'));
            deleteModal.show();
        }
        
        // Preview profile image before upload
        function previewImage(input) {
            if (input.files && input.files[0]) {
                var reader = new FileReader();
                
                reader.onload = function(e) {
                    document.getElementById('profilePreview').src = e.target.result;
                }
                
                reader.readAsDataURL(input.files[0]);
            }
        }
    </script>
</body>
</html>
