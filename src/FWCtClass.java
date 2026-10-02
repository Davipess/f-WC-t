import alerts.*;
import auth.*;
import bathrooms.*;
import buildings.*;
import exceptions.*;
import reviews.*;
import users.*;

import java.util.Iterator;
import java.util.Map;

/**
 * Main system class implementing the FWCt facade interface.
 * Delegates operations to dedicated managers for users, buildings, reviews, and alerts.
 */
public class FWCtClass implements FWCt {

    private final UserManager userManager;
    private final SessionManager sessionManager;
    private final BuildingManager buildingManager;
    private final AlertManager alertManager;
    private final ReviewManager reviewManager;

    /**
     * Creates the system managers and seeds initial campus data.
     */
    public FWCtClass() {
        this.userManager = new UserManagerClass();
        this.sessionManager = new SessionManagerClass();
        this.buildingManager = new BuildingManagerClass();
        this.alertManager = new AlertManagerClass();
        this.reviewManager = new ReviewManagerClass();

        seedDefaultCampusData();
    }

    /**
     * Seeds default buildings, bathrooms, and test users.
     */
    private void seedDefaultCampusData() {
        try {
            // Seed campus buildings
            buildingManager.registerBuilding("VII", "Edificio VII");
            buildingManager.registerBuilding("DI", "Departamento de Informatica");
            buildingManager.registerBuilding("DM", "Departamento de Matematica");
            buildingManager.registerBuilding("DF", "Departamento de Fisica");
            buildingManager.registerBuilding("BIB", "Biblioteca");
            buildingManager.registerBuilding("II", "Edificio II");
            buildingManager.registerBuilding("MACE", "Edificio MACE");
            buildingManager.registerBuilding("CANTINA", "Cantina Central");

            // Seed initial accounts
            userManager.registerUser("david", "David Figueiredo", Role.STUDENT, "1234");
            userManager.registerUser("maria", "Maria Santos", Role.STUDENT, "1234");
            userManager.registerUser("bernardo", "Bernardo Silva", Role.STUDENT, "1234");
            userManager.registerUser("admin", "Campus Facilities Admin", Role.ADMIN, "admin");

            // Seed bathrooms in Departamento de Informatica (differentiated bathrooms on floor 0)
            buildingManager.addBathroom(new StandardBathroomClass("DI-0-M1", "DI", 0, "MALE", "Atrio de Entrada"));
            buildingManager.addBathroom(new StandardBathroomClass("DI-0-M2", "DI", 0, "MALE", "Corredor dos Laboratorios"));
            buildingManager.addBathroom(new StandardBathroomClass("DI-0-F1", "DI", 0, "FEMALE", "Atrio de Entrada"));
            buildingManager.addBathroom(new StandardBathroomClass("DI-0-F2", "DI", 0, "FEMALE", "Corredor dos Laboratorios"));
            buildingManager.addBathroom(new StandardBathroomClass("DI-1-U", "DI", 1, "UNISEX", "Ala dos Gabinetes"));

            // Seed bathrooms in Edificio VII
            buildingManager.addBathroom(new StandardBathroomClass("VII-0-M", "VII", 0, "MALE", "Atrio Principal"));
            buildingManager.addBathroom(new StandardBathroomClass("VII-0-F", "VII", 0, "FEMALE", "Atrio Principal"));
            buildingManager.addBathroom(new StandardBathroomClass("VII-1-M", "VII", 1, "MALE", "Ala Norte"));
            buildingManager.addBathroom(new StandardBathroomClass("VII-1-F", "VII", 1, "FEMALE", "Ala Norte"));
            buildingManager.addBathroom(new AccessibleBathroomClass("VII-1-A", "VII", 1, "ACCESSIBLE", "Junto ao Elevador", true, true));

            // Seed bathrooms in Biblioteca
            buildingManager.addBathroom(new StandardBathroomClass("BIB-0-M", "BIB", 0, "MALE", "Hall de Entrada"));
            buildingManager.addBathroom(new StandardBathroomClass("BIB-0-F", "BIB", 0, "FEMALE", "Hall de Entrada"));
            buildingManager.addBathroom(new AccessibleBathroomClass("BIB-1-A", "BIB", 1, "ACCESSIBLE", "Sala de Estudo", true, true));

            // Seed bathrooms in Edificio II
            buildingManager.addBathroom(new StandardBathroomClass("II-1-M", "II", 1, "MALE", "Corredor Central"));
            buildingManager.addBathroom(new StandardBathroomClass("II-1-F", "II", 1, "FEMALE", "Corredor Central"));

            // Seed bathrooms in MACE and Cantina
            buildingManager.addBathroom(new StandardBathroomClass("MACE-0-U", "MACE", 0, "UNISEX", "Piso Terreo"));
            buildingManager.addBathroom(new StandardBathroomClass("CANT-0-M", "CANTINA", 0, "MALE", "Hall da Cantina"));
            buildingManager.addBathroom(new StandardBathroomClass("CANT-0-F", "CANTINA", 0, "FEMALE", "Hall da Cantina"));

            // Seed bathrooms in Departamento de Matematica and Departamento de Fisica
            buildingManager.addBathroom(new StandardBathroomClass("DM-0-M", "DM", 0, "MALE", "Piso Terreo"));
            buildingManager.addBathroom(new StandardBathroomClass("DM-0-F", "DM", 0, "FEMALE", "Piso Terreo"));
            buildingManager.addBathroom(new StandardBathroomClass("DF-0-M", "DF", 0, "MALE", "Piso Terreo"));
            buildingManager.addBathroom(new StandardBathroomClass("DF-0-F", "DF", 0, "FEMALE", "Piso Terreo"));

            // Seed initial reviews to provide realistic baseline ratings and verified supplies
            User userDavid = userManager.getUser("david");
            User userMaria = userManager.getUser("maria");

            Review rev1 = reviewManager.createReview("DI-0-M1", userDavid, 5, "Excellent and very clean.");
            buildingManager.getBathroom("DI-0-M1").addReview(rev1);

            Review rev2 = reviewManager.createReview("DI-0-M2", userMaria, 4, "Good option, but longer lines during class breaks.");
            buildingManager.getBathroom("DI-0-M2").addReview(rev2);

            Review rev3 = reviewManager.createReview("VII-0-M", userDavid, 4, "Clean and functional.");
            buildingManager.getBathroom("VII-0-M").addReview(rev3);

        } catch (Exception ignored) {
            // Ignore pre-seeding errors
        }
    }

