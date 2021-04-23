package model;

import model.identifiers.TransportIdentifier;

/**
 * This class represents a transport
 */
public class Transport {

    private TransportIdentifier name;
    private float cost;
    private boolean isPublicTransport;

    /**
     * Constructs transport
     * @param name Name of the transport
     * @param cost Cost of the transport
     * @param isPublicTransport If the transport is a public transport
     */
    public Transport(model.identifiers.TransportIdentifier name, float cost, boolean isPublicTransport) {
        this.name = name;
        this.cost = cost;
        this.isPublicTransport = isPublicTransport;
    }

    /**
     * This method returns the type of this transport
     * @return Identifier type of this transport
     */
    public TransportIdentifier getIdentifier() {
        return name;
    }

    /**
     * This method returns the cost of this transport
     * @return Cost of this transport
     */
    public float getCost() {
        return cost;
    }

    /**
     * This method returns if this transport is a public transport
     * @return True if this transport is a public transport, else returns false
     */
    public boolean isPublicTransport() {
        return isPublicTransport;
    }

    /**
     * This method returns if this is a foot transport
     * @return True if this transport is a foot transport, else returns false
     */
    public boolean isFoot() {
        return name.equals(TransportIdentifier.FOOT) ;
    }

    @Override
    public String toString() {
        return "Transport{" +
                "name=" + name +
                ", coast=" + cost +
                ", isPublicTransport=" + isPublicTransport +
                '}';
    }

}
