package gui;

import model.Node;
import model.Way;
import model.WayType;
import model.identifiers.WayIdentifier;

import java.awt.*;

public class PaintStrategy {

    public static final Color DEFAULT_NODE_COLOR = Color.BLACK;
    public static final Color DEFAULT_WAY_COLOR = Color.WHITE;

    public static final Color DEFAULT_ATTRACTION_NODE_COLOR = Color.ORANGE;
    public static final Color DEFAULT_BUILDING_NODE_COLOR = new Color(112, 112, 112);
    public static final Color DEFAULT_STATION_NODE_COLOR = new Color(69, 69, 69);

    public static final Color DEFAULT_HIGHWAY_WAY_COLOR = new Color(255,255,86);

    public void paint(Node node, int decX, int decY, Graphics graphics) {
        graphics.setColor(getNodeTypeColor(node));
        graphics.fillOval(node.getPosition().getX() - 6 + decX,node.getPosition().getY() - 6 + decY,12,12);
    }

    public void paint(Way way, int decX, int decY, Graphics2D g2d) {
        if (way.getType().getIdentifier() != WayIdentifier.FOOT) g2d.setStroke(new BasicStroke(3));
        else g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
        g2d.setColor(getWayTypeColor(way.getType()));
        int x1 = way.getNode1().getPosition().getX() + decX;
        int x2 = way.getNode2().getPosition().getX() + decX;
        int y1 = way.getNode1().getPosition().getY() + decY;
        int y2 = way.getNode2().getPosition().getY() + decY;
        if (y1 > y2) {
            y1 = y1 + 1;
            y2 = y2 + 1;
        } else {
            y1 = y1 - 1;
            y2 = y2 - 1;
        }
        if (x1 > x2) {
            x1 = x1 - 1;
            x2 = x2 - 1;
        } else {
            x1 = x1 + 1;
            x2 = x2 + 1;
        }
        if (x1 == x2 && y1 > y2) {
            x1 = x1 + 1;
            x2 = x2 + 1;
        } else if (x1 == x2 && y1 < y2) {
            x1 = x1 - 1;
            x2 = x2 - 1;
        }
        if (y1 == y2 && x1 > x2) {
            y1 = y1 - 1;
            y2 = y2 - 1;
        } else if (y1 == y2 && x1 < x2) {
            y1 = y1 + 1;
            y2 = y2 + 1;
        }
        g2d.drawLine(x1,y1, x2, y2);
    }

    public void paint(Node node1, Node node2, int decX, int decY, Graphics2D g2d){
        if(node1 != null && node2 != null) {
            g2d.setStroke(new BasicStroke(7));
            g2d.setColor(new Color(124, 0, 187));
            g2d.drawLine(node1.getPosition().getX() + decX, node1.getPosition().getY() + decY, node2.getPosition().getX() + decX, node2.getPosition().getY() + decY);
        }
    }

    private Color getNodeTypeColor(Node node){
        Color color = Color.BLACK;
        if(node.getPoi() != null) {
            switch (node.getPoi().getType()) {
                case ATTRACTION:
                    color = DEFAULT_ATTRACTION_NODE_COLOR;
                    break;
                case BUILDING:
                    color = DEFAULT_BUILDING_NODE_COLOR;
                    break;
                case STATION:
                    color = DEFAULT_STATION_NODE_COLOR;
                    break;
                default:
                    color = DEFAULT_NODE_COLOR;
            }
        }
        return color;
    }

    private Color getWayTypeColor(WayType wayType) {
        Color color;
        switch (wayType.getIdentifier()) {
            case FOOT:
                color = Color.orange;
                break;
            case HIGHWAY:
                color = DEFAULT_HIGHWAY_WAY_COLOR;
                break;
            case METRO:
                color = Color.GREEN;
                break;
            case ROAD:
                color = new Color(189,189,189);
                break;
            case BOAT_LANE:
                color = new Color(38,196,236);
                break;
            case BUS_LANE:
                color = new Color(255, 155, 203);
                break;
            default:
                color = DEFAULT_WAY_COLOR;
        }
        return color;
    }

}
