package data;

import java.util.ArrayList;
import java.util.HashMap;

public class Network {

    private NetworkType type ;
    private ArrayList<Way>  ways;
    private HashMap<String,Line> lines = null ;


    public Network(NetworkType type, ArrayList<Way> ways){

        this.type = type ;
        this.ways = ways ;

    }

    public Network(NetworkType type, ArrayList<Way> ways, HashMap<String,Line> lines){

        this.type = type ;
        this.ways = ways ;
        this.lines = lines ;

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

    public ArrayList<Way> getWays() {
        return ways;
    }

    public HashMap<String, Line> getLines() {
        return lines;
    }
}
