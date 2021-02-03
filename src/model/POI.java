package model;

public class POI {

    private String name;
    private POIType type;

    public POI(String name, POIType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public POIType getType() {
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
