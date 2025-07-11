<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Item - BidMaster</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <!-- Flatpickr CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
    <!-- Custom CSS -->
    <style>
        .preview-image {
            max-height: 200px;
            object-fit: contain;
        }
    </style>
</head>
<body>
    <!-- Include Header -->
    <jsp:include page="../includes/header.jsp" />
    
    <div class="container py-5 mt-5">
        <div class="row justify-content-center">
            <div class="col-lg-8">
                <div class="card shadow">
                    <div class="card-header bg-primary text-white">
                        <h4 class="mb-0">Edit Item</h4>
                    </div>
                    <div class="card-body">
                        <!-- Alert Messages -->
                        <c:if test="${not empty errorMessage}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                ${errorMessage}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </c:if>
                        
                        <form action="UpdateItemServlet" method="post" enctype="multipart/form-data">
                            <input type="hidden" name="itemId" value="${item.itemId}">
                            
                            <div class="mb-3">
                                <label for="title" class="form-label">Title</label>
                                <input type="text" class="form-control ${not empty titleError ? 'is-invalid' : ''}" 
                                       id="title" name="title" value="${not empty title ? title : item.title}" required>
                                <c:if test="${not empty titleError}">
                                    <div class="invalid-feedback">${titleError}</div>
                                </c:if>
                            </div>
                            
                            <div class="mb-3">
                                <label for="description" class="form-label">Description</label>
                                <textarea class="form-control ${not empty descriptionError ? 'is-invalid' : ''}" 
                                          id="description" name="description" rows="5" required>${not empty description ? description : item.description}</textarea>
                                <c:if test="${not empty descriptionError}">
                                    <div class="invalid-feedback">${descriptionError}</div>
                                </c:if>
                            </div>
                            
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label for="startingPrice" class="form-label">Starting Price ($)</label>
                                    <input type="number" class="form-control ${not empty startingPriceError ? 'is-invalid' : ''}" 
                                           id="startingPrice" name="startingPrice" step="0.01" min="0.01" 
                                           value="${not empty startingPrice ? startingPrice : item.startingPrice}" required>
                                    <c:if test="${not empty startingPriceError}">
                                        <div class="invalid-feedback">${startingPriceError}</div>
                                    </c:if>
                                </div>
                                <div class="col-md-6">
                                    <label for="reservePrice" class="form-label">Reserve Price ($) <small class="text-muted">(Optional)</small></label>
                                    <input type="number" class="form-control ${not empty reservePriceError ? 'is-invalid' : ''}" 
                                           id="reservePrice" name="reservePrice" step="0.01" min="0" 
                                           value="${not empty reservePrice ? reservePrice : item.reservePrice}">
                                    <c:if test="${not empty reservePriceError}">
                                        <div class="invalid-feedback">${reservePriceError}</div>
                                    </c:if>
                                </div>
                            </div>
                            
                            <div class="mb-3">
                                <label for="categoryId" class="form-label">Category</label>
                                <select class="form-select ${not empty categoryIdError ? 'is-invalid' : ''}" 
                                        id="categoryId" name="categoryId" required>
                                    <option value="">Select a category</option>
                                    <c:forEach var="category" items="${categories}">
                                        <option value="${category.categoryId}" 
                                                ${(not empty categoryId && categoryId eq category.categoryId) || 
                                                  (empty categoryId && item.categoryId eq category.categoryId) ? 'selected' : ''}>
                                            ${category.categoryName}
                                        </option>
                                    </c:forEach>
                                </select>
                                <c:if test="${not empty categoryIdError}">
                                    <div class="invalid-feedback">${categoryIdError}</div>
                                </c:if>
                            </div>
                            
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label for="startTime" class="form-label">Start Time</label>
                                    <input type="datetime-local" class="form-control ${not empty startTimeError ? 'is-invalid' : ''}" 
                                           id="startTime" name="startTime" 
                                           value="${not empty startTime ? startTime : item.startTime}" required>
                                    <c:if test="${not empty startTimeError}">
                                        <div class="invalid-feedback">${startTimeError}</div>
                                    </c:if>
                                </div>
                                <div class="col-md-6">
                                    <label for="endTime" class="form-label">End Time</label>
                                    <input type="datetime-local" class="form-control ${not empty endTimeError ? 'is-invalid' : ''}" 
                                           id="endTime" name="endTime" 
                                           value="${not empty endTime ? endTime : item.endTime}" required>
                                    <c:if test="${not empty endTimeError}">
                                        <div class="invalid-feedback">${endTimeError}</div>
                                    </c:if>
                                </div>
                            </div>
                            
                            <div class="mb-3">
                                <label for="status" class="form-label">Status</label>
                                <select class="form-select" id="status" name="status" required>
                                    <option value="pending" ${(not empty status && status eq 'pending') || 
                                                             (empty status && item.status eq 'pending') ? 'selected' : ''}>Pending</option>
                                    <option value="active" ${(not empty status && status eq 'active') || 
                                                           (empty status && item.status eq 'active') ? 'selected' : ''}>Active</option>
                                    <option value="completed" ${(not empty status && status eq 'completed') || 
                                                              (empty status && item.status eq 'completed') ? 'selected' : ''}>Completed</option>
                                    <option value="cancelled" ${(not empty status && status eq 'cancelled') || 
                                                              (empty status && item.status eq 'cancelled') ? 'selected' : ''}>Cancelled</option>
                                </select>
                            </div>
                            
                            <div class="mb-4">
                                <label for="image" class="form-label">Item Image</label>
                                <input type="file" class="form-control ${not empty imageError ? 'is-invalid' : ''}" 
                                       id="image" name="image" accept="image/*">
                                <c:if test="${not empty imageError}">
                                    <div class="invalid-feedback">${imageError}</div>
                                </c:if>
                                <small class="form-text text-muted">Leave empty to keep current image</small>
                                
                                <c:if test="${not empty item.imageUrl}">
                                    <div class="mt-2">
                                        <p>Current Image:</p>
                                        <img src="${item.imageUrl}" alt="Current Item Image" class="preview-image border rounded">
                                    </div>
                                </c:if>
                            </div>
                            
                            <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                <a href="ItemDetailsServlet?id=${item.itemId}" class="btn btn-secondary me-md-2">
                                    <i class="fas fa-times me-2"></i>Cancel
                                </a>
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-save me-2"></i>Save Changes
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Include Footer -->
    <jsp:include page="../includes/footer.jsp" />
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <!-- Flatpickr JS -->
    <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
    
    <script>
        // Initialize datetime pickers
        flatpickr("#startTime", {
            enableTime: true,
            dateFormat: "Y-m-d H:i",
            time_24hr: true
        });
        
        flatpickr("#endTime", {
            enableTime: true,
            dateFormat: "Y-m-d H:i",
            time_24hr: true
        });
        
        // Image preview
        document.getElementById('image').addEventListener('change', function(event) {
            const file = event.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    const previewContainer = document.createElement('div');
                    previewContainer.className = 'mt-2';
                    previewContainer.innerHTML = `
                        <p>New Image Preview:</p>
                        <img src="${e.target.result}" alt="New Item Image" class="preview-image border rounded">
                    `;
                    
                    // Remove any existing preview
                    const existingPreview = document.querySelector('.preview-container');
                    if (existingPreview) {
                        existingPreview.remove();
                    }
                    
                    // Add new preview
                    previewContainer.classList.add('preview-container');
                    document.getElementById('image').parentNode.appendChild(previewContainer);
                };
                reader.readAsDataURL(file);
            }
        });
    </script>
</body>
</html>
