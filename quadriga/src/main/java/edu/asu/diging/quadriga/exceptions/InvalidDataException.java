package edu.asu.diging.quadriga.exceptions;

/**
 * 
 * @author Veena Borannagowda
 *
 */
public class InvalidDataException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidDataException() {
        super();
    }

    public InvalidDataException(String exception) {
        super(exception);
    }

}