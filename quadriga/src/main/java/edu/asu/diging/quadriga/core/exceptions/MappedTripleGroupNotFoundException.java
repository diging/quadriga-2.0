package edu.asu.diging.quadriga.core.exceptions;

public class MappedTripleGroupNotFoundException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -4659088638657190516L;
    
    public MappedTripleGroupNotFoundException() {
        super();
    }

    public MappedTripleGroupNotFoundException(String exception) {
        super(exception);
    }
    
    public MappedTripleGroupNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public MappedTripleGroupNotFoundException(Throwable cause) {
        super(cause);
    }

}
