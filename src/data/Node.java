package data;
import data.Point;
import data.POI;
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
        if (getPoi() == null) {
            return false;
        } else {
            return true;
        }
    }
}