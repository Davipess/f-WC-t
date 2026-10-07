import alerts.Alert;
import bathrooms.Bathroom;
import exceptions.DuplicateReviewInContestException;
import exceptions.InvalidPasswordException;
import exceptions.NoActiveUserException;
import exceptions.UserDoesNotExistException;
import org.junit.jupiter.api.Test;
import reviews.Review;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

public class FWCtTest {

    @Test
    public void testAuthenticationLifecycle() throws Exception {
        FWCt system = new FWCtClass();
        assertFalse(system.hasActiveUser());
        assertNull(system.getCurrentUser());

        system.login("david", "1234");
        assertTrue(system.hasActiveUser());
        assertEquals("david", system.getCurrentUser().getId());

        system.logout();
        assertFalse(system.hasActiveUser());

        assertThrows(InvalidPasswordException.class, () -> {
            system.login("david", "wrong_password");
        });

        assertThrows(UserDoesNotExistException.class, () -> {
            system.login("nonexistent", "1234");
        });
    }

    @Test
    public void testAntiReviewBombingInContestCycle() throws Exception {
        FWCt system = new FWCtClass();
        system.login("david");

        // First review in week 1 succeeds
        Review rev1 = system.addReview("II-1-M", 5, "Excellent!");
        assertNotNull(rev1);

        // Immediate second review by same user on same bathroom fails
        assertThrows(DuplicateReviewInContestException.class, () -> {
            system.addReview("II-1-M", 4, "Spam attempt");
        });

        // Advance to next contest week
        system.advanceContestWeek();
        assertEquals(2, system.getCurrentContestWeek());

        // Now the user can review again for the new week
        Review rev2 = system.addReview("II-1-M", 4, "Week 2 review");
        assertNotNull(rev2);
    }

    @Test
    public void testFindBestBathroomInBuilding() throws Exception {
        FWCt system = new FWCtClass();
        Bathroom best = system.findBestBathroom("DI", "MALE");
        assertNotNull(best);
        assertEquals("DI-0-M1", best.getId());
        assertEquals("Atrio de Entrada", best.getLocation());
        assertTrue(best.isOperational());
    }

    @Test
    public void testCommunityConsensusAlerts() throws Exception {
        FWCt system = new FWCtClass();
        // Student david reports alert (initial vote 1/2: pending, does not block immediately)
        system.login("david");
        Alert alert = system.addAlert("CANT-0-M", "MAINTENANCE", "Broken faucet");
        assertNotNull(alert);
        assertFalse(alert.isConfirmed());

        Bathroom wc = system.getBathroom("CANT-0-M");
        assertTrue(wc.isOperational()); // Still operational because pending confirmation

        // Student maria confirms (2/2 threshold reached -> confirmed and blocks bathroom!)
        system.login("maria");
        boolean confirmed = system.confirmAlert(alert.getId());
        assertTrue(confirmed);
        assertTrue(alert.isConfirmed());
        assertFalse(wc.isOperational()); // Now blocked

        // Student david votes to resolve (1/2: resolution pending)
        system.login("david");
        boolean resolvedStep1 = system.resolveAlert(alert.getId());
        assertFalse(resolvedStep1);
        assertFalse(wc.isOperational());

        // Student maria votes to resolve (2/2 threshold reached -> officially resolved and reopened!)
        system.login("maria");
        boolean resolvedStep2 = system.resolveAlert(alert.getId());
        assertTrue(resolvedStep2);
        assertTrue(wc.isOperational()); // Reopened!
    }
}
