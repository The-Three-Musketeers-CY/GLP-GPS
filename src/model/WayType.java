package model;

import model.identifiers.TransportIdentifier;
import model.identifiers.WayIdentifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

/**
 * This class represents a way type
 */
public class WayType {

    private WayIdentifier identifier;
    private TransportIdentifier[] availableTransports;
    private HashMap<TransportIdentifier, Integer> speeds;

    /**
     * Constructs a way type
     * @param identifier Identifier of the way type
     * @param availableTransports Available transports list of the way type
     */
    public WayType(WayIdentifier identifier, TransportIdentifier[] availableTransports) {
        this.identifier = identifier;
        this.availableTransports = availableTransports;
        this.speeds = new HashMap<>();
    }

    /**
     * This method returns the identifier of this way type
     * @return identifier of this way type
     */
    public WayIdentifier getIdentifier() {
        return identifier;
    }

    /**
     * This method returns all available transports of this way type
     * @return Available transports list of this way type
     */
    public TransportIdentifier[] getAvailableTransports() {
        return availableTransports;
    }

    /**
     * This method set speed for one transport of this way type
     * @param identifier Type of the transport
     * @param speed Speed of the transport of this way type
     */
    public void setSpeed(TransportIdentifier identifier, int speed) {
        this.speeds.put(identifier, speed);
    }

    /**
     * This method returns all transports speeds of this way type
     * @return Transports speeds list of this way type
     */
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
