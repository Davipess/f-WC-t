import alerts.Alert;
import bathrooms.Bathroom;
import buildings.Building;
import exceptions.*;
import reviews.Review;
import users.User;

import java.util.Iterator;
import java.util.Map;

/**
 * Main system interface for the fWCt platform.
 * Connects console commands to user, building, review, and alert operations.
 */
public interface FWCt {

    // User and session operations

    /**
     * Registers a new user with credentials and password.
     *
     * @param id       unique user ID
     * @param name     full display name
     * @param roleStr  role designation string
     * @param password secret password
     * @throws UserAlreadyExistsException if ID is already registered
     */
    void registerUser(String id, String name, String roleStr, String password) throws UserAlreadyExistsException;

    /**
     * Registers a new user with default password (1234).
     *
     * @param id      unique user ID (e.g. student number)
     * @param name    full display name
     * @param roleStr role designation string (STUDENT, ADMIN)
     * @throws UserAlreadyExistsException if ID is already registered
     */
    void registerUser(String id, String name, String roleStr) throws UserAlreadyExistsException;

    /**
     * Authenticates and starts a session for the user verifying password.
     *
     * @param id       the user ID
     * @param password the password
     * @throws UserDoesNotExistException if user not found
     * @throws InvalidPasswordException  if password does not match
     */
    void login(String id, String password) throws UserDoesNotExistException, InvalidPasswordException;

    /**
     * Authenticates and starts a session for the user with the given ID.
     *
     * @param id the user ID
     * @throws UserDoesNotExistException if user not found
     */
    void login(String id) throws UserDoesNotExistException;

    /**
     * Clears the current active session.
     *
     * @throws NoActiveUserException if no user is logged in
     */
    void logout() throws NoActiveUserException;

    /**
     * Retrieves the currently authenticated user.
     *
     * @return current User or null
     */
    User getCurrentUser();

    /**
     * Checks if a user is currently logged in.
     *
     * @return true if logged in, false otherwise
     */
    boolean hasActiveUser();

    /**
     * Checks if a user with the specified ID exists.
     *
     * @param id user ID
     * @return true if exists, false otherwise
     */
    boolean hasUser(String id);

    /**
     * Checks if a bathroom with the specified ID exists in the catalog.
     *
     * @param id bathroom ID
     * @return true if exists, false otherwise
     */
    boolean hasBathroom(String id);

    /**
     * Checks if a building with the specified code exists on campus.
     *
     * @param code building code
     * @return true if exists, false otherwise
     */
    boolean hasBuilding(String code);

    /**
     * Returns an iterator over all registered users.
     *
     * @return iterator of User
     */
    Iterator<User> listUsers();


    // Building and bathroom operations

    /**
     * Registers a physical building on the campus.
     *
     * @param code building acronym or code (e.g. VII, DI, DM)
     * @param name full building name
     * @throws BuildingAlreadyExistsException if building code already registered
     */
    void registerBuilding(String code, String name) throws BuildingAlreadyExistsException;

    /**
     * Retrieves a building by its code.
     *
     * @param code building code
     * @return Building instance
     * @throws BuildingDoesNotExistException if not found
     */
    Building getBuilding(String code) throws BuildingDoesNotExistException;

    /**
     * Returns an iterator over all registered buildings.
     *
     * @return iterator of Building
     */
    Iterator<Building> listBuildings();

    /**
     * Registers a standard bathroom in a building with location details.
     *
     * @param id           unique bathroom ID
     * @param buildingCode building where located
     * @param floor        floor level
     * @param gender       gender designation (MALE, FEMALE, UNISEX)
     * @param location     specific location description (e.g. Atrio de Entrada)
     * @throws BathroomAlreadyExistsException if ID already exists
     * @throws BuildingDoesNotExistException  if building does not exist
     */
    void addStandardBathroom(String id, String buildingCode, int floor, String gender, String location)
            throws BathroomAlreadyExistsException, BuildingDoesNotExistException;

