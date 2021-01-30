package data;

import java.util.ArrayList;

public class Line {

    private String numLine ;
    private ArrayList<Node> nodes = null;

    public Line(String numLine) {
        this.numLine = numLine;
    }

    public void addStation(Node station){
        nodes.add(station);
    }

}
