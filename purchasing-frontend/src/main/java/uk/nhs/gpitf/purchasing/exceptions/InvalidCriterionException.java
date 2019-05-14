package uk.nhs.gpitf.purchasing.exceptions;

public class InvalidCriterionException extends Exception {
	String userMessage;
    public InvalidCriterionException(String message) {
        super(message);
        this.userMessage = message;
     }
    
    public InvalidCriterionException(String message, String userMessage) {
        super(message);
        this.userMessage = userMessage;
    }
    
    public String getUserMessage() {
    	return this.userMessage;
    }
}
