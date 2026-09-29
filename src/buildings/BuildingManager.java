package buildings;

import bathrooms.Bathroom;
import exceptions.BuildingAlreadyExistsException;
import exceptions.BuildingDoesNotExistException;
import exceptions.NoBathroomsFoundException;

import java.util.Iterator;
import java.util.Map;

/**
 * Manager interface responsible for campus buildings and cross-building bathroom discovery.
 */
public interface BuildingManager {

    /**
     * Registers a new building on the campus.
     *
     * @param code building code (e.g. VII, DI, DM)
     * @param name full building name
     * @throws BuildingAlreadyExistsException if code already registered
     */
    void registerBuilding(String code, String name) throws BuildingAlreadyExistsException;

    /**
     * Retrieves a building by its code or name match.
     *
     * @param code building code or partial name
     * @return the Building instance
     * @throws BuildingDoesNotExistException if not found
     */
    Building getBuilding(String code) throws BuildingDoesNotExistException;

    /**
     * Checks if a building exists.
     *
     * @param code building code
     * @return true if exists
     */
    boolean hasBuilding(String code);

    /**
     * Returns an iterator over all buildings on campus.
     *
     * @return iterator of Building
     */
    Iterator<Building> getBuildings();

    /**
     * Adds a bathroom to the appropriate building.
     *
     * @param bathroom the bathroom to add
     * @throws BuildingDoesNotExistException if the building code is not registered
     */
    void addBathroom(Bathroom bathroom) throws BuildingDoesNotExistException;

    /**
     * Finds a bathroom across all campus buildings by ID.
     *
     * @param id bathroom ID
     * @return Bathroom instance or null
     */
    Bathroom getBathroom(String id);

    /**
     * Checks if a bathroom exists in any building.
     *
     * @param id bathroom ID
     * @return true if exists
     */
    boolean hasBathroom(String id);

    /**
     * Returns an iterator over all bathrooms across all campus buildings.
     *
     * @return iterator of Bathroom
     */
    Iterator<Bathroom> getAllBathrooms();

    /**
     * Returns an iterator over operational bathrooms across all campus buildings.
     *
     * @return iterator of operational Bathroom
     */
    Iterator<Bathroom> getAvailableBathrooms();

    /**
     * Returns an iterator over bathrooms in a specific building.
     *
     * @param buildingCode building code or name
     * @return iterator of Bathroom
     * @throws BuildingDoesNotExistException if building not found
     */
    Iterator<Bathroom> getBathroomsInBuilding(String buildingCode) throws BuildingDoesNotExistException;

    /**
     * Returns an iterator over bathrooms matching a gender/category.
     *
     * @param gender gender or category string
     * @return iterator of matching Bathroom
     */
    Iterator<Bathroom> getBathroomsByGender(String gender);

    /**
     * Returns the highest rated bathrooms on campus, sorted descending by average rating.
     *
     * @param limit maximum count (<= 0 for all)
     * @return iterator of top Bathroom
     */
    Iterator<Bathroom> getTopRatedBathrooms(int limit);

    /**
     * Finds the best operational bathroom in a building matching the desired gender/type.
     *
     * @param buildingCode building code or name
     * @param gender       desired gender/type
     * @return the best Bathroom instance
     * @throws BuildingDoesNotExistException if building not found
     * @throws NoBathroomsFoundException     if no operational matching bathroom exists
     */
    Bathroom findBestBathroom(String buildingCode, String gender)
            throws BuildingDoesNotExistException, NoBathroomsFoundException;

    /**
     * Computes the rating ranking across all campus buildings.
     *
     * @return Map of Building to average rating score
     */
    Map<Building, Double> getBuildingRanking();
}
