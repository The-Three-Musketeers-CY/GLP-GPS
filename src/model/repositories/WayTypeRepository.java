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

    public void addWayType(WayType wayType) {
        wayTypes.put(wayType.getIdentifier(), wayType);
    }

    public HashMap<WayIdentifier, WayType> getWayTypes() {
        return wayTypes;
    }

    public static WayTypeRepository getInstance() {
        return instance;
    }

}
