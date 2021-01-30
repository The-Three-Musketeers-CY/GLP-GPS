package data;

import java.util.Collections;
import java.util.HashMap;

public class WayType {

    private String name;
    private HashMap<String, TransportType> availableTransport;
    private HashMap<TransportType, Integer> speed;
    private static HashMap<String, WayType> list = initWayTypes();

    private WayType(String name) {
        this.name = name;
    }

    private static HashMap<String, WayType> initWayTypes() {
        // Instructions
        return null;
    }

    public static WayType getInstance(String name) {
        return list.get(name);
    }

    public static HashMap<String, WayType> getInstances() {
        return (HashMap<String, WayType>) Collections.unmodifiableMap(list);
    }

    public static boolean isExistWayType(String name) {
        return list.containsKey(name);
    }

    public String getName() {
        return name;
    }

    public HashMap<String, TransportType> getAvailableTransports() {
        return (HashMap<String, TransportType>) Collections.unmodifiableMap(availableTransport);
    }

    public HashMap<TransportType, Integer> getSpeeds() {
        return (HashMap<TransportType, Integer>) Collections.unmodifiableMap(speed);
    }

}
