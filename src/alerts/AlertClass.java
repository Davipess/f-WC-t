package alerts;

import exceptions.AlertAlreadyConfirmedException;
import users.Role;
import users.User;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Represents an alert reported for a bathroom.
 */
public class AlertClass implements Alert {

    private static final int DEFAULT_THRESHOLD = 2;

    private final String id;
    private final String bathroomId;
    private final String reporterId;
    private final String reporterName;
    private final AlertType type;
    private final String description;
    private final LocalDateTime createdAt;

    private final Set<String> confirmations;
    private final Set<String> resolutionConfirmations;

    private AlertStatus status;
    private boolean confirmed;
    private LocalDateTime resolvedAt;
    private String resolverId;

    /**
     * Constructs an AlertClass instance with consensus tracking.
     *
     * @param id          unique alert ID
     * @param bathroomId  associated bathroom ID
     * @param reporter    user reporting the alert
     * @param type        type of incident
     * @param description details of the issue
     * @param createdAt   creation timestamp
     */
    public AlertClass(String id, String bathroomId, User reporter,
                      AlertType type, String description, LocalDateTime createdAt) {
        this.id = id;
        this.bathroomId = bathroomId;
        this.reporterId = reporter.getId();
        this.reporterName = reporter.getName();
        this.type = type;
        this.description = description;
        this.createdAt = createdAt;
        this.status = AlertStatus.ACTIVE;
        this.resolvedAt = null;
        this.resolverId = null;

        this.confirmations = new LinkedHashSet<>();
        this.resolutionConfirmations = new LinkedHashSet<>();

        this.confirmations.add(reporter.getId());

        // Admin alerts are confirmed right away
        if (reporter.getRole() == Role.ADMIN) {
            this.confirmed = true;
        } else {
            this.confirmed = confirmations.size() >= DEFAULT_THRESHOLD;
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getBathroomId() {
        return bathroomId;
    }

    @Override
    public String getReporterId() {
        return reporterId;
    }

    @Override
    public String getReporterName() {
        return reporterName;
    }

    @Override
    public AlertType getType() {
        return type;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public AlertStatus getStatus() {
        return status;
    }

    @Override
    public boolean isActive() {
        return status == AlertStatus.ACTIVE;
    }

    @Override
    public boolean isConfirmed() {
        return confirmed;
    }

    @Override
    public int getConfirmationCount() {
        return confirmations.size();
    }

    @Override
    public int getRequiredConfirmations() {
        return DEFAULT_THRESHOLD;
    }

    @Override
    public boolean addConfirmation(User user) throws AlertAlreadyConfirmedException {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        if (!isActive()) {
            return false;
        }
        if (confirmations.contains(user.getId())) {
            throw new AlertAlreadyConfirmedException();
        }

        confirmations.add(user.getId());

        if (!confirmed) {
            if (user.getRole() == Role.ADMIN ||
                confirmations.size() >= DEFAULT_THRESHOLD) {
                this.confirmed = true;
                return true; // Reached confirmation threshold
            }
        }
        return false;
    }

    @Override
    public int getResolutionConfirmationCount() {
        return resolutionConfirmations.size();
    }

    @Override
    public int getRequiredResolutionConfirmations() {
        return DEFAULT_THRESHOLD;
    }

    @Override
    public boolean addResolutionConfirmation(User user) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        if (!isActive()) {
            return false;
        }

        resolutionConfirmations.add(user.getId());

        // Admin resolves immediately, or we need 2 student votes
        if (user.getRole() == Role.ADMIN ||
            resolutionConfirmations.size() >= DEFAULT_THRESHOLD) {
            this.status = AlertStatus.RESOLVED;
            this.resolvedAt = LocalDateTime.now();
            this.resolverId = user.getId();
            return true;
        }

        return false;
    }

    @Override
    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    @Override
    public String getResolverId() {
        return resolverId;
    }
}
