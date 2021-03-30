package model;

public class StepItinerary {
    private Node[] stepItineraryNodes;
    private Transport[] transportsUsed ;
    private float time;
    private float cost;

    public StepItinerary(float time, float cost, Node[] stepItineraryNodes, Transport[] transportsUsed) {
        this.time = time;
        this.cost = cost;
        this.stepItineraryNodes = stepItineraryNodes;
        this.transportsUsed = transportsUsed ;
    }

    public float getTime(){
        return time;
    }

    public float getCost() {
        return cost;
    }

    public Node[] getStepItineraryNodes(){
        return stepItineraryNodes;
    }

    public Transport[] getTransportsUsed() {
        return transportsUsed;
    }
}
