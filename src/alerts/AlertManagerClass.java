package alerts;

import exceptions.AlertAlreadyConfirmedException;
import exceptions.AlertAlreadyResolvedException;
import exceptions.AlertNotFoundException;
import users.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores and manages alerts.
 */
public class AlertManagerClass implements AlertManager {

    private final Map<String, Alert> alerts;
    private int sequence;

    public AlertManagerClass() {
        this.alerts = new LinkedHashMap<>();
        this.sequence = 0;
    }

    @Override
    public Alert createAlert(String bathroomId, User reporter, AlertType type, String description) {
        if (reporter == null) {
            throw new IllegalArgumentException();
        }
        if (bathroomId == null || bathroomId.trim().isEmpty()) {
            throw new IllegalArgumentException();
        }

        AlertType alertType = AlertType.MAINTENANCE;
        if (type != null) {
            alertType = type;
        }

        String alertDesc = "";
        if (description != null) {
            alertDesc = description.trim();
        }

        String altId = "ALT-" + (++sequence);
        Alert alert = new AlertClass(
                altId,
                bathroomId.trim().toUpperCase(),
                reporter,
                alertType,
                alertDesc,
                LocalDateTime.now()
        );

        alerts.put(altId.toUpperCase(), alert);
        reporter.incrementAlertCount();
        return alert;
    }

    @Override
    public boolean confirmAlert(String alertId, User user)
            throws AlertNotFoundException, AlertAlreadyConfirmedException {
        if (alertId == null) {
            throw new AlertNotFoundException();
        }
        Alert alert = alerts.get(alertId.trim().toUpperCase());
        if (alert == null) {
            throw new AlertNotFoundException();
        }
        return alert.addConfirmation(user);
    }

    @Override
    public boolean resolveAlert(String alertId, User resolver)
            throws AlertNotFoundException, AlertAlreadyResolvedException {
        if (alertId == null) {
            throw new AlertNotFoundException();
        }
        Alert alert = alerts.get(alertId.trim().toUpperCase());
        if (alert == null) {
            throw new AlertNotFoundException();
        }
        if (!alert.isActive()) {
            throw new AlertAlreadyResolvedException();
        }

        return alert.addResolutionConfirmation(resolver);
    }

    @Override
    public Alert getAlert(String alertId) throws AlertNotFoundException {
        if (alertId == null) {
            throw new AlertNotFoundException();
        }
        Alert alert = alerts.get(alertId.trim().toUpperCase());
        if (alert == null) {
            throw new AlertNotFoundException();
        }
        return alert;
    }

    @Override
    public Iterator<Alert> getActiveAlerts() {
        List<Alert> active = new ArrayList<>();
        for (Alert a : alerts.values()) {
            if (a.isActive()) {
                active.add(a);
            }
        }
        return active.iterator();
    }

    @Override
    public Iterator<Alert> getActiveAlertsForBathroom(String bathroomId) {
        List<Alert> active = new ArrayList<>();
        if (bathroomId == null) {
            return active.iterator();
        }
        String cleanId = bathroomId.trim().toUpperCase();
        for (Alert a : alerts.values()) {
            if (a.getBathroomId().equalsIgnoreCase(cleanId) && a.isActive()) {
                active.add(a);
            }
        }
        return active.iterator();
    }

    @Override
    public int getActiveAlertCount() {
        int count = 0;
        for (Alert a : alerts.values()) {
            if (a.isActive()) {
                count++;
            }
        }
        return count;
    }
}
