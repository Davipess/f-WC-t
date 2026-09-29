package alerts;

import exceptions.AlertAlreadyConfirmedException;
import exceptions.AlertAlreadyResolvedException;
import exceptions.AlertNotFoundException;
import users.User;

import java.util.Iterator;

/**
 * Manager interface responsible for tracking, creating, confirming, and resolving campus alerts.
 */
public interface AlertManager {

    /**
     * Creates and registers a new alert.
     *
     * @param bathroomId  the ID of the bathroom affected
     * @param reporter    the user who reported the incident
     * @param type        the type of alert
     * @param description description of the issue
     * @return the created Alert instance
     */
    Alert createAlert(String bathroomId, User reporter, AlertType type, String description);

    /**
     * Confirms an existing alert by another user.
     *
     * @param alertId the ID of the alert
     * @param user    the user confirming the incident
     * @return true if this confirmation met the threshold and confirmed the alert
     * @throws AlertNotFoundException         if alert not found
     * @throws AlertAlreadyConfirmedException if user already confirmed
     */
    boolean confirmAlert(String alertId, User user)
            throws AlertNotFoundException, AlertAlreadyConfirmedException;

    /**
     * Adds a resolution confirmation indicating the incident has ended.
     *
     * @param alertId  the ID of the alert to resolve
     * @param resolver the user confirming the resolution
     * @return true if resolution threshold was met and alert is officially resolved
     * @throws AlertNotFoundException        if the alert ID does not exist
     * @throws AlertAlreadyResolvedException if the alert is already resolved
     */
    boolean resolveAlert(String alertId, User resolver)
            throws AlertNotFoundException, AlertAlreadyResolvedException;

    /**
     * Retrieves an alert by its unique identifier.
     *
     * @param alertId the ID of the alert
     * @return the Alert instance
     * @throws AlertNotFoundException if not found
     */
    Alert getAlert(String alertId) throws AlertNotFoundException;

    /**
     * Returns an iterator over all active alerts across campus.
     *
     * @return iterator of active Alert instances
     */
    Iterator<Alert> getActiveAlerts();

    /**
     * Returns an iterator over active alerts for a specific bathroom.
     *
     * @param bathroomId the bathroom ID
     * @return iterator of active Alert instances
     */
    Iterator<Alert> getActiveAlertsForBathroom(String bathroomId);

    /**
     * Retrieves the count of currently active alerts.
     *
     * @return active alert count
     */
    int getActiveAlertCount();
}