    /**
     * Registers a standard bathroom in a building.
     *
     * @param id           unique bathroom ID
     * @param buildingCode building where located
     * @param floor        floor level
     * @param gender       gender designation (MALE, FEMALE, UNISEX)
     * @throws BathroomAlreadyExistsException if ID already exists
     * @throws BuildingDoesNotExistException  if building does not exist
     */
    void addStandardBathroom(String id, String buildingCode, int floor, String gender)
            throws BathroomAlreadyExistsException, BuildingDoesNotExistException;

    /**
     * Registers an accessible bathroom with location details in a building.
     *
     * @param id             unique bathroom ID
     * @param buildingCode   building where located
     * @param floor          floor level
     * @param gender         gender or unisex designation
     * @param location       specific location description
     * @param emergencyAlarm whether emergency alarm cord is present
     * @param supportBars    whether support grab bars are installed
     * @throws BathroomAlreadyExistsException if ID already exists
     * @throws BuildingDoesNotExistException  if building does not exist
     */
    void addAccessibleBathroom(String id, String buildingCode, int floor, String gender, String location,
                               boolean emergencyAlarm, boolean supportBars)
            throws BathroomAlreadyExistsException, BuildingDoesNotExistException;

    /**
     * Registers an accessible bathroom adapted for reduced mobility in a building.
     *
     * @param id             unique bathroom ID
     * @param buildingCode   building where located
     * @param floor          floor level
     * @param gender         gender or unisex designation
     * @param emergencyAlarm whether emergency alarm cord is present
     * @param supportBars    whether support grab bars are installed
     * @throws BathroomAlreadyExistsException if ID already exists
     * @throws BuildingDoesNotExistException  if building does not exist
     */
    void addAccessibleBathroom(String id, String buildingCode, int floor, String gender,
                               boolean emergencyAlarm, boolean supportBars)
            throws BathroomAlreadyExistsException, BuildingDoesNotExistException;

    /**
     * Retrieves a bathroom across all buildings by ID.
     *
     * @param id bathroom ID
     * @return Bathroom instance
     * @throws BathroomDoesNotExistException if not found
     */
    Bathroom getBathroom(String id) throws BathroomDoesNotExistException;

    /**
     * Returns an iterator over all campus bathrooms.
     *
     * @return iterator of Bathroom
     */
    Iterator<Bathroom> listAllBathrooms();

    /**
     * Returns an iterator over currently operational (available) bathrooms.
     *
     * @return iterator of operational Bathroom
     */
    Iterator<Bathroom> listAvailableBathrooms();

    /**
     * Finds bathrooms within a specific building.
     *
     * @param buildingCode building code or name
     * @return iterator of Bathroom
     * @throws BuildingDoesNotExistException if building not found
     * @throws NoBathroomsFoundException     if building has no bathrooms
     */
    Iterator<Bathroom> findBathroomsByBuilding(String buildingCode)
            throws BuildingDoesNotExistException, NoBathroomsFoundException;

    /**
     * Finds bathrooms by gender or category.
     *
     * @param gender gender or category string
     * @return iterator of Bathroom
     * @throws NoBathroomsFoundException if none found
     */
    Iterator<Bathroom> findBathroomsByGender(String gender) throws NoBathroomsFoundException;

    /**
     * Finds the best operational bathroom in a specific building matching the user criteria.
     *
     * @param buildingCode building code or name
     * @param gender       desired gender/type
     * @return best Bathroom instance
     * @throws BuildingDoesNotExistException if building not found
     * @throws NoBathroomsFoundException     if no operational matching bathroom exists
     */
    Bathroom findBestBathroom(String buildingCode, String gender)
            throws BuildingDoesNotExistException, NoBathroomsFoundException;

