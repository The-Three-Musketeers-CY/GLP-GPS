package gui;

import model.Node;
import model.Way;
import model.WayType;
import model.identifiers.WayIdentifier;

import java.awt.*;

/**
 * This class represents how we will draw elements on the map view
 */
public class PaintStrategy {

    private static final Color DEFAULT_ITINERARY_COLOR = new Color(124, 0, 187);

    private static final Color DEFAULT_NODE_COLOR = Color.BLACK;
    private static final Color DEFAULT_WAY_COLOR = Color.WHITE;

    private static final Color DEFAULT_ATTRACTION_NODE_COLOR = Color.ORANGE;
    private static final Color DEFAULT_BUILDING_NODE_COLOR = new Color(112, 112, 112);
    private static final Color DEFAULT_STATION_NODE_COLOR = new Color(69, 69, 69);

    private static final Color DEFAULT_HIGHWAY_WAY_COLOR = new Color(255,255,86);
    private static final Color DEFAULT_ROAD_WAY_COLOR = new Color(189, 189, 189);
    private static final Color DEFAULT_BOAT_LANE_WAY_COLOR = new Color(38, 196, 236);
    private static final Color DEFAULT_BUS_LANE_WAY_COLOR = new Color(255, 155, 203);
    private static final Color DEFAULT_CYCLE_LANE_WAY_COLOR = new Color(255, 128, 0);

    /**
     * Draw node on map view
     * @param node node to draw
     * @param decX x-dec of the map view
     * @param decY y-dec of the map view
     * @param graphics graphic component
     */
    public void paint(Node node, int decX, int decY, Graphics graphics) {
        graphics.setColor(getNodeTypeColor(node));
        graphics.fillOval(node.getPosition().getX() - 6 + decX,node.getPosition().getY() - 6 + decY,12,12);
    }

    /**
     * Draw way on map view
     * @param way way to draw
     * @param decX x-dec of the map view
     * @param decY y-dec of the map view
     * @param g2d 2D graphic component
     */
    public void paint(Way way, int decX, int decY, Graphics2D g2d) {
        if (way.getType().getIdentifier() != WayIdentifier.FOOT) g2d.setStroke(new BasicStroke(3));
        else g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
        g2d.setColor(getWayTypeColor(way.getType()));
        int x1 = way.getNodeA().getPosition().getX() + decX;
        int x2 = way.getNodeB().getPosition().getX() + decX;
        int y1 = way.getNodeA().getPosition().getY() + decY;
        int y2 = way.getNodeB().getPosition().getY() + decY;
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
        g2d.drawLine(x1, y1, x2, y2);
    }

    /**
     * Draw a step from the itinerary on map view
     * @param node1 Departure node
     * @param node2 Arrival node
     * @param decX x-dec of the map view
     * @param decY y-dec of the map view
     * @param g2d 2D graphic component
     */
    public void paint(Node node1, Node node2, int decX, int decY, Graphics2D g2d){
        if(node1 != null && node2 != null) {
            g2d.setStroke(new BasicStroke(7, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
            g2d.setColor(DEFAULT_ITINERARY_COLOR);
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
                color = DEFAULT_ROAD_WAY_COLOR;
                break;
            case BOAT_LANE:
                color = DEFAULT_BOAT_LANE_WAY_COLOR;
                break;
            case BUS_LANE:
                color = DEFAULT_BUS_LANE_WAY_COLOR;
                break;
            case CYCLE_LANE:
                color = DEFAULT_CYCLE_LANE_WAY_COLOR;
                break;
            default:
                color = DEFAULT_WAY_COLOR;
        }
        return color;
    }

}
