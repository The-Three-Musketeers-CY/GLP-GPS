package model;

import model.identifiers.TransportIdentifier;
import model.identifiers.WayIdentifier;
import model.repositories.WayTypeRepository;

import java.util.HashMap;

public class Way {

    private WayIdentifier identifier;
    private Node node1;
    private Node node2;

    public Way(WayIdentifier identifier, Node node1, Node node2) {
        this.identifier = identifier;
        this.node1 = node1;
        this.node2 = node2;
    }

    public WayIdentifier getIdentifier() {
        return identifier;
    }

    public WayType getType() {
        return WayTypeRepository.getInstance().getWayTypes().get(identifier);
    }

    public Node getNode1() {
        return node1;
    }

    public Node getNode2() {
        return node2;
    }

    public float getDistance() {
        Point node1Position = node1.getPosition();
        Point node2Position = node2.getPosition();
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
