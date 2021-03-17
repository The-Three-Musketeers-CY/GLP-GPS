package model;

public class StepItinerary {
    private Node[] stepItineraryNodes;
    private float totalStepNode ;

    public StepItinerary(float totalStepNode, Node[] stepItineraryNodes) {
        this.totalStepNode = totalStepNode;
        this.stepItineraryNodes = stepItineraryNodes;
    }

    public float getTotalStepNode(){
        return totalStepNode;
    }

    public Node[] getStepItineraryNodes(){
        return stepItineraryNodes;
    }
    
}
