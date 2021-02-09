package model;

import model.identifiers.NetworkIdentifier;

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

    public HashMap<NetworkIdentifier, Network> getNetworks() {
        return networks;
    }

    @Override
    public String toString() {
        return "Map{" +
                "nodes=" + nodes +
                '}';
    }

}
