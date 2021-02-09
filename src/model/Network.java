package model;

import model.identifiers.NetworkIdentifier;
import model.identifiers.WayIdentifier;

import java.util.Arrays;
import java.util.HashMap;

public class Network {

    private NetworkIdentifier type ;
    private HashMap<String,HashMap<String,WayType>> ways;
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

    public void addWay(WayType type, Node node1, Node node2){
        ways.putIfAbsent(node1.getId(), new HashMap<>());
        ways.get(node1.getId()).put(node2.getId(), type);
    }

    public NetworkIdentifier getType() {
        return type;
    }

    public HashMap<String,HashMap<String,WayType>> getWays() {
        return ways;
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
