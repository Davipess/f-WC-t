
import exceptions.UserAlreadyExistsException;
import exceptions.UserDoesNotExistException;
import org.junit.jupiter.api.Test;
import users.Role;
import users.User;
import users.UserClass;
import users.UserManager;
import users.UserManagerClass;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testUserCreationAndProperties() {
        User user = new UserClass("74167", "David Figueiredo", Role.STUDENT);
        assertEquals("74167", user.getId());
        assertEquals("David Figueiredo", user.getName());
        assertEquals(Role.STUDENT, user.getRole());
        assertEquals(0, user.getReviewCount());
        assertEquals(0, user.getAlertCount());

        user.incrementReviewCount();
        user.incrementAlertCount();
        assertEquals(1, user.getReviewCount());
        assertEquals(1, user.getAlertCount());
    }

    @Test
    public void testRegisterAndRetrieveUser() throws Exception {
        UserManager userManager = new UserManagerClass();
        userManager.registerUser("74167", "David Figueiredo", Role.STUDENT);
        assertTrue(userManager.hasUser("74167"));

        User fetched = userManager.getUser("74167");
        assertNotNull(fetched);
        assertEquals("David Figueiredo", fetched.getName());
        assertEquals(1, userManager.getUserCount());
    }

    @Test
    public void testDuplicateUserThrowsException() throws Exception {
        UserManager userManager = new UserManagerClass();
        userManager.registerUser("david", "David", Role.STUDENT);
        assertThrows(UserAlreadyExistsException.class, () -> {
            userManager.registerUser("david", "Another David", Role.STUDENT);
        });
    }

    @Test
    public void testPasswordAuthentication() throws Exception {
        UserManager userManager = new UserManagerClass();
        userManager.registerUser("bernardo", "Bernardo", Role.STUDENT, "secret123");
        User user = userManager.getUser("bernardo");
        assertTrue(user.validatePassword("secret123"));
        assertFalse(user.validatePassword("wrong"));

        user.setPassword("new123");
        assertTrue(user.validatePassword("new123"));
        assertFalse(user.validatePassword("secret123"));
    }
}
