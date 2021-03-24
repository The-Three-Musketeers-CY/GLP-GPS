package model;

public class StepItinerary {
    private Node[] stepItineraryNodes;
    private Transport[] transportsUsed ;
    private float totalStepNode ;

    public StepItinerary(float totalStepNode, Node[] stepItineraryNodes, Transport[] transportsUsed) {
        this.totalStepNode = totalStepNode;
        this.stepItineraryNodes = stepItineraryNodes;
        this.transportsUsed = transportsUsed ;
    }

    public float getTotalStepNode(){
        return totalStepNode;
    }

    public Node[] getStepItineraryNodes(){
        return stepItineraryNodes;
    }

    public Transport[] getTransportsUsed() {
        return transportsUsed;
    }
}
