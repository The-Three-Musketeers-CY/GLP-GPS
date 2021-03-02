package process.repositories;

import model.Node;
import model.NodeWays;
import model.Way;
import model.identifiers.WayIdentifier;

import java.util.HashMap;

public class MapRepository {

    private HashMap<String, Node> nodes;
    private static MapRepository instance = new MapRepository();
    private HashMap<String, NodeWays> ways;

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

    public void addWayToNode(String idNodeA, String idNodeB, WayIdentifier identifier){
        ways.putIfAbsent(idNodeA, new NodeWays(nodes.get(idNodeA)));
        ways.get(idNodeA).getWays().put(idNodeB, new Way(identifier, nodes.get(idNodeA), nodes.get(idNodeB)));
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

    public NodeWays getNodeWays(String nodeID) {
        return ways.get(nodeID);
    }

}