    // User and session operations

    @Override
    public void registerUser(String id, String name, String roleStr, String password) throws UserAlreadyExistsException {
        Role role = Role.fromString(roleStr);
        userManager.registerUser(id, name, role, password);
    }

    @Override
    public void registerUser(String id, String name, String roleStr) throws UserAlreadyExistsException {
        registerUser(id, name, roleStr, "1234");
    }

    @Override
    public void login(String id, String password) throws UserDoesNotExistException, InvalidPasswordException {
        User user = userManager.getUser(id);
        if (!user.validatePassword(password)) {
            throw new InvalidPasswordException();
        }
        sessionManager.login(user);
    }

    @Override
    public void login(String id) throws UserDoesNotExistException {
        User user = userManager.getUser(id);
        sessionManager.login(user);
    }

    @Override
    public void logout() throws NoActiveUserException {
        sessionManager.logout();
    }

    @Override
    public User getCurrentUser() {
        return sessionManager.getCurrentUser();
    }

    @Override
    public boolean hasActiveUser() {
        return sessionManager.hasActiveUser();
    }

    @Override
    public boolean hasUser(String id) {
        return userManager.hasUser(id);
    }

    @Override
    public boolean hasBathroom(String id) {
        return buildingManager.hasBathroom(id);
    }

    @Override
    public boolean hasBuilding(String code) {
        return buildingManager.hasBuilding(code);
    }

