package model;

import model.identifiers.TransportIdentifier;

public class Transport {

    private model.identifiers.TransportIdentifier name;
    private float cost;
    private boolean isPublicTransport;

    public Transport(model.identifiers.TransportIdentifier name, float cost, boolean isPublicTransport) {
        this.name = name;
        this.cost = cost;
        this.isPublicTransport = isPublicTransport;
    }

    public model.identifiers.TransportIdentifier getIdentifier() {
        return name;
    }

    public float getCost() {
        return cost;
    }

    public boolean isPublicTransport() {
        return isPublicTransport;
    }

    @Override
    public String toString() {
        return "Transport{" +
                "name=" + name +
                ", coast=" + cost +
                ", isPublicTransport=" + isPublicTransport +
                '}';
    }

    public boolean isFoot() {
        return name.equals(TransportIdentifier.FOOT) ;
    }
}
