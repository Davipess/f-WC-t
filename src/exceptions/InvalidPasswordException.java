package exceptions;

/**
 * Thrown when an invalid password is provided during user authentication.
 */
public class InvalidPasswordException extends Exception {

    public InvalidPasswordException() {
        super("Password incorreta.");
    }

    public InvalidPasswordException(String message) {
        super(message);
    }
}
