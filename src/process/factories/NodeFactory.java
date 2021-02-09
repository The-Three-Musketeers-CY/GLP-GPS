package process.factories;

import model.Node;
import model.POI;
import model.identifiers.POIIdentifier;
import model.Point;

import java.util.UUID;

public class NodeFactory {

    private static Point creatPoint(float x, float y){
        return new Point(x, y);
    }

    private static  POI creatPOI(String name, POIIdentifier type){
        return new POI(name, type);
    }

    public static Node creatNode(float x, float y, String name, POIIdentifier type){
        String id = UUID.randomUUID().toString();
        return new Node(id, creatPoint(x, y), creatPOI(name, type));
    }
}
