<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Report Management - BidMaster Admin</title>
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
        .report-card { transition: all 0.3s ease; }
        .report-card:hover { transform: translateY(-5px); box-shadow: 0 10px 20px rgba(0,0,0,0.1); }
        .table td, .table th { vertical-align: middle; }
        .report-type-badge {
            font-size: 0.7rem;
            padding: 0.25rem 0.5rem;
        }
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
                    <a class="nav-link" href="AdminItemServlet"><i class="fas fa-gavel me-2"></i>Items</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="AdminCategoryServlet"><i class="fas fa-tags me-2"></i>Categories</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="AdminTransactionServlet"><i class="fas fa-money-bill-wave me-2"></i>Transactions</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" href="AdminReportServlet"><i class="fas fa-chart-bar me-2"></i>Reports</a>
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
                    <span class="navbar-brand fw-bold">Report Management</span>
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
            
            <!-- Statistics Overview -->
            <div class="row mb-4">
                <div class="col-md-4">
                    <div class="card shadow-sm stats-card bg-primary text-white">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-center">
                                <div>
                                    <h6 class="card-subtitle mb-2 text-white-50">Total Users</h6>
                                    <h3 class="card-title mb-0">${userCount}</h3>
                                </div>
                                <div class="fs-1 opacity-50">
                                    <i class="fas fa-users"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card shadow-sm stats-card bg-success text-white">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-center">
                                <div>
                                    <h6 class="card-subtitle mb-2 text-white-50">Active Items</h6>
                                    <h3 class="card-title mb-0">${activeItemCount}</h3>
                                </div>
                                <div class="fs-1 opacity-50">
                                    <i class="fas fa-gavel"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card shadow-sm stats-card bg-info text-white">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-center">
                                <div>
                                    <h6 class="card-subtitle mb-2 text-white-50">Total Revenue</h6>
                                    <h3 class="card-title mb-0">$<fmt:formatNumber value="${totalRevenue}" pattern="#,##0.00"/></h3>
                                </div>
                                <div class="fs-1 opacity-50">
                                    <i class="fas fa-dollar-sign"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- Revenue Chart -->
            <div class="card shadow-sm mb-4">
                <div class="card-header bg-white">
                    <h5 class="card-title mb-0">Monthly Revenue</h5>
                </div>
                <div class="card-body">
                    <canvas id="revenueChart" height="100"></canvas>
                </div>
            </div>
            
            <!-- Reports Section -->
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h4>Reports</h4>
                <div>
                    <button type="button" class="btn btn-primary me-2" data-bs-toggle="modal" data-bs-target="#generateReportModal">
                        <i class="fas fa-file-alt me-2"></i>Generate Report
                    </button>
                    <a href="AdminReportServlet?action=create" class="btn btn-success">
                        <i class="fas fa-plus me-2"></i>Create Report
                    </a>
                </div>
            </div>
            
            <!-- Reports Table -->
            <div class="card shadow-sm">
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-hover align-middle">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Title</th>
                                    <th>Type</th>
                                    <th>Date Range</th>
                                    <th>Created By</th>
                                    <th>Created At</th>
                                    <th>Status</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="report" items="${reports}">
                                    <tr>
                                        <td>${report.reportId}</td>
                                        <td>${report.title}</td>
                                        <td>
                                            <span class="badge bg-${report.reportType == 'sales' ? 'success' : report.reportType == 'users' ? 'primary' : report.reportType == 'items' ? 'warning' : 'info'} report-type-badge">
                                                ${report.reportType}
                                            </span>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty report.startDate && not empty report.endDate}">
                                                    ${report.startDate} to ${report.endDate}
                                                </c:when>
                                                <c:when test="${not empty report.startDate}">
                                                    From ${report.startDate}
                                                </c:when>
                                                <c:when test="${not empty report.endDate}">
                                                    Until ${report.endDate}
                                                </c:when>
                                                <c:otherwise>
                                                    All time
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>${report.createdByUsername}</td>
                                        <td><fmt:formatDate value="${report.createdAt}" pattern="MMM dd, yyyy HH:mm" /></td>
                                        <td>
                                            <span class="badge bg-${report.status == 'active' ? 'success' : 'secondary'}">
                                                ${report.status}
                                            </span>
                                        </td>
                                        <td>
                                            <div class="btn-group btn-group-sm">
                                                <a href="AdminReportServlet?action=view&id=${report.reportId}" class="btn btn-outline-primary" title="View">
                                                    <i class="fas fa-eye"></i>
                                                </a>
                                                <a href="AdminReportServlet?action=edit&id=${report.reportId}" class="btn btn-outline-secondary" title="Edit">
                                                    <i class="fas fa-edit"></i>
                                                </a>
                                                <div class="btn-group btn-group-sm" role="group">
                                                    <button type="button" class="btn btn-outline-success dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false" title="Download">
                                                        <i class="fas fa-download"></i>
                                                    </button>
                                                    <ul class="dropdown-menu">
                                                        <li><a class="dropdown-item" href="AdminReportServlet?action=download&id=${report.reportId}&format=pdf">PDF</a></li>
                                                        <li><a class="dropdown-item" href="AdminReportServlet?action=download&id=${report.reportId}&format=csv">CSV</a></li>
                                                        <li><a class="dropdown-item" href="AdminReportServlet?action=download&id=${report.reportId}&format=xlsx">Excel</a></li>
                                                    </ul>
                                                </div>
                                                <button type="button" class="btn btn-outline-danger" 
                                                        data-bs-toggle="modal" data-bs-target="#deleteReportModal" 
                                                        data-report-id="${report.reportId}" 
                                                        data-report-title="${report.title}" 
                                                        title="Delete">
                                                    <i class="fas fa-trash"></i>
                                                </button>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                                
                                <c:if test="${empty reports}">
                                    <tr>
                                        <td colspan="8" class="text-center py-4">
                                            <i class="fas fa-chart-bar fa-3x text-muted mb-3"></i>
                                            <p class="mb-0">No reports found</p>
                                        </td>
                                    </tr>
                                </c:if>
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
    
    <!-- Generate Report Modal -->
    <div class="modal fade" id="generateReportModal" tabindex="-1" aria-labelledby="generateReportModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="generateReportModalLabel">Generate Report</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form action="AdminReportServlet" method="post">
                    <div class="modal-body">
                        <input type="hidden" name="action" value="generate">
                        
                        <div class="mb-3">
                            <label for="reportType" class="form-label">Report Type</label>
                            <select class="form-select" id="reportType" name="reportType" required>
                                <option value="">Select a report type</option>
                                <option value="sales">Sales Report</option>
                                <option value="users">User Activity Report</option>
                                <option value="items">Item Listing Report</option>
                                <option value="bids">Bidding Activity Report</option>
                            </select>
                        </div>
                        
                        <div class="mb-3">
                            <label for="title" class="form-label">Report Title (Optional)</label>
                            <input type="text" class="form-control" id="title" name="title" placeholder="Leave blank for auto-generated title">
                        </div>
                        
                        <div class="mb-3">
                            <label for="startDate" class="form-label">Start Date</label>
                            <input type="date" class="form-control" id="startDate" name="startDate">
                        </div>
                        
                        <div class="mb-3">
                            <label for="endDate" class="form-label">End Date</label>
                            <input type="date" class="form-control" id="endDate" name="endDate">
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-primary">Generate</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    
    <!-- Delete Report Modal -->
    <div class="modal fade" id="deleteReportModal" tabindex="-1" aria-labelledby="deleteReportModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="deleteReportModalLabel">Confirm Delete</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <p>Are you sure you want to delete the report: <strong id="deleteReportTitle"></strong>?</p>
                    <p class="text-danger">This action cannot be undone.</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <form action="AdminReportServlet" method="post">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" id="deleteReportId" name="reportId">
                        <button type="submit" class="btn btn-danger">Delete</button>
                    </form>
                </div>
            </div>
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
                labels: [
                    <c:forEach var="entry" items="${monthlyRevenue}" varStatus="status">
                        '${entry.key}'${!status.last ? ',' : ''}
                    </c:forEach>
                ],
                datasets: [{
                    label: 'Monthly Revenue',
                    data: [
                        <c:forEach var="entry" items="${monthlyRevenue}" varStatus="status">
                            ${entry.value}${!status.last ? ',' : ''}
                        </c:forEach>
                    ],
                    backgroundColor: 'rgba(54, 162, 235, 0.2)',
                    borderColor: 'rgba(54, 162, 235, 1)',
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
                                return '$' + context.parsed.y.toFixed(2);
                            }
                        }
                    }
                }
            }
        });
        
        // Delete modal setup
        const deleteReportModal = document.getElementById('deleteReportModal');
        if (deleteReportModal) {
            deleteReportModal.addEventListener('show.bs.modal', function (event) {
                const button = event.relatedTarget;
                const reportId = button.getAttribute('data-report-id');
                const reportTitle = button.getAttribute('data-report-title');
                
                document.getElementById('deleteReportId').value = reportId;
                document.getElementById('deleteReportTitle').textContent = reportTitle;
            });
        }
        
        // Date validation
        document.getElementById('endDate').addEventListener('change', function() {
            const startDate = document.getElementById('startDate').value;
            const endDate = this.value;
            
            if (startDate && endDate && new Date(endDate) < new Date(startDate)) {
                alert('End date must be after start date');
                this.value = '';
            }
        });
        
        document.getElementById('startDate').addEventListener('change', function() {
            const startDate = this.value;
            const endDate = document.getElementById('endDate').value;
            
            if (startDate && endDate && new Date(endDate) < new Date(startDate)) {
                alert('Start date must be before end date');
                this.value = '';
            }
        });
    </script>
</body>
</html>
