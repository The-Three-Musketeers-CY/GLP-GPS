package model;

import model.identifiers.NetworkIdentifier;
import model.identifiers.WayIdentifier;

import java.util.Arrays;
import java.util.HashMap;

/**
 * This class represent a network
 */
public class Network {

    private NetworkIdentifier type ;
    private HashMap<String,NodeWays> ways;
    private WayIdentifier[] acceptedWays;

    /**
     * Constructs the network
     * @param type Type of the network
     * @param acceptedWays Accepted ways list of the network
     */
    public Network(NetworkIdentifier type, WayIdentifier[] acceptedWays){
        this.type = type ;
        ways = new HashMap<>();
        this.acceptedWays = acceptedWays;
    }

    /**
     * This method add a way to the network
     * @param identifier Type of the way
     * @param nodeA Departure node
     * @param nodeB Arrival node
     * @param lineNumber Bus line number who is on the way
     */
    public void addWay(WayIdentifier identifier, Node nodeA, Node nodeB, String lineNumber){
        Way way = new Way(identifier, nodeA, nodeB, lineNumber);
        ways.putIfAbsent(nodeA.getId(), new NodeWays(nodeA));
        ways.get(nodeA.getId()).getWays().put(nodeB.getId(), way);
    }

    /**
     * This method returns type of this network
     * @return Identifier type of this network
     */
    public NetworkIdentifier getType() {
        return type;
    }

    /**
     * This method returns all nodes ways composing this network
     * @return Nodes ways list
     */
    public HashMap<String,NodeWays> getNodeWays() {
        return ways;
    }

    /**
     * This method returns all ways on this network for one node
     * @param node Node
     * @return Node ways list
     */
    public NodeWays getWaysFromNode(Node node){
        return ways.get(node.getId());
    }

    /**
     * This method returns if this way type is accepted on this network
     * @param wayIdentifier Type of the way
     * @return True if the way is accepted, else returns false
     */
    public boolean isAcceptedWayType(WayIdentifier wayIdentifier) {
        for (WayIdentifier acceptedWay : acceptedWays) {
            if (acceptedWay == wayIdentifier) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Network{" +
                "type=" + type +
                ", ways=" + ways +
                ", acceptedWays=" + Arrays.toString(acceptedWays) +
                '}';
    }

}
