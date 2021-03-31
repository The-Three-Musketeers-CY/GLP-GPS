package model;

import java.util.HashMap;

public class NodeWays {

    private Node node;
    private HashMap<String,Way> ways;

    public NodeWays(Node node) {
        this.node = node;
        ways = new HashMap<>();
    }

    public Node getNode() {
        return node;
    }

    public HashMap<String, Way> getWays() {
        return ways;
    }

}
