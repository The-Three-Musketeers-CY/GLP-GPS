package model;

import model.identifiers.POIIdentifier;

/**
 * This class represents a POI
 */
public class POI {

    private String name;
    private POIIdentifier type;

    /**
     * Constructs POI
     * @param name Name of the POI
     * @param type Type of the POI
     */
    public POI(String name, POIIdentifier type) {
        this.name = name;
        this.type = type;
    }

    /**
     * This method returns the name of this POI
     * @return Name of this POI
     */
    public String getName() {
        return name;
    }

    /**
     * This method returns the type of this POI
     * @return Type of this POI
     */
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
