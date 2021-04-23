package process.factories;

import model.Node;
import model.POI;
import model.identifiers.POIIdentifier;
import model.Point;

/**
 * This factory class contains nodes constructing methods
 */
public class NodeFactory {

    private static Point createPoint(int x, int y){
        return new Point(x, y);
    }

    private static POI createPOI(String name, POIIdentifier type){
        return new POI(name, type);
    }

    /**
     * This method built POI's nodes
     * @param id the node's id
     * @param x the position in the x axis
     * @param y the position int the y axis
     * @param name the POI's name
     * @param type the POI's type
     * @return the new node
     * @see POIIdentifier
     */
    public static Node create(String id, int x, int y, String name, POIIdentifier type){
        return new Node(id, createPoint(x, y), createPOI(name, type));
    }

    /**
     * This method built simple nodes
     * @param id the node's id
     * @param x the position in the x axis
     * @param y the position int the y axis
     * @return the new node
     */
    public static Node create(String id, int x, int y){
        return new Node(id, createPoint(x, y));
    }

}
