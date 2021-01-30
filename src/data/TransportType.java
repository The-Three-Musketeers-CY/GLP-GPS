package data;

import java.util.Collections;
import java.util.HashMap;

public class TransportType {

    private String name;
    private float coast;
    private boolean isPublicTransport;
    private static HashMap<String, TransportType> list = initTransportTypes();

    private TransportType(String name, float coast, boolean isPublicTransport) {
        this.name = name;
        this.coast = coast;
        this.isPublicTransport = isPublicTransport;
    }

    private static HashMap<String, TransportType> initTransportTypes() {
        // Instructions
        return new HashMap<>();
    }

    public static TransportType getInstance(String name) {
        return list.get(name);
    }

    public static HashMap<String, TransportType> getInstances() {
        return (HashMap<String, TransportType>) Collections.unmodifiableMap(list);
    }

    public static boolean isExistTransportType(String name) {
        return list.containsKey(name);
    }

    public String getName() {
        return name;
    }

    public float getCoast() {
        return coast;
    }

    public boolean isPublicTransport() {
        return isPublicTransport;
    }

}
