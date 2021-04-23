package model;

import model.identifiers.TransportIdentifier;
import model.identifiers.WayIdentifier;
import model.repositories.TransportRepository;
import model.repositories.WayTypeRepository;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represents a way
 */
public class Way {

    private WayIdentifier identifier;
    private Node nodeA;
    private Node nodeB;
    private String lineNumber;

    /**
     * Constructs the way
     * @param identifier Type of the way
     * @param nodeA Departure node of the way
     * @param nodeB Arrival node of the way
     */
    public Way(WayIdentifier identifier, Node nodeA, Node nodeB) {
        this.identifier = identifier;
        this.nodeA = nodeA;
        this.nodeB = nodeB;
        lineNumber = null;
    }

    /**
     * Constructs the way with line
     * @param identifier Type of the way
     * @param nodeA Departure node of the way
     * @param nodeB Arrival node of the way
     * @param lineNumber Bus line number who is on the way
     */
    public Way(WayIdentifier identifier, Node nodeA, Node nodeB, String lineNumber) {
        this.identifier = identifier;
        this.nodeA = nodeA;
        this.nodeB = nodeB;
        this.lineNumber = lineNumber;
    }

    /**
     * This method returns the identifier type of this way
     * @return Identifier type of this way
     */
    public WayIdentifier getIdentifier() {
        return identifier;
    }

    /**
     * This method returns the type of this way
     * @return Type of this way
     */
    public WayType getType() {
        return WayTypeRepository.getInstance().getWayTypes().get(identifier);
    }

    /**
     * This method returns the departure node of this way
     * @return Departure node of this way
     */
    public Node getNodeA() {
        return nodeA;
    }

    /**
     * This method returns the arrival node of this way
     * @return Arrival node of this way
     */
    public Node getNodeB() {
        return nodeB;
    }

    /**
     * This method returns distance between the two nodes
     * @return Distance between the two nodes
     */
    public float getDistance() {
        Point node1Position = nodeA.getPosition();
        Point node2Position = nodeB.getPosition();
        return (float) Math.abs(Math.sqrt(Math.pow(node2Position.getX() - node1Position.getX(), 2) + Math.pow(node2Position.getY() - node1Position.getY(), 2)));
    }

    /**
     * This method returns the highest speed on this way
     * @param transportsToAvoid Exclude transport list
     * @return Highest speed on this way
     */
    public int getHigherSpeed(ArrayList<Transport> transportsToAvoid) {

        HashMap<TransportIdentifier, Integer> speeds = getType().getSpeeds();

        int maxSpeed = 0 ;

        for(TransportIdentifier identifier : speeds.keySet()){
            if(identifier != TransportIdentifier.BUS || lineNumber != null) {
                if (speeds.get(identifier) > maxSpeed && !transportsToAvoid.contains(TransportRepository.getInstance().getTransports().get(identifier))) {
                    maxSpeed = speeds.get(identifier);
                }
            }
        }

        return maxSpeed ;
    }

    /**
     * This method returns the transport with the best price on this way
     * @param transportsToAvoid Excludes transport list
     * @return Transport with the best price on this way
     */
    public float getBestPrice(ArrayList<Transport> transportsToAvoid){

        TransportIdentifier[] transportIdentifiers = getType().getAvailableTransports();

        float minCost = -1 ;

        for(TransportIdentifier identifier : transportIdentifiers){
            if(identifier != TransportIdentifier.BUS || lineNumber != null) {
                if ((TransportRepository.getInstance().getTransports().get(identifier).getCost() < minCost || minCost == -1) && !transportsToAvoid.contains(TransportRepository.getInstance().getTransports().get(identifier))) {
                    minCost = TransportRepository.getInstance().getTransports().get(identifier).getCost();
                }
            }
        }

        return minCost;
    }

    /**
     * This method returns the transport with the highest speed on this way
     * @param transportsToAvoid Excludes transport list
     * @return Transport with the highest speed on this way
     */
    public Transport getHigherSpeedTransport(ArrayList<Transport> transportsToAvoid){
        HashMap<TransportIdentifier, Integer> speeds = getType().getSpeeds();

        int maxSpeed = 0 ;
        TransportIdentifier identifierHigherSpeed = null;

        for(TransportIdentifier identifier : speeds.keySet()){
            if(identifier != TransportIdentifier.BUS || lineNumber != null) {
                if (speeds.get(identifier) > maxSpeed && !transportsToAvoid.contains(TransportRepository.getInstance().getTransports().get(identifier))) {
                    maxSpeed = speeds.get(identifier);
                    identifierHigherSpeed = identifier;
                }
            }
        }

        return TransportRepository.getInstance().getTransports().get(identifierHigherSpeed);
    }

    /**
     * This method returns the transport with the cheaper price on this way
     * @param transportsToAvoid Excludes transport list
     * @return Transport with the cheaper price on this way
     */
    public Transport getCheaperTransport(ArrayList<Transport> transportsToAvoid){

        TransportIdentifier[] transportIdentifiers = getType().getAvailableTransports();

        float minCost = -1 ;
        TransportIdentifier identifierCheaper = null;

        for(TransportIdentifier identifier : transportIdentifiers){
            if(identifier != TransportIdentifier.BUS || lineNumber != null) {
                if ((TransportRepository.getInstance().getTransports().get(identifier).getCost() < minCost || minCost == -1) && !transportsToAvoid.contains(TransportRepository.getInstance().getTransports().get(identifier))) {
                    minCost = TransportRepository.getInstance().getTransports().get(identifier).getCost();
                    identifierCheaper = identifier;
                }
            }
        }

        return TransportRepository.getInstance().getTransports().get(identifierCheaper);

    }

    /**
     * This method returns the bus line number associated to this way
     * @return Bus line number associated to this way
     */
    public String getLineNumber() {
        return lineNumber;
    }

    @Override
    public String toString() {
        return "Way{" +
                "identifier=" + identifier +
                ", nodeA=" + nodeA +
                ", nodeB=" + nodeB +
                ", lineNumber='" + lineNumber + '\'' +
                '}';
    }

}
