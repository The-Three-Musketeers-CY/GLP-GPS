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

    public void addWayToNode(String idNodeA, String idNodeB, WayIdentifier way){
        ways.putIfAbsent(idNodeA, new HashMap<>());
        ways.get(idNodeA).put(idNodeB, way);
    }

    public void addNodeWays(String idNodeA, HashMap<String, WayIdentifier> ways) {
        this.ways.put(idNodeA, ways);
    }

    public Node getNode(String id){
        return nodes.get(id);
    }

    public Node getNode(float x, float y) {
        for (Node node : nodes.values()) {
            if (node.getPosition().getX() == x && node.getPosition().getY() == y) {
                return node;
            }
        }
        return null;
    }

    public HashMap<String, Node> getNodes() {
        return nodes;
    }

    public HashMap<String, WayIdentifier> getNodeWays(String nodeID) {
        return ways.get(nodeID);
    }

}
