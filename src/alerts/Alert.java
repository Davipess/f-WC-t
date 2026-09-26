package alerts;

import exceptions.AlertAlreadyConfirmedException;
import users.User;

import java.time.LocalDateTime;

/**
 * Interface representing a real-time alert for a campus bathroom.
 * Implements community consensus logic to prevent false alarms.
 */
public interface Alert {

    /**
     * Retrieves the unique identifier of the alert.
     *
     * @return alert ID
     */
    String getId();

    /**
     * Retrieves the bathroom ID associated with this alert.
     *
     * @return bathroom ID
     */
    String getBathroomId();

    /**
     * Retrieves the user ID of the initial reporter.
     *
     * @return reporter ID
     */
    String getReporterId();

    /**
     * Retrieves the display name of the reporter.
     *
     * @return reporter name
     */
    String getReporterName();

    /**
     * Retrieves the type/category of the alert.
     *
     * @return AlertType
     */
    AlertType getType();

    /**
     * Retrieves the description or details of the problem.
     *
     * @return description
     */
    String getDescription();

    /**
     * Retrieves the creation timestamp of the alert.
     *
     * @return creation timestamp
     */
    LocalDateTime getCreatedAt();

    /**
     * Retrieves the current lifecycle status (ACTIVE, RESOLVED).
     *
     * @return AlertStatus
     */
    AlertStatus getStatus();

    /**
     * Checks if this alert is currently active (not yet resolved).
     *
     * @return true if active, false otherwise
     */
    boolean isActive();

    /**
     * Checks if this alert has reached the necessary community threshold to be confirmed.
     *
     * @return true if confirmed by staff or community consensus
     */
    boolean isConfirmed();

    /**
     * Retrieves current number of user confirmations.
     *
     * @return confirmation count
     */
    int getConfirmationCount();

    /**
     * Retrieves the required number of confirmations to validate this alert.
     *
     * @return required confirmation count
     */
    int getRequiredConfirmations();

    /**
     * Adds a community confirmation from a user.
     *
     * @param user the confirming user
     * @return true if this confirmation activated/confirmed the alert
     * @throws AlertAlreadyConfirmedException if user already confirmed
     */
    boolean addConfirmation(User user) throws AlertAlreadyConfirmedException;

    /**
     * Retrieves current number of resolution votes confirming that the issue has ended.
     *
     * @return resolution confirmation count
     */
    int getResolutionConfirmationCount();

    /**
     * Retrieves the required number of resolution confirmations to reopen the facility.
     *
     * @return required resolution confirmation count
     */
    int getRequiredResolutionConfirmations();

    /**
     * Adds a resolution confirmation from a user confirming the issue has ended.
     *
     * @param user the resolving user
     * @return true if resolution threshold was met and alert is officially resolved
     */
    boolean addResolutionConfirmation(User user);

    /**
     * Retrieves the timestamp when this alert was marked as resolved.
     *
     * @return resolution timestamp or null if still active
     */
    LocalDateTime getResolvedAt();

    /**
     * Retrieves the user ID of the person who finalized the resolution.
     *
     * @return resolver user ID or null
     */
    String getResolverId();
}
