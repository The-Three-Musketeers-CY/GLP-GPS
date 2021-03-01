package process;

import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Graph {

    public static Itinerary calculateItinerary(Node startingNode, Node arrivalNode, Map map){

        Node currentNode = startingNode ;

        HashMap<String,CoveredNode> coveredNodes ;
        HashMap<String,Integer> weights ;

        coveredNodes = new HashMap<>();
        weights = new HashMap<>();

        coveredNodes.put(startingNode.getId(),new CoveredNode(startingNode,null,0));

        while (!coveredNodes.containsKey(arrivalNode.getId())){



            //Etape 1 : Noeuds adjacents
            for (Network network : map.getNetworks().values()){
                if(network.getWaysFromNode(currentNode) != null) {
                    System.out.println(currentNode.toString());
                    for (String idNode : network.getWaysFromNode(currentNode).keySet()) {
                        Node node = map.getNodes().get(idNode);
                        WayType wayType = network.getWaysFromNode(currentNode).get(node.getId());
                        int time = calculateTime(calculateDistance(currentNode, node), wayType.getHigherSpeed());
                        updateWeight(time, node, weights);
                    }
                }
            }
            //Etape 2 : plus petit poids
            int minWeight = (int) weights.values().toArray()[0];
            Node previousNode = null ;
            for (String idNode : weights.keySet()){
                previousNode = map.getNodes().get(idNode);
                int weight = weights.get(previousNode.getId());
                if(weight < minWeight){
                    currentNode = previousNode ;
                    minWeight = weight ;
                }
            }
            coveredNodes.put(currentNode.getId(),new CoveredNode(currentNode,previousNode,minWeight));
            weights.remove(currentNode.getId());

        }
        Stack<Node> nodeStack = new Stack<>();
        int total = 0;
        while (currentNode.getId() != startingNode.getId()){
            System.out.println("PUTE 2");
            nodeStack.push(coveredNodes.get(currentNode.getId()).getNode()) ;
            total+= coveredNodes.get(currentNode.getId()).getWeight() ;
            currentNode = coveredNodes.get(currentNode.getId()).getPreviousNode() ;
        }
        Itinerary itinerary = new Itinerary(total, (Node[]) nodeStack.toArray());
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
