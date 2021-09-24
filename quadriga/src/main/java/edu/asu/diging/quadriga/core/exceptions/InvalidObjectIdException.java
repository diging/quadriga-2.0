package edu.asu.diging.quadriga.core.exceptions;

public class InvalidObjectIdException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 4182523153285020271L;
    
    public InvalidObjectIdException() {
        super();
    }

    public InvalidObjectIdException(String exception) {
        super(exception);
    }
    
    public InvalidObjectIdException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public InvalidObjectIdException(Throwable cause) {
        super(cause);
    }

}
