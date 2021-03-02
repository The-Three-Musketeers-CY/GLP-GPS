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

    public void addWayToNode(Way way){
        ways.putIfAbsent(way.getNode1().getId(), new NodeWays(way.getNode1()));
        ways.get(way.getNode1().getId()).getWays().put(way.getNode2().getId(), way);
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
