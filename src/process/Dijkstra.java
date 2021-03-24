package process;

import log.LoggerUtility;
import model.*;
import org.apache.log4j.Logger;
import process.builders.MapBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 * This class contains all the processes to find the best itinerary using Dijkstra algorithm
 */
public class Dijkstra {


    private static Logger logger = LoggerUtility.getLogger(Dijkstra.class, "html");
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
        accessibleNodes.put(startingNode.getId(),new AccessibleNode(startingNode,null,null,0));

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
                            //Get the higher transport
                            Transport transport = way.getHigherTransport();
                            //Update weight of the node, the node is now accessible
                            updateWeight(time + accessibleNodes.get(currentNode.getId()).getWeight(), node,currentNode,transport,accessibleNodes);
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

                        //chek if it's public transport is used during the itinerary

                        boolean publicTransportUsed = false ;
                        AccessibleNode currentAccessibleNode = accessibleNode ;
                        while (!currentAccessibleNode.getNode().getId().equals(startingNode.getId())){
                            if(currentAccessibleNode.getTransport().isPublicTransport()) publicTransportUsed = true;
                            currentAccessibleNode = accessibleNodes.get(currentAccessibleNode.getPreviousNode().getId()) ;
                        }

                        //Update the current node
                        if(publicTransportUsed && !accessibleNode.getTransport().isBicycle() && !accessibleNode.getTransport().isCar()) {
                            currentNode = accessibleNodes.get(idNode).getNode();
                            minWeight = weight;
                        }else if(!publicTransportUsed){
                            currentNode = accessibleNodes.get(idNode).getNode();
                            minWeight = weight;
                        }

                    }
                }
            }
            //Add the new current node to the covered nodes
            coveredNodes.add(currentNode.getId());
        }

        //Get the itinerary
        Stack<Node> nodeStack = new Stack<>();
        Stack<Transport> transportStack = new Stack<>();
        float total = accessibleNodes.get(currentNode.getId()).getWeight();
        while (currentNode != null){
            nodeStack.push(accessibleNodes.get(currentNode.getId()).getNode()) ;
            transportStack.push(accessibleNodes.get(currentNode.getId()).getTransport());
            currentNode = accessibleNodes.get(currentNode.getId()).getPreviousNode();
        }

        ArrayList<Node> nodeList = new ArrayList<>();
        while (nodeStack.size() != 0) {
            nodeList.add(nodeStack.peek());
            nodeStack.pop();
        }

        ArrayList<Transport> transportList = new ArrayList<>();
        while (transportStack.size() != 0) {
            transportList.add(transportStack.peek());
            transportStack.pop();
        }

        //Return the best itinerary
        return new StepItinerary(total, nodeList.toArray(new Node[0]), transportList.toArray(new Transport[0]));
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
    private static void updateWeight(float value, Node node, Node previousNode ,Transport higherTransport, HashMap<String,AccessibleNode> accessibleNodes){
        if(accessibleNodes.containsKey(node.getId())){
                if(value < accessibleNodes.get(node.getId()).getWeight()){
                    accessibleNodes.get(node.getId()).setWeight(value);
                    accessibleNodes.get(node.getId()).setPreviousNode(previousNode);
                    accessibleNodes.get(node.getId()).setTransport(higherTransport);
                }
        }else{
            accessibleNodes.put(node.getId(), new AccessibleNode(node,previousNode,higherTransport,value));
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
