package model;

public class StepItinerary {
    private Node[] stepItineraryNodes;
    private Transport[] transportsUsed ;
    private float time;
    private float cost;
    private float distance;

    public StepItinerary(float time, float cost, float distance, Node[] stepItineraryNodes, Transport[] transportsUsed) {
        this.time = time;
        this.cost = cost;
        this.distance = distance;
        this.stepItineraryNodes = stepItineraryNodes;
        this.transportsUsed = transportsUsed ;
    }

    public float getTime(){
        return time;
    }

    public float getCost() {
        return cost;
    }

    public float getDistance() {
        return distance;
    }

    public Node[] getStepItineraryNodes(){
        return stepItineraryNodes;
    }

    public Transport[] getTransportsUsed() {
        return transportsUsed;
    }
}
