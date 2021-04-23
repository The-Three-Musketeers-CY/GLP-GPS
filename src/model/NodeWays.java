package model;

import java.util.HashMap;

/**
 * This class represents node ways
 */
public class NodeWays {

    private Node node;
    private HashMap<String,Way> ways;

    /**
     * Construct node ways
     * @param node Node associated to these ways
     */
    public NodeWays(Node node) {
        this.node = node;
        ways = new HashMap<>();
    }

    /**
     * This method returns the node associated to these ways
     * @return Node associated to these ways
     */
    public Node getNode() {
        return node;
    }

    /**
     * This method returns all ways associated to this node
     * @return Way list associated to this node
     */
    public HashMap<String, Way> getWays() {
        return ways;
    }

}
