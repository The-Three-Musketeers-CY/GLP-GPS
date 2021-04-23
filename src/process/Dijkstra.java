package process;

import log.config.LoggerConfig;
import log.LoggerUtility;
import model.*;
import model.identifiers.POIIdentifier;
import model.identifiers.TransportIdentifier;
import model.identifiers.WayIdentifier;
import model.repositories.TransportRepository;
import model.repositories.WayTypeRepository;
import org.apache.log4j.Logger;
import process.model.AccessibleNode;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Stack;

/**
 * This class contains all the processes to find the best itinerary using Dijkstra algorithm
 */
public class Dijkstra {

    public static final int DEFAULT_BY_TIME = 0;
    public static final int BY_DISTANCE = 1;
    public static final int BY_COST = 2;
    private static final int SCALE = 2;

    private static Logger logger = LoggerUtility.getLogger(Dijkstra.class, LoggerConfig.LOG_FILE_TYPE);

    /**
     * This method calculate the best itinerary in terms of time between two specific points
     * @param startingNode the starting node of the itinerary
     * @param arrivalNode the arrival node of the itinerary
     * @param map the main map
     * @return the best itinerary between the two points
     */
    private static StepItinerary calculateStepItinerary(Node startingNode, Node arrivalNode, Map map, ArrayList<Transport> transportsToAvoid, int weightType) throws IllegalArgumentException{

        //The node currently covered
        Node currentNode = startingNode;
        //All nodes previously covered
        ArrayList<String> coveredNodes;
        //All nodes who are accessible for the itinerary
        HashMap<String, AccessibleNode> accessibleNodes;
        //All the transports
        HashMap<TransportIdentifier,Transport> transportRepository = TransportRepository.getInstance().getTransports();
        //Intern transport's constraints
        ArrayList<Transport> internTransportsToAvoid = new ArrayList<>(transportsToAvoid);

        coveredNodes = new ArrayList<>();
        accessibleNodes = new HashMap<>();

        //Init the starting node
        coveredNodes.add(startingNode.getId());
        accessibleNodes.put(startingNode.getId(),new AccessibleNode(startingNode,null, null,0, 0, 0, 0));

        while (!coveredNodes.contains(arrivalNode.getId())) {
            //FIRST STEP : find all the adjacent nodes to the current node and update their weight
            for (Network network : map.getNetworks().values()) {
                if(network.getWaysFromNode(currentNode) != null) {
                    for (Way way : network.getWaysFromNode(currentNode).getWays().values()) {
                        String idNode = way.getNodeB().getId();
                        if (!coveredNodes.contains(idNode)) {
                            //Get the adjacent node
                            Node node = map.getNodes().get(idNode);

                            //Get transports used previously
                            ArrayList<Transport> transports = transportsUsed(accessibleNodes, accessibleNodes.get(currentNode.getId()));

                            //Transport constraints

                            //After car, only public transport
                            if(transports.contains(transportRepository.get(TransportIdentifier.CAR))) {
                                internTransportsToAvoid.add(transportRepository.get(TransportIdentifier.BICYCLE));
                            }
                            if (transports.contains(transportRepository.get(TransportIdentifier.BICYCLE))) {
                                internTransportsToAvoid.add(transportRepository.get(TransportIdentifier.CAR));
                            }
                            //After foot, only public transport or foot
                            if (transports.contains(transportRepository.get(TransportIdentifier.FOOT)) || transports.contains(transportRepository.get(TransportIdentifier.METRO)) || transports.contains(transportRepository.get(TransportIdentifier.BUS)) || transports.contains(transportRepository.get(TransportIdentifier.TRAIN)) || transports.contains(transportRepository.get(TransportIdentifier.BOAT)) || transports.contains(transportRepository.get(TransportIdentifier.PLANE))){
                                internTransportsToAvoid.add(transportRepository.get(TransportIdentifier.BICYCLE));
                                internTransportsToAvoid.add(transportRepository.get(TransportIdentifier.CAR));
                            }

                            //Calculate the travel time of this way with the higher speed without the transports to avoid
                            float weight;
                            float time;
                            float cost;
                            float distance;
                            Transport transport;
                            switch (weightType) {
                                case DEFAULT_BY_TIME:
                                    weight = calculateTime(way.getDistance() * SCALE, way.getHigherSpeed(internTransportsToAvoid));
                                    transport = way.getHigherTransport(internTransportsToAvoid);
                                    time = weight;
                                    if (transport != null) {
                                        cost = transport.getCost();
                                    } else {
                                        cost = 0;
                                    }
                                    distance = way.getDistance() * SCALE ;
                                    break;
                                case BY_DISTANCE:
                                    weight = way.getDistance() * SCALE ;
                                    transport = way.getHigherTransport(internTransportsToAvoid);
                                    time = calculateTime(weight, way.getHigherSpeed(internTransportsToAvoid));
                                    cost = way.getBestPrice(internTransportsToAvoid);
                                    distance = weight ;
                                    break;
                                case BY_COST:
                                    transport = way.getCheaperTransport(internTransportsToAvoid);
                                    if (transport != null) {
                                        time = calculateTime(way.getDistance() * SCALE, WayTypeRepository.getInstance().getWayTypes().get(way.getIdentifier()).getSpeeds().get(transport.getIdentifier()));
                                    } else {
                                        continue;
                                    }
                                    cost = way.getBestPrice(internTransportsToAvoid);
                                    distance = way.getDistance() * SCALE ;
                                    weight = cost + time;
                                    break;
                                default:
                                    throw new IllegalArgumentException("Invalid itinerary criteria. Available criteria are BY_TIME, BY_DISTANCE and BY_COST.");
                            }

                            //Update weight of the node, the node is now accessible
                            updateWeight(weight + accessibleNodes.get(currentNode.getId()).getWeight(), time, cost, distance ,node, currentNode, transport, accessibleNodes);

                        }
                    }
                    internTransportsToAvoid.clear();
                    internTransportsToAvoid.addAll(transportsToAvoid);
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

        //GET THE ITINERARY

        //init
        Stack<Node> nodeStack = new Stack<>();
        Stack<Transport> transportStack = new Stack<>();
        float time = 0;
        float cost = 0;
        float distance = 0;

        //Check car price
        cost += carPrice(accessibleNodes,accessibleNodes.get(currentNode.getId()));

        while (currentNode != null){
            //Add to the stack
            nodeStack.push(accessibleNodes.get(currentNode.getId()).getNode()) ;
            transportStack.push(accessibleNodes.get(currentNode.getId()).getTransport());
            //Update the duration of the itinerary and the distance
            time += accessibleNodes.get(currentNode.getId()).getTime();
            distance += accessibleNodes.get(currentNode.getId()).getDistance();
            currentNode = accessibleNodes.get(currentNode.getId()).getPreviousNode();
        }

        ArrayList<Node> nodeList = new ArrayList<>();
        while (nodeStack.size() != 0) {
            nodeList.add(nodeStack.peek());
            nodeStack.pop();
        }

        //Get transports used previously
        ArrayList<Transport> transports = transportsUsed(accessibleNodes, accessibleNodes.get(nodeList.get(nodeList.size() - 1).getId()));

        //Transport constraints

        //After car, only public transport or foot
        if (transports.contains(transportRepository.get(TransportIdentifier.CAR))) {
            transportsToAvoid.add(transportRepository.get(TransportIdentifier.BICYCLE));
        }
        //After bicycle, only public transport, foot, or bicycle
        if (transports.contains(transportRepository.get(TransportIdentifier.BICYCLE))) {
            transportsToAvoid.add(transportRepository.get(TransportIdentifier.CAR));
        }
        //After foot, only public transport or foot
        if(transports.contains(transportRepository.get(TransportIdentifier.FOOT))) {
            transportsToAvoid.add(transportRepository.get(TransportIdentifier.BICYCLE));
            transportsToAvoid.add(transportRepository.get(TransportIdentifier.CAR));
        }
        //After public transports, only public transport or foot
        if (transports.contains(transportRepository.get(TransportIdentifier.METRO)) || transports.contains(transportRepository.get(TransportIdentifier.BUS))) {
            transportsToAvoid.add(transportRepository.get(TransportIdentifier.BICYCLE));
            transportsToAvoid.add(transportRepository.get(TransportIdentifier.CAR));
            cost += transportRepository.get(TransportIdentifier.METRO).getCost() ;
        }

        if (transports.contains(transportRepository.get(TransportIdentifier.PLANE))) {
            transportsToAvoid.add(transportRepository.get(TransportIdentifier.BICYCLE));
            transportsToAvoid.add(transportRepository.get(TransportIdentifier.CAR));
            cost += transportRepository.get(TransportIdentifier.PLANE).getCost() ;
        }

        if (transports.contains(transportRepository.get(TransportIdentifier.BOAT))) {
            transportsToAvoid.add(transportRepository.get(TransportIdentifier.BICYCLE));
            transportsToAvoid.add(transportRepository.get(TransportIdentifier.CAR));
            cost += transportRepository.get(TransportIdentifier.BOAT).getCost() ;
        }

        if (transports.contains(transportRepository.get(TransportIdentifier.TRAIN))) {
            transportsToAvoid.add(transportRepository.get(TransportIdentifier.BICYCLE));
            transportsToAvoid.add(transportRepository.get(TransportIdentifier.CAR));
            cost += transportRepository.get(TransportIdentifier.TRAIN).getCost() ;
        }

        ArrayList<Transport> transportList = new ArrayList<>();
        while (transportStack.size() != 0) {
            Transport transport = transportStack.peek();
            transportList.add(transport);
            transportStack.pop();
        }

        //Return the best itinerary
        return new StepItinerary(time, cost, distance,nodeList.toArray(new Node[0]), transportList.toArray(new Transport[0]));
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
     * @param weight the new weight
     * @param node the node to update
     * @param previousNode the previous node
     * @param accessibleNodes all the accessible nodes
     */
    private static void updateWeight(float weight, float time, float cost, float distance, Node node, Node previousNode ,Transport transport, HashMap<String,AccessibleNode> accessibleNodes){
        if(accessibleNodes.containsKey(node.getId())){
            if(weight < accessibleNodes.get(node.getId()).getWeight()){
                accessibleNodes.get(node.getId()).setWeight(weight);
                accessibleNodes.get(node.getId()).setTime(time);
                accessibleNodes.get(node.getId()).setCost(cost);
                accessibleNodes.get(node.getId()).setDistance(distance);
                accessibleNodes.get(node.getId()).setPreviousNode(previousNode);
                accessibleNodes.get(node.getId()).setTransport(transport);
            }
        }else{
            accessibleNodes.put(node.getId(), new AccessibleNode(node,previousNode,transport,weight, time, cost, distance));
        }
    }

    private static ArrayList<Transport> transportsUsed(HashMap<String, AccessibleNode> accessibleNodes, AccessibleNode currentNode){

        ArrayList<Transport> transportsUsed = new ArrayList<>();

        while (currentNode.getPreviousNode() != null){
            //add the transport to the list
            if (!transportsUsed.contains(currentNode.getTransport())) {
                transportsUsed.add(currentNode.getTransport());
            }
            currentNode = accessibleNodes.get(currentNode.getPreviousNode().getId());
        }

        return transportsUsed ;
    }

    private static double carPrice(HashMap<String, AccessibleNode> accessibleNodes, AccessibleNode currentNode){

        double price = 0 ;

        while (currentNode.getPreviousNode() != null){

            if(currentNode.getTransport() == TransportRepository.getInstance().getTransports().get(TransportIdentifier.CAR)){
                price += currentNode.getDistance() * TransportRepository.getInstance().getTransports().get(TransportIdentifier.CAR).getCost() ;
            }
            currentNode =  accessibleNodes.get(currentNode.getPreviousNode().getId());
        }

        return price ;
    }

    /**
     * This method calculate the best itinerary in terms of time between all points given by the user with transport constraints
     * @param nodes Collection of all points into the itinerary path
     * @param map the main map
     * @param transportsToAvoid The transports to avoid in the itinerary
     * @return the best itinerary between all points
     */
    public static Itinerary calculateItinerary(ArrayList<Node> nodes, Map map, ArrayList<Transport> transportsToAvoid, int weightType) throws IllegalArgumentException{

        logger.info("Start itinerary calculation");
        Date startTime = new Date();

        ArrayList<StepItinerary> stepItineraries = new ArrayList<>();
        int i =  0;
        while (i< nodes.size() -1){
            Node nodeA = nodes.get(i);
            Node nodeB = nodes.get(i + 1);
            StepItinerary stepItinerary = calculateStepItinerary(nodeA, nodeB, map, transportsToAvoid, weightType);
            stepItineraries.add(stepItinerary);
            i++;
        }

        float time = 0;
        float cost = 0;
        float distance = 0;
        for (StepItinerary stepItinerary : stepItineraries) {
            time += stepItinerary.getTime();
            cost += stepItinerary.getCost();
            distance += stepItinerary.getDistance();
        }

        Date finishTime = new Date();
        logger.info("Best itinerary found in "+(finishTime.getTime() - startTime.getTime())+" milliseconds");

        return new Itinerary(time, cost, distance, stepItineraries);
    }

    public static Itinerary calculateTouristicItinerary(ArrayList<Node> nodes, Map map, ArrayList<Transport> transportsToAvoid, int weightType) throws IllegalArgumentException{

        logger.info("Start itinerary calculation");
        Date startTime = new Date();

        ArrayList<Node> touristicNodes = map.getTouristicNodes();
        Itinerary bestItinerary = null ;
        boolean isTouristic = false ;

        //Check if the itinerary is already touristic or not
        for (Node node : nodes){
            if(node.isPOI() && node.getPoi().getType() == POIIdentifier.ATTRACTION){
                isTouristic = true ;
            }
        }

        //If not, find the best one with an attraction
        if(!isTouristic) {
            for (Node touristicNode : touristicNodes) {
                //Add the touristic node
                nodes.add(1, touristicNode);

                Itinerary itinerary = calculateItinerary(nodes, map, transportsToAvoid, weightType);

                if (bestItinerary == null || itinerary.getTime() < bestItinerary.getTime()) {
                    bestItinerary = itinerary;
                }
            }
        }else{
            bestItinerary = calculateItinerary(nodes, map, transportsToAvoid, weightType);
        }

        Date finishTime = new Date();
        logger.info("Best itinerary found in " + (finishTime.getTime() - startTime.getTime()) + " milliseconds");

        return bestItinerary;
    }

}
