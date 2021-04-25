package model.repositories;

import model.WayType;
import model.identifiers.WayIdentifier;

import java.util.HashMap;

public class WayTypeRepository {

    private HashMap<WayIdentifier, WayType> wayTypes;
    private static WayTypeRepository instance = new WayTypeRepository();

    private WayTypeRepository() {
        wayTypes = new HashMap<>();
    }

    /**
     * This method add WayType to WayType's list
     * @param wayType WayType to add
     */
    public void addWayType(WayType wayType) {
        wayTypes.put(wayType.getIdentifier(), wayType);
    }

    /**
     * This method returns WayType's list
     * @return The WayType's list
     */
    public HashMap<WayIdentifier, WayType> getWayTypes() {
        return wayTypes;
    }

    /**
     * This method returns instance of this class
     * @return Instance of this class
     */
    public static WayTypeRepository getInstance() {
        return instance;
    }

}
