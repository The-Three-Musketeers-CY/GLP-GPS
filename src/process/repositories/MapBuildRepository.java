package process.repositories;

import model.Node;
import model.NodeWays;
import model.Way;

import java.util.HashMap;

/**
 * This repository class contains all nodes & ways from the XML map before building map
 */
public class MapBuildRepository {

    private static MapBuildRepository instance = new MapBuildRepository();

    private HashMap<String, Node> nodes;
    private HashMap<String, NodeWays> ways;

    private MapBuildRepository(){
        nodes = new HashMap<>();
        ways = new HashMap<>();
    }

    /**
     * This method returns the only instance of this class
     * @return instance of this class
     */
    public static MapBuildRepository getInstance() {
        return instance;
    }

    /**
     * This methods put a node object into map node list
     * @param node node of the map
     */
    public void addNode(Node node){
        nodes.put(node.getId(), node);
    }

    /**
     * This methods put a way object into map way list
     * @param way way of the map
     */
    public void addWayToNode(Way way){
        ways.putIfAbsent(way.getNodeA().getId(), new NodeWays(way.getNodeA()));
        ways.get(way.getNodeA().getId()).getWays().put(way.getNodeB().getId(), way);
    }

    /**
     * This methods returns a node by its id
     * @param id id of the node
     * @return node
     */
    public Node getNode(String id){
        return nodes.get(id);
    }

    /**
     * This methods returns a node by its location
     * @param x x position of the node
     * @param y y position of the node
     * @return node
     */
    public Node getNode(float x, float y) {
        for (Node node : nodes.values()) {
            if (node.getPosition().getX() == x && node.getPosition().getY() == y) {
                return node;
            }
        }
        return null;
    }

    /**
     * This methods returns the node list
     * @return dictionnary of all map nodes
     */
    public HashMap<String, Node> getNodes() {
        return nodes;
    }

    /**
     * This methods returns all ways connected to one node
     * @param nodeID id of the node
     * @return NodeWays object containing all ways connected to this node
     */
    public NodeWays getNodeWays(String nodeID) {
        return ways.get(nodeID);
    }

}
