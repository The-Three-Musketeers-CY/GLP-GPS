package process;

import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Dijkstra {

    public static Itinerary calculateItinerary(Node startingNode, Node arrivalNode, Map map){

        Node currentNode = startingNode ;

        ArrayList<String> coveredNodes ;
        HashMap<String,AccessibleNode> accessibleNodes ;

        coveredNodes = new ArrayList<>();
        accessibleNodes = new HashMap<>();

        //Init the starting node
        coveredNodes.add(startingNode.getId());
        accessibleNodes.put(startingNode.getId(),new AccessibleNode(startingNode,null,0));


        while (!coveredNodes.contains(arrivalNode.getId())){

            //First step : find all the adjacent nodes to the current node and update their weight
            for (Network network : map.getNetworks().values()){
                if(network.getWaysFromNode(currentNode) != null) {
                    for (String idNode : network.getWaysFromNode(currentNode).getWays().keySet()) {
                        if (!coveredNodes.contains(idNode)) {
                            //Get the adjacent node
                            Node node = map.getNodes().get(idNode);
                            //Get the adjacent node's way
                            Way way = network.getWaysFromNode(currentNode).getWays().get(node.getId());
                            //Calculate the travel time of this way with the higher speed
                            float time = calculateTime(calculateDistance(currentNode, node), way.getHigherSpeed());
                            //Update weight of the node, the node is now accessible
                            updateWeight(time + accessibleNodes.get(currentNode.getId()).getWeight(), node,currentNode,accessibleNodes);
                        }
                    }
                }
            }
            //Step 2 : get the smallest weight
            float minWeight = -1;
            Node node;
            //Browse all accessible nodes
            for (String idNode : accessibleNodes.keySet()){
                //Check if this node has not already been visited
                if(!coveredNodes.contains(idNode)){
                    float weight = accessibleNodes.get(idNode).getWeight() ;
                    //Check if this weight is the smallest
                    if(weight < minWeight || minWeight == -1){
                        //Update the current node
                        currentNode = accessibleNodes.get(idNode).getNode() ;
                        minWeight = weight ;
                    }
                }
            }
            //Add the new current node to the covered nodes
            coveredNodes.add(currentNode.getId());
        }

        //Get the itinerary
        Stack<Node> nodeStack = new Stack<>();
        float total = accessibleNodes.get(currentNode.getId()).getWeight();
        while (currentNode != null){
            nodeStack.push(accessibleNodes.get(currentNode.getId()).getNode()) ;
            currentNode = accessibleNodes.get(currentNode.getId()).getPreviousNode();
        }
        ArrayList<Node> nodeList = new ArrayList<>();
        while (nodeStack.size() != 0) {
            nodeList.add(nodeStack.peek());
            nodeStack.pop();
        }
        Itinerary itinerary = new Itinerary(total, nodeList.toArray(new Node[0]));
        return itinerary ;
    }

    private static float calculateDistance(Node node1, Node node2){
        int x1,x2,y1,y2;
        x1 = node1.getPosition().getX();
        x2 = node2.getPosition().getX();
        y1 = node1.getPosition().getY();
        y2 = node2.getPosition().getY();

        return (float) Math.abs(Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2)));
    }
    private static float calculateTime(float distance, float speed){
        return distance/speed ;
    }
    private static void updateWeight(float value, Node node, Node previousNode , HashMap<String,AccessibleNode> accessibleNodes){
        if(accessibleNodes.containsKey(node.getId())){
                if(value < accessibleNodes.get(node.getId()).getWeight()){
                    accessibleNodes.get(node.getId()).setWeight(value);
                    accessibleNodes.get(node.getId()).setPreviousNode(previousNode);
                }
        }else{
            accessibleNodes.put(node.getId(), new AccessibleNode(node,previousNode,value));
        }
    }

}
