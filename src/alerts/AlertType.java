package alerts;

/**
 * Categories of alerts that can be reported for a bathroom.
 */
public enum AlertType {
    CLEANING(true),
    MAINTENANCE(true);

    private final boolean blocking;

    AlertType(boolean blocking) {
        this.blocking = blocking;
    }

    /**
     * Checks if this alert type makes the bathroom unusable.
     *
     * @return true if the alert causes the bathroom to be blocked
     */
    public boolean isBlocking() {
        return blocking;
    }

    /**
     * Parses an AlertType from a string representation.
     *
     * @param text input string to match
     * @return matching AlertType or MAINTENANCE by default
     */
    public static AlertType fromString(String text) {
        if (text == null) {
            return MAINTENANCE;
        }
        String clean = text.trim().toUpperCase();
        for (AlertType a : AlertType.values()) {
            if (a.name().equalsIgnoreCase(clean)) {
                return a;
            }
        }
        if (clean.contains("LIMP") || clean.contains("CLEAN")) {
            return CLEANING;
        }
        return MAINTENANCE;
    }
}
