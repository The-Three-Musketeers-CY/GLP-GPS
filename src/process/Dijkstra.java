package process;

import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Dijkstra {

    public static Itinerary calculateItinerary(Node startingNode, Node arrivalNode, Map map){

        Node currentNode = startingNode ;

        HashMap<String,CoveredNode> coveredNodes ;
        HashMap<String,Integer> weights ;
        HashMap<String, Node> previousNodeForNodes;

        coveredNodes = new HashMap<>();
        weights = new HashMap<>();
        previousNodeForNodes = new HashMap<>();

        coveredNodes.put(startingNode.getId(), new CoveredNode(startingNode,null,0));

        while (!coveredNodes.containsKey(arrivalNode.getId())){

            //Etape 1 : Noeuds adjacents
            for (Network network : map.getNetworks().values()){
                if(network.getWaysFromNode(currentNode) != null) {
                    System.out.println(currentNode.toString());
                    for (String idNode : network.getWaysFromNode(currentNode).getWays().keySet()) {
                        if (!coveredNodes.containsKey(idNode)) {
                            Node node = map.getNodes().get(idNode);
                            Way way = network.getWaysFromNode(currentNode).getWays().get(node.getId());
                            int time = calculateTime(calculateDistance(currentNode, node), way.getHigherSpeed());
                            updateWeight(time + coveredNodes.get(currentNode.getId()).getWeight(), node, weights);
                            previousNodeForNodes.put(node.getId(), currentNode);
                        }
                    }
                }
            }
            //Etape 2 : plus petit poids
            int minWeight = -1;
            Node node;
            for (String idNode : weights.keySet()){
                node = map.getNodes().get(idNode);
                int weight = weights.get(node.getId());
                if(weight < minWeight || minWeight == -1){
                    currentNode = node ;
                    minWeight = weight ;
                }
            }
            coveredNodes.put(currentNode.getId(),new CoveredNode(currentNode,previousNodeForNodes.get(currentNode.getId()),minWeight));
            weights.remove(currentNode.getId());
        }
        Stack<Node> nodeStack = new Stack<>();
        int total = coveredNodes.get(currentNode.getId()).getWeight();
        while (currentNode != null){
            nodeStack.push(coveredNodes.get(currentNode.getId()).getNode()) ;
            currentNode = coveredNodes.get(currentNode.getId()).getPreviousNode() ;
        }
        ArrayList<Node> nodeList = new ArrayList<>();
        while (nodeStack.size() != 0) {
            nodeList.add(nodeStack.peek());
            nodeStack.pop();
        }
        Itinerary itinerary = new Itinerary(total, nodeList.toArray(new Node[0]));
        return itinerary ;
    }

    private static int calculateDistance(Node node1, Node node2){
        int x1,x2,y1,y2;
        x1 = node1.getPosition().getX();
        x2 = node2.getPosition().getX();
        y1 = node1.getPosition().getY();
        y2 = node2.getPosition().getY();

        return (int) Math.abs(Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2)));
    }
    private static int calculateTime(int distance, int speed){
        return distance/speed ;
    }
    private static void updateWeight(int value, Node node, HashMap<String,Integer> weights){
        if(weights.containsKey(node.getId())){
            if(value < weights.get(node.getId())){
                weights.replace(node.getId(),value);
            }
        }else{
            weights.put(node.getId(),value);
        }
    }

}
