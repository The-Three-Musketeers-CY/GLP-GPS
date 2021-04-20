package model;

import model.identifiers.TransportIdentifier;
import model.identifiers.WayIdentifier;
import model.repositories.TransportRepository;
import model.repositories.WayTypeRepository;

import java.util.ArrayList;
import java.util.HashMap;

public class Way {

    private WayIdentifier identifier;
    private Node nodeA;
    private Node nodeB;
    private String lineNumber;

    public Way(WayIdentifier identifier, Node nodeA, Node nodeB) {
        this.identifier = identifier;
        this.nodeA = nodeA;
        this.nodeB = nodeB;
        lineNumber = null;
    }
    public Way(WayIdentifier identifier, Node nodeA, Node nodeB, String lineNumber) {
        this.identifier = identifier;
        this.nodeA = nodeA;
        this.nodeB = nodeB;
        this.lineNumber = lineNumber;
    }
    public WayIdentifier getIdentifier() {
        return identifier;
    }

    public WayType getType() {
        return WayTypeRepository.getInstance().getWayTypes().get(identifier);
    }

    public Node getNodeA() {
        return nodeA;
    }

    public Node getNodeB() {
        return nodeB;
    }

    public float getDistance() {
        Point node1Position = nodeA.getPosition();
        Point node2Position = nodeB.getPosition();
        return (float) Math.abs(Math.sqrt(Math.pow(node2Position.getX() - node1Position.getX(), 2) + Math.pow(node2Position.getY() - node1Position.getY(), 2)));
    }

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

    public Transport getHigherTransport(ArrayList<Transport> transportsToAvoid){
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
