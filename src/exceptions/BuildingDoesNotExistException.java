package exceptions;

/**
 * Thrown when attempting to access a building that does not exist.
 */
public class BuildingDoesNotExistException extends Exception {
    public BuildingDoesNotExistException() {
        super();
    }
}
