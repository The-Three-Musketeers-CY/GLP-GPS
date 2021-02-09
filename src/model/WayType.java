package model;

import model.identifiers.TransportIdentifier;
import model.identifiers.WayIdentifier;

import java.util.Collections;
import java.util.HashMap;

public class WayType {

    private WayIdentifier identifier;
    private TransportIdentifier[] availableTransports;
    private HashMap<TransportIdentifier, Integer> speed;

    public WayType(WayIdentifier identifier, TransportIdentifier[] availableTransports) {
        this.identifier = identifier;
        this.availableTransports = availableTransports;
        this.speed = new HashMap<>();
    }

    public WayIdentifier getIdentifier() {
        return identifier;
    }

    public TransportIdentifier[] getAvailableTransports() {
        return availableTransports;
    }

    public void setSpeed(TransportIdentifier identifier, int speed) {
        this.speed.put(identifier, speed);
    }

    public HashMap<TransportIdentifier, Integer> getSpeeds() {
        return (HashMap<TransportIdentifier, Integer>) Collections.unmodifiableMap(speed);
    }

}
