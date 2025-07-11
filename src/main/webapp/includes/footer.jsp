<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<footer class="bg-dark text-white py-5 mt-5">
    <div class="container">
        <div class="row">
            <div class="col-md-4 mb-4 mb-md-0">
                <h5 class="mb-3">About BidMaster</h5>
                <p class="text-muted">BidMaster is your premier online bidding platform for unique items and amazing deals. We connect buyers and sellers in a secure and exciting auction environment.</p>
                <div class="d-flex mt-3">
                    <a href="#" class="text-white me-3"><i class="fab fa-facebook-f"></i></a>
                    <a href="#" class="text-white me-3"><i class="fab fa-twitter"></i></a>
                    <a href="#" class="text-white me-3"><i class="fab fa-instagram"></i></a>
                    <a href="#" class="text-white"><i class="fab fa-linkedin-in"></i></a>
                </div>
            </div>
            
            <div class="col-md-2 mb-4 mb-md-0">
                <h5 class="mb-3">Quick Links</h5>
                <ul class="list-unstyled">
                    <li class="mb-2"><a href="index.jsp" class="text-muted">Home</a></li>
                    <li class="mb-2"><a href="ItemListServlet?action=listActive" class="text-muted">Browse Auctions</a></li>
                    <li class="mb-2"><a href="ItemListServlet?action=listEndingSoon" class="text-muted">Ending Soon</a></li>
                    <li class="mb-2"><a href="about.jsp" class="text-muted">About Us</a></li>
                    <li><a href="contact.jsp" class="text-muted">Contact</a></li>
                </ul>
            </div>
            
            <div class="col-md-2 mb-4 mb-md-0">
                <h5 class="mb-3">Categories</h5>
                <ul class="list-unstyled">
                    <li class="mb-2"><a href="ItemListServlet?action=listByCategory&categoryId=1" class="text-muted">Electronics</a></li>
                    <li class="mb-2"><a href="ItemListServlet?action=listByCategory&categoryId=2" class="text-muted">Collectibles</a></li>
                    <li class="mb-2"><a href="ItemListServlet?action=listByCategory&categoryId=3" class="text-muted">Fashion</a></li>
                    <li><a href="ItemListServlet?action=listByCategory&categoryId=4" class="text-muted">Home & Garden</a></li>
                </ul>
            </div>
            
            <div class="col-md-4">
                <h5 class="mb-3">Contact Us</h5>
                <ul class="list-unstyled text-muted">
                    <li class="mb-2"><i class="fas fa-map-marker-alt me-2"></i> 123 Auction Street, City, Country</li>
                    <li class="mb-2"><i class="fas fa-phone me-2"></i> +1 (555) 123-4567</li>
                    <li class="mb-2"><i class="fas fa-envelope me-2"></i> info@bidmaster.com</li>
                </ul>
                <div class="mt-3">
                    <h6>Subscribe to our Newsletter</h6>
                    <form class="d-flex mt-2">
                        <input type="email" class="form-control me-2" placeholder="Your email">
                        <button class="btn btn-primary" type="submit">Subscribe</button>
                    </form>
                </div>
            </div>
        </div>
        
        <hr class="my-4 bg-secondary">
        
        <div class="row align-items-center">
            <div class="col-md-6 text-center text-md-start mb-3 mb-md-0">
                <p class="mb-0 text-muted">&copy; <%= java.time.Year.now().getValue() %> BidMaster. All rights reserved.</p>
            </div>
            <div class="col-md-6 text-center text-md-end">
                <ul class="list-inline mb-0">
                    <li class="list-inline-item"><a href="#" class="text-muted">Privacy Policy</a></li>
                    <li class="list-inline-item"><span class="text-muted">|</span></li>
                    <li class="list-inline-item"><a href="#" class="text-muted">Terms of Use</a></li>
                    <li class="list-inline-item"><span class="text-muted">|</span></li>
                    <li class="list-inline-item"><a href="#" class="text-muted">FAQ</a></li>
                </ul>
            </div>
        </div>
    </div>
</footer>
