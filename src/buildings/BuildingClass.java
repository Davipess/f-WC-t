package buildings;

import bathrooms.Bathroom;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Building implementation maintaining a catalog of associated bathrooms.
 */
public class BuildingClass implements Building {

    private final String code;
    private final String name;
    private final Map<String, Bathroom> bathrooms;

    /**
     * Constructs a BuildingClass instance.
     *
     * @param code building acronym or code
     * @param name full building name
     */
    public BuildingClass(String code, String name) {
        this.code = code.trim().toUpperCase();
        this.name = name.trim();
        this.bathrooms = new LinkedHashMap<>();
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void addBathroom(Bathroom bathroom) {
        if (bathroom == null) {
            throw new IllegalArgumentException();
        }
        bathrooms.put(bathroom.getId().trim().toUpperCase(), bathroom);
    }

    @Override
    public boolean hasBathroom(String id) {
        return id != null && bathrooms.containsKey(id.trim().toUpperCase());
    }

    @Override
    public Bathroom getBathroom(String id) {
        if (id == null) {
            return null;
        }
        return bathrooms.get(id.trim().toUpperCase());
    }

    @Override
    public Iterator<Bathroom> getBathrooms() {
        return bathrooms.values().iterator();
    }

    @Override
    public int getBathroomCount() {
        return bathrooms.size();
    }

    @Override
    public double getAverageRating() {
        double sum = 0.0;
        int count = 0;
        for (Bathroom b : bathrooms.values()) {
            if (b.getReviewCount() > 0) {
                sum += b.getAverageRating();
                count++;
            }
        }
        if (count == 0) {
            return 0.0;
        }
        return sum / count;
    }

    @Override
    public Bathroom getBestBathroom(String gender) {
        Bathroom best = null;
        String cleanGender = "";
        if (gender != null) {
            cleanGender = gender.trim().toUpperCase();
        }

        for (Bathroom b : bathrooms.values()) {
            if (!b.isOperational()) {
                continue;
            }

            boolean matches = cleanGender.isEmpty() ||
                    b.getGender().equalsIgnoreCase(cleanGender) ||
                    b.getGender().equalsIgnoreCase("UNISEX");

            if (!matches) {
                continue;
            }

            if (best == null) {
                best = b;
            } else {
                int cmp = Double.compare(b.getAverageRating(), best.getAverageRating());
                if (cmp > 0) {
                    best = b;
                } else if (cmp == 0) {
                    if (b.getReviewCount() > best.getReviewCount()) {
                        best = b;
                    }
                }
            }
        }
        return best;
    }
}
