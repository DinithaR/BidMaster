package com.bidmaster.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.Part;

public class ImageUploadUtil {
    private static final Logger LOGGER = Logger.getLogger(ImageUploadUtil.class.getName());
    
    private static final String UPLOAD_DIRECTORY = "assets/images/items";
    
    /**
     * Uploads an image file and returns the relative URL
     * 
     * @param filePart The file part from multipart request
     * @param context The servlet context
     * @return The relative URL of the uploaded image
     * @throws IOException if an I/O error occurs
     */
    public static String uploadImage(Part filePart, ServletContext context) throws IOException {
        // Get file name
        String fileName = getSubmittedFileName(filePart);
        
        // Validate file type
        if (!isValidImageFile(fileName)) {
            throw new IOException("Invalid file type. Only JPG, PNG, and GIF are allowed.");
        }
        
        // Generate unique file name
        String uniqueFileName = generateUniqueFileName(fileName);
        
        // Get upload directory path
        String uploadPath = context.getRealPath("") + File.separator + UPLOAD_DIRECTORY;
        
        // Create directory if it doesn't exist
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        
        // Normalize and validate the path to prevent path traversal attacks
        Path filePath = Paths.get(uploadPath, uniqueFileName).normalize();
        if (!filePath.startsWith(uploadPath)) {
            throw new IOException("Invalid file path: possible path traversal attempt");
        }
        
        // Save file
        Files.copy(filePart.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        LOGGER.log(Level.INFO, "File uploaded: {0}", uniqueFileName);
        
        // Return relative URL
        return UPLOAD_DIRECTORY + "/" + uniqueFileName;
    }
    
    /**
     * Deletes an image file
     * 
     * @param imageUrl The relative URL of the image to delete
     * @param context The servlet context
     * @return true if the file was deleted, false otherwise
     */
    public static boolean deleteImage(String imageUrl, ServletContext context) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return false;
        }
        
        try {
            // Get absolute path
            String uploadPath = context.getRealPath("") + File.separator;
            Path imagePath = Paths.get(uploadPath, imageUrl).normalize();
            
            // Validate path is within the upload directory
            if (!imagePath.startsWith(uploadPath)) {
                LOGGER.log(Level.WARNING, "Invalid image path: possible path traversal attempt");
                return false;
            }
            
            // Delete file if it exists
            if (Files.exists(imagePath)) {
                Files.delete(imagePath);
                LOGGER.log(Level.INFO, "File deleted: {0}", imageUrl);
                return true;
            } else {
                LOGGER.log(Level.WARNING, "File not found: {0}", imageUrl);
                return false;
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error deleting image: " + imageUrl, e);
            return false;
        }
    }
    
    /**
     * Gets the submitted file name from a Part
     * 
     * @param part The part from multipart request
     * @return The submitted file name
     */
    private static String getSubmittedFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        
        for (String item : items) {
            if (item.trim().startsWith("filename")) {
                return item.substring(item.indexOf('=') + 2, item.length() - 1);
            }
        }
        
        return "";
    }
    
    /**
     * Checks if the file is a valid image file
     * 
     * @param fileName The file name
     * @return true if the file is a valid image file, false otherwise
     */
    private static boolean isValidImageFile(String fileName) {
        String lowerCaseFileName = fileName.toLowerCase();
        return lowerCaseFileName.endsWith(".jpg") || 
               lowerCaseFileName.endsWith(".jpeg") || 
               lowerCaseFileName.endsWith(".png") || 
               lowerCaseFileName.endsWith(".gif");
    }
    
    /**
     * Generates a unique file name
     * 
     * @param originalFileName The original file name
     * @return The unique file name
     */
    private static String generateUniqueFileName(String originalFileName) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        return UUID.randomUUID().toString() + extension;
    }
}
