package edu.asu.diging.quadriga.core.exceptions;

/**
 * 
 * @author Veena Borannagowda
 *
 */
@Deprecated
public class InvalidDataException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidDataException() {
        super();
    }

    public InvalidDataException(String exception) {
        super(exception);
    }

}