package model;

import model.enums.NetworkType;

import java.util.HashMap;

public class Map {

    private HashMap<String,Node> nodes;
    private HashMap<NetworkType,Network> networks;

    public Map() {
        nodes = new HashMap<>();
        networks = new HashMap<>();
    }

    public HashMap<String, Node> getNodes() {
         return nodes;
    }

    public HashMap<NetworkType, Network> getNetworks() {
        return networks;
    }

    @Override
    public String toString() {
        return "Map{" +
                "nodes=" + nodes +
                '}';
    }

}
