package edu.asu.diging.quadriga.core.exceptions;

public class UserAppNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -653364011878713913L;
	
	public UserAppNotFoundException() {
        super();
    }

    public UserAppNotFoundException(String exception) {
        super(exception);
    }
    
    public UserAppNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public UserAppNotFoundException(Throwable cause) {
        super(cause);
    }

}
