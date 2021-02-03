package data;

import java.util.HashMap;

public class Map {

    private HashMap<String,Node> nodes;
    private HashMap<NetworkType,Network> networks;
    private static Map instance = new Map();

    public Map() {
        nodes = new HashMap<>();
    }

    public HashMap<String, Node> getNodes() {
         return nodes;
    }

    public HashMap<NetworkType, Network> getNetworks() {
        return networks;
    }

    public static Map getInstance() {
        return instance;
    }

    @Override
    public String toString() {
        return "Map{" +
                "nodes=" + nodes +
                '}';
    }

}