    @Override
    public Iterator<User> listUsers() {
        return userManager.getUsers();
    }

    // Building and bathroom operations

    @Override
    public void registerBuilding(String code, String name) throws BuildingAlreadyExistsException {
        buildingManager.registerBuilding(code, name);
    }

    @Override
    public Building getBuilding(String code) throws BuildingDoesNotExistException {
        return buildingManager.getBuilding(code);
    }

    @Override
    public Iterator<Building> listBuildings() {
        return buildingManager.getBuildings();
    }

    @Override
    public void addStandardBathroom(String id, String buildingCode, int floor, String gender, String location)
            throws BathroomAlreadyExistsException, BuildingDoesNotExistException {
        if (buildingManager.hasBathroom(id)) {
            throw new BathroomAlreadyExistsException();
        }
        Bathroom b = new StandardBathroomClass(id, buildingCode, floor, gender, location);
        buildingManager.addBathroom(b);
    }

    @Override
    public void addStandardBathroom(String id, String buildingCode, int floor, String gender)
            throws BathroomAlreadyExistsException, BuildingDoesNotExistException {
        addStandardBathroom(id, buildingCode, floor, gender, "");
    }

    @Override
    public void addAccessibleBathroom(String id, String buildingCode, int floor, String gender, String location,
                                       boolean emergencyAlarm, boolean supportBars)
            throws BathroomAlreadyExistsException, BuildingDoesNotExistException {
        if (buildingManager.hasBathroom(id)) {
            throw new BathroomAlreadyExistsException();
        }
        Bathroom b = new AccessibleBathroomClass(id, buildingCode, floor, gender, location, emergencyAlarm, supportBars);
        buildingManager.addBathroom(b);
    }

    @Override
    public void addAccessibleBathroom(String id, String buildingCode, int floor, String gender,
                                       boolean emergencyAlarm, boolean supportBars)
            throws BathroomAlreadyExistsException, BuildingDoesNotExistException {
        addAccessibleBathroom(id, buildingCode, floor, gender, "", emergencyAlarm, supportBars);
    }

    @Override
    public Bathroom getBathroom(String id) throws BathroomDoesNotExistException {
        Bathroom b = buildingManager.getBathroom(id);
        if (b == null) {
            throw new BathroomDoesNotExistException();
        }
        return b;
    }

    @Override
    public Iterator<Bathroom> listAllBathrooms() {
        return buildingManager.getAllBathrooms();
    }

    @Override
    public Iterator<Bathroom> listAvailableBathrooms() {
        return buildingManager.getAvailableBathrooms();
    }

    @Override
    public Iterator<Bathroom> findBathroomsByBuilding(String buildingCode)
            throws BuildingDoesNotExistException, NoBathroomsFoundException {
        Iterator<Bathroom> it = buildingManager.getBathroomsInBuilding(buildingCode);
        if (!it.hasNext()) {
            throw new NoBathroomsFoundException();
        }
        return it;
    }

    @Override
    public Iterator<Bathroom> findBathroomsByGender(String gender) throws NoBathroomsFoundException {
        Iterator<Bathroom> it = buildingManager.getBathroomsByGender(gender);
        if (!it.hasNext()) {
            throw new NoBathroomsFoundException();
        }
        return it;
    }

    @Override
    public Bathroom findBestBathroom(String buildingCode, String gender)
            throws BuildingDoesNotExistException, NoBathroomsFoundException {
        return buildingManager.findBestBathroom(buildingCode, gender);
    }

    @Override
    public Iterator<Bathroom> getTopRatedBathrooms(int limit) {
        return buildingManager.getTopRatedBathrooms(limit);
    }

    @Override
    public Map<Building, Double> getBuildingRanking() {
        return buildingManager.getBuildingRanking();
    }

    // Review operations

