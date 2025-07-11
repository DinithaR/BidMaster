<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - BidMaster</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <!-- Chart.js -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <!-- Custom CSS -->
    <style>
        body { background: #f6f7fa; }
        .sidebar { min-width: 220px; max-width: 220px; background: #212529; min-height: 100vh; }
        .sidebar .nav-link, .sidebar .sidebar-header { color: #fff; }
        .sidebar .nav-link.active { background: #343a40; }
        .sidebar .sidebar-header { font-size: 1.3rem; font-weight: bold; padding: 1.5rem 1rem; }
        .content { flex: 1; padding: 2rem; }
        .card { border-radius: 0.5rem; }
        .stats-card { transition: all 0.3s ease; }
        .stats-card:hover { transform: translateY(-5px); }
        .avatar { width: 40px; height: 40px; object-fit: cover; border-radius: 50%; }
        .table td, .table th { vertical-align: middle; }
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
                    <a class="nav-link active" href="dashboard.jsp"><i class="fas fa-tachometer-alt me-2"></i>Dashboard</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="AdminUserServlet"><i class="fas fa-users me-2"></i>Users</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="AdminItemServlet"><i class="fas fa-gavel me-2"></i>Items</a>
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
                    <span class="navbar-brand fw-bold">Dashboard</span>
                    <div class="d-flex align-items-center">
                        <img src="../assets/images/admin-avatar.png" alt="Admin" class="avatar me-2">
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
            
            <!-- Stats Cards -->
            <div class="row mb-4">
                <div class="col-md-3">
                    <div class="card shadow-sm stats-card bg-primary text-white">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-center">
                                <div>
                                    <h6 class="card-subtitle mb-2 text-white-50">Total Users</h6>
                                    <h3 class="card-title mb-0">${stats.userCount}</h3>
                                </div>
                                <div class="fs-1 opacity-50">
                                    <i class="fas fa-users"></i>
                                </div>
                            </div>
                            <div class="mt-3">
                                <span class="badge bg-light text-primary">
                                    <i class="fas fa-arrow-up me-1"></i>${stats.newUserCount} new this week
                                </span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card shadow-sm stats-card bg-success text-white">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-center">
                                <div>
                                    <h6 class="card-subtitle mb-2 text-white-50">Active Items</h6>
                                    <h3 class="card-title mb-0">${stats.activeItemCount}</h3>
                                </div>
                                <div class="fs-1 opacity-50">
                                    <i class="fas fa-gavel"></i>
                                </div>
                            </div>
                            <div class="mt-3">
                                <span class="badge bg-light text-success">
                                    <i class="fas fa-arrow-up me-1"></i>${stats.newItemCount} new this week
                                </span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card shadow-sm stats-card bg-info text-white">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-center">
                                <div>
                                    <h6 class="card-subtitle mb-2 text-white-50">Total Bids</h6>
                                    <h3 class="card-title mb-0">${stats.bidCount}</h3>
                                </div>
                                <div class="fs-1 opacity-50">
                                    <i class="fas fa-hand-paper"></i>
                                </div>
                            </div>
                            <div class="mt-3">
                                <span class="badge bg-light text-info">
                                    <i class="fas fa-arrow-up me-1"></i>${stats.newBidCount} new today
                                </span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card shadow-sm stats-card bg-warning text-white">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-center">
                                <div>
                                    <h6 class="card-subtitle mb-2 text-white-50">Revenue</h6>
                                    <h3 class="card-title mb-0">$<fmt:formatNumber value="${stats.revenue}" pattern="#,##0.00"/></h3>
                                </div>
                                <div class="fs-1 opacity-50">
                                    <i class="fas fa-dollar-sign"></i>
                                </div>
                            </div>
                            <div class="mt-3">
                                <span class="badge bg-light text-warning">
                                    <i class="fas fa-arrow-up me-1"></i>$<fmt:formatNumber value="${stats.revenueIncrease}" pattern="#,##0.00"/> increase
                                </span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- Charts Row -->
            <div class="row mb-4">
                <div class="col-md-8">
                    <div class="card shadow-sm">
                        <div class="card-header bg-white">
                            <h5 class="card-title mb-0">Revenue Overview</h5>
                        </div>
                        <div class="card-body">
                            <canvas id="revenueChart" height="300"></canvas>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card shadow-sm">
                        <div class="card-header bg-white">
                            <h5 class="card-title mb-0">User Distribution</h5>
                        </div>
                        <div class="card-body">
                            <canvas id="userChart" height="300"></canvas>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- Recent Data Row -->
            <div class="row">
                <!-- Recent Users -->
                <div class="col-md-6 mb-4">
                    <div class="card shadow-sm">
                        <div class="card-header bg-white d-flex justify-content-between align-items-center">
                            <h5 class="card-title mb-0">Recent Users</h5>
                            <a href="AdminUserServlet" class="btn btn-sm btn-outline-primary">View All</a>
                        </div>
                        <div class="card-body p-0">
                            <div class="table-responsive">
                                <table class="table table-hover mb-0">
                                    <thead>
                                        <tr>
                                            <th>User</th>
                                            <th>Email</th>
                                            <th>Role</th>
                                            <th>Joined</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="user" items="${recentUsers}">
                                            <tr>
                                                <td>
                                                    <div class="d-flex align-items-center">
                                                        <img src="${empty user.profileImage ? '../assets/images/default-avatar.png' : user.profileImage}" 
                                                             alt="${user.username}" class="avatar me-2">
                                                        <span>${user.fullName}</span>
                                                    </div>
                                                </td>
                                                <td>${user.email}</td>
                                                <td><span class="badge bg-${user.role == 'admin' ? 'danger' : user.role == 'seller' ? 'primary' : 'secondary'}">${user.role}</span></td>
                                                <td><fmt:formatDate value="${user.registrationDate}" pattern="MMM dd, yyyy" /></td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- Recent Items -->
                <div class="col-md-6 mb-4">
                    <div class="card shadow-sm">
                        <div class="card-header bg-white d-flex justify-content-between align-items-center">
                            <h5 class="card-title mb-0">Recent Items</h5>
                            <a href="AdminItemServlet" class="btn btn-sm btn-outline-primary">View All</a>
                        </div>
                        <div class="card-body p-0">
                            <div class="table-responsive">
                                <table class="table table-hover mb-0">
                                    <thead>
                                        <tr>
                                            <th>Item</th>
                                            <th>Seller</th>
                                            <th>Price</th>
                                            <th>Status</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="item" items="${recentItems}">
                                            <tr>
                                                <td>
                                                    <div class="d-flex align-items-center">
                                                        <img src="${empty item.imageUrl ? '../assets/images/no-image.png' : item.imageUrl}" 
                                                             alt="${item.title}" class="me-2" width="40" height="40" style="object-fit: cover;">
                                                        <span class="text-truncate" style="max-width: 150px;">${item.title}</span>
                                                    </div>
                                                </td>
                                                <td>${item.sellerUsername}</td>
                                                <td>$<fmt:formatNumber value="${item.currentPrice}" pattern="#,##0.00"/></td>
                                                <td><span class="badge bg-${item.status == 'active' ? 'success' : item.status == 'completed' ? 'secondary' : 'primary'}">${item.status}</span></td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- Quick Actions -->
            <div class="card shadow-sm mb-4">
                <div class="card-header bg-white">
                    <h5 class="card-title mb-0">Quick Actions</h5>
                </div>
                <div class="card-body">
                    <div class="row g-3">
                        <div class="col-md-3">
                            <a href="AdminUserServlet?action=create" class="btn btn-outline-primary w-100 p-3">
                                <i class="fas fa-user-plus mb-2 fs-3"></i>
                                <div>Add New User</div>
                            </a>
                        </div>
                        <div class="col-md-3">
                            <a href="AdminCategoryServlet" class="btn btn-outline-success w-100 p-3">
                                <i class="fas fa-folder-plus mb-2 fs-3"></i>
                                <div>Manage Categories</div>
                            </a>
                        </div>
                        <div class="col-md-3">
                            <a href="AdminReportServlet?action=create" class="btn btn-outline-info w-100 p-3">
                                <i class="fas fa-file-alt mb-2 fs-3"></i>
                                <div>Generate Report</div>
                            </a>
                        </div>
                        <div class="col-md-3">
                            <a href="AdminTransactionServlet" class="btn btn-outline-warning w-100 p-3">
                                <i class="fas fa-money-check-alt mb-2 fs-3"></i>
                                <div>View Transactions</div>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- Footer -->
            <footer class="text-center text-muted small mt-5">
                &copy; <%= java.time.Year.now().getValue() %> BidMaster Admin. All rights reserved.
            </footer>
        </div>
    </div>
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
        // Revenue Chart
        const revenueCtx = document.getElementById('revenueChart').getContext('2d');
        const revenueChart = new Chart(revenueCtx, {
            type: 'line',
            data: {
                labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
                datasets: [{
                    label: 'Revenue',
                    data: [1200, 1900, 3000, 2500, 2800, 3500, 4000, 4200, 4800, 5200, 5500, 6000],
                    backgroundColor: 'rgba(255, 193, 7, 0.2)',
                    borderColor: 'rgba(255, 193, 7, 1)',
                    borderWidth: 2,
                    tension: 0.3
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            callback: function(value) {
                                return '$' + value;
                            }
                        }
                    }
                },
                plugins: {
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                return '$' + context.parsed.y;
                            }
                        }
                    }
                }
            }
        });
        
        // User Chart
        const userCtx = document.getElementById('userChart').getContext('2d');
        const userChart = new Chart(userCtx, {
            type: 'doughnut',
            data: {
                labels: ['Regular Users', 'Sellers', 'Admins'],
                datasets: [{
                    data: [65, 30, 5],
                    backgroundColor: [
                        'rgba(108, 117, 125, 0.7)',
                        'rgba(13, 110, 253, 0.7)',
                        'rgba(220, 53, 69, 0.7)'
                    ],
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'bottom'
                    }
                }
            }
        });
    </script>
</body>
</html>
