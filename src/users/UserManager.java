package users;

import exceptions.UserAlreadyExistsException;
import exceptions.UserDoesNotExistException;

import java.util.Iterator;

/**
 * Manager interface responsible for user registration, persistence, and querying.
 */
public interface UserManager {

    /**
     * Registers a new user into the system with credentials.
     *
     * @param id       the unique user ID
     * @param name     the full name of the user
     * @param role     the assigned role
     * @param password the password for authentication
     * @throws UserAlreadyExistsException if a user with this ID already exists
     */
    void registerUser(String id, String name, Role role, String password) throws UserAlreadyExistsException;

    /**
     * Registers a new user with default password.
     *
     * @param id   the unique user ID
     * @param name the full name of the user
     * @param role the assigned role
     * @throws UserAlreadyExistsException if a user with this ID already exists
     */
    void registerUser(String id, String name, Role role) throws UserAlreadyExistsException;

    /**
     * Retrieves an existing user by unique ID.
     *
     * @param id the user ID to search for
     * @return the corresponding User instance
     * @throws UserDoesNotExistException if no user with this ID is found
     */
    User getUser(String id) throws UserDoesNotExistException;

    /**
     * Checks whether a user with the specified ID exists.
     *
     * @param id the user ID to verify
     * @return true if user exists, false otherwise
     */
    boolean hasUser(String id);

    /**
     * Returns an iterator over all registered users.
     *
     * @return an iterator of User instances
     */
    Iterator<User> getUsers();

    /**
     * Retrieves the total count of registered users.
     *
     * @return user count
     */
    int getUserCount();
}
