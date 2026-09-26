package bathrooms;

/**
 * Interface representing a specialized bathroom adapted for individuals with reduced mobility.
 */
public interface AccessibleBathroom extends Bathroom {

    /**
     * Checks if an emergency cord/alarm is available in the cabin.
     *
     * @return true if emergency alarm is present
     */
    boolean hasEmergencyAlarm();

    /**
     * Checks if support grab bars are installed.
     *
     * @return true if support bars are present
     */
    boolean hasSupportBars();
}
