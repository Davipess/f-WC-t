package bathrooms;

import alerts.Alert;
import alerts.AlertType;
import reviews.Review;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Base class containing shared logic for all bathroom types.
 */
public abstract class AbstractBathroomClass implements Bathroom {

    private final String id;
    private final String buildingCode;
    private final int floor;
    private final String gender;
    private String location;
    private BathroomStatus status;

    private final List<Review> reviews;
    private final List<Alert> alerts;

    /**
     * Base constructor for a bathroom facility with location details.
     *
     * @param id           unique bathroom identifier
     * @param buildingCode building where located
     * @param floor        floor level
     * @param gender       gender or category designation
     * @param location     specific location description
     */
    public AbstractBathroomClass(String id, String buildingCode, int floor, String gender, String location) {
        this.id = id;
        this.buildingCode = buildingCode;
        this.floor = floor;
        this.gender = gender;
        if (location != null) {
            this.location = location.trim();
        } else {
            this.location = "";
        }
        this.status = BathroomStatus.AVAILABLE;
        this.reviews = new ArrayList<>();
        this.alerts = new ArrayList<>();
    }

    /**
     * Base constructor for a bathroom facility.
     *
     * @param id           unique bathroom identifier
     * @param buildingCode building where located
     * @param floor        floor level
     * @param gender       gender or category designation
     */
    public AbstractBathroomClass(String id, String buildingCode, int floor, String gender) {
        this(id, buildingCode, floor, gender, "");
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getBuildingCode() {
        return buildingCode;
    }

    @Override
    public int getFloor() {
        return floor;
    }

    @Override
    public String getGender() {
        return gender;
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public void setLocation(String location) {
        if (location != null) {
            this.location = location.trim();
        } else {
            this.location = "";
        }
    }

    @Override
    public BathroomStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(BathroomStatus status) {
        this.status = status;
    }

    @Override
    public boolean isOperational() {
        return status.isOperational();
    }

    @Override
    public void addReview(Review review) {
        reviews.add(review);
    }

    @Override
    public Iterator<Review> getReviews() {
        return reviews.iterator();
    }

    @Override
    public int getReviewCount() {
        return reviews.size();
    }

    @Override
    public double getAverageRating() {
        if (reviews.isEmpty()) {
            return 0.0;
        }
        int total = 0;
        for (Review r : reviews) {
            total += r.getRating();
        }
        return (double) total / reviews.size();
    }

    @Override
    public void addAlert(Alert alert) {
        alerts.add(alert);
        updateStatusAfterAlertChange();
    }

    @Override
    public boolean resolveAlert(String alertId, String resolverId) {
        for (Alert a : alerts) {
            if (a.getId().equalsIgnoreCase(alertId)) {
                updateStatusAfterAlertChange();
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<Alert> getActiveAlerts() {
        List<Alert> active = new ArrayList<>();
        for (Alert a : alerts) {
            if (a.isActive()) {
                active.add(a);
            }
        }
        return active.iterator();
    }

    @Override
    public int getActiveAlertCount() {
        int count = 0;
        for (Alert a : alerts) {
            if (a.isActive()) {
                count++;
            }
        }
        return count;
    }

    @Override
    public void updateStatusAfterAlertChange() {
        for (Alert a : alerts) {
            if (a.isActive() && a.isConfirmed() && a.getType().isBlocking()) {
                if (a.getType() == AlertType.CLEANING) {
                    this.status = BathroomStatus.CLEANING;
                } else {
                    this.status = BathroomStatus.MAINTENANCE;
                }
                return;
            }
        }
        this.status = BathroomStatus.AVAILABLE;
    }
}
