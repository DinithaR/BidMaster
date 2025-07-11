<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - BidMaster</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <!-- Custom CSS -->
    <style>
        body {
            background-color: #f8f9fa;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }
        .register-container {
            max-width: 800px;
            margin: 2rem auto;
        }
        .register-header {
            background-color: #3a5a78;
            color: white;
            padding: 1.5rem;
            border-radius: 0.5rem 0.5rem 0 0;
        }
        .register-form {
            background-color: white;
            padding: 2rem;
            border-radius: 0 0 0.5rem 0.5rem;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }
        .btn-register {
            background-color: #3a5a78;
            border-color: #3a5a78;
        }
        .btn-register:hover {
            background-color: #2d4861;
            border-color: #2d4861;
        }
    </style>
</head>
<body>
    <!-- Include Header -->
    <jsp:include page="includes/header.jsp" />
    
    <div class="container register-container">
        <div class="register-header">
            <h2><i class="fas fa-user-plus me-2"></i>Create an Account</h2>
            <p class="mb-0">Join BidMaster to start bidding and selling items</p>
        </div>
        <div class="register-form">
            <!-- Alert Messages -->
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-circle me-2"></i>${errorMessage}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            
            <form action="RegisterServlet" method="post" class="needs-validation" novalidate>
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="username" class="form-label">Username</label>
                        <input type="text" class="form-control ${not empty usernameError ? 'is-invalid' : ''}" id="username" name="username" value="${username}" required>
                        <c:if test="${not empty usernameError}">
                            <div class="invalid-feedback">${usernameError}</div>
                        </c:if>
                        <div class="form-text">Choose a unique username (3-20 characters)</div>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="email" class="form-label">Email Address</label>
                        <input type="email" class="form-control ${not empty emailError ? 'is-invalid' : ''}" id="email" name="email" value="${email}" required>
                        <c:if test="${not empty emailError}">
                            <div class="invalid-feedback">${emailError}</div>
                        </c:if>
                    </div>
                </div>
                
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="password" class="form-label">Password</label>
                        <input type="password" class="form-control ${not empty passwordError ? 'is-invalid' : ''}" id="password" name="password" required>
                        <c:if test="${not empty passwordError}">
                            <div class="invalid-feedback">${passwordError}</div>
                        </c:if>
                        <div class="form-text">Minimum 8 characters</div>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="confirmPassword" class="form-label">Confirm Password</label>
                        <input type="password" class="form-control ${not empty confirmPasswordError ? 'is-invalid' : ''}" id="confirmPassword" name="confirmPassword" required>
                        <c:if test="${not empty confirmPasswordError}">
                            <div class="invalid-feedback">${confirmPasswordError}</div>
                        </c:if>
                    </div>
                </div>
                
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="fullName" class="form-label">Full Name</label>
                        <input type="text" class="form-control ${not empty fullNameError ? 'is-invalid' : ''}" id="fullName" name="fullName" value="${fullName}" required>
                        <c:if test="${not empty fullNameError}">
                            <div class="invalid-feedback">${fullNameError}</div>
                        </c:if>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="contactNo" class="form-label">Contact Number (Optional)</label>
                        <input type="tel" class="form-control ${not empty contactNoError ? 'is-invalid' : ''}" id="contactNo" name="contactNo" value="${contactNo}">
                        <c:if test="${not empty contactNoError}">
                            <div class="invalid-feedback">${contactNoError}</div>
                        </c:if>
                    </div>
                </div>
                
                <div class="mb-3">
                    <label for="role" class="form-label">Account Type</label>
                    <select class="form-select" id="role" name="role">
                        <option value="user" ${role == 'user' ? 'selected' : ''}>Buyer Account</option>
                        <option value="seller" ${role == 'seller' ? 'selected' : ''}>Seller Account</option>
                    </select>
                    <div class="form-text">Select "Seller Account" if you want to list items for sale.</div>
                </div>
                
                <div class="mb-3 form-check">
                    <input type="checkbox" class="form-check-input" id="terms" required>
                    <label class="form-check-label" for="terms">I agree to the <a href="#">Terms of Service</a> and <a href="#">Privacy Policy</a></label>
                    <div class="invalid-feedback">You must agree before submitting.</div>
                </div>
                
                <div class="d-grid gap-2">
                    <button type="submit" class="btn btn-register btn-lg text-white">Create Account</button>
                </div>
                
                <div class="text-center mt-3">
                    <p>Already have an account? <a href="login.jsp">Login here</a></p>
                </div>
            </form>
        </div>
    </div>
    
    <!-- Include Footer -->
    <jsp:include page="includes/footer.jsp" />
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
        // Form validation
        (function () {
            'use strict'
            
            // Fetch all forms we want to apply validation to
            var forms = document.querySelectorAll('.needs-validation')
            
            // Loop over them and prevent submission
            Array.prototype.slice.call(forms)
                .forEach(function (form) {
                    form.addEventListener('submit', function (event) {
                        if (!form.checkValidity()) {
                            event.preventDefault()
                            event.stopPropagation()
                        }
                        
                        form.classList.add('was-validated')
                    }, false)
                })
        })()
        
        // Password match validation
        document.getElementById('confirmPassword').addEventListener('input', function() {
            const password = document.getElementById('password').value;
            const confirmPassword = this.value;
            
            if (password !== confirmPassword) {
                this.setCustomValidity('Passwords do not match');
            } else {
                this.setCustomValidity('');
            }
        });
    </script>
</body>
</html>
