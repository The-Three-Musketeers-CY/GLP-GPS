package data;

public class POI {
    String name;
    String type;

    public POI(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
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
