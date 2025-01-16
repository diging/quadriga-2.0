package edu.asu.diging.quadriga.core.exceptions;

public class UserNotAuthorizedException extends SecurityException {
    
    private static final long serialVersionUID = 1L;

    public UserNotAuthorizedException() {
        super();
    }

    public UserNotAuthorizedException(String message) {
        super(message);
    }

    public UserNotAuthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotAuthorizedException(Throwable cause) {
        super(cause);
    }

}
