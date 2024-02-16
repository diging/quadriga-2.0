package edu.asu.diging.quadriga.core.exceptions;

public class SimpleUserAppNotFoundException extends Exception{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;    
    public SimpleUserAppNotFoundException() {
        super();
    }

    public SimpleUserAppNotFoundException(String exception) {
        super(exception);
    }
    
    public SimpleUserAppNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public SimpleUserAppNotFoundException(Throwable cause) {
        super(cause);
    }

}
