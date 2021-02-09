package model;

import java.util.HashMap;

public class Network {

    private NetworkType type ;
    private HashMap<String,HashMap<String,WayIdentifier>>  ways;
    private HashMap<String,Line> lines = null ;
    private WayIdentifier[] acceptedWays;

    public Network(NetworkType type, WayIdentifier[] acceptedWays){
        this.type = type ;
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
        // Process...
    }

    public NetworkType getType() {
        return type;
    }

    public HashMap<String,HashMap<String,WayIdentifier>> getWays() {
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

}
