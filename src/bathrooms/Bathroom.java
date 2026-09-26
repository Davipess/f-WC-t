package bathrooms;

import alerts.Alert;
import reviews.Review;

import java.util.Iterator;

/**
 * Core interface representing a bathroom facility on the campus.
 */
public interface Bathroom {

    /**
     * Retrieves the unique identifier of the bathroom (e.g. DI-0-M1).
     *
     * @return bathroom ID
     */
    String getId();

    /**
     * Retrieves the building code where this bathroom is located (e.g. VII, DI, DM).
     *
     * @return building code
     */
    String getBuildingCode();

    /**
     * Retrieves the floor number where the bathroom is located.
     *
     * @return floor level
     */
    int getFloor();

    /**
     * Retrieves the gender designation (e.g. MALE, FEMALE, UNISEX).
     *
     * @return gender designation
     */
    String getGender();

    /**
     * Retrieves the specific location description (e.g. Main Entrance, Lab Hallway).
     *
     * @return specific location description
     */
    String getLocation();

    /**
     * Updates the specific location description.
     *
     * @param location new location description
     */
    void setLocation(String location);

    /**
     * Retrieves the operational status of the bathroom.
     *
     * @return BathroomStatus
     */
    BathroomStatus getStatus();

    /**
     * Updates the bathroom operational status directly.
     *
     * @param status new BathroomStatus
     */
    void setStatus(BathroomStatus status);

    /**
     * Checks if the bathroom is currently operational.
     *
     * @return true if operational, false otherwise
     */
    boolean isOperational();

    /**
     * Checks if this facility has accessibility adaptations for reduced mobility.
     *
     * @return true if accessible, false otherwise
     */
    boolean isAccessible();

    /**
     * Registers a review for this bathroom.
     *
     * @param review the review to add
     */
    void addReview(Review review);

    /**
     * Returns an iterator over all reviews posted for this bathroom.
     *
     * @return iterator of Review
     */
    Iterator<Review> getReviews();

    /**
     * Retrieves the count of reviews for this bathroom.
     *
     * @return review count
     */
    int getReviewCount();

    /**
     * Computes the average star rating (1.0 to 5.0).
     *
     * @return average rating, or 0.0 if unrated
     */
    double getAverageRating();

    /**
     * Adds an alert affecting this bathroom and updates operational status if blocking.
     *
     * @param alert the alert to associate
     */
    void addAlert(Alert alert);

    /**
     * Resolves an active alert and recalculates operational status.
     *
     * @param alertId    the ID of the alert to resolve
     * @param resolverId the user ID of the person resolving it
     * @return true if resolved, false if not found
     */
    boolean resolveAlert(String alertId, String resolverId);

    /**
     * Returns an iterator over active alerts for this bathroom.
     *
     * @return iterator of active Alert
     */
    Iterator<Alert> getActiveAlerts();

    /**
     * Retrieves the count of active alerts affecting this bathroom.
     *
     * @return active alert count
     */
    int getActiveAlertCount();

    /**
     * Recalculates operational status based on remaining active alerts.
     */
    void updateStatusAfterAlertChange();
}
