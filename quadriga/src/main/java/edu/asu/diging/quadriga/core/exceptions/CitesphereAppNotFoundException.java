package edu.asu.diging.quadriga.core.exceptions;

public class CitesphereAppNotFoundException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 2478415220280672102L;
    
    public CitesphereAppNotFoundException() {
        super();
    }

    public CitesphereAppNotFoundException(String exception) {
        super(exception);
    }
    
    public CitesphereAppNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public CitesphereAppNotFoundException(Throwable cause) {
        super(cause);
    }
     
}
