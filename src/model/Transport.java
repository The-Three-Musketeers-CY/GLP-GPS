package model;

import model.enums.TransportIdentifier;

public class Transport {

    private model.enums.TransportIdentifier name;
    private float coast;
    private boolean isPublicTransport;

    public Transport(model.enums.TransportIdentifier name, float coast, boolean isPublicTransport) {
        this.name = name;
        this.coast = coast;
        this.isPublicTransport = isPublicTransport;
    }

    public model.enums.TransportIdentifier getIdentifier() {
        return name;
    }

    public float getCoast() {
        return coast;
    }

    public boolean isPublicTransport() {
        return isPublicTransport;
    }

}
