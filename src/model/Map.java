package model;

import model.identifiers.NetworkIdentifier;
import model.identifiers.POIIdentifier;

import java.util.ArrayList;
import java.util.HashMap;

public class Map {

    private HashMap<String,Node> nodes;
    private HashMap<NetworkIdentifier,Network> networks;

    public Map() {
        nodes = new HashMap<>();
        networks = new HashMap<>();
    }

    public HashMap<String, Node> getNodes() {
         return nodes;
    }

    public ArrayList<Node> getTouristicNodes() {

        ArrayList<Node> touristicNodes = new ArrayList<>();

        for (Node node : getNodes().values()){
            if(node.isPOI() && node.getPoi().getType() == POIIdentifier.ATTRACTION){
                touristicNodes.add(node);
            }
        }

        return touristicNodes;
    }

    public Node getNodeFromId(String id){
        return nodes.get(id);
    }
    public Node getNodeFromName(String nodeName){
        for(Node node : nodes.values()){
            if(node.isPOI() && node.getPoi().getName().equals(nodeName)) return node ;
        }
        return null ;
    }

    public HashMap<NetworkIdentifier, Network> getNetworks() {
        return networks;
    }

    public ArrayList<String> getAllNodeNames(){

        ArrayList<String> nodeNames = new ArrayList<>();

        for(Node node : getNodes().values()){
            if(node.isPOI()) nodeNames.add(node.getPoi().getName());
        }

        return nodeNames ;
    }

    @Override
    public String toString() {
        return "Map{" +
                "nodes=" + nodes +
                '}';
    }

}
