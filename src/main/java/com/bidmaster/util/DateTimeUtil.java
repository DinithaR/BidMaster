package com.bidmaster.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for date and time operations
 */
public final class DateTimeUtil {
    private static final Logger LOGGER = Logger.getLogger(DateTimeUtil.class.getName());
    
    // Prevent instantiation
    private DateTimeUtil() {
        throw new AssertionError("DateTimeUtil class should not be instantiated");
    }
    
    /**
     * Formats a Date object to a string using the specified pattern
     * 
     * @param date The Date to format
     * @param pattern The date format pattern
     * @return The formatted date string
     */
    public static String formatDate(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }
    
    /**
     * Formats a LocalDateTime object to a string using the specified pattern
     * 
     * @param dateTime The LocalDateTime to format
     * @param pattern The date format pattern
     * @return The formatted date string
     */
    public static String formatDateTime(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(formatter);
    }
    
    /**
     * Parses a date string to a Date object using the specified pattern
     * 
     * @param dateStr The date string to parse
     * @param pattern The date format pattern
     * @return The parsed Date object, or null if parsing fails
     */
    public static Date parseDate(String dateStr, String pattern) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            LOGGER.log(Level.WARNING, "Error parsing date: " + dateStr, e);
            return null;
        }
    }
    
    /**
     * Parses a date string to a LocalDateTime object using the specified pattern
     * 
     * @param dateStr The date string to parse
     * @param pattern The date format pattern
     * @return The parsed LocalDateTime object, or null if parsing fails
     */
    public static LocalDateTime parseDateTime(String dateStr, String pattern) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return LocalDateTime.parse(dateStr, formatter);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error parsing datetime: " + dateStr, e);
            return null;
        }
    }
    
    /**
     * Converts a Date object to a LocalDateTime object
     * 
     * @param date The Date to convert
     * @return The converted LocalDateTime object
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
    
    /**
     * Converts a LocalDateTime object to a Date object
     * 
     * @param localDateTime The LocalDateTime to convert
     * @return The converted Date object
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    
    /**
     * Gets the current date and time as a LocalDateTime object
     * 
     * @return The current LocalDateTime
     */
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }
    
    /**
     * Gets the current date and time as a Date object
     * 
     * @return The current Date
     */
    public static Date getCurrentDate() {
        return new Date();
    }
    
    /**
     * Calculates the time difference between two dates as a formatted string
     * 
     * @param endDate The end date
     * @param startDate The start date
     * @return The formatted time difference (e.g., "2d 5h 30m")
     */
    public static String getTimeDifference(Date endDate, Date startDate) {
        if (endDate == null || startDate == null) {
            return "";
        }
        
        long diffInMillis = endDate.getTime() - startDate.getTime();
        return formatDuration(diffInMillis);
    }
    
    /**
     * Calculates the time difference between two LocalDateTime objects as a formatted string
     * 
     * @param endDateTime The end date and time
     * @param startDateTime The start date and time
     * @return The formatted time difference (e.g., "2d 5h 30m")
     */
    public static String getTimeDifference(LocalDateTime endDateTime, LocalDateTime startDateTime) {
        if (endDateTime == null || startDateTime == null) {
            return "";
        }
        
        Duration duration = Duration.between(startDateTime, endDateTime);
        return formatDuration(duration.toMillis());
    }
    
    /**
     * Formats a duration in milliseconds to a readable string
     * 
     * @param durationMillis The duration in milliseconds
     * @return The formatted duration string
     */
    public static String formatDuration(long durationMillis) {
        if (durationMillis < 0) {
            return "0s";
        }
        
        long seconds = durationMillis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        
        seconds %= 60;
        minutes %= 60;
        hours %= 24;
        
        StringBuilder result = new StringBuilder();
        
        if (days > 0) {
            result.append(days).append("d ");
        }
        
        if (hours > 0 || days > 0) {
            result.append(hours).append("h ");
        }
        
        if (minutes > 0 || hours > 0 || days > 0) {
            result.append(minutes).append("m ");
        }
        
        if (seconds > 0 && days == 0 && hours == 0) {
            result.append(seconds).append("s");
        }
        
        return result.toString().trim();
    }
    
    /**
     * Adds a specified number of days to a Date
     * 
     * @param date The base date
     * @param days The number of days to add
     * @return The new Date with added days
     */
    public static Date addDays(Date date, int days) {
        if (date == null) {
            return null;
        }
        return new Date(date.getTime() + ((long) days * 24 * 60 * 60 * 1000));
    }
    
    /**
     * Adds a specified number of days to a LocalDateTime
     * 
     * @param dateTime The base date and time
     * @param days The number of days to add
     * @return The new LocalDateTime with added days
     */
    public static LocalDateTime addDays(LocalDateTime dateTime, int days) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.plusDays(days);
    }
    
    /**
     * Adds a specified number of hours to a Date
     * 
     * @param date The base date
     * @param hours The number of hours to add
     * @return The new Date with added hours
     */
    public static Date addHours(Date date, int hours) {
        if (date == null) {
            return null;
        }
        return new Date(date.getTime() + ((long) hours * 60 * 60 * 1000));
    }
    
    /**
     * Adds a specified number of hours to a LocalDateTime
     * 
     * @param dateTime The base date and time
     * @param hours The number of hours to add
     * @return The new LocalDateTime with added hours
     */
    public static LocalDateTime addHours(LocalDateTime dateTime, int hours) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.plusHours(hours);
    }
    
    /**
     * Checks if a date is in the past
     * 
     * @param date The date to check
     * @return true if the date is in the past, false otherwise
     */
    public static boolean isInPast(Date date) {
        if (date == null) {
            return false;
        }
        return date.before(new Date());
    }
    
    /**
     * Checks if a date and time is in the past
     * 
     * @param dateTime The date and time to check
     * @return true if the date and time is in the past, false otherwise
     */
    public static boolean isInPast(LocalDateTime dateTime) {
        if (dateTime == null) {
            return false;
        }
        return dateTime.isBefore(LocalDateTime.now());
    }
    
    /**
     * Checks if a date is in the future
     * 
     * @param date The date to check
     * @return true if the date is in the future, false otherwise
     */
    public static boolean isInFuture(Date date) {
        if (date == null) {
            return false;
        }
        return date.after(new Date());
    }
    
    /**
     * Checks if a date and time is in the future
     * 
     * @param dateTime The date and time to check
     * @return true if the date and time is in the future, false otherwise
     */
    public static boolean isInFuture(LocalDateTime dateTime) {
        if (dateTime == null) {
            return false;
        }
        return dateTime.isAfter(LocalDateTime.now());
    }
}
