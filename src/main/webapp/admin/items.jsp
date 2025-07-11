<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Item Management - BidMaster Admin</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <!-- DataTables CSS -->
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.4/css/dataTables.bootstrap5.min.css">
    <!-- Custom CSS -->
    <style>
        body { background: #f6f7fa; }
        .sidebar { min-width: 220px; max-width: 220px; background: #212529; min-height: 100vh; }
        .sidebar .nav-link, .sidebar .sidebar-header { color: #fff; }
        .sidebar .nav-link.active { background: #343a40; }
        .sidebar .sidebar-header { font-size: 1.3rem; font-weight: bold; padding: 1.5rem 1rem; }
        .content { flex: 1; padding: 2rem; }
        .card { border-radius: 0.5rem; }
        .item-image { width: 60px; height: 60px; object-fit: cover; border-radius: 0.25rem; }
        .badge-active { background-color: #28a745; }
        .badge-pending { background-color: #ffc107; }
        .badge-completed { background-color: #6c757d; }
    </style>
</head>
<body>
    <div class="d-flex">
        <!-- Sidebar -->
        <div class="sidebar d-flex flex-column">
            <div class="sidebar-header d-flex align-items-center">
                <img src="../assets/images/logo.png" alt="BidMaster Logo" width="40" class="me-2">
                BidMaster Admin
            </div>
            <ul class="nav flex-column mb-auto">
                <li class="nav-item">
                    <a class="nav-link" href="dashboard.jsp"><i class="fas fa-tachometer-alt me-2"></i>Dashboard</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="AdminUserServlet"><i class="fas fa-users me-2"></i>Users</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" href="AdminItemServlet"><i class="fas fa-gavel me-2"></i>Items</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="AdminCategoryServlet"><i class="fas fa-tags me-2"></i>Categories</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="AdminTransactionServlet"><i class="fas fa-money-bill-wave me-2"></i>Transactions</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="AdminReportServlet"><i class="fas fa-chart-bar me-2"></i>Reports</a>
                </li>
                <li class="nav-item mt-4">
                    <a class="nav-link" href="../LogoutServlet"><i class="fas fa-sign-out-alt me-2"></i>Logout</a>
                </li>
            </ul>
        </div>
        
        <!-- Main Content -->
        <div class="content w-100">
            <!-- Top Navbar -->
            <nav class="navbar navbar-expand navbar-light bg-white shadow-sm rounded mb-4">
                <div class="container-fluid">
                    <span class="navbar-brand fw-bold">Item Management</span>
                    <div class="d-flex align-items-center">
                        <img src="../assets/images/admin-avatar.png" alt="Admin" class="rounded-circle me-2" width="40">
                        <span class="fw-bold">${sessionScope.username}</span>
                    </div>
                </div>
            </nav>
            
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
            
            <!-- Items Card -->
            <div class="card shadow-sm">
                <div class="card-header bg-white d-flex justify-content-between align-items-center">
                    <h5 class="card-title mb-0">All Items</h5>
                    <div>
                        <button type="button" class="btn btn-success me-2" data-bs-toggle="modal" data-bs-target="#addItemModal">
                            <i class="fas fa-plus-circle me-2"></i>Add Item
                        </button>
                        <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#filterModal">
                            <i class="fas fa-filter me-2"></i>Filter
                        </button>
                    </div>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-hover" id="itemsTable">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Image</th>
                                    <th>Title</th>
                                    <th>Seller</th>
                                    <th>Category</th>
                                    <th>Current Price</th>
                                    <th>Bids</th>
                                    <th>End Time</th>
                                    <th>Status</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${items}">
                                    <tr>
                                        <td>${item.itemId}</td>
                                        <td>
                                            <img src="${empty item.imageUrl ? '../assets/images/no-image.png' : item.imageUrl}" 
                                                 alt="${item.title}" class="item-image">
                                        </td>
                                        <td>
                                            <div class="fw-bold">${item.title}</div>
                                            <div class="small text-muted">Created: 
                                                <c:if test="${not empty item.createdAt}">
                                                    ${item.createdAt.toLocalDate()}
                                                </c:if>
                                                <c:if test="${empty item.createdAt}">
                                                    N/A
                                                </c:if>
                                            </div>
                                        </td>
                                        <td>${item.sellerUsername}</td>
                                        <td>${item.categoryName}</td>
                                        <td>$<fmt:formatNumber value="${item.currentPrice}" pattern="#,##0.00"/></td>
                                        <td>${item.bidCount}</td>
                                        <td>
                                            <c:if test="${not empty item.endTime}">
                                                ${item.endTime.toLocalDate()} ${item.endTime.toLocalTime().toString().substring(0, 5)}
                                            </c:if>
                                            <c:if test="${empty item.endTime}">
                                                N/A
                                            </c:if>
                                        </td>
                                        <td>
                                            <span class="badge ${item.status == 'active' ? 'badge-active' : (item.status == 'pending' ? 'badge-pending' : 'badge-completed')}">
                                                ${item.status}
                                            </span>
                                        </td>
                                        <td>
                                            <div class="btn-group btn-group-sm">
                                                <a href="../ItemDetailsServlet?id=${item.itemId}" class="btn btn-info" title="View">
                                                    <i class="fas fa-eye"></i>
                                                </a>
                                                <button type="button" class="btn btn-warning" 
                                                        onclick="editItem(${item.itemId})" title="Edit">
                                                    <i class="fas fa-edit"></i>
                                                </button>
                                                <button type="button" class="btn btn-danger" 
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
                </div>
            </div>
            
            <!-- Footer -->
            <footer class="text-center text-muted small mt-5">
                &copy; <%= java.time.Year.now().getValue() %> BidMaster Admin. All rights reserved.
            </footer>
        </div>
    </div>
    
    <!-- Filter Modal -->
    <div class="modal fade" id="filterModal" tabindex="-1" aria-labelledby="filterModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="filterModalLabel">Filter Items</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form action="AdminItemServlet" method="get">
                    <div class="modal-body">
                        <div class="mb-3">
                            <label for="statusFilter" class="form-label">Status</label>
                            <select class="form-select" id="statusFilter" name="status">
                                <option value="">All Statuses</option>
                                <option value="active">Active</option>
                                <option value="pending">Pending</option>
                                <option value="completed">Completed</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="categoryFilter" class="form-label">Category</label>
                            <select class="form-select" id="categoryFilter" name="categoryId">
                                <option value="">All Categories</option>
                                <c:forEach var="category" items="${categories}">
                                    <option value="${category.categoryId}">${category.categoryName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="sellerFilter" class="form-label">Seller</label>
                            <select class="form-select" id="sellerFilter" name="sellerId">
                                <option value="">All Sellers</option>
                                <c:forEach var="seller" items="${sellers}">
                                    <option value="${seller.userId}">${seller.username}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="priceMinFilter" class="form-label">Min Price</label>
                            <input type="number" class="form-control" id="priceMinFilter" name="priceMin" min="0" step="0.01">
                        </div>
                        <div class="mb-3">
                            <label for="priceMaxFilter" class="form-label">Max Price</label>
                            <input type="number" class="form-control" id="priceMaxFilter" name="priceMax" min="0" step="0.01">
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="submit" class="btn btn-primary">Apply Filters</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    
    <!-- Add Item Modal -->
    <div class="modal fade" id="addItemModal" tabindex="-1" aria-labelledby="addItemModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="addItemModalLabel">Add New Item</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form action="../CreateItemServlet" method="post" enctype="multipart/form-data" id="addItemForm">
                    <input type="hidden" name="adminCreated" value="true">
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
                                <label for="sellerId" class="form-label">Seller</label>
                                <select class="form-select" id="sellerId" name="sellerId" required>
                                    <option value="">Select Seller</option>
                                    <c:forEach var="seller" items="${sellers}">
                                        <option value="${seller.userId}">${seller.username}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        
                        <div class="row mb-3">
                            <div class="col-md-6">
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
                            <div class="col-md-6">
                                <label for="status" class="form-label">Status</label>
                                <select class="form-select" id="status" name="status" required>
                                    <option value="pending">Pending</option>
                                    <option value="active" selected>Active</option>
                                    <option value="completed">Completed</option>
                                </select>
                            </div>
                        </div>
                        
                        <div class="mb-3">
                            <label for="itemImage" class="form-label">Item Image</label>
                            <input type="file" class="form-control" id="itemImage" name="itemImage" accept="image/*">
                            <div class="form-text">Max file size: 5MB. Supported formats: JPG, PNG, GIF</div>
                            <div id="imagePreviewContainer" class="mt-2 d-none">
                                <img id="imagePreview" class="img-thumbnail" style="max-height: 150px;" alt="Image Preview">
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-success">Create Item</button>
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
                    <p>Are you sure you want to delete the item: <span id="deleteItemName" class="fw-bold"></span>?</p>
                    <p class="text-danger">This action cannot be undone.</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <form action="AdminItemServlet" method="post">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" id="deleteItemId" name="itemId">
                        <button type="submit" class="btn btn-danger">Delete</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <!-- DataTables JS -->
    <script src="https://cdn.datatables.net/1.13.4/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.4/js/dataTables.bootstrap5.min.js"></script>
    
    <script>
        $(document).ready(function() {
            $('#itemsTable').DataTable({
                order: [[0, 'desc']], // Sort by ID descending by default
                pageLength: 10,
                lengthMenu: [10, 25, 50, 100],
                language: {
                    search: "_INPUT_",
                    searchPlaceholder: "Search items..."
                }
            });
        });
        
        function editItem(itemId) {
            window.location.href = "AdminItemServlet?action=edit&id=" + itemId;
        }
        
        function confirmDelete(itemId, itemTitle) {
            document.getElementById('deleteItemId').value = itemId;
            document.getElementById('deleteItemName').textContent = itemTitle;
            
            const deleteModal = new bootstrap.Modal(document.getElementById('deleteModal'));
            deleteModal.show();
        }
        
        // Image preview for add item form
        document.getElementById('itemImage').addEventListener('change', function(event) {
            const file = event.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    document.getElementById('imagePreview').src = e.target.result;
                    document.getElementById('imagePreviewContainer').classList.remove('d-none');
                };
                reader.readAsDataURL(file);
            }
        });
    </script>
</body>
</html>
