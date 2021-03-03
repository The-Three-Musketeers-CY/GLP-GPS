package gui;

import model.Node;
import model.Way;
import model.WayType;
import model.identifiers.WayIdentifier;

import java.awt.*;

public class PaintStrategy {

    public static final Color DEFAULT_NODE_COLOR = Color.BLACK;
    public static final Color DEFAULT_WAY_COLOR = Color.DARK_GRAY;

    public static final Color DEFAULT_ATTRACTION_NODE_COLOR = Color.ORANGE;
    public static final Color DEFAULT_BUILDING_NODE_COLOR = Color.BLUE;
    public static final Color DEFAULT_STATION_NODE_COLOR = Color.GRAY;

    public static final Color DEFAULT_HIGHWAY_WAY_COLOR = Color.RED;

    public void paint(Node node, int decX, int decY, Graphics graphics) {
        graphics.setColor(getNodeTypeColor(node));
        graphics.fillOval(node.getPosition().getX() - 6 + decX,node.getPosition().getY() - 6 + decY,12,12);
    }

    public void paint(Way way, int decX, int decY, Graphics2D g2d) {
        if (way.getType().getIdentifier() != WayIdentifier.FOOT) g2d.setStroke(new BasicStroke(5));
        else g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
        g2d.setColor(getWayTypeColor(way.getType()));
        g2d.drawLine(way.getNode1().getPosition().getX() + decX,way.getNode1().getPosition().getY() + decY, way.getNode2().getPosition().getX() + decX, way.getNode2().getPosition().getY() + decY);
    }

    private Color getNodeTypeColor(Node node){
        Color color = Color.BLACK;
        if(node.getPoi() != null) {
            switch (node.getPoi().getType()) {
                case ATTRACTION: {
                    color = DEFAULT_ATTRACTION_NODE_COLOR;
                    break;
                }
                case BUILDING: {
                    color = DEFAULT_BUILDING_NODE_COLOR;
                    break;
                }
                case STATION: {
                    color = DEFAULT_STATION_NODE_COLOR;
                    break;
                }
                default:
                    color = DEFAULT_NODE_COLOR;
            }
        }
        return color;
    }

    private Color getWayTypeColor(WayType wayType) {
        Color color;
        switch (wayType.getIdentifier()) {
            case HIGHWAY:
                color = DEFAULT_HIGHWAY_WAY_COLOR;
                break;
            default:
                color = DEFAULT_WAY_COLOR;
        }
        return color;
    }

}
