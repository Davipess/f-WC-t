package exceptions;

/**
 * Thrown when a user attempts to confirm an alert they have already confirmed.
 */
public class AlertAlreadyConfirmedException extends Exception {
    public AlertAlreadyConfirmedException() {
        super();
    }
}
