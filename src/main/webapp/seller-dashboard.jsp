<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Seller Dashboard - BidMaster</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <!-- Chart.js -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <!-- Custom CSS -->
    <style>
        body {
            background-color: #f8f9fa;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }
        .dashboard-header {
            background-color: #3a5a78;
            color: white;
            padding: 2rem 0;
            margin-bottom: 2rem;
        }
        .stats-card {
            transition: all 0.3s ease;
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            height: 100%;
        }
        .stats-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 20px rgba(0, 0, 0, 0.15);
        }
        .card-icon {
            font-size: 2.5rem;
            margin-bottom: 1rem;
            color: #3a5a78;
        }
        .countdown {
            font-size: 0.8rem;
            color: #dc3545;
        }
        .badge-active {
            background-color: #28a745;
        }
        .badge-pending {
            background-color: #ffc107;
        }
        .badge-completed {
            background-color: #6c757d;
        }
        .badge-outbid {
            background-color: #dc3545;
        }
        .badge-winning {
            background-color: #28a745;
        }
        .item-img {
            height: 120px;
            object-fit: cover;
        }
        .action-btn {
            background-color: #3a5a78;
            border-color: #3a5a78;
        }
        .action-btn:hover {
            background-color: #2d4861;
            border-color: #2d4861;
        }
        .nav-pills .nav-link.active {
            background-color: #3a5a78;
        }
        .nav-pills .nav-link {
            color: #3a5a78;
        }
        .delete-btn {
            background-color: #dc3545;
            border-color: #dc3545;
        }
        .delete-btn:hover {
            background-color: #c82333;
            border-color: #bd2130;
        }
        .edit-btn {
            background-color: #ffc107;
            border-color: #ffc107;
            color: #212529;
        }
        .edit-btn:hover {
            background-color: #e0a800;
            border-color: #d39e00;
            color: #212529;
        }
        .preview-image {
            max-height: 150px;
            object-fit: contain;
        }
    </style>
