package edu.asu.diging.quadriga.core.exceptions;

public class CollectionNotFoundException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 2478415220280672102L;
    
    public CollectionNotFoundException() {
        super();
    }

    public CollectionNotFoundException(String exception) {
        super(exception);
    }
    
    public CollectionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public CollectionNotFoundException(Throwable cause) {
        super(cause);
    }

}