    /**
     * Returns the highest-rated bathrooms on campus, sorted descending by average stars.
     *
     * @param limit maximum count
     * @return iterator of top Bathroom
     */
    Iterator<Bathroom> getTopRatedBathrooms(int limit);

    /**
     * Calculates the rating ranking across all buildings.
     *
     * @return Map of Building to average rating score
     */
    Map<Building, Double> getBuildingRanking();


    // Review and rating operations

    /**
     * Submits a rating and commentary for a bathroom from the currently logged in user.
     * Prevents review-bombing: only 1 review allowed per user per bathroom in each contest period.
     *
     * @param bathroomId target bathroom ID
     * @param rating     star rating (0 to 5)
     * @param comment    review commentary
     * @return the created Review instance
     * @throws NoActiveUserException             if no user is logged in
     * @throws BathroomDoesNotExistException     if bathroom does not exist
     * @throws InvalidRatingException            if ratings are not in range 0..5
     * @throws DuplicateReviewInContestException if user already reviewed in this contest cycle
     */
    Review addReview(String bathroomId, int rating, String comment)
            throws NoActiveUserException, BathroomDoesNotExistException, InvalidRatingException,
                   DuplicateReviewInContestException;

    /**
     * Retrieves all reviews recorded for a specific bathroom.
     *
     * @param bathroomId target bathroom ID
     * @return iterator of Review
     * @throws BathroomDoesNotExistException if bathroom not found
     */
    Iterator<Review> getBathroomReviews(String bathroomId) throws BathroomDoesNotExistException;

    /**
     * Retrieves the current contest week number.
     *
     * @return active contest week
     */
    int getCurrentContestWeek();

    /**
     * Advances the contest cycle to the next week.
     */
    void advanceContestWeek();


    // Alert and consensus operations

    /**
     * Reports an alert/incident on a bathroom from the currently logged in user.
     * If user is Staff/Admin, activates immediately; if Student, records initial report (1/2 votes).
     *
     * @param bathroomId   target bathroom ID
     * @param alertTypeStr type of incident
     * @param description  details of the issue
     * @return the created Alert instance
     * @throws NoActiveUserException         if no user is logged in
     * @throws BathroomDoesNotExistException if bathroom does not exist
     */
    Alert addAlert(String bathroomId, String alertTypeStr, String description)
            throws NoActiveUserException, BathroomDoesNotExistException;

    /**
     * Confirms an active alert by another student.
     *
     * @param alertId ID of the alert to confirm
     * @return true if alert reached confirmation threshold and officially activated
     * @throws NoActiveUserException          if no user is logged in
     * @throws AlertNotFoundException         if alert not found
     * @throws AlertAlreadyConfirmedException if user already confirmed this alert
     */
    boolean confirmAlert(String alertId)
            throws NoActiveUserException, AlertNotFoundException, AlertAlreadyConfirmedException;

    /**
     * Submits a resolution confirmation indicating the incident has ended.
     * If user is Staff/Admin or threshold is reached, reopens the facility.
     *
     * @param alertId ID of the alert to resolve
     * @return true if resolution threshold was met and alert is officially resolved
     * @throws NoActiveUserException         if no user is logged in
     * @throws AlertNotFoundException        if alert not found
     * @throws AlertAlreadyResolvedException if alert was already resolved
     */
    boolean resolveAlert(String alertId)
            throws NoActiveUserException, AlertNotFoundException, AlertAlreadyResolvedException;

    /**
     * Returns an iterator over all active alerts across the entire campus.
     *
     * @return iterator of active Alert
     */
    Iterator<Alert> listActiveAlerts();

    /**
     * Returns an iterator over active alerts affecting a specific bathroom.
     *
     * @param bathroomId bathroom ID
     * @return iterator of active Alert
     * @throws BathroomDoesNotExistException if bathroom not found
     */
    Iterator<Alert> listBathroomActiveAlerts(String bathroomId)
            throws BathroomDoesNotExistException;
}