</head>
<body>
    <!-- Include Header -->
    <jsp:include page="includes/header.jsp" />
    
    <!-- Dashboard Header -->
    <div class="dashboard-header">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-md-6">
                    <h1>Seller Dashboard</h1>
                    <p class="lead mb-0">Welcome, ${sessionScope.fullName}!</p>
                </div>
                <div class="col-md-6 text-md-end">
                    <button type="button" class="btn btn-light btn-lg" data-bs-toggle="modal" data-bs-target="#createItemModal">
                        <i class="fas fa-plus-circle me-2"></i>List New Item
                    </button>
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
        
        <!-- Stats Cards -->
        <div class="row mb-4">
            <div class="col-md-3 mb-4">
                <div class="stats-card">
                    <div class="card-body text-center">
                        <div class="card-icon">
                            <i class="fas fa-box"></i>
                        </div>
                        <h5 class="card-title">Active Listings</h5>
                        <h2 class="mb-0">${stats.activeListingsCount}</h2>
                        <p class="text-muted">Items you're selling</p>
                        <a href="#" class="btn btn-sm action-btn text-white" data-bs-toggle="pill" data-bs-target="#active-listings">View All</a>
                    </div>
                </div>
            </div>
            <div class="col-md-3 mb-4">
                <div class="stats-card">
                    <div class="card-body text-center">
                        <div class="card-icon">
                            <i class="fas fa-check-circle"></i>
                        </div>
                        <h5 class="card-title">Completed Sales</h5>
                        <h2 class="mb-0">${stats.completedSalesCount}</h2>
                        <p class="text-muted">Items you've sold</p>
                        <a href="#" class="btn btn-sm action-btn text-white" data-bs-toggle="pill" data-bs-target="#completed-sales">View All</a>
                    </div>
                </div>
            </div>
            <div class="col-md-3 mb-4">
                <div class="stats-card">
                    <div class="card-body text-center">
                        <div class="card-icon">
                            <i class="fas fa-hand-paper"></i>
                        </div>
                        <h5 class="card-title">Total Bids</h5>
                        <h2 class="mb-0">${stats.totalBidsReceived}</h2>
                        <p class="text-muted">On your items</p>
                        <a href="#" class="btn btn-sm action-btn text-white" data-bs-toggle="pill" data-bs-target="#recent-bids">View All</a>
                    </div>
                </div>
            </div>
            <div class="col-md-3 mb-4">
                <div class="stats-card">
                    <div class="card-body text-center">
                        <div class="card-icon">
                            <i class="fas fa-dollar-sign"></i>
                        </div>
                        <h5 class="card-title">Total Revenue</h5>
                        <h2 class="mb-0">$<fmt:formatNumber value="${stats.totalRevenue}" pattern="#,##0.00"/></h2>
                        <p class="text-muted">From sales</p>
                        <a href="#" class="btn btn-sm action-btn text-white" data-bs-toggle="pill" data-bs-target="#sales-revenue">View All</a>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Main Content Tabs -->
        <ul class="nav nav-pills mb-4" id="dashboardTab" role="tablist">
            <li class="nav-item" role="presentation">
                <button class="nav-link active" id="active-listings-tab" data-bs-toggle="pill" data-bs-target="#active-listings" 
                        type="button" role="tab" aria-controls="active-listings" aria-selected="true">
                    <i class="fas fa-box me-2"></i>Active Listings
                </button>
            </li>
            <li class="nav-item" role="presentation">
                <button class="nav-link" id="manage-items-tab" data-bs-toggle="pill" data-bs-target="#manage-items" 
                        type="button" role="tab" aria-controls="manage-items" aria-selected="false">
                    <i class="fas fa-tasks me-2"></i>Manage Items
                </button>
            </li>
            <li class="nav-item" role="presentation">
                <button class="nav-link" id="recent-bids-tab" data-bs-toggle="pill" data-bs-target="#recent-bids" 
                        type="button" role="tab" aria-controls="recent-bids" aria-selected="false">
                    <i class="fas fa-hand-paper me-2"></i>Recent Bids
                </button>
            </li>
            <li class="nav-item" role="presentation">
                <button class="nav-link" id="completed-sales-tab" data-bs-toggle="pill" data-bs-target="#completed-sales" 
                        type="button" role="tab" aria-controls="completed-sales" aria-selected="false">
                    <i class="fas fa-check-circle me-2"></i>Completed Sales
                </button>
            </li>
            <li class="nav-item" role="presentation">
                <button class="nav-link" id="sales-revenue-tab" data-bs-toggle="pill" data-bs-target="#sales-revenue" 
                        type="button" role="tab" aria-controls="sales-revenue" aria-selected="false">
                    <i class="fas fa-chart-line me-2"></i>Sales Analytics
                </button>
            </li>
        </ul>
        
        <!-- Tab Content -->
        <div class="tab-content" id="dashboardTabContent">
            <!-- Active Listings Tab -->
            <div class="tab-pane fade show active" id="active-listings" role="tabpanel" aria-labelledby="active-listings-tab">
                <div class="card shadow-sm">
                    <div class="card-header bg-white d-flex justify-content-between align-items-center">
                        <h5 class="card-title mb-0">Your Active Listings</h5>
                        <button type="button" class="btn btn-sm action-btn text-white" data-bs-toggle="modal" data-bs-target="#createItemModal">
                            <i class="fas fa-plus-circle me-2"></i>List New Item
                        </button>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${not empty activeListings}">
                                <div class="table-responsive">
                                    <table class="table table-hover">
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
                                            <c:forEach var="item" items="${activeListings}">
                                                <tr>
                                                    <td>
                                                        <div class="d-flex align-items-center">
                                                            <img src="${empty item.imageUrl ? 'assets/images/no-image.png' : item.imageUrl}" 
                                                                 alt="${item.title}" class="me-2" width="50" height="50" style="object-fit: cover;">
                                                            <div class="fw-bold">${item.title}</div>
                                                        </div>
                                                    </td>
                                                    <td>$<fmt:formatNumber value="${item.currentPrice}" pattern="#,##0.00"/></td>
                                                    <td>${item.bidCount}</td>
                                                    <td>
                                                        <c:if test="${not empty item.endTime}">
                                                            <span class="countdown" data-end="${item.endTime}">
                                                                ${item.endTime.toLocalDate()} ${item.endTime.toLocalTime().toString().substring(0, 5)}
                                                            </span>
                                                        </c:if>
                                                    </td>
                                                    <td>
                                                        <span class="badge badge-active">
                                                            ${item.status}
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <div class="btn-group btn-group-sm">
                                                            <a href="ItemDetailsServlet?id=${item.itemId}" class="btn action-btn text-white" title="View">
                                                                <i class="fas fa-eye"></i>
                                                            </a>
                                                            <button type="button" class="btn edit-btn" 
                                                                    onclick="editItem(${item.itemId})" title="Edit">
                                                                <i class="fas fa-edit"></i>
                                                            </button>
                                                            <button type="button" class="btn delete-btn text-white" 
                                                                    onclick="confirmDelete(${item.itemId}, '${item.title}')" title="Delete">
                                                                <i class="fas fa-trash"></i>
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
                                    <i class="fas fa-box fa-4x text-muted mb-3"></i>
                                    <h5>You don't have any active listings</h5>
                                    <p class="text-muted">Start selling by creating a new listing!</p>
                                    <button type="button" class="btn action-btn text-white mt-3" data-bs-toggle="modal" data-bs-target="#createItemModal">
                                        <i class="fas fa-plus-circle me-2"></i>List New Item
                                    </button>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
            
            <!-- Manage Items Tab -->
            <div class="tab-pane fade" id="manage-items" role="tabpanel" aria-labelledby="manage-items-tab">
                <div class="card shadow-sm">
                    <div class="card-header bg-white d-flex justify-content-between align-items-center">
                        <h5 class="card-title mb-0">Manage All Your Items</h5>
                        <div>
                            <button type="button" class="btn btn-sm action-btn text-white" data-bs-toggle="modal" data-bs-target="#createItemModal">
                                <i class="fas fa-plus-circle me-2"></i>List New Item
                            </button>
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="mb-3">
                            <div class="input-group">
                                <input type="text" class="form-control" id="itemSearchInput" placeholder="Search your items...">
                                <select class="form-select" id="itemStatusFilter" style="max-width: 150px;">
                                    <option value="all">All Status</option>
                                    <option value="active">Active</option>
                                    <option value="pending">Pending</option>
                                    <option value="completed">Completed</option>
                                </select>
                                <button class="btn action-btn text-white" type="button" id="searchItemsBtn">
                                    <i class="fas fa-search"></i>
                                </button>
                            </div>
                        </div>
                        
                        <div class="table-responsive">
                            <table class="table table-hover" id="itemsTable">
                                <thead>
                                    <tr>
                                        <th>Item</th>
                                        <th>Category</th>
                                        <th>Current Price</th>
                                        <th>Bids</th>
                                        <th>End Time</th>
                                        <th>Status</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="item" items="${allSellerItems}">
                                        <tr data-status="${item.status}">
                                            <td>
                                                <div class="d-flex align-items-center">
                                                    <img src="${empty item.imageUrl ? 'assets/images/no-image.png' : item.imageUrl}" 
                                                         alt="${item.title}" class="me-2" width="50" height="50" style="object-fit: cover;">
                                                    <div class="fw-bold">${item.title}</div>
                                                </div>
                                            </td>
                                            <td>${item.categoryName}</td>
                                            <td>$<fmt:formatNumber value="${item.currentPrice}" pattern="#,##0.00"/></td>
                                            <td>${item.bidCount}</td>
                                            <td>
                                                <c:if test="${not empty item.endTime}">
                                                    <span class="countdown" data-end="${item.endTime}">
                                                        ${item.endTime.toLocalDate()} ${item.endTime.toLocalTime().toString().substring(0, 5)}
                                                    </span>
                                                </c:if>
                                            </td>
                                            <td>
                                                <span class="badge ${item.status == 'active' ? 'badge-active' : (item.status == 'pending' ? 'badge-pending' : 'badge-completed')}">
                                                    ${item.status}
                                                </span>
                                            </td>
                                            <td>
                                                <div class="btn-group btn-group-sm">
                                                    <a href="ItemDetailsServlet?id=${item.itemId}" class="btn action-btn text-white" title="View">
                                                        <i class="fas fa-eye"></i>
                                                    </a>
                                                    <c:if test="${item.status != 'completed'}">
                                                        <button type="button" class="btn edit-btn" 
                                                                onclick="editItem(${item.itemId})" title="Edit">
                                                            <i class="fas fa-edit"></i>
                                                        </button>
                                                        <button type="button" class="btn delete-btn text-white" 
                                                                onclick="confirmDelete(${item.itemId}, '${item.title}')" title="Delete">
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
                    </div>
                </div>
            </div>
            
            <!-- Recent Bids Tab -->
            <div class="tab-pane fade" id="recent-bids" role="tabpanel" aria-labelledby="recent-bids-tab">
                <div class="card shadow-sm">
                    <div class="card-header bg-white">
                        <h5 class="card-title mb-0">Recent Bids on Your Items</h5>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${not empty recentBids}">
                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead>
                                            <tr>
                                                <th>Item</th>
                                                <th>Bidder</th>
                                                <th>Bid Amount</th>
                                                <th>Bid Time</th>
                                                <th>Status</th>
                                                <th>Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="bid" items="${recentBids}">
                                                <tr>
                                                    <td>
                                                        <div class="d-flex align-items-center">
                                                            <img src="${empty bid.itemImageUrl ? 'assets/images/no-image.png' : bid.itemImageUrl}" 
                                                                 alt="${bid.itemTitle}" class="me-2" width="50" height="50" style="object-fit: cover;">
                                                            <div class="fw-bold">${bid.itemTitle}</div>
                                                        </div>
                                                    </td>
                                                    <td>${bid.bidderUsername}</td>
                                                    <td>$<fmt:formatNumber value="${bid.bidAmount}" pattern="#,##0.00"/></td>
                                                    <td>
                                                        <c:if test="${not empty bid.bidTime}">
                                                            <fmt:formatDate value="${bid.bidTime}" pattern="MMM dd, yyyy HH:mm" />
                                                        </c:if>
                                                    </td>
                                                    <td>
                                                        <span class="badge ${bid.status == 'winning' ? 'badge-winning' : 'badge-outbid'}">
                                                            ${bid.status}
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
                                    <i class="fas fa-hand-paper fa-4x text-muted mb-3"></i>
                                    <h5>No bids on your items yet</h5>
                                    <p class="text-muted">When users bid on your items, they'll appear here.</p>
                                    <button type="button" class="btn action-btn text-white mt-3" data-bs-toggle="modal" data-bs-target="#createItemModal">
                                        <i class="fas fa-plus-circle me-2"></i>List New Item
                                    </button>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
            
            <!-- Completed Sales Tab -->
            <div class="tab-pane fade" id="completed-sales" role="tabpanel" aria-labelledby="completed-sales-tab">
                <div class="card shadow-sm">
                    <div class="card-header bg-white">
                        <h5 class="card-title mb-0">Your Completed Sales</h5>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${not empty completedSales}">
                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead>
                                            <tr>
                                                <th>Item</th>
                                                <th>Buyer</th>
                                                <th>Final Price</th>
                                                <th>End Date</th>
                                                <th>Payment Status</th>
                                                <th>Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="sale" items="${completedSales}">
                                                <tr>
                                                    <td>
                                                        <div class="d-flex align-items-center">
                                                            <img src="${empty sale.imageUrl ? 'assets/images/no-image.png' : sale.imageUrl}" 
                                                                 alt="${sale.itemTitle}" class="me-2" width="50" height="50" style="object-fit: cover;">
                                                            <div class="fw-bold">${sale.itemTitle}</div>
                                                        </div>
                                                    </td>
                                                    <td>${sale.buyerUsername}</td>
                                                    <td>$<fmt:formatNumber value="${sale.amount}" pattern="#,##0.00"/></td>
                                                    <td>
                                                        <c:if test="${not empty sale.endTime}">
                                                            ${sale.endTime.toLocalDate()}
                                                        </c:if>
                                                    </td>
                                                    <td>
                                                        <span class="badge ${sale.status == 'completed' ? 'badge-active' : 'badge-pending'}">
                                                            ${sale.status}
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <a href="ItemDetailsServlet?id=${sale.itemId}" class="btn btn-sm action-btn text-white">
                                                            <i class="fas fa-eye me-1"></i>View
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
                                    <i class="fas fa-check-circle fa-4x text-muted mb-3"></i>
                                    <h5>No completed sales yet</h5>
                                    <p class="text-muted">When your items sell, they'll appear here.</p>
                                    <button type="button" class="btn action-btn text-white mt-3" data-bs-toggle="modal" data-bs-target="#createItemModal">
                                        <i class="fas fa-plus-circle me-2"></i>List New Item
                                    </button>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
            
            <!-- Sales Analytics Tab -->
            <div class="tab-pane fade" id="sales-revenue" role="tabpanel" aria-labelledby="sales-revenue-tab">
                <div class="card shadow-sm">
                    <div class="card-header bg-white">
                        <h5 class="card-title mb-0">Sales Analytics</h5>
                    </div>
                    <div class="card-body">
                        <div class="row mb-4">
                            <div class="col-md-8">
                                <h6 class="mb-3">Monthly Revenue</h6>
                                <canvas id="revenueChart" height="300"></canvas>
                            </div>
                            <div class="col-md-4">
                                <h6 class="mb-3">Sales by Category</h6>
                                <canvas id="categoryChart" height="300"></canvas>
                            </div>
                        </div>
                        
                        <div class="row">
                            <div class="col-md-6">
                                <div class="card">
                                    <div class="card-body">
                                        <h6 class="card-title">Top Selling Items</h6>
                                        <div class="table-responsive">
                                            <table class="table table-sm">
                                                <thead>
                                                    <tr>
                                                        <th>Item</th>
                                                        <th>Price</th>
                                                        <th>Bids</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="item" items="${topSellingItems}" varStatus="loop">
                                                        <c:if test="${loop.index < 5}">
                                                            <tr>
                                                                <td>${item.title}</td>
                                                                <td>$<fmt:formatNumber value="${item.finalPrice}" pattern="#,##0.00"/></td>
                                                                <td>${item.bidCount}</td>
                                                            </tr>
                                                        </c:if>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="card">
                                    <div class="card-body">
                                        <h6 class="card-title">Recent Transactions</h6>
                                        <div class="table-responsive">
                                            <table class="table table-sm">
                                                <thead>
                                                    <tr>
                                                        <th>Date</th>
                                                        <th>Item</th>
                                                        <th>Amount</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="transaction" items="${recentTransactions}" varStatus="loop">
                                                        <c:if test="${loop.index < 5}">
                                                            <tr>
                                                                <td>
                                                                    <c:if test="${not empty transaction.transactionDate}">
                                                                        <fmt:formatDate value="${transaction.transactionDate}" pattern="MMM dd, yyyy" />
                                                                    </c:if>
                                                                </td>
                                                                <td>${transaction.itemTitle}</td>
                                                                <td>$<fmt:formatNumber value="${transaction.amount}" pattern="#,##0.00"/></td>
                                                            </tr>
                                                        </c:if>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Create Item Modal -->
    <div class="modal fade" id="createItemModal" tabindex="-1" aria-labelledby="createItemModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="createItemModalLabel">List New Item</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form action="CreateItemServlet" method="post" enctype="multipart/form-data" id="createItemForm">
                    <div class="modal-body">
                        <div class="row mb-3">
                            <div class="col-md-8">
                                <label for="title" class="form-label">Item Title</label>
                                <input type="text" class="form-control" id="title" name="title" required>
                            </div>
                            <div class="col-md-4">
                                <label for="categoryId" class="form-label">Category</label>
                                <select class="form-select" id="categoryId" name="categoryId" required>
                                    <option value="">Select Category</option>
                                    <c:forEach var="category" items="${categories}">
                                        <option value="${category.categoryId}">${category.categoryName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        
                        <div class="mb-3">
                            <label for="description" class="form-label">Description</label>
                            <textarea class="form-control" id="description" name="description" rows="4" required></textarea>
                        </div>
                        
                        <div class="row mb-3">
                            <div class="col-md-4">
                                <label for="startingPrice" class="form-label">Starting Price ($)</label>
                                <input type="number" class="form-control" id="startingPrice" name="startingPrice" min="0.01" step="0.01" required>
                            </div>
                            <div class="col-md-4">
                                <label for="reservePrice" class="form-label">Reserve Price ($) (Optional)</label>
                                <input type="number" class="form-control" id="reservePrice" name="reservePrice" min="0.01" step="0.01">
                            </div>
                            <div class="col-md-4">
                                <label for="auctionDuration" class="form-label">Duration (Days)</label>
                                <select class="form-select" id="auctionDuration" name="auctionDuration" required>
                                    <option value="1">1 Day</option>
                                    <option value="3">3 Days</option>
                                    <option value="5">5 Days</option>
                                    <option value="7" selected>7 Days</option>
                                    <option value="10">10 Days</option>
                                    <option value="14">14 Days</option>
                                    <option value="30">30 Days</option>
                                </select>
                            </div>
                        </div>
                        
                        <div class="mb-3">
                            <label for="itemImage" class="form-label">Item Image</label>
                            <input type="file" class="form-control" id="itemImage" name="itemImage" accept="image/*">
                            <div class="form-text">Max file size: 5MB. Supported formats: JPG, PNG, GIF</div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn action-btn text-white">Create Listing</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    
    <!-- Edit Item Modal -->
    <div class="modal fade" id="editItemModal" tabindex="-1" aria-labelledby="editItemModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="editItemModalLabel">Edit Item</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form action="UpdateItemServlet" method="post" enctype="multipart/form-data" id="editItemForm">
                    <div class="modal-body">
                        <input type="hidden" id="editItemId" name="itemId">
                        
                        <div class="row mb-3">
                            <div class="col-md-8">
                                <label for="editTitle" class="form-label">Item Title</label>
                                <input type="text" class="form-control" id="editTitle" name="title" required>
                            </div>
                            <div class="col-md-4">
                                <label for="editCategoryId" class="form-label">Category</label>
                                <select class="form-select" id="editCategoryId" name="categoryId" required>
                                    <option value="">Select Category</option>
                                    <c:forEach var="category" items="${categories}">
                                        <option value="${category.categoryId}">${category.categoryName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        
                        <div class="mb-3">
                            <label for="editDescription" class="form-label">Description</label>
                            <textarea class="form-control" id="editDescription" name="description" rows="4" required></textarea>
                        </div>
                        
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label for="editStartingPrice" class="form-label">Starting Price ($)</label>
                                <input type="number" class="form-control" id="editStartingPrice" name="startingPrice" min="0.01" step="0.01" required>
                                <div class="form-text">Note: Cannot be changed if bidding has started</div>
                            </div>
                            <div class="col-md-6">
                                <label for="editReservePrice" class="form-label">Reserve Price ($) (Optional)</label>
                                <input type="number" class="form-control" id="editReservePrice" name="reservePrice" min="0.01" step="0.01">
                            </div>
                        </div>
                        
                        <div class="mb-3">
                            <label for="editItemImage" class="form-label">Item Image</label>
                            <input type="file" class="form-control" id="editItemImage" name="itemImage" accept="image/*">
                            <div class="form-text">Leave empty to keep current image</div>
                            <div id="currentImageContainer" class="mt-2">
                                <p>Current image:</p>
                                <img id="currentImage" src="" alt="Current Item Image" class="preview-image border rounded">
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn action-btn text-white">Save Changes</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    
    <!-- Delete Confirmation Modal -->
    <div class="modal fade" id="deleteModal" tabindex="-1" aria-labelledby="deleteModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="deleteModalLabel">Confirm Delete</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <p>Are you sure you want to delete <span id="deleteItemName" class="fw-bold"></span>?</p>
                    <p class="text-danger">This action cannot be undone. Items with active bids cannot be deleted.</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <form action="DeleteItemServlet" method="post">
                        <input type="hidden" id="deleteItemId" name="itemId">
                        <button type="submit" class="btn delete-btn text-white">Delete</button>
                    </form>
                </div>
            </div>
        </div>
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
                    element.classList.add('text-danger');
                } else {
                    const days = Math.floor(distance / (1000 * 60 * 60 * 24));
                    const hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
                    const minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
                    const seconds = Math.floor((distance % (1000 * 60)) / 1000);
                    
                    let countdownText = "";
                    if (days > 0) {
                        countdownText = days + "d " + hours + "h remaining";
                    } else if (hours > 0) {
                        countdownText = hours + "h " + minutes + "m remaining";
                    } else if (minutes > 0) {
                        countdownText = minutes + "m " + seconds + "s remaining";
                    } else {
                        countdownText = seconds + "s remaining";
                        element.classList.add('text-danger');
                    }
                    
                    element.innerHTML = countdownText;
                }
            });
        }
        
        // Update countdowns immediately and then every second
        updateCountdowns();
        setInterval(updateCountdowns, 1000);
        
        // Edit item function
        function editItem(itemId) {
            // Fetch item details via AJAX
            fetch('GetItemDetailsServlet?id=' + itemId)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.json();
                })
                .then(item => {
                    document.getElementById('editItemId').value = item.itemId;
                    document.getElementById('editTitle').value = item.title;
                    document.getElementById('editDescription').value = item.description;
                    document.getElementById('editStartingPrice').value = item.startingPrice;
                    document.getElementById('editReservePrice').value = item.reservePrice || '';
                    document.getElementById('editCategoryId').value = item.categoryId;
                    
                    // Set current image
                    const currentImage = document.getElementById('currentImage');
                    if (item.imageUrl) {
                        currentImage.src = item.imageUrl;
                        document.getElementById('currentImageContainer').style.display = 'block';
                    } else {
                        currentImage.src = 'assets/images/no-image.png';
                        document.getElementById('currentImageContainer').style.display = 'block';
                    }
                    
                    // Show the modal
                    const editModal = new bootstrap.Modal(document.getElementById('editItemModal'));
                    editModal.show();
                })
                .catch(error => {
                    console.error('Error fetching item details:', error);
                    alert('Failed to load item details. Please try again.');
                });
        }
        
        // Delete confirmation
        function confirmDelete(itemId, itemTitle) {
            document.getElementById('deleteItemId').value = itemId;
            document.getElementById('deleteItemName').textContent = itemTitle;
            
            const deleteModal = new bootstrap.Modal(document.getElementById('deleteModal'));
            deleteModal.show();
        }
        
        // Item search and filter functionality
        document.getElementById('searchItemsBtn').addEventListener('click', function() {
            filterItems();
        });
        
        document.getElementById('itemSearchInput').addEventListener('keyup', function(event) {
            if (event.key === 'Enter') {
                filterItems();
            }
        });
        
        document.getElementById('itemStatusFilter').addEventListener('change', function() {
            filterItems();
        });
        
        function filterItems() {
            const searchTerm = document.getElementById('itemSearchInput').value.toLowerCase();
            const statusFilter = document.getElementById('itemStatusFilter').value;
            const rows = document.querySelectorAll('#itemsTable tbody tr');
            
            rows.forEach(row => {
                const itemTitle = row.querySelector('.fw-bold').textContent.toLowerCase();
                const itemStatus = row.dataset.status;
                
                const matchesSearch = itemTitle.includes(searchTerm);
                const matchesStatus = statusFilter === 'all' || itemStatus === statusFilter;
                
                if (matchesSearch && matchesStatus) {
                    row.style.display = '';
                } else {
                    row.style.display = 'none';
                }
            });
        }
        
        // Image preview for create form
        document.getElementById('itemImage').addEventListener('change', function(event) {
            const file = event.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    const previewContainer = document.createElement('div');
                    previewContainer.className = 'mt-2';
                    previewContainer.innerHTML = `
                        <p>Image preview:</p>
                        <img src="${e.target.result}" alt="Preview" class="preview-image border rounded">
                    `;
                    
                    // Remove any existing preview
                    const existingPreview = document.querySelector('#createItemForm .preview-container');
                    if (existingPreview) {
                        existingPreview.remove();
                    }
                    
                    // Add new preview
                    previewContainer.classList.add('preview-container');
                    document.getElementById('itemImage').parentNode.appendChild(previewContainer);
                };
                reader.readAsDataURL(file);
            }
        });
        
        // Image preview for edit form
        document.getElementById('editItemImage').addEventListener('change', function(event) {
            const file = event.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    const previewContainer = document.createElement('div');
                    previewContainer.className = 'mt-2';
                    previewContainer.innerHTML = `
                        <p>New image preview:</p>
                        <img src="${e.target.result}" alt="Preview" class="preview-image border rounded">
                    `;
                    
                    // Remove any existing preview
                    const existingPreview = document.querySelector('#editItemForm .preview-container');
                    if (existingPreview) {
                        existingPreview.remove();
                    }
                    
                    // Add new preview
                    previewContainer.classList.add('preview-container');
                    document.getElementById('editItemImage').parentNode.appendChild(previewContainer);
                };
                reader.readAsDataURL(file);
            }
        });
        
        // Initialize charts
        document.addEventListener('DOMContentLoaded', function() {
            // Revenue Chart
            const revenueCtx = document.getElementById('revenueChart').getContext('2d');
            const revenueChart = new Chart(revenueCtx, {
                type: 'line',
                data: {
                    labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
                    datasets: [{
                        label: 'Monthly Revenue',
                        data: [0, 0, 0, 0, 0, 0],
                        backgroundColor: 'rgba(54, 162, 235, 0.2)',
                        borderColor: 'rgba(54, 162, 235, 1)',
                        borderWidth: 2,
                        tension: 0.3
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false
                }
            });
            
            // Category Chart
            const categoryCtx = document.getElementById('categoryChart').getContext('2d');
            const categoryChart = new Chart(categoryCtx, {
                type: 'doughnut',
                data: {
                    labels: ['Electronics', 'Collectibles', 'Fashion', 'Home & Garden'],
                    datasets: [{
                        data: [0, 0, 0, 0],
                        backgroundColor: [
                            'rgba(255, 99, 132, 0.7)',
                            'rgba(54, 162, 235, 0.7)',
                            'rgba(255, 206, 86, 0.7)',
                            'rgba(75, 192, 192, 0.7)'
                        ],
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false
                }
            });
        });
    </script>
</body>
</html>
