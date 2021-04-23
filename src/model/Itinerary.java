package model;

import java.util.ArrayList;

/**
 * This class represents an itinerary
 */
public class Itinerary {

    private float time;
    private float cost;
    private float distance;
    private ArrayList<StepItinerary> stepItineraries;

    /**
     * Construct an itinerary
     * @param time Travel time of this itinerary
     * @param cost Cost of this itinerary
     * @param distance Distance travelled with this itinerary
     * @param stepItineraries Sub-routes producing this itinerary
     */
    public Itinerary(float time, float cost, float distance, ArrayList<StepItinerary> stepItineraries){
        this.time = time;
        this.cost = cost;
        this.distance = distance;
        this.stepItineraries = stepItineraries ;
    }

    /**
     * This method returns travel time of this itinerary
     * @return Travel time of this itinerary
     */
    public float getTime() {
        return time;
    }

    /**
     * This method returns cost of this itinerary
     * @return Cost of this itinerary
     */
    public float getCost() {
        return cost;
    }

    /**
     * This method returns distance travelled with this itinerary
     * @return Distance travelled with this itinerary
     */
    public float getDistance() {
        return distance;
    }

    /**
     * This method returns sub-routes list
     * @return Sub-routes list
     */
    public ArrayList<StepItinerary> getStepItineraries() {
        return stepItineraries;
    }

    @Override
    public String toString() {
        String res = "Meilleur itinéraire trouvé !\n" +
                "Durée totale : " + time +
                "\nPassage par : " + "\n";
        for (StepItinerary stepItinerary : stepItineraries) {
            for (Node node : stepItinerary.getStepItineraryNodes()) {
                res += node.toString() + "\n";
            }
            for(Transport transport : stepItinerary.getTransportsUsed()){
                if(transport != null) res += transport.toString() + "\n";
            }
        }
        res += "}";
        return res;
    }
}
