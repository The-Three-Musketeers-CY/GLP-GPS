package model;

/**
 * This class represents a step-route from an itinerary
 */
public class StepItinerary {
    private Node[] stepItineraryNodes;
    private Transport[] transportsUsed ;
    private float time;
    private float cost;
    private float distance;

    /**
     * Constructs step itinerary
     * @param time Travel time of this step itinerary
     * @param cost Cost of this step itinerary
     * @param distance Distance travelled with this step itinerary
     * @param stepItineraryNodes Nodes composing the step itinerary
     * @param transportsUsed Transport used on the step itinerary
     */
    public StepItinerary(float time, float cost, float distance, Node[] stepItineraryNodes, Transport[] transportsUsed) {
        this.time = time;
        this.cost = cost;
        this.distance = distance;
        this.stepItineraryNodes = stepItineraryNodes;
        this.transportsUsed = transportsUsed ;
    }

    /**
     * This method returns travel time of this step itinerary
     * @return Travel time of this step itinerary
     */
    public float getTime(){
        return time;
    }

    /**
     * This method returns cost of this step itinerary
     * @return Cost of this step itinerary
     */
    public float getCost() {
        return cost;
    }

    /**
     * This method returns distance travelled with this step itinerary
     * @return Distance travelled with this step itinerary
     */
    public float getDistance() {
        return distance;
    }

    /**
     * This method returns nodes composing this step itinerary
     * @return Nodes list composing this step itinerary
     */
    public Node[] getStepItineraryNodes(){
        return stepItineraryNodes;
    }

    /**
     * This method returns all the transport used on this step itinerary
     * @return Transports list used on this step itinerary
     */
    public Transport[] getTransportsUsed() {
        return transportsUsed;
    }
}
