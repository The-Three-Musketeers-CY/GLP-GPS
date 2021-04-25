package model;

import model.identifiers.NetworkIdentifier;
import model.identifiers.POIIdentifier;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represents the map
 */
public class Map {

    private HashMap<String,Node> nodes;
    private HashMap<NetworkIdentifier,Network> networks;

    /**
     * Constructs a map
     */
    public Map() {
        nodes = new HashMap<>();
        networks = new HashMap<>();
    }

    /**
     * This method returns all nodes composing this map
     * @return Nodes composing this map
     */
    public HashMap<String, Node> getNodes() {
         return nodes;
    }

    /**
     * This method returns all touristic nodes composing this map
     * @return Touristic nodes composing this map
     */
    public ArrayList<Node> getTouristicNodes() {

        ArrayList<Node> touristicNodes = new ArrayList<>();

        for (Node node : getNodes().values()){
            if(node.isPOI() && node.getPoi().getType() == POIIdentifier.ATTRACTION){
                touristicNodes.add(node);
            }
        }

        return touristicNodes;
    }

    /**
     * This method returns one node from the map
     * @param id ID of the node
     * @return Node corresponding to the ID
     */
    public Node getNodeFromId(String id){
        return nodes.get(id);
    }

    /**
     * This method returns one node from the map
     * @param nodeName Name of the node
     * @return Node corresponding to the name
     */
    public Node getNodeFromName(String nodeName){
        for(Node node : nodes.values()){
            if(node.isPOI() && node.getPoi().getName().equals(nodeName)) return node ;
        }
        return null ;
    }

    /**
     * This method returns all networks composing this map
     * @return Networks list
     */
    public HashMap<NetworkIdentifier, Network> getNetworks() {
        return networks;
    }

    /**
     * This method returns all nodes names
     * @return Nodes names list
     */
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
