package buildings;

import bathrooms.Bathroom;

import java.util.Iterator;

/**
 * Interface representing a physical building on the campus.
 * Each building encapsulates its own set of bathroom facilities.
 */
public interface Building {

    /**
     * Retrieves the short building code (e.g. VII, DI, DM, II, MACE).
     *
     * @return building code
     */
    String getCode();

    /**
     * Retrieves the official display name of the building.
     *
     * @return building name
     */
    String getName();

    /**
     * Adds a bathroom facility to this building.
     *
     * @param bathroom the bathroom to add
     */
    void addBathroom(Bathroom bathroom);

    /**
     * Checks if a bathroom with the given ID exists in this building.
     *
     * @param id bathroom ID
     * @return true if found, false otherwise
     */
    boolean hasBathroom(String id);

    /**
     * Retrieves a bathroom in this building by ID.
     *
     * @param id bathroom ID
     * @return the Bathroom instance or null
     */
    Bathroom getBathroom(String id);

    /**
     * Returns an iterator over all bathrooms in this building.
     *
     * @return iterator of Bathroom
     */
    Iterator<Bathroom> getBathrooms();

    /**
     * Retrieves the count of bathrooms in this building.
     *
     * @return bathroom count
     */
    int getBathroomCount();

    /**
     * Computes the average rating across all rated bathrooms in this building.
     *
     * @return average rating score, or 0.0 if unrated
     */
    double getAverageRating();

    /**
     * Finds the best operational bathroom in this building matching the desired gender/type.
     * Evaluates highest star rating and operational availability.
     *
     * @param gender requested gender/type (e.g. MALE, FEMALE, UNISEX, ACCESSIBLE)
     * @return highest-rated operational Bathroom, or null if none available
     */
    Bathroom getBestBathroom(String gender);
}
