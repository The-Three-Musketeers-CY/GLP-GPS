package model;

import java.util.ArrayList;

public class Itinerary {

    private float time;
    private float cost;
    private ArrayList<StepItinerary> stepItineraries;

    public Itinerary(float time, float cost, ArrayList<StepItinerary> stepItineraries){
        this.time = time;
        this.cost = cost;
        this.stepItineraries = stepItineraries ;
    }

    public float getTime() {
        return time;
    }

    public float getCost() {
        return cost;
    }

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
                if(transport != null) res+= transport.toString() + "\n";
            }
        }
        res += "}";
        return res;
    }
}