    @Override
    public Review addReview(String bathroomId, int rating, String comment)
            throws NoActiveUserException, BathroomDoesNotExistException, InvalidRatingException,
                   DuplicateReviewInContestException {
        if (!sessionManager.hasActiveUser()) {
            throw new NoActiveUserException();
        }
        Bathroom b = getBathroom(bathroomId);
        Review review = reviewManager.createReview(b.getId(), sessionManager.getCurrentUser(),
                rating, comment);
        b.addReview(review);
        return review;
    }

    @Override
    public Iterator<Review> getBathroomReviews(String bathroomId) throws BathroomDoesNotExistException {
        Bathroom b = getBathroom(bathroomId);
        return b.getReviews();
    }

    @Override
    public int getCurrentContestWeek() {
        return reviewManager.getCurrentContestWeek();
    }

    @Override
    public void advanceContestWeek() {
        reviewManager.advanceContestWeek();
    }

    // Alert operations

    @Override
    public Alert addAlert(String bathroomId, String alertTypeStr, String description)
            throws NoActiveUserException, BathroomDoesNotExistException {
        if (!sessionManager.hasActiveUser()) {
            throw new NoActiveUserException();
        }
        Bathroom b = getBathroom(bathroomId);
        AlertType type = AlertType.fromString(alertTypeStr);
        Alert alert = alertManager.createAlert(b.getId(), sessionManager.getCurrentUser(), type, description);
        b.addAlert(alert);
        return alert;
    }

    private String findTargetAlertId(String identifier) throws AlertNotFoundException {
        if (identifier == null) {
            throw new AlertNotFoundException();
        }
        try {
            alertManager.getAlert(identifier);
            return identifier;
        } catch (AlertNotFoundException e) {
            String targetBathroomId = identifier;
            if (buildingManager.hasBathroom(identifier)) {
                targetBathroomId = buildingManager.getBathroom(identifier).getId();
            }
            Iterator<Alert> alerts = alertManager.getActiveAlertsForBathroom(targetBathroomId);
            if (alerts.hasNext()) {
                return alerts.next().getId();
            }
            throw e;
        }
    }

    @Override
    public boolean confirmAlert(String alertIdOrBathroomId)
            throws NoActiveUserException, AlertNotFoundException, AlertAlreadyConfirmedException {
        if (!sessionManager.hasActiveUser()) {
            throw new NoActiveUserException();
        }
        String alertId = findTargetAlertId(alertIdOrBathroomId);
        boolean newlyConfirmed = alertManager.confirmAlert(alertId, sessionManager.getCurrentUser());
        if (newlyConfirmed) {
            Alert alert = alertManager.getAlert(alertId);
            try {
                Bathroom b = getBathroom(alert.getBathroomId());
                b.updateStatusAfterAlertChange();
            } catch (BathroomDoesNotExistException ignored) {
            }
        }
        return newlyConfirmed;
    }

    @Override
    public boolean resolveAlert(String alertIdOrBathroomId)
            throws NoActiveUserException, AlertNotFoundException, AlertAlreadyResolvedException {
        if (!sessionManager.hasActiveUser()) {
            throw new NoActiveUserException();
        }
        String alertId = findTargetAlertId(alertIdOrBathroomId);
        Alert alert = alertManager.getAlert(alertId);
        boolean fullyResolved = alertManager.resolveAlert(alertId, sessionManager.getCurrentUser());
        if (fullyResolved) {
            try {
                Bathroom b = getBathroom(alert.getBathroomId());
                b.resolveAlert(alert.getId(), sessionManager.getCurrentUser().getId());
            } catch (BathroomDoesNotExistException ignored) {
            }
        }
        return fullyResolved;
    }

    @Override
    public Iterator<Alert> listActiveAlerts() {
        return alertManager.getActiveAlerts();
    }

    @Override
    public Iterator<Alert> listBathroomActiveAlerts(String bathroomId) throws BathroomDoesNotExistException {
        Bathroom b = getBathroom(bathroomId);
        return b.getActiveAlerts();
    }
}
