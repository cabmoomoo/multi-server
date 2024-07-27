package com.revature.barbee.model;

/**
 * Generic error for the program, typically used for grouping different errors under one banner.
 * 
 * Example:
 * <pre>
 *  catch (URISyntaxException | IOException ex) {
 *      throw new MultiServerError("Summary message for what the block was trying to do" + ex.getMessage());
 *  }
 * </pre>
 */
public class MultiServerError extends Exception {
    private final String message;

    public MultiServerError(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
