package model;

import model.identifiers.NetworkIdentifier;
import model.identifiers.WayIdentifier;

import java.util.Arrays;
import java.util.HashMap;

public class Network {

    private NetworkIdentifier type ;
    private HashMap<String,NodeWays> ways;
    private WayIdentifier[] acceptedWays;

    public Network(NetworkIdentifier type, WayIdentifier[] acceptedWays){
        this.type = type ;
        ways = new HashMap<>();
        this.acceptedWays = acceptedWays;
    }

    public void addWay(WayIdentifier identifier, Node node1, Node node2, String lineNumber){
        Way way = new Way(identifier, node1, node2, lineNumber);
        ways.putIfAbsent(node1.getId(), new NodeWays(node1));
        ways.get(node1.getId()).getWays().put(node2.getId(), way);
    }

    public NetworkIdentifier getType() {
        return type;
    }

    public HashMap<String,NodeWays> getNodeWays() {
        return ways;
    }

    public NodeWays getWaysFromNode(Node node){
        return ways.get(node.getId());
    }

    public boolean isAcceptedWay(WayIdentifier wayIdentifier) {
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
