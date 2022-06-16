package edu.asu.diging.quadriga.core.exceptions;

public class TokenNotFoundException extends Exception {

    private static final long serialVersionUID = -1722030829513272540L;

    public TokenNotFoundException() {
        super();
    }

    public TokenNotFoundException(String exception) {
        super(exception);
    }

    public TokenNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenNotFoundException(Throwable cause) {
        super(cause);
    }

}
