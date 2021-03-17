package model;

import model.identifiers.TransportIdentifier;
import model.identifiers.WayIdentifier;
import model.repositories.WayTypeRepository;

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

    public int getHigherSpeed() {
        HashMap<TransportIdentifier, Integer> speeds = getType().getSpeeds();
        int maxSpeed = 0 ;
        for(int speed : speeds.values()){
            if(speed>maxSpeed) maxSpeed = speed ;
        }
        return maxSpeed ;
    }

}
