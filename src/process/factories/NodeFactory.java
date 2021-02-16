package process.factories;

import model.Node;
import model.POI;
import model.identifiers.POIIdentifier;
import model.Point;

public class NodeFactory {

    private static Point creatPoint(int x, int y){
        return new Point(x, y);
    }

    private static POI creatPOI(String name, POIIdentifier type){
        return new POI(name, type);
    }

    public static Node create(String id, int x, int y, String name, POIIdentifier type){
        return new Node(id, creatPoint(x, y), creatPOI(name, type));
    }

    public static Node create(String id, int x, int y){
        return new Node(id, creatPoint(x, y));
    }

}
