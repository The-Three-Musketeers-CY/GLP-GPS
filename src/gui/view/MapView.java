package gui.view;

import config.GPSConfig;
import gui.PaintStrategy;
import model.*;
import model.identifiers.WayIdentifier;

import javax.swing.*;
import java.awt.*;

public class MapView extends JPanel{

    private static final long serialVersionUID = 1L;

    private Map map ;

    private int newDecX = 0;
    private int newDecY = 0;

    private int decPosX = 0;
    private int decPosY = 0;

    private PaintStrategy paintStrategy = new PaintStrategy();

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
        testY.setText("Y : " + (GPSConfig.MAP_SIZE_HEIGHT - (GPSConfig.MAP_SIZE_HEIGHT + newDecY)));

        for (Network network : map.getNetworks().values()){
           for(NodeWays nodeWays : network.getNodeWays().values()){
               for(Way way : nodeWays.getWays().values()){
                   if (way.getIdentifier() != WayIdentifier.FOOT) paintStrategy.paint(way, newDecX, newDecY, g2d);
               }
           }
        }
        for(Node node : map.getNodes().values()){
            paintStrategy.paint(node, newDecX, newDecY, g);
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
