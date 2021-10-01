package edu.asu.diging.quadriga.core.exceptions;

public class MappedCollectionNotFoundException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -4659088638657190516L;
    
    public MappedCollectionNotFoundException() {
        super();
    }

    public MappedCollectionNotFoundException(String exception) {
        super(exception);
    }
    
    public MappedCollectionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public MappedCollectionNotFoundException(Throwable cause) {
        super(cause);
    }

}
