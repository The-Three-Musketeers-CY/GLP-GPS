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

    public void addLine(String number){

        line = new Line() ;
        lines.put(number, line);

    }

    public void addStationToLine(String numLine, String name, Node node){


    }

    public String getType() {
        return type;
    }

    public ArrayList<Way> getWays() {
        return ways;
    }

    public HashMap<String, Line> getLines() {
        return lines;
    }
}
