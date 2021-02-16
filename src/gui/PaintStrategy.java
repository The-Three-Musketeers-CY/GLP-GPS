package gui;

import model.Node;

import java.awt.*;

public class PaintStrategy {

    public void paint(Node node, int decX, int decY, Graphics graphics) {
        graphics.setColor(getNodeTypeColor(node));
        graphics.fillOval(node.getPosition().getX() - 6 + decX,node.getPosition().getY() - 6 + decY,12,12);
    }

    public void paint(Node node1, Node node2, int decX, int decY, Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(5));
        g2d.drawLine(node1.getPosition().getX() + decX,node1.getPosition().getY() + decY, node2.getPosition().getX() + decX, node2.getPosition().getY() + decY);
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

}
