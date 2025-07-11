<!-- Report description -->
${report.description}

<!-- Transactions Table -->
<table class="table table-hover data-table">
    <thead>
        <tr>
            <th>ID</th>
            <th>Item</th>
            <th>Buyer</th>
            <th>Seller</th>
            <th>Amount</th>
            <th>Date</th>
            <th>Status</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="transaction" items="${reportData.transactions}" varStatus="loop">
            <c:if test="${loop.index < 10}">
                <tr>
                    <td>${transaction.transactionId}</td>
                    <td>${transaction.itemTitle}</td>
                    <td>${transaction.buyerUsername}</td>
                    <td>${transaction.sellerUsername}</td>
                    <td>$<fmt:formatNumber value="${transaction.amount}" pattern="#,##0.00"/></td>
                    <td><fmt:formatDate value="${transaction.transactionDate}" pattern="MMM dd, yyyy" /></td>
                    <td>
                        <span class="badge bg-${transaction.status == 'completed' ? 'success' : transaction.status == 'pending' ? 'warning' : 'secondary'}">
                            ${transaction.status}
                        </span>
                    </td>
                </tr>
            </c:if>
        </c:forEach>
    </tbody>
</table>

<!-- Users Table -->
<table class="table table-hover data-table">
    <thead>
        <tr>
            <th>ID</th>
            <th>Username</th>
            <th>Email</th>
            <th>Full Name</th>
            <th>Role</th>
            <th>Registration Date</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="user" items="${reportData.users}" varStatus="loop">
            <c:if test="${loop.index < 10}">
                <tr>
                    <td>${user.userId}</td>
                    <td>${user.username}</td>
                    <td>${user.email}</td>
                    <td>${user.fullName}</td>
                    <td>${user.role}</td>
                    <td><fmt:formatDate value="${user.registrationDate}" pattern="MMM dd, yyyy" /></td>
                </tr>
            </c:if>
        </c:forEach>
    </tbody>
</table>

<!-- Items Table -->
<table class="table table-hover data-table">
    <thead>
        <tr>
            <th>ID</th>
            <th>Title</th>
            <th>Seller</th>
            <th>Category</th>
            <th>Current Price</th>
            <th>End Time</th>
            <th>Status</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="item" items="${reportData.items}" varStatus="loop">
            <c:if test="${loop.index < 10}">
                <tr>
                    <td>${item.itemId}</td>
                    <td>${item.title}</td>
                    <td>${item.sellerUsername}</td>
                    <td>${item.categoryName}</td>
                    <td>$<fmt:formatNumber value="${item.currentPrice}" pattern="#,##0.00"/></td>
                    <td><fmt:formatDate value="${item.endTime}" pattern="MMM dd, yyyy" /></td>
                    <td>
                        <span class="badge bg-${item.status == 'active' ? 'success' : item.status == 'completed' ? 'secondary' : 'primary'}">
                            ${item.status}
                        </span>
                    </td>
                </tr>
            </c:if>
        </c:forEach>
    </tbody>
</table>

<!-- Bids Table -->
<table class="table table-hover data-table">
    <thead>
        <tr>
            <th>ID</th>
            <th>Item</th>
            <th>Bidder</th>
            <th>Amount</th>
            <th>Time</th>
            <th>Status</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="bid" items="${reportData.bids}" varStatus="loop">
            <c:if test="${loop.index < 10}">
                <tr>
                    <td>${bid.bidId}</td>
                    <td>${bid.itemTitle}</td>
                    <td>${bid.bidderUsername}</td>
                    <td>$<fmt:formatNumber value="${bid.bidAmount}" pattern="#,##0.00"/></td>
                    <td><fmt:formatDate value="${bid.bidTime}" pattern="MMM dd, yyyy HH:mm" /></td>
                    <td>
                        <span class="badge bg-${bid.status == 'winning' ? 'success' : bid.status == 'outbid' ? 'warning' : 'secondary'}">
                            ${bid.status}
                        </span>
                    </td>
                </tr>
            </c:if>
        </c:forEach>
    </tbody>
</table>

<!-- No data message -->
<div class="text-center py-4">
    <i class="fas fa-chart-bar fa-3x text-muted mb-3"></i>
    <p>No data available for this report type</p>
</div>

<!-- No summary message -->
<div class="text-center py-4">
    <p>No summary data available</p>
</div>
