package gui.view;

import model.Map;
import model.Network;
import model.Node;

import javax.swing.*;
import java.awt.*;

public class MapView extends JPanel{

    private Map map ;

    private int newDecX = 0;
    private int newDecY = 0;

    private int decPosX = 0;
    private int decPosY = 0;

    // TODO : just some tests :p
    private JLabel test = new JLabel("Echelle |");
    private JLabel testX = new JLabel("X : " + newDecX);
    private JLabel testY = new JLabel("Y : " + newDecY);

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
        testX.setText("X : " + newDecX);
        testY.setText("Y : " + newDecY);

        for (Network network : map.getNetworks().values()){
           for(String nodeId1 : network.getWays().keySet()){
               Node node1 = map.getNodes().get(nodeId1);
               for(String nodeId2 : network.getWays().get(nodeId1).keySet()){
                   Node node2 = map.getNodes().get(nodeId2);
                   g2d.setStroke(new BasicStroke(5));
                   g2d.drawLine(node1.getPosition().getX() + newDecX,node1.getPosition().getY() + newDecY, node2.getPosition().getX() + newDecX, node2.getPosition().getY() + newDecY);
               }
           }
        }
        for(Node node : map.getNodes().values()){
            g.setColor(getNodeTypeColor(node));
            g.fillOval(node.getPosition().getX()-6 + newDecX,node.getPosition().getY()-6 + newDecY,12,12);
        }
    }

    private Color getNodeTypeColor(Node node){
        Color color = Color.BLACK;
        if(node.getPoi() != null) {
            switch (node.getPoi().getType()) {
                case ATTRACTION: {
                    color = Color.ORANGE;
                    break;
                }
                case BUILDING: {
                    color = Color.BLUE;
                    break;
                }
                case STATION: {
                    color = Color.GRAY;
                    break;
                }
                default:
                    color = Color.BLACK;
            }
        }
        return color;
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
