package exceptions;

/**
 * Thrown when confirmation password does not match during user registration.
 */
public class PasswordsDoNotMatchException extends Exception {

    public PasswordsDoNotMatchException() {
        super("As passwords introduzidas nao coincidem.");
    }

    public PasswordsDoNotMatchException(String message) {
        super(message);
    }
}
