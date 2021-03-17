package model;

import java.util.ArrayList;

public class Itinerary {

    private float total ;
    private ArrayList<StepItinerary> stepItineraries;

    public Itinerary(float total, ArrayList<StepItinerary> stepItineraries){
        this.total = total ;
        this.stepItineraries = stepItineraries ;
    }

    public float getTotal() {
        return total;
    }

    public ArrayList<StepItinerary> getStepItineraries() {
        return stepItineraries;
    }

    @Override
    public String toString() {
        String res = "Meilleur itinéraire trouvé !\n" +
                "Durée totale : " + total +
                "\nPassage par : " + "\n";
        for (StepItinerary stepItinerary : stepItineraries) {
            for (Node node : stepItinerary.getStepItineraryNodes()) {
                res += node.toString() + "\n";
            }
        }
        res += "}";
        return res;
    }
}
