package edu.asu.diging.quadriga.core.exceptions;

public class JobNotFoundException extends Exception {
    

    private static final long serialVersionUID = 1;
    
    public JobNotFoundException() {
        super();
    }

    public JobNotFoundException(String exception) {
        super(exception);
    }
    
    public JobNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public JobNotFoundException(Throwable cause) {
        super(cause);
    }
       
}