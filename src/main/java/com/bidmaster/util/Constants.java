package com.bidmaster.util;

/**
 * Utility class containing constants used throughout the application
 */
public final class Constants {
    
    // Prevent instantiation
    private Constants() {
        throw new AssertionError("Constants class should not be instantiated");
    }
    
    /**
     * Database related constants
     */
    public static final class DB {
        // Prevent instantiation
        private DB() {}
        
        public static final String DRIVER = "com.mysql.cj.jdbc.Driver";
        public static final String URL = "jdbc:mysql://localhost:3306/bidmaster";
        public static final String USERNAME = "root";
        public static final String PASSWORD = "password";
        
        public static final int CONNECTION_POOL_INITIAL_SIZE = 5;
        public static final int CONNECTION_POOL_MAX_SIZE = 20;
    }
    
    /**
     * User related constants
     */
    public static final class User {
        // Prevent instantiation
        private User() {}
        
        public static final String ROLE_ADMIN = "admin";
        public static final String ROLE_USER = "user";
        public static final String ROLE_SELLER = "seller";
        
        public static final int MIN_USERNAME_LENGTH = 3;
        public static final int MAX_USERNAME_LENGTH = 50;
        public static final int MIN_PASSWORD_LENGTH = 8;
        
        public static final String DEFAULT_PROFILE_IMAGE = "assets/images/default-avatar.png";
    }
    
    /**
     * Item related constants
     */
    public static final class Item {
        // Prevent instantiation
        private Item() {}
        
        public static final String STATUS_PENDING = "pending";
        public static final String STATUS_ACTIVE = "active";
        public static final String STATUS_COMPLETED = "completed";
        public static final String STATUS_CANCELLED = "cancelled";
        
        public static final String DEFAULT_ITEM_IMAGE = "assets/images/no-image.png";
        
        public static final int MIN_AUCTION_DURATION_HOURS = 1;
        public static final int MAX_AUCTION_DURATION_DAYS = 30;
        
        public static final int ENDING_SOON_HOURS = 24;
    }
    
    /**
     * Bid related constants
     */
    public static final class Bid {
        // Prevent instantiation
        private Bid() {}
        
        public static final String STATUS_ACTIVE = "active";
        public static final String STATUS_WINNING = "winning";
        public static final String STATUS_OUTBID = "outbid";
        public static final String STATUS_CANCELLED = "cancelled";
        
        public static final double MIN_BID_INCREMENT = 1.0;
    }
    
    /**
     * Transaction related constants
     */
    public static final class Transaction {
        // Prevent instantiation
        private Transaction() {}
        
        public static final String STATUS_PENDING = "pending";
        public static final String STATUS_COMPLETED = "completed";
        public static final String STATUS_FAILED = "failed";
        public static final String STATUS_REFUNDED = "refunded";
        
        public static final String PAYMENT_METHOD_CREDIT_CARD = "credit_card";
        public static final String PAYMENT_METHOD_PAYPAL = "paypal";
        public static final String PAYMENT_METHOD_BANK_TRANSFER = "bank_transfer";
    }
    
    /**
     * File upload related constants
     */
    public static final class Upload {
        // Prevent instantiation
        private Upload() {}
        
        public static final String UPLOAD_DIRECTORY = "uploads";
        public static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
        
        public static final String[] ALLOWED_IMAGE_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif"};
    }
    
    /**
     * Validation related constants
     */
    public static final class Validation {
        // Prevent instantiation
        private Validation() {}
        
        public static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        public static final String USERNAME_REGEX = "^[a-zA-Z0-9_]{3,50}$";
        public static final String CONTACT_REGEX = "^\\+?[0-9]{10,15}$";
    }
    
    /**
     * Date and time related constants
     */
    public static final class DateTime {
        // Prevent instantiation
        private DateTime() {}
        
        public static final String DATE_FORMAT = "yyyy-MM-dd";
        public static final String TIME_FORMAT = "HH:mm:ss";
        public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
        public static final String DISPLAY_DATE_FORMAT = "MMM dd, yyyy";
        public static final String DISPLAY_DATETIME_FORMAT = "MMM dd, yyyy HH:mm";
    }
    
    /**
     * Session attribute names
     */
    public static final class Session {
        // Prevent instantiation
        private Session() {}
        
        public static final String USER_ID = "userId";
        public static final String USERNAME = "username";
        public static final String FULL_NAME = "fullName";
        public static final String ROLE = "role";
    }
    
    /**
     * Message keys
     */
    public static final class Message {
        // Prevent instantiation
        private Message() {}
        
        public static final String SUCCESS = "successMessage";
        public static final String ERROR = "errorMessage";
        public static final String WARNING = "warningMessage";
        public static final String INFO = "infoMessage";
    }
}
