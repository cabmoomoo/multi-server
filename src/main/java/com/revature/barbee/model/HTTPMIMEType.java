package com.revature.barbee.model;

// Enum built based off of:
// https://stackoverflow.com/a/48704300

/**
 * Enum of HTTP MIME types known to the the program. Enums naturally sort
 * into the order they were declared. As the Accepted header has a secondary
 * ordering of specificity, less specific MIME types are declared later.
 * 
 * <code> HTML < ANY_TEXT < ANY </code>
 */
public enum HTTPMIMEType {
    HTML("text/html"),
    CSS("text/css"),
    CSV("text/csv"),
    PLAIN("text/plain"),
    JSON("application/json"),
    PNG("img/png"),
    ICON("img/vnd.microsoft.icon"), // https://stackoverflow.com/a/13828914
    MP4("video/mp4"),
    
    ANY_TEXT("text/*"),
    ANY_IMAGE("img/*"),
    
    ANY("*/*");

    private final String type;

    HTTPMIMEType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
