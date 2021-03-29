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

    public Way(WayIdentifier identifier, Node nodeA, Node nodeB) {
        this.identifier = identifier;
        this.nodeA = nodeA;
        this.nodeB = nodeB;
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
            if(speeds.get(identifier) > maxSpeed && !transportsToAvoid.contains(TransportRepository.getInstance().getTransports().get(identifier))){
                maxSpeed = speeds.get(identifier) ;
            }
        }

        return maxSpeed ;
    }

    public Transport getHigherTransport(ArrayList<Transport> transportsToAvoid){
        HashMap<TransportIdentifier, Integer> speeds = getType().getSpeeds();

        int maxSpeed = 0 ;
        TransportIdentifier identifierHigherSpeed = null ;

        for(TransportIdentifier identifier : speeds.keySet()){
            if(speeds.get(identifier) > maxSpeed && !transportsToAvoid.contains(TransportRepository.getInstance().getTransports().get(identifier))){
                maxSpeed = speeds.get(identifier) ;
                identifierHigherSpeed = identifier ;
            }
        }

        return TransportRepository.getInstance().getTransports().get(identifierHigherSpeed);
    }

}
