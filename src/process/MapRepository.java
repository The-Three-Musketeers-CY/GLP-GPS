package process;

import model.Node;

import java.util.HashMap;

public class MapRepository {
    private HashMap<String, Node> nodes;
    private static MapRepository instance = new MapRepository();

    private MapRepository(){
    }

    public static MapRepository getInstance() {
        return instance;
    }

    public void addNode(Node node){
        nodes.put(node.getId(), node);
    }

    public Node getNode(String id){
        return nodes.get(id);
    }
}
