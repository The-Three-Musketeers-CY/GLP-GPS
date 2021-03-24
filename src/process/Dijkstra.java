package process;

import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 * This class contains all the processes to find the best itinerary using Dijkstra algorithm
 */
public class Dijkstra {

    /**
     * This method calculate the best itinerary in terms of time between two specific points
     * @param startingNode the starting node of the itinerary
     * @param arrivalNode the arrival node of the itinerary
     * @param map the main map
     * @return the best itinerary between the two points
     */
    private static StepItinerary calculateStepItinerary(Node startingNode, Node arrivalNode, Map map){

        //The node currently covered
        Node currentNode = startingNode ;
        //All nodes previously covered
        ArrayList<String> coveredNodes ;
        //All nodes who are accessible for the itinerary
        HashMap<String,AccessibleNode> accessibleNodes ;

        coveredNodes = new ArrayList<>();
        accessibleNodes = new HashMap<>();

        //Init the starting node
        coveredNodes.add(startingNode.getId());
        accessibleNodes.put(startingNode.getId(),new AccessibleNode(startingNode,null,0));

        while (!coveredNodes.contains(arrivalNode.getId())){
            //FIRST STEP : find all the adjacent nodes to the current node and update their weight
            for (Network network : map.getNetworks().values()){
                if(network.getWaysFromNode(currentNode) != null) {
                    for (Way way : network.getWaysFromNode(currentNode).getWays().values()) {
                        String idNode = way.getNodeB().getId();
                        if (!coveredNodes.contains(idNode)) {
                            //Get the adjacent node
                            Node node = map.getNodes().get(idNode);
                            //Calculate the travel time of this way with the higher speed
                            float time = calculateTime(way.getDistance() * 2, way.getHigherSpeed());
                            //Update weight of the node, the node is now accessible
                            updateWeight(time + accessibleNodes.get(currentNode.getId()).getWeight(), node,currentNode,accessibleNodes);
                        }
                    }
                }
            }

            //STEP 2 : get the smallest weight
            float minWeight = -1;
            //Browse all accessible nodes
            for (AccessibleNode accessibleNode : accessibleNodes.values()){
                String idNode = accessibleNode.getNode().getId();
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

        //Return the best itinerary
        return new StepItinerary(total, nodeList.toArray(new Node[0]));
    }

    /**
     * this method calculates travel time
     * @param distance the distance between two points
     * @param speed the average speed between the two points
     * @return the travel time between the two points
     */
    private static float calculateTime(float distance, float speed){
        return distance/speed ;
    }

    /**
     * this methode update accessible node's weight
     * @param value the new weight
     * @param node the node to update
     * @param previousNode the previous node
     * @param accessibleNodes all the accessible nodes
     */
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

    /**
     * This method calculate the best itinerary in terms of time between all points given by the user
     * @param nodes Collection of all points into the itinerary path
     * @param map the main map
     * @return the best itinerary between all points
     */
    public static Itinerary calculateItinerary(ArrayList<Node> nodes, Map map){
        ArrayList<StepItinerary> stepItineraries = new ArrayList<>();
        int i =  0;
        while (i< nodes.size() -1){
            Node nodeA = nodes.get(i);
            Node nodeB = nodes.get(i + 1);
            StepItinerary stepItinerary = calculateStepItinerary(nodeA, nodeB, map);
            stepItineraries.add(stepItinerary);
            i++;
        }

        float total = 0;
        for (StepItinerary stepItinerary : stepItineraries) {
            total += stepItinerary.getTotalStepNode();
        }

        return new Itinerary(total, stepItineraries);
    }

}
