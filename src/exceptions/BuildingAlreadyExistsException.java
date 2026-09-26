package exceptions;

/**
 * Thrown when trying to add a building that already exists in the system.
 */
public class BuildingAlreadyExistsException extends Exception {
    public BuildingAlreadyExistsException() {
        super();
    }
}
