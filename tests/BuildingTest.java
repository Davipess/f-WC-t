import bathrooms.Bathroom;
import bathrooms.StandardBathroomClass;
import buildings.Building;
import buildings.BuildingClass;
import exceptions.BuildingAlreadyExistsException;
import exceptions.BuildingDoesNotExistException;
import org.junit.jupiter.api.Test;
import reviews.ReviewClass;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class BuildingTest {

    @Test
    public void testBuildingCreationAndBathrooms() {
        Building b = new BuildingClass("VII", "Edificio VII");
        assertEquals("VII", b.getCode());
        assertEquals("Edificio VII", b.getName());
        assertEquals(0, b.getBathroomCount());

        Bathroom wc = new StandardBathroomClass("VII-0-M", "VII", 0, "MALE");
        b.addBathroom(wc);

        assertEquals(1, b.getBathroomCount());
        assertTrue(b.hasBathroom("VII-0-M"));
        assertEquals(wc, b.getBathroom("VII-0-M"));
    }

    @Test
    public void testFindBestBathroomInBuilding() throws Exception {
        Building b = new BuildingClass("DI", "Departamento de Informatica");
        Bathroom wc1 = new StandardBathroomClass("DI-0-M", "DI", 0, "MALE");
        Bathroom wc2 = new StandardBathroomClass("DI-1-M", "DI", 1, "MALE");

        wc1.addReview(new ReviewClass("R1", "DI-0-M", "u1", "Ana", 3, "Ok", LocalDateTime.now(), 1));
        wc2.addReview(new ReviewClass("R2", "DI-1-M", "u2", "Rui", 5, "Flawless", LocalDateTime.now(), 1));

        b.addBathroom(wc1);
        b.addBathroom(wc2);

        Bathroom best = b.getBestBathroom("MALE");
        assertNotNull(best);
        assertEquals("DI-1-M", best.getId());
        assertEquals(5.0, best.getAverageRating(), 0.001);
    }

    @Test
    public void testBuildingAverageRating() throws Exception {
        Building b = new BuildingClass("BIB", "Biblioteca");
        Bathroom wc1 = new StandardBathroomClass("BIB-0-M", "BIB", 0, "MALE");
        Bathroom wc2 = new StandardBathroomClass("BIB-0-F", "BIB", 0, "FEMALE");

        wc1.addReview(new ReviewClass("R1", "BIB-0-M", "u1", "User 1", 4, "Good", LocalDateTime.now(), 1));
        wc2.addReview(new ReviewClass("R2", "BIB-0-F", "u2", "User 2", 5, "Great", LocalDateTime.now(), 1));

        b.addBathroom(wc1);
        b.addBathroom(wc2);

        assertEquals(4.5, b.getAverageRating(), 0.001);
    }
}
