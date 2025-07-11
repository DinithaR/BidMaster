<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Item - BidMaster Admin</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <!-- Flatpickr CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
    <!-- Custom CSS -->
    <style>
        body { background: #f6f7fa; }
        .sidebar { min-width: 220px; max-width: 220px; background: #212529; min-height: 100vh; }
        .sidebar .nav-link, .sidebar .sidebar-header { color: #fff; }
        .sidebar .nav-link.active { background: #343a40; }
        .sidebar .sidebar-header { font-size: 1.3rem; font-weight: bold; padding: 1.5rem 1rem; }
        .content { flex: 1; padding: 2rem; }
        .card { border-radius: 0.5rem; }
        .preview-image { max-height: 200px; object-fit: contain; border-radius: 0.25rem; }
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
                    <span class="navbar-brand fw-bold">Edit Item</span>
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
            
            <!-- Edit Item Form -->
            <div class="card shadow-sm">
                <div class="card-header bg-white">
                    <h5 class="card-title mb-0">Edit Item #${item.itemId}</h5>
                </div>
                <div class="card-body">
                    <form action="AdminItemServlet" method="post" enctype="multipart/form-data">
                        <input type="hidden" name="action" value="update">
                        <input type="hidden" name="itemId" value="${item.itemId}">
                        
                        <div class="row mb-4">
                            <div class="col-md-8">
                                <div class="mb-3">
                                    <label for="title" class="form-label">Item Title</label>
                                    <input type="text" class="form-control ${not empty titleError ? 'is-invalid' : ''}" 
                                           id="title" name="title" value="${item.title}" required>
                                    <c:if test="${not empty titleError}">
                                        <div class="invalid-feedback">${titleError}</div>
                                    </c:if>
                                </div>
                                
                                <div class="mb-3">
                                    <label for="description" class="form-label">Description</label>
                                    <textarea class="form-control ${not empty descriptionError ? 'is-invalid' : ''}" 
                                              id="description" name="description" rows="5" required>${item.description}</textarea>
                                    <c:if test="${not empty descriptionError}">
                                        <div class="invalid-feedback">${descriptionError}</div>
                                    </c:if>
                                </div>
                            </div>
                            
                            <div class="col-md-4">
                                <div class="mb-3">
                                    <label for="image" class="form-label">Item Image</label>
                                    <input type="file" class="form-control ${not empty imageError ? 'is-invalid' : ''}" 
                                           id="image" name="image" accept="image/*">
                                    <div class="form-text">Leave empty to keep current image</div>
                                    <c:if test="${not empty imageError}">
                                        <div class="invalid-feedback">${imageError}</div>
                                    </c:if>
                                    
                                    <c:if test="${not empty item.imageUrl}">
                                        <div class="mt-2">
                                            <p>Current image:</p>
                                            <img src="${item.imageUrl}" alt="${item.title}" class="preview-image img-thumbnail">
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                        
                        <div class="row mb-4">
                            <div class="col-md-4">
                                <div class="mb-3">
                                    <label for="startingPrice" class="form-label">Starting Price ($)</label>
                                    <input type="number" class="form-control ${not empty startingPriceError ? 'is-invalid' : ''}" 
                                           id="startingPrice" name="startingPrice" value="${item.startingPrice}" 
                                           min="0.01" step="0.01" required>
                                    <c:if test="${not empty startingPriceError}">
                                        <div class="invalid-feedback">${startingPriceError}</div>
                                    </c:if>
                                </div>
                            </div>
                            
                            <div class="col-md-4">
                                <div class="mb-3">
                                    <label for="currentPrice" class="form-label">Current Price ($)</label>
                                    <input type="number" class="form-control" 
                                           id="currentPrice" value="${item.currentPrice}" 
                                           min="0.01" step="0.01" readonly>
                                    <div class="form-text">Current bid price (read-only)</div>
                                </div>
                            </div>
                            
                            <div class="col-md-4">
                                <div class="mb-3">
                                    <label for="reservePrice" class="form-label">Reserve Price ($)</label>
                                    <input type="number" class="form-control ${not empty reservePriceError ? 'is-invalid' : ''}" 
                                           id="reservePrice" name="reservePrice" value="${item.reservePrice}" 
                                           min="0.01" step="0.01">
                                    <div class="form-text">Optional minimum sale price</div>
                                    <c:if test="${not empty reservePriceError}">
                                        <div class="invalid-feedback">${reservePriceError}</div>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                        
                        <div class="row mb-4">
                            <div class="col-md-4">
                                <div class="mb-3">
                                    <label for="categoryId" class="form-label">Category</label>
                                    <select class="form-select ${not empty categoryIdError ? 'is-invalid' : ''}" 
                                            id="categoryId" name="categoryId" required>
                                        <option value="">Select Category</option>
                                        <c:forEach var="category" items="${categories}">
                                            <option value="${category.categoryId}" ${item.categoryId == category.categoryId ? 'selected' : ''}>
                                                ${category.categoryName}
                                            </option>
                                        </c:forEach>
                                    </select>
                                    <c:if test="${not empty categoryIdError}">
                                        <div class="invalid-feedback">${categoryIdError}</div>
                                    </c:if>
                                </div>
                            </div>
                            
                            <div class="col-md-4">
                                <div class="mb-3">
                                    <label for="sellerId" class="form-label">Seller</label>
                                    <select class="form-select" id="sellerId" name="sellerId" required>
                                        <c:forEach var="seller" items="${sellers}">
                                            <option value="${seller.userId}" ${item.sellerId == seller.userId ? 'selected' : ''}>
                                                ${seller.username}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            
                            <div class="col-md-4">
                                <div class="mb-3">
                                    <label for="status" class="form-label">Status</label>
                                    <select class="form-select" id="status" name="status" required>
                                        <option value="pending" ${item.status == 'pending' ? 'selected' : ''}>Pending</option>
                                        <option value="active" ${item.status == 'active' ? 'selected' : ''}>Active</option>
                                        <option value="completed" ${item.status == 'completed' ? 'selected' : ''}>Completed</option>
                                        <option value="cancelled" ${item.status == 'cancelled' ? 'selected' : ''}>Cancelled</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                        
                        <div class="row mb-4">
                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label for="startTime" class="form-label">Start Time</label>
                                    <input type="text" class="form-control ${not empty startTimeError ? 'is-invalid' : ''}" 
                                           id="startTime" name="startTime" value="${item.startTime}" required>
                                    <c:if test="${not empty startTimeError}">
                                        <div class="invalid-feedback">${startTimeError}</div>
                                    </c:if>
                                </div>
                            </div>
                            
                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label for="endTime" class="form-label">End Time</label>
                                    <input type="text" class="form-control ${not empty endTimeError ? 'is-invalid' : ''}" 
                                           id="endTime" name="endTime" value="${item.endTime}" required>
                                    <c:if test="${not empty endTimeError}">
                                        <div class="invalid-feedback">${endTimeError}</div>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                        
                        <div class="d-flex justify-content-between">
                            <a href="AdminItemServlet" class="btn btn-secondary">
                                <i class="fas fa-arrow-left me-2"></i>Back to Items
                            </a>
                            <div>
                                <button type="button" class="btn btn-danger me-2" data-bs-toggle="modal" data-bs-target="#deleteModal">
                                    <i class="fas fa-trash me-2"></i>Delete
                                </button>
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-save me-2"></i>Save Changes
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            
            <!-- Footer -->
            <footer class="text-center text-muted small mt-5">
                &copy; <%= java.time.Year.now().getValue() %> BidMaster Admin. All rights reserved.
            </footer>
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
                    <p>Are you sure you want to delete this item: <span class="fw-bold">${item.title}</span>?</p>
                    <p class="text-danger">This action cannot be undone.</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <form action="AdminItemServlet" method="post">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="itemId" value="${item.itemId}">
                        <button type="submit" class="btn btn-danger">Delete</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <!-- Flatpickr JS -->
    <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
    
    <script>
        // Initialize datetime pickers
        flatpickr("#startTime", {
            enableTime: true,
            dateFormat: "Y-m-d H:i",
            defaultDate: "${item.startTime}"
        });
        
        flatpickr("#endTime", {
            enableTime: true,
            dateFormat: "Y-m-d H:i",
            defaultDate: "${item.endTime}"
        });
        
        // Image preview
        document.getElementById('image').addEventListener('change', function(event) {
            const file = event.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    // Create or update image preview
                    let previewContainer = document.querySelector('.preview-image');
                    if (!previewContainer) {
                        const container = document.createElement('div');
                        container.className = 'mt-2';
                        container.innerHTML = `
                            <p>New image preview:</p>
                            <img src="${e.target.result}" alt="Preview" class="preview-image img-thumbnail">
                        `;
                        document.getElementById('image').parentNode.appendChild(container);
                    } else {
                        // Update existing preview
                        previewContainer.src = e.target.result;
                        previewContainer.parentNode.querySelector('p').textContent = 'New image preview:';
                    }
                };
                reader.readAsDataURL(file);
            }
        });
    </script>
</body>
</html>
