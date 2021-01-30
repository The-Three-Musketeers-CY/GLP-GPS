package data;

public class Node {

    String id;
    Point position;
    POI poi = null;

    public Node(String id, Point position) {
        this.id = id;
        this.position = position;
    }

    public Node(String id, Point position, POI poi) {
        this.id = id;
        this.position = position;
        this.poi = poi;
    }

    public POI getPoi() {
        return poi;
    }

    public boolean isStation(POI poi) {
        return poi != null && poi.getType().equals(POIType.STATION);
    }

}