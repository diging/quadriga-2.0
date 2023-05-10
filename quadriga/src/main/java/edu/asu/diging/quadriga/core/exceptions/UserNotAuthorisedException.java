package edu.asu.diging.quadriga.core.exceptions;

public class UserNotAuthorisedException extends SecurityException {
    
    private static final long serialVersionUID = 1L;

    public UserNotAuthorisedException() {
        super();
    }

    public UserNotAuthorisedException(String message) {
        super(message);
    }

    public UserNotAuthorisedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotAuthorisedException(Throwable cause) {
        super(cause);
    }

}
