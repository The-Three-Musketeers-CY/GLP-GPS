package data;

import java.util.ArrayList;
import java.util.HashMap;

public class Network {

    private NetworkType type ;
    private HashMap<String,HashMap<String,WayIdentifier>>  ways;
    private HashMap<String,Line> lines = null ;


    public Network(NetworkType type){

        this.type = type ;

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
}
