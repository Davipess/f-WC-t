package auth;

import exceptions.NoActiveUserException;
import users.User;

/**
 * Tracks the current user session in memory.
 */
public class SessionManagerClass implements SessionManager {

    private User currentUser;

    public SessionManagerClass() {
        this.currentUser = null;
    }

    @Override
    public void login(User user) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        this.currentUser = user;
    }

    @Override
    public void logout() throws NoActiveUserException {
        if (this.currentUser == null) {
            throw new NoActiveUserException();
        }
        this.currentUser = null;
    }

    @Override
    public User getCurrentUser() {
        return this.currentUser;
    }

    @Override
    public boolean hasActiveUser() {
        return this.currentUser != null;
    }
}
