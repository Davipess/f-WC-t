package bathrooms;

/**
 * Accessible bathroom implementation.
 */
public class AccessibleBathroomClass extends AbstractBathroomClass implements AccessibleBathroom {

    private final boolean emergencyAlarm;
    private final boolean supportBars;

    /**
     * Constructs an AccessibleBathroomClass with location and accessibility amenities.
     *
     * @param id             unique bathroom ID
     * @param buildingCode   building code
     * @param floor          floor level
     * @param gender         gender or unisex designation
     * @param location       specific location description
     * @param emergencyAlarm presence of emergency cord
     * @param supportBars    presence of support bars
     */
    public AccessibleBathroomClass(String id, String buildingCode, int floor, String gender, String location,
                                   boolean emergencyAlarm, boolean supportBars) {
        super(id, buildingCode, floor, gender, location);
        this.emergencyAlarm = emergencyAlarm;
        this.supportBars = supportBars;
    }

    /**
     * Constructs an AccessibleBathroomClass with accessibility amenities.
     *
     * @param id             unique bathroom ID
     * @param buildingCode   building code
     * @param floor          floor level
     * @param gender         gender or unisex designation
     * @param emergencyAlarm presence of emergency cord
     * @param supportBars    presence of support bars
     */
    public AccessibleBathroomClass(String id, String buildingCode, int floor, String gender,
                                   boolean emergencyAlarm, boolean supportBars) {
        this(id, buildingCode, floor, gender, "", emergencyAlarm, supportBars);
    }

    @Override
    public boolean isAccessible() {
        return true;
    }

    @Override
    public boolean hasEmergencyAlarm() {
        return emergencyAlarm;
    }

    @Override
    public boolean hasSupportBars() {
        return supportBars;
    }
}
