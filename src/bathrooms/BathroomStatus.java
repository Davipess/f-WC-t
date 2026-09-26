package bathrooms;

/**
 * Operational states of a bathroom.
 */
public enum BathroomStatus {
    AVAILABLE,
    CLEANING,
    MAINTENANCE;

    /**
     * Returns true if the bathroom is open for use.
     * @return true if operational, false otherwise
     */
    public boolean isOperational() {
        return this == AVAILABLE;
    }
}
