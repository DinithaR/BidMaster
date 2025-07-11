-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost:8889
-- Generation Time: May 19, 2025 at 01:15 PM
-- Server version: 8.0.40
-- PHP Version: 8.3.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `bidmaster`
--

-- --------------------------------------------------------

--
-- Table structure for table `Bids`
--

CREATE TABLE `Bids` (
  `bidId` int NOT NULL,
  `itemId` int NOT NULL,
  `bidderId` int NOT NULL,
  `bidAmount` decimal(10,2) NOT NULL,
  `bidTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `status` enum('active','winning','outbid','cancelled') DEFAULT 'active'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `Bids`
--

INSERT INTO `Bids` (`bidId`, `itemId`, `bidderId`, `bidAmount`, `bidTime`, `status`) VALUES
(3, 3, 4, 320.00, '2025-05-19 08:57:37', 'outbid'),
(4, 3, 5, 350.00, '2025-05-19 08:57:37', 'active'),
(5, 4, 4, 60.00, '2025-05-19 08:57:37', 'active'),
(6, 5, 5, 1250.00, '2025-05-19 08:57:37', 'winning');

-- --------------------------------------------------------

--
-- Table structure for table `Categories`
--

CREATE TABLE `Categories` (
  `categoryId` int NOT NULL,
  `categoryName` varchar(100) NOT NULL,
  `description` text,
  `parentCategoryId` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `Categories`
--

INSERT INTO `Categories` (`categoryId`, `categoryName`, `description`, `parentCategoryId`) VALUES
(1, 'Electronics', 'Electronic devices and gadgets', NULL),
(2, 'Collectibles', 'Rare and collectible items', NULL),
(3, 'Fashion', 'Clothing, shoes, and accessories', NULL),
(4, 'Home & Garden', 'Items for home and garden', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `Feedback`
--

CREATE TABLE `Feedback` (
  `feedbackId` int NOT NULL,
  `transactionId` int NOT NULL,
  `fromUserId` int NOT NULL,
  `toUserId` int NOT NULL,
  `rating` int NOT NULL,
  `comment` text,
  `feedbackDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ;

-- --------------------------------------------------------

--
-- Table structure for table `Items`
--

CREATE TABLE `Items` (
  `itemId` int NOT NULL,
  `title` varchar(100) NOT NULL,
  `description` text NOT NULL,
  `startingPrice` decimal(10,2) NOT NULL,
  `reservePrice` decimal(10,2) DEFAULT NULL,
  `currentPrice` decimal(10,2) DEFAULT NULL,
  `imageUrl` varchar(255) DEFAULT NULL,
  `categoryId` int DEFAULT NULL,
  `sellerId` int NOT NULL,
  `startTime` datetime NOT NULL,
  `endTime` datetime NOT NULL,
  `status` enum('pending','active','completed','cancelled') DEFAULT 'pending',
  `createdAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `Items`
--

INSERT INTO `Items` (`itemId`, `title`, `description`, `startingPrice`, `reservePrice`, `currentPrice`, `imageUrl`, `categoryId`, `sellerId`, `startTime`, `endTime`, `status`, `createdAt`) VALUES
(3, 'Vintage Comic Book', 'Rare vintage comic book from 1960s', 100.00, NULL, 150.00, 'assets/images/items/comic.jpg', 2, 2, '2025-05-19 14:27:37', '2025-05-24 14:27:37', 'active', '2025-05-19 08:57:37'),
(4, 'Designer Handbag', 'Authentic designer handbag, barely used', 300.00, NULL, 350.00, 'assets/images/items/handbag.jpg', 3, 3, '2025-05-19 14:27:37', '2025-05-22 14:27:37', 'active', '2025-05-19 08:57:37'),
(5, 'Garden Tools Set', 'Complete set of garden tools', 50.00, NULL, 60.00, 'assets/images/items/tools.jpg', 4, 3, '2025-05-19 14:27:37', '2025-05-29 14:27:37', 'active', '2025-05-19 08:57:37'),
(6, 'Gaming Laptop', 'High-performance gaming laptop', 1200.00, NULL, 1250.00, 'assets/images/items/laptop.jpg', 1, 2, '2025-05-04 14:27:37', '2025-05-11 14:27:37', 'completed', '2025-05-19 08:57:37'),
(7, 'Gaming Chair', 'Gaming Chair', 45.00, 76.00, 45.00, 'assets/images/items/c3ab5e17-93bb-49c2-8b5c-42a73a742806.jpg', 4, 3, '2025-05-19 09:53:05', '2025-05-24 09:53:05', 'active', '2025-05-19 09:53:05');

-- --------------------------------------------------------

--
-- Table structure for table `reports`
--

CREATE TABLE `reports` (
  `reportId` int NOT NULL,
  `title` varchar(255) NOT NULL,
  `description` text,
  `reportType` varchar(50) NOT NULL,
  `startDate` date DEFAULT NULL,
  `endDate` date DEFAULT NULL,
  `createdBy` int NOT NULL,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NULL DEFAULT NULL,
  `status` varchar(20) NOT NULL DEFAULT 'active'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `reports`
--

INSERT INTO `reports` (`reportId`, `title`, `description`, `reportType`, `startDate`, `endDate`, `createdBy`, `createdAt`, `updatedAt`, `status`) VALUES
(1, 'test', 'Automatically generated sales report', 'sales', '2025-05-01', '2025-05-31', 1, '2025-05-19 07:04:26', NULL, 'active'),
(2, 'test', 'test', 'sales', '2025-05-01', '2025-05-31', 1, '2025-05-19 07:26:53', NULL, 'active'),
(3, 'test', 'test', 'users', '2025-05-01', '2025-05-20', 1, '2025-05-19 07:31:30', NULL, 'active');

-- --------------------------------------------------------

--
-- Table structure for table `Transactions`
--

CREATE TABLE `Transactions` (
  `transactionId` int NOT NULL,
  `itemId` int NOT NULL,
  `sellerId` int NOT NULL,
  `buyerId` int NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `transactionDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `status` enum('pending','completed','failed','refunded') DEFAULT 'pending',
  `paymentMethod` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `Transactions`
--

INSERT INTO `Transactions` (`transactionId`, `itemId`, `sellerId`, `buyerId`, `amount`, `transactionDate`, `status`, `paymentMethod`) VALUES
(1, 5, 2, 5, 1250.00, '2025-05-19 08:57:37', 'completed', 'Credit Card');

-- --------------------------------------------------------

--
-- Table structure for table `Users`
--

CREATE TABLE `Users` (
  `userId` int NOT NULL,
  `username` varchar(50) NOT NULL,
  `email` varchar(75) NOT NULL,
  `password` varchar(255) NOT NULL,
  `fullName` varchar(100) NOT NULL,
  `contactNo` varchar(20) DEFAULT NULL,
  `role` enum('user','admin','seller') DEFAULT 'user',
  `profileImage` varchar(255) DEFAULT NULL,
  `registrationDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `Users`
--

INSERT INTO `Users` (`userId`, `username`, `email`, `password`, `fullName`, `contactNo`, `role`, `profileImage`, `registrationDate`) VALUES
(1, 'admin', 'admin@bidmaster.com', 'admin123', 'System Administrator', NULL, 'admin', NULL, '2025-05-19 02:23:00'),
(2, 'test', 'test@gmail.com', '12345678', 'test now', '0777656755', 'user', NULL, '2025-05-19 03:09:04'),
(3, 'seller', 'seller@gmail.com', 'seller123', 'test seller', '0777360146', 'seller', NULL, '2025-05-19 07:45:41'),
(4, 'seller1', 'seller1@example.com', 'password123', 'John', '', 'seller', NULL, '2025-05-19 08:57:37'),
(5, 'seller2', 'seller2@example.com', 'password123', 'Jane Seller', NULL, 'seller', NULL, '2025-05-19 08:57:37'),
(6, 'buyer1', 'buyer1@example.com', 'password123', 'Bob Buyer', NULL, 'user', NULL, '2025-05-19 08:57:37'),
(7, 'buyer2', 'buyer2@example.com', 'password123', 'Alice Buyer', NULL, 'user', NULL, '2025-05-19 08:57:37');

-- --------------------------------------------------------

--
-- Table structure for table `Watchlist`
--

CREATE TABLE `Watchlist` (
  `watchlistId` int NOT NULL,
  `userId` int NOT NULL,
  `itemId` int NOT NULL,
  `addedDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `Watchlist`
--

INSERT INTO `Watchlist` (`watchlistId`, `userId`, `itemId`, `addedDate`) VALUES
(2, 4, 3, '2025-05-19 08:57:37'),
(4, 5, 4, '2025-05-19 08:57:37');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Bids`
--
ALTER TABLE `Bids`
  ADD PRIMARY KEY (`bidId`),
  ADD KEY `itemId` (`itemId`),
  ADD KEY `bidderId` (`bidderId`);

--
-- Indexes for table `Categories`
--
ALTER TABLE `Categories`
  ADD PRIMARY KEY (`categoryId`),
  ADD KEY `parentCategoryId` (`parentCategoryId`);

--
-- Indexes for table `Feedback`
--
ALTER TABLE `Feedback`
  ADD PRIMARY KEY (`feedbackId`),
  ADD KEY `transactionId` (`transactionId`),
  ADD KEY `fromUserId` (`fromUserId`),
  ADD KEY `toUserId` (`toUserId`);

--
-- Indexes for table `Items`
--
ALTER TABLE `Items`
  ADD PRIMARY KEY (`itemId`),
  ADD KEY `categoryId` (`categoryId`),
  ADD KEY `sellerId` (`sellerId`);

--
-- Indexes for table `reports`
--
ALTER TABLE `reports`
  ADD PRIMARY KEY (`reportId`),
  ADD KEY `fk_reports_users` (`createdBy`);

--
-- Indexes for table `Transactions`
--
ALTER TABLE `Transactions`
  ADD PRIMARY KEY (`transactionId`),
  ADD KEY `itemId` (`itemId`),
  ADD KEY `sellerId` (`sellerId`),
  ADD KEY `buyerId` (`buyerId`);

--
-- Indexes for table `Users`
--
ALTER TABLE `Users`
  ADD PRIMARY KEY (`userId`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `Watchlist`
--
ALTER TABLE `Watchlist`
  ADD PRIMARY KEY (`watchlistId`),
  ADD UNIQUE KEY `user_item_unique` (`userId`,`itemId`),
  ADD KEY `itemId` (`itemId`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Bids`
--
ALTER TABLE `Bids`
  MODIFY `bidId` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `Categories`
--
ALTER TABLE `Categories`
  MODIFY `categoryId` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `Feedback`
--
ALTER TABLE `Feedback`
  MODIFY `feedbackId` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Items`
--
ALTER TABLE `Items`
  MODIFY `itemId` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `reports`
--
ALTER TABLE `reports`
  MODIFY `reportId` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `Transactions`
--
ALTER TABLE `Transactions`
  MODIFY `transactionId` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `Users`
--
ALTER TABLE `Users`
  MODIFY `userId` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `Watchlist`
--
ALTER TABLE `Watchlist`
  MODIFY `watchlistId` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `Bids`
--
ALTER TABLE `Bids`
  ADD CONSTRAINT `bids_ibfk_1` FOREIGN KEY (`itemId`) REFERENCES `Items` (`itemId`) ON DELETE CASCADE,
  ADD CONSTRAINT `bids_ibfk_2` FOREIGN KEY (`bidderId`) REFERENCES `Users` (`userId`) ON DELETE CASCADE;

--
-- Constraints for table `Categories`
--
ALTER TABLE `Categories`
  ADD CONSTRAINT `categories_ibfk_1` FOREIGN KEY (`parentCategoryId`) REFERENCES `Categories` (`categoryId`) ON DELETE SET NULL;

--
-- Constraints for table `Feedback`
--
ALTER TABLE `Feedback`
  ADD CONSTRAINT `feedback_ibfk_1` FOREIGN KEY (`transactionId`) REFERENCES `Transactions` (`transactionId`) ON DELETE CASCADE,
  ADD CONSTRAINT `feedback_ibfk_2` FOREIGN KEY (`fromUserId`) REFERENCES `Users` (`userId`) ON DELETE CASCADE,
  ADD CONSTRAINT `feedback_ibfk_3` FOREIGN KEY (`toUserId`) REFERENCES `Users` (`userId`) ON DELETE CASCADE;

--
-- Constraints for table `Items`
--
ALTER TABLE `Items`
  ADD CONSTRAINT `items_ibfk_1` FOREIGN KEY (`categoryId`) REFERENCES `Categories` (`categoryId`) ON DELETE SET NULL,
  ADD CONSTRAINT `items_ibfk_2` FOREIGN KEY (`sellerId`) REFERENCES `Users` (`userId`) ON DELETE CASCADE;

--
-- Constraints for table `reports`
--
ALTER TABLE `reports`
  ADD CONSTRAINT `fk_reports_users` FOREIGN KEY (`createdBy`) REFERENCES `users` (`userId`) ON DELETE CASCADE;

--
-- Constraints for table `Transactions`
--
ALTER TABLE `Transactions`
  ADD CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`itemId`) REFERENCES `Items` (`itemId`) ON DELETE CASCADE,
  ADD CONSTRAINT `transactions_ibfk_2` FOREIGN KEY (`sellerId`) REFERENCES `Users` (`userId`) ON DELETE CASCADE,
  ADD CONSTRAINT `transactions_ibfk_3` FOREIGN KEY (`buyerId`) REFERENCES `Users` (`userId`) ON DELETE CASCADE;

--
-- Constraints for table `Watchlist`
--
ALTER TABLE `Watchlist`
  ADD CONSTRAINT `watchlist_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `Users` (`userId`) ON DELETE CASCADE,
  ADD CONSTRAINT `watchlist_ibfk_2` FOREIGN KEY (`itemId`) REFERENCES `Items` (`itemId`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
