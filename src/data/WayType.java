package data;

import java.util.Collections;
import java.util.HashMap;

public class WayType {

    private WayIdentifier name;
    private HashMap<String, TransportType> availableTransport;
    private HashMap<TransportType, Integer> speed;
    private static HashMap<WayIdentifier, WayType> list = initWayTypes();

    private WayType(WayIdentifier name) {
        this.name = name;
        this.availableTransport = new HashMap<>();
        this.speed = new HashMap<>();
    }

    private static HashMap<WayIdentifier, WayType> initWayTypes() {
        // Instructions
        return new HashMap<>();
    }

    public static WayType getInstance(WayIdentifier name) {
        return list.get(name);
    }

    public static HashMap<WayIdentifier, WayType> getInstances() {
        return (HashMap<WayIdentifier, WayType>) Collections.unmodifiableMap(list);
    }

    public static boolean isExistWayType(WayIdentifier name) {
        return list.containsKey(name);
    }

    private void addAvailableTransport(TransportType transportType, int speed) {
        availableTransport.put(transportType.getName(), transportType);
        this.speed.put(transportType, speed);
    }

    public WayIdentifier getName() {
        return name;
    }

    public HashMap<String, TransportType> getAvailableTransports() {
        return (HashMap<String, TransportType>) Collections.unmodifiableMap(availableTransport);
    }

    public HashMap<TransportType, Integer> getSpeeds() {
        return (HashMap<TransportType, Integer>) Collections.unmodifiableMap(speed);
    }

}
