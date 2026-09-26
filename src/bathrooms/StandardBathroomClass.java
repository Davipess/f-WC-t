package bathrooms;

/**
 * Standard campus bathroom implementation.
 */
public class StandardBathroomClass extends AbstractBathroomClass implements StandardBathroom {

    /**
     * Constructs a new StandardBathroomClass with location details.
     *
     * @param id           unique bathroom ID
     * @param buildingCode building where located
     * @param floor        floor level
     * @param gender       gender or category designation
     * @param location     specific location description
     */
    public StandardBathroomClass(String id, String buildingCode, int floor, String gender, String location) {
        super(id, buildingCode, floor, gender, location);
    }

    /**
     * Constructs a new StandardBathroomClass.
     *
     * @param id           unique bathroom ID
     * @param buildingCode building where located
     * @param floor        floor level
     * @param gender       gender or category designation
     */
    public StandardBathroomClass(String id, String buildingCode, int floor, String gender) {
        super(id, buildingCode, floor, gender);
    }

    @Override
    public boolean isAccessible() {
        return false;
    }
}
