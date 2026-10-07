import alerts.Alert;
import alerts.AlertClass;
import alerts.AlertType;
import bathrooms.AccessibleBathroomClass;
import bathrooms.Bathroom;
import bathrooms.BathroomStatus;
import bathrooms.StandardBathroomClass;
import org.junit.jupiter.api.Test;
import reviews.Review;
import reviews.ReviewClass;
import users.Role;
import users.User;
import users.UserClass;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class BathroomTest {

    @Test
    public void testStandardAndAccessibleTypes() {
        Bathroom std = new StandardBathroomClass("VII-0-M", "VII", 0, "MALE");
        assertFalse(std.isAccessible());
        assertEquals("VII", std.getBuildingCode());
        assertEquals(0, std.getFloor());
        assertEquals(BathroomStatus.AVAILABLE, std.getStatus());

        AccessibleBathroomClass acc = new AccessibleBathroomClass("VII-1-A", "VII", 1, "ACCESSIBLE", true, true);
        assertTrue(acc.isAccessible());
        assertTrue(acc.hasEmergencyAlarm());
        assertTrue(acc.hasSupportBars());
    }

    @Test
    public void testReviewAggregation() {
        Bathroom wc = new StandardBathroomClass("DI-0-M", "DI", 0, "MALE");
        assertEquals(0, wc.getReviewCount());
        assertEquals(0.0, wc.getAverageRating());

        Review r1 = new ReviewClass("R1", "DI-0-M", "u1", "Ana", 4, "Nice", LocalDateTime.now(), 1);
        Review r2 = new ReviewClass("R2", "DI-0-M", "u2", "Rui", 2, "Bad", LocalDateTime.now(), 1);
        wc.addReview(r1);
        wc.addReview(r2);

        assertEquals(2, wc.getReviewCount());
        assertEquals(3.0, wc.getAverageRating(), 0.001);
    }

    @Test
    public void testBlockingAlertAndResolution() {
        Bathroom wc = new StandardBathroomClass("BIB-0-M", "BIB", 0, "MALE");
        assertTrue(wc.isOperational());

        User admin = new UserClass("admin", "Facilities Admin", Role.ADMIN);

        // Admin report is automatically confirmed
        Alert cleaning = new AlertClass("ALT-1", "BIB-0-M", admin, AlertType.CLEANING, "Cleaning in progress", LocalDateTime.now());
        wc.addAlert(cleaning);

        assertEquals(BathroomStatus.CLEANING, wc.getStatus());
        assertFalse(wc.isOperational());
        assertEquals(1, wc.getActiveAlertCount());

        // Resolve alert
        cleaning.addResolutionConfirmation(admin);
        boolean resolved = wc.resolveAlert("ALT-1", "admin");
        assertTrue(resolved);
        assertEquals(0, wc.getActiveAlertCount());
        assertEquals(BathroomStatus.AVAILABLE, wc.getStatus());
        assertTrue(wc.isOperational());
    }

    @Test
    public void testBathroomLocation() {
        Bathroom wc = new StandardBathroomClass("DI-0-M1", "DI", 0, "MALE", "Atrio de Entrada");
        assertEquals("Atrio de Entrada", wc.getLocation());
        wc.setLocation("North Hallway");
        assertEquals("North Hallway", wc.getLocation());
    }
}
