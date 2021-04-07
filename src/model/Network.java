package model;

import model.identifiers.NetworkIdentifier;
import model.identifiers.WayIdentifier;

import java.util.Arrays;
import java.util.HashMap;

public class Network {

    private NetworkIdentifier type ;
    private HashMap<String,NodeWays> ways;
    private HashMap<String,Line> lines;
    private WayIdentifier[] acceptedWays;

    public Network(NetworkIdentifier type, WayIdentifier[] acceptedWays){
        this.type = type ;
        ways = new HashMap<>();
        lines = new HashMap<>();
        this.acceptedWays = acceptedWays;
    }

    public void addLine(String numLine){
        Line line = new Line(numLine) ;
        lines.put(numLine, line);
    }

    public void addStationToLine(String numLine,Node node){
        Line line = lines.get(numLine) ;
        line.addStation(node);
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

    public HashMap<String, Line> getLines() {
        return lines;
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
                ", lines=" + lines +
                ", acceptedWays=" + Arrays.toString(acceptedWays) +
                '}';
    }

}
