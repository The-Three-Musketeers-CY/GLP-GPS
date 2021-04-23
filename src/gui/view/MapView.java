package gui.view;

import config.GPSConfig;
import gui.PaintStrategy;
import model.*;
import model.identifiers.WayIdentifier;

import javax.swing.*;
import java.awt.*;

/**
 * This class represents the map view panel of the GPS
 */
public class MapView extends JPanel{

    private static final long serialVersionUID = 1L;

    private Map map;
    private Itinerary itinerary;

    private int newDecX = 0;
    private int newDecY = 0;

    private int decPosX = 0;
    private int decPosY = 0;

    private PaintStrategy paintStrategy = new PaintStrategy();

    private JLabel test = new JLabel("Décalage |");
    private JLabel testX = new JLabel();
    private JLabel testY = new JLabel();

    /**
     * This methods construct the map view panel of the GPS from the GPS map
     * @param map GPS map
     */
    public MapView(Map map){
        this.map = map;

        // Display dec on map
        add(test);
        add(testX);
        add(testY);
    }

    /**
     * This method draws the GPS map view with the offset on the x and y axes
     * @param g Graphic component
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g.create();

        testX.setText("X : " + (GPSConfig.MAP_SIZE_WIDTH - (GPSConfig.MAP_SIZE_WIDTH + newDecX)));
        testY.setText("Y : " + (GPSConfig.MAP_SIZE_HEIGHT - (GPSConfig.MAP_SIZE_HEIGHT + newDecY)));

        for (Network network : map.getNetworks().values()) {
           for (NodeWays nodeWays : network.getNodeWays().values()) {
               for (Way way : nodeWays.getWays().values()) {
                   paintStrategy.paint(way, newDecX, newDecY, g2d);
               }
           }
        }

        //Draw itinerary
        if (itinerary != null) {
            Node previousNode = null;
            for (StepItinerary stepItinerary : itinerary.getStepItineraries()) {
                for (Node node : stepItinerary.getStepItineraryNodes()) {
                    if (previousNode != null) {
                        paintStrategy.paint(previousNode, node, newDecX, newDecY, g2d);
                    }
                    previousNode = node;
                }
            }
        }

        for (Node node : map.getNodes().values()) {
            if (node.isPOI()) paintStrategy.paint(node, newDecX, newDecY, g);
        }
    }

    /**
     * This method retrieves the X-dec of the map view
     * @return The X-dec of the map view
     */
    public int getDecPosX() {
        return decPosX;
    }

    /**
     * This method retrieves the Y-dec of the map view
     * @return The Y-dec of the map view
     */
    public int getDecPosY() {
        return decPosY;
    }

    /**
     * This method retrieves the new X-dec of the map view
     * @return The new X-dec of the map view
     */
    public int getNewDecX() {
        return newDecX;
    }

    /**
     * This method retrieves the new Y-dec of the map view
     * @return The new Y-dec of the map view
     */
    public int getNewDecY() {
        return newDecY;
    }

    /**
     * This method sets the X-dec of the map view
     * @param decPosX Offset of the X-coordinate
     */
    public void setDecPosX(int decPosX) {
        this.decPosX = decPosX;
    }

    /**
     * This method sets the Y-dec of the map view
     * @param decPosY Offset of the Y-coordinate
     */
    public void setDecPosY(int decPosY) {
        this.decPosY = decPosY;
    }

    /**
     * This method sets the new X-dec of the map view
     * @param newDecX Offset of the new X-coordinate
     */
    public void setNewDecX(int newDecX) {
        this.newDecX = newDecX;
    }

    /**
     * This method sets the new Y-dec of the map view
     * @param newDecY Offset of the new Y-coordinate
     */
    public void setNewDecY(int newDecY) {
        this.newDecY = newDecY;
    }

    /**
     * This method sets the itinerary
     * @param itinerary Itinerary displayed on map
     */
    public void setItinerary(Itinerary itinerary) {
        this.itinerary = itinerary;
    }

}
