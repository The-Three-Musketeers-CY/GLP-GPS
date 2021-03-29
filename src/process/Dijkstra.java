package process;

import log.LoggerUtility;
import model.*;
import model.identifiers.TransportIdentifier;
import model.repositories.TransportRepository;
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
    private static StepItinerary calculateStepItinerary(Node startingNode, Node arrivalNode, Map map, ArrayList<Transport> transportsToAvoid){

        //The node currently covered
        Node currentNode = startingNode ;
        //All nodes previously covered
        ArrayList<String> coveredNodes ;
        //All nodes who are accessible for the itinerary
        HashMap<String,AccessibleNode> accessibleNodes ;
        //All the transports
        HashMap<TransportIdentifier,Transport> transportRepository = TransportRepository.getInstance().getTransports() ;

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
                            //Get transports used previously
                            ArrayList<Transport> transports = transportsUsed(accessibleNodes,coveredNodes);

                            //Transport constraints
                            //TODO : here add user's transport constraints

                            //After car, only public transport
                            if(transports.contains(transportRepository.get(TransportIdentifier.CAR))){
                                transportsToAvoid.add(transportRepository.get(TransportIdentifier.BICYCLE));
                            }
                            //After foot, only public transport or foot
                            if(transports.contains(transportRepository.get(TransportIdentifier.FOOT))){
                                transportsToAvoid.add(transportRepository.get(TransportIdentifier.BICYCLE));
                                transportsToAvoid.add(transportRepository.get(TransportIdentifier.CAR));
                            }
                            //After public transport, only public transport or foot
                            if(transports.contains(transportRepository.get(TransportIdentifier.METRO)) || transports.contains(transportRepository.get(TransportIdentifier.BUS))){
                                transportsToAvoid.add(transportRepository.get(TransportIdentifier.BICYCLE));
                                transportsToAvoid.add(transportRepository.get(TransportIdentifier.CAR));
                            }

                            //Calculate the travel time of this way with the higher speed without the transports to avoid
                            float time = calculateTime(way.getDistance() * 2, way.getHigherSpeed(transportsToAvoid));
                            //Get the higher transport without the transports to avoid
                            Transport transport = way.getHigherTransport(transportsToAvoid);
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
                        currentNode = accessibleNodes.get(idNode).getNode();
                        minWeight = weight;
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

    private static ArrayList<Transport> transportsUsed(HashMap<String,AccessibleNode> accessibleNodes, ArrayList<String> coveredNodes){

        ArrayList<Transport> transportsUsed = new ArrayList<>();

        for(AccessibleNode accessibleNode : accessibleNodes.values()){
            //Check if the node is covered
            if(coveredNodes.contains(accessibleNode.getNode().getId())){
                //add the transport to the list
                if(!transportsUsed.contains(accessibleNode.getTransport())){
                    transportsUsed.add(accessibleNode.getTransport());
                }
            }
        }
        return transportsUsed ;
    }

    /**
     * This method calculate the best itinerary in terms of time between all points given by the user with transport constraints
     * @param nodes Collection of all points into the itinerary path
     * @param map the main map
     * @param transportsToAvoid The transports to avoid in the itinerary
     * @return the best itinerary between all points
     */
    public static Itinerary calculateItinerary(ArrayList<Node> nodes, Map map, ArrayList<Transport> transportsToAvoid){
        ArrayList<StepItinerary> stepItineraries = new ArrayList<>();
        int i =  0;
        while (i< nodes.size() -1){
            Node nodeA = nodes.get(i);
            Node nodeB = nodes.get(i + 1);
            StepItinerary stepItinerary = calculateStepItinerary(nodeA, nodeB, map, transportsToAvoid);
            stepItineraries.add(stepItinerary);
            i++;
        }

        float total = 0;
        for (StepItinerary stepItinerary : stepItineraries) {
            total += stepItinerary.getTotalStepNode();
        }

        return new Itinerary(total, stepItineraries);
    }
    /**
     * This method calculate the best itinerary in terms of time between all points given by the user
     * @param nodes Collection of all points into the itinerary path
     * @param map the main map
     * @return the best itinerary between all points
     */
    public static Itinerary calculateItinerary(ArrayList<Node> nodes, Map map){
        return calculateItinerary(nodes,map,new ArrayList<>());
    }

}
