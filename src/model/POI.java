package model;

import model.identifiers.POIIdentifier;

public class POI {

    private String name;
    private POIIdentifier type;

    public POI(String name, POIIdentifier type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public POIIdentifier getType() {
        return type;
    }

    @Override
    public String toString() {
        return "POI{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
