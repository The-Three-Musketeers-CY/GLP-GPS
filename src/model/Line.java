package model;

import java.awt.*;
import java.util.ArrayList;

public class Line {

    private String lineNumber;
    private Color lineColor;
    private ArrayList<Node> nodes = null;

    public Line(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void addStation(Node station){
        nodes.add(station);
    }

}
