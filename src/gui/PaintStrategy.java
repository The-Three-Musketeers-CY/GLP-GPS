package gui;

import gui.view.MapView;
import model.Node;

import java.awt.*;

public class PaintStrategy {

    private MapView mapView;

    public PaintStrategy(MapView mapView) {
        this.mapView = mapView;
    }

    public void paint(Node node, Graphics graphics) {
        graphics.setColor(getNodeTypeColor(node));
        graphics.fillOval(node.getPosition().getX() - 6 + mapView.getNewDecX(),node.getPosition().getY() - 6 + mapView.getNewDecY(),12,12);
    }

    public void paint(Node node1, Node node2, Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(5));
        g2d.drawLine(node1.getPosition().getX() + mapView.getNewDecX(),node1.getPosition().getY() + mapView.getNewDecY(), node2.getPosition().getX() + mapView.getNewDecX(), node2.getPosition().getY() + mapView.getNewDecY());
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
