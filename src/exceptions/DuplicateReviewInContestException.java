package exceptions;

/**
 * Thrown when a user attempts to review the same bathroom multiple times within the same contest cycle.
 */
public class DuplicateReviewInContestException extends Exception {
    public DuplicateReviewInContestException() {
        super();
    }
}
