package model;

import model.identifiers.TransportIdentifier;
import model.identifiers.WayIdentifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class WayType {

    private WayIdentifier identifier;
    private TransportIdentifier[] availableTransports;
    private HashMap<TransportIdentifier, Integer> speeds;

    public WayType(WayIdentifier identifier, TransportIdentifier[] availableTransports) {
        this.identifier = identifier;
        this.availableTransports = availableTransports;
        this.speeds = new HashMap<>();
    }

    public WayIdentifier getIdentifier() {
        return identifier;
    }

    public TransportIdentifier[] getAvailableTransports() {
        return availableTransports;
    }

    public void setSpeed(TransportIdentifier identifier, int speed) {
        this.speeds.put(identifier, speed);
    }

    public HashMap<TransportIdentifier, Integer> getSpeeds() {
        return speeds;
    }

    @Override
    public String toString() {
        return "WayType{" +
                "identifier=" + identifier +
                ", availableTransports=" + Arrays.toString(availableTransports) +
                ", speed=" + speeds +
                '}';
    }

}
