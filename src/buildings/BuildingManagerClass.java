package buildings;

import bathrooms.Bathroom;
import exceptions.BuildingAlreadyExistsException;
import exceptions.BuildingDoesNotExistException;
import exceptions.NoBathroomsFoundException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * In-memory manager for campus buildings and bathrooms.
 */
public class BuildingManagerClass implements BuildingManager {

    private final Map<String, Building> buildings;

    public BuildingManagerClass() {
        this.buildings = new LinkedHashMap<>();
    }

    @Override
    public void registerBuilding(String code, String name) throws BuildingAlreadyExistsException {
        if (code == null || code.trim().isEmpty() || name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException();
        }
        String cleanCode = code.trim().toUpperCase();
        if (buildings.containsKey(cleanCode)) {
            throw new BuildingAlreadyExistsException();
        }
        Building b = new BuildingClass(cleanCode, name.trim());
        buildings.put(cleanCode, b);
    }

    @Override
    public Building getBuilding(String code) throws BuildingDoesNotExistException {
        if (code == null) {
            throw new BuildingDoesNotExistException();
        }
        String clean = code.trim().toUpperCase();
        Building b = buildings.get(clean);
        if (b != null) {
            return b;
        }
        // Also check if matches full name
        for (Building item : buildings.values()) {
            if (item.getName().equalsIgnoreCase(code.trim()) ||
                item.getName().toUpperCase().contains(clean)) {
                return item;
            }
        }
        throw new BuildingDoesNotExistException();
    }

    @Override
    public boolean hasBuilding(String code) {
        if (code == null || code.trim().isEmpty()) {
            return false;
        }
        try {
            getBuilding(code);
            return true;
        } catch (BuildingDoesNotExistException e) {
            return false;
        }
    }

    @Override
    public Iterator<Building> getBuildings() {
        return buildings.values().iterator();
    }

    @Override
    public void addBathroom(Bathroom bathroom) throws BuildingDoesNotExistException {
        if (bathroom == null) {
            throw new IllegalArgumentException();
        }
        Building b = getBuilding(bathroom.getBuildingCode());
        b.addBathroom(bathroom);
    }

    @Override
    public Bathroom getBathroom(String id) {
        if (id == null) {
            return null;
        }
        String cleanId = id.trim().toUpperCase();
        for (Building b : buildings.values()) {
            if (b.hasBathroom(cleanId)) {
                return b.getBathroom(cleanId);
            }
        }
        // Check case-insensitively and ignoring separator characters
        String normalizedId = cleanId.replace("-", "").replace(" ", "");
        for (Building b : buildings.values()) {
            Iterator<Bathroom> it = b.getBathrooms();
            while (it.hasNext()) {
                Bathroom bath = it.next();
                if (bath.getId().equalsIgnoreCase(cleanId)) {
                    return bath;
                }
                String bathNorm = bath.getId().toUpperCase().replace("-", "").replace(" ", "");
                if (bathNorm.equals(normalizedId)) {
                    return bath;
                }
            }
        }
        return null;
    }

    @Override
    public boolean hasBathroom(String id) {
        return getBathroom(id) != null;
    }

    @Override
    public Iterator<Bathroom> getAllBathrooms() {
        List<Bathroom> list = new ArrayList<>();
        for (Building b : buildings.values()) {
            Iterator<Bathroom> it = b.getBathrooms();
            while (it.hasNext()) {
                list.add(it.next());
            }
        }
        return list.iterator();
    }

    @Override
    public Iterator<Bathroom> getAvailableBathrooms() {
        List<Bathroom> list = new ArrayList<>();
        for (Building b : buildings.values()) {
            Iterator<Bathroom> it = b.getBathrooms();
            while (it.hasNext()) {
                Bathroom bath = it.next();
                if (bath.isOperational()) {
                    list.add(bath);
                }
            }
        }
        return list.iterator();
    }

    @Override
    public Iterator<Bathroom> getBathroomsInBuilding(String buildingCode) throws BuildingDoesNotExistException {
        Building b = getBuilding(buildingCode);
        return b.getBathrooms();
    }

    @Override
    public Iterator<Bathroom> getBathroomsByGender(String gender) {
        List<Bathroom> list = new ArrayList<>();
        if (gender == null) {
            return list.iterator();
        }
        String clean = gender.trim().toUpperCase();
        for (Building b : buildings.values()) {
            Iterator<Bathroom> it = b.getBathrooms();
            while (it.hasNext()) {
                Bathroom bath = it.next();
                if (bath.getGender().equalsIgnoreCase(clean)) {
                    list.add(bath);
                }
            }
        }
        return list.iterator();
    }

    @Override
    public Iterator<Bathroom> getTopRatedBathrooms(int limit) {
        List<Bathroom> list = new ArrayList<>();
        for (Building b : buildings.values()) {
            Iterator<Bathroom> it = b.getBathrooms();
            while (it.hasNext()) {
                list.add(it.next());
            }
        }

        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - i - 1; j++) {
                Bathroom b1 = list.get(j);
                Bathroom b2 = list.get(j + 1);
                int cmp = Double.compare(b1.getAverageRating(), b2.getAverageRating());
                if (cmp == 0) {
                    cmp = Integer.compare(b1.getReviewCount(), b2.getReviewCount());
                }
                if (cmp < 0) {
                    list.set(j, b2);
                    list.set(j + 1, b1);
                }
            }
        }

        if (limit > 0 && limit < list.size()) {
            return list.subList(0, limit).iterator();
        }
        return list.iterator();
    }

    @Override
    public Bathroom findBestBathroom(String buildingCode, String gender)
            throws BuildingDoesNotExistException, NoBathroomsFoundException {
        Building b = getBuilding(buildingCode);
        Bathroom best = b.getBestBathroom(gender);
        if (best == null) {
            throw new NoBathroomsFoundException();
        }
        return best;
    }

    @Override
    public Map<Building, Double> getBuildingRanking() {
        Map<Building, Double> ranking = new LinkedHashMap<>();
        for (Building b : buildings.values()) {
            double avg = b.getAverageRating();
            if (avg > 0.0) {
                ranking.put(b, avg);
            }
        }
        return ranking;
    }
}
