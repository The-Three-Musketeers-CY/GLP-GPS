package test;

import model.Map;
import model.Network;
import model.Node;
import process.builders.MapBuilder;

public class Test {

    public static void main(String[] args) {
        MapBuilder mapBuilder = new MapBuilder("map.xml");
        Map map = mapBuilder.buildMap();

        for(Node node : map.getNodes().values()) {
            System.out.println(node.toString());
        }

        for (Network network : map.getNetworks().values()) {
            System.out.println(network.toString());
        }
    }

}
