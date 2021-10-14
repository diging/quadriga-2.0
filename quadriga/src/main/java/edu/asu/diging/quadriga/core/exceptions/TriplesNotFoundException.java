package edu.asu.diging.quadriga.core.exceptions;

public class TriplesNotFoundException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -7199809962065530379L;
    
    public TriplesNotFoundException() {
        super();
    }

    public TriplesNotFoundException(String exception) {
        super(exception);
    }
    
    public TriplesNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public TriplesNotFoundException(Throwable cause) {
        super(cause);
    }

}
