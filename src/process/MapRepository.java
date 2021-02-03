package process;

import model.Node;
import model.WayIdentifier;

import java.util.HashMap;

public class MapRepository {
    private HashMap<String, Node> nodes;
    private static MapRepository instance = new MapRepository();
    private HashMap<String,HashMap<String, WayIdentifier>> ways;

    private MapRepository(){
        nodes = new HashMap<>();
        ways = new HashMap<>();
    }

    public static MapRepository getInstance() {
        return instance;
    }

    public void addNode(Node node){
        nodes.put(node.getId(), node);
    }
    public void addWay(String idNodeA, String idNodeB, WayIdentifier way){
        ways.putIfAbsent(idNodeA,new HashMap<>());
        ways.get(idNodeA).put(idNodeB, way);
    }
    public Node getNode(String id){
        return nodes.get(id);
    }
}
