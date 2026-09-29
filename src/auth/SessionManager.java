package auth;

import exceptions.NoActiveUserException;
import users.User;

/**
 * Interface responsible for session lifecycle, login, and logout state management.
 */
public interface SessionManager {

    /**
     * Authenticates and sets the active user for the session.
     *
     * @param user the User instance to authenticate
     */
    void login(User user);

    /**
     * Clears the current session and logs out the active user.
     *
     * @throws NoActiveUserException if no user is currently authenticated
     */
    void logout() throws NoActiveUserException;

    /**
     * Retrieves the currently authenticated user in the session.
     *
     * @return the authenticated User, or null if no session is active
     */
    User getCurrentUser();

    /**
     * Checks whether an active user session exists.
     *
     * @return true if a user is logged in, false otherwise
     */
    boolean hasActiveUser();
}
