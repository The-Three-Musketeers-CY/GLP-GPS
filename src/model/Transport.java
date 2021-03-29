package model;

import model.identifiers.TransportIdentifier;

public class Transport {

    private model.identifiers.TransportIdentifier name;
    private float coast;
    private boolean isPublicTransport;

    public Transport(model.identifiers.TransportIdentifier name, float coast, boolean isPublicTransport) {
        this.name = name;
        this.coast = coast;
        this.isPublicTransport = isPublicTransport;
    }

    public model.identifiers.TransportIdentifier getIdentifier() {
        return name;
    }

    public float getCoast() {
        return coast;
    }

    public boolean isPublicTransport() {
        return isPublicTransport;
    }

    @Override
    public String toString() {
        return "Transport{" +
                "name=" + name +
                ", coast=" + coast +
                ", isPublicTransport=" + isPublicTransport +
                '}';
    }

    public boolean isFoot() {
        return name.equals(TransportIdentifier.FOOT) ;
    }
}
