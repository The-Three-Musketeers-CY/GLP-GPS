package model;

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

    public String getId() {
        return id;
    }

    public POI getPoi() {
        return poi;
    }

    public Point getPosition() {
        return position;
    }

    public boolean isPOI() {
        return poi != null;
    }

    public boolean isStation() {
        return poi != null && poi.getType() == POIType.STATION;
    }

    public boolean isAttraction() {
        return poi != null && poi.getType() == POIType.ATTRACTION;
    }

}