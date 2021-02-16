package gui.view;

import config.GPSConfig;
import gui.PaintStrategy;
import model.Map;
import model.Network;
import model.Node;

import javax.swing.*;
import java.awt.*;

public class MapView extends JPanel{

    private static final long serialVersionUID = 1L;

    private Map map ;

    private int newDecX = 0;
    private int newDecY = 0;

    private int decPosX = 0;
    private int decPosY = 0;

    private PaintStrategy paintStrategy = new PaintStrategy(this);

    // TODO : just some tests :p
    private JLabel test = new JLabel("Décalage |");
    private JLabel testX = new JLabel();
    private JLabel testY = new JLabel();

    public MapView(Map map){
        this.map = map;

        // TODO : just some tests :p
        add(test);
        add(testX);
        add(testY);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g.create();

        // TODO : just some tests :p
        testX.setText("X : " + (GPSConfig.MAP_SIZE_WIDTH - (GPSConfig.MAP_SIZE_WIDTH + newDecX)));
        testY.setText("Y : " + (GPSConfig.MAP_SIZE_WIDTH - (GPSConfig.MAP_SIZE_WIDTH + newDecX)));

        for (Network network : map.getNetworks().values()){
           for(String nodeId1 : network.getWays().keySet()){
               Node node1 = map.getNodes().get(nodeId1);
               for(String nodeId2 : network.getWays().get(nodeId1).keySet()){
                   Node node2 = map.getNodes().get(nodeId2);
                   paintStrategy.paint(node1, node2, g2d);
               }
           }
        }
        for(Node node : map.getNodes().values()){
            paintStrategy.paint(node, g);
        }
    }

    public int getDecPosX() {
        return decPosX;
    }

    public int getDecPosY() {
        return decPosY;
    }

    public int getNewDecX() {
        return newDecX;
    }

    public int getNewDecY() {
        return newDecY;
    }

    public void setDecPosX(int decPosX) {
        this.decPosX = decPosX;
    }

    public void setDecPosY(int decPosY) {
        this.decPosY = decPosY;
    }

    public void setNewDecX(int newDecX) {
        this.newDecX = newDecX;
    }

    public void setNewDecY(int newDecY) {
        this.newDecY = newDecY;
    }

}
