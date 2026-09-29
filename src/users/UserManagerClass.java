package users;

import exceptions.UserAlreadyExistsException;
import exceptions.UserDoesNotExistException;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * In-memory user manager using a Map.
 */
public class UserManagerClass implements UserManager {

    private final Map<String, User> users;

    public UserManagerClass() {
        this.users = new LinkedHashMap<>();
    }

    @Override
    public void registerUser(String id, String name, Role role, String password) throws UserAlreadyExistsException {
        if (id == null || id.trim().isEmpty() || name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException();
        }
        String normalizedId = id.trim().toLowerCase();
        if (users.containsKey(normalizedId)) {
            throw new UserAlreadyExistsException();
        }
        Role userRole = Role.STUDENT;
        if (role != null) {
            userRole = role;
        }
        User user = new UserClass(normalizedId, name.trim(), userRole, password);
        users.put(normalizedId, user);
    }

    @Override
    public void registerUser(String id, String name, Role role) throws UserAlreadyExistsException {
        registerUser(id, name, role, "1234");
    }

    @Override
    public User getUser(String id) throws UserDoesNotExistException {
        if (id == null) {
            throw new UserDoesNotExistException();
        }
        User user = users.get(id.trim().toLowerCase());
        if (user == null) {
            throw new UserDoesNotExistException();
        }
        return user;
    }

    @Override
    public boolean hasUser(String id) {
        return id != null && users.containsKey(id.trim().toLowerCase());
    }

    @Override
    public Iterator<User> getUsers() {
        return users.values().iterator();
    }

    @Override
    public int getUserCount() {
        return users.size();
    }
}
