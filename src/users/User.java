package users;

/**
 * Interface representing a user within the FCT Bathrooms domain model.
 */
public interface User {

    /**
     * Retrieves the unique identifier of the user (e.g., student number).
     *
     * @return the unique user ID
     */
    String getId();

    /**
     * Retrieves the full name of the user.
     *
     * @return the user full name
     */
    String getName();

    /**
     * Retrieves the role assigned to the user.
     *
     * @return the user role
     */
    Role getRole();

    /**
     * Checks if the provided candidate password matches the user credentials.
     *
     * @param candidate candidate password string
     * @return true if password matches, false otherwise
     */
    boolean validatePassword(String candidate);

    /**
     * Updates the password for this user.
     *
     * @param newPassword the new password
     */
    void setPassword(String newPassword);

    /**
     * Retrieves the total count of reviews posted by this user.
     *
     * @return number of submitted reviews
     */
    int getReviewCount();

    /**
     * Retrieves the total count of alerts reported by this user.
     *
     * @return number of reported alerts
     */
    int getAlertCount();

    /**
     * Increments the user's review submission counter.
     */
    void incrementReviewCount();

    /**
     * Increments the user's alert report counter.
     */
    void incrementAlertCount();
}
