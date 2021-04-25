package model;

import model.identifiers.POIIdentifier;

/**
 * This class represents a node
 */
public class Node {

    private String id;
    private Point position;
    private POI poi = null;

    /**
     * Constructs node
     * @param id ID of the node
     * @param position Position of the node
     */
    public Node(String id, Point position) {
        this.id = id;
        this.position = position;
    }

    /**
     * Constructs POI nodes
     * @param id ID of the node
     * @param position Position of the node
     * @param poi POI associated with this node
     */
    public Node(String id, Point position, POI poi) {
        this.id = id;
        this.position = position;
        this.poi = poi;
    }

    /**
     * This method returns ID of this node
     * @return ID of this node
     */
    public String getId() {
        return id;
    }

    /**
     * This method returns POI associated with this node
     * @return POI associated with this node
     */
    public POI getPoi() {
        return poi;
    }

    /**
     * This method returns position of this node
     * @return Position of this node
     */
    public Point getPosition() {
        return position;
    }

    /**
     * This method returns if this node is associated with a POI
     * @return True if this node is associated with a POI, else returns false
     */
    public boolean isPOI() {
        return poi != null;
    }

    /**
     * This method returns if this node is a station
     * @return True if this node is a station, else returns false
     */
    public boolean isStation() {
        return poi != null && poi.getType() == POIIdentifier.STATION;
    }

    /**
     * This method returns if this node is an attraction
     * @return True if this node is an attraction, else returns false
     */
    public boolean isAttraction() {
        return poi != null && poi.getType() == POIIdentifier.ATTRACTION;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id='" + id + '\'' +
                ", position=" + position +
                ", poi=" + poi +
                '}';
    }

}