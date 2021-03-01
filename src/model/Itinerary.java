package model;

import java.util.Arrays;

public class Itinerary {

    private int total ;
    private Node[] itineraryNodes ;

    public Itinerary(int total, Node[] itineraryNodes){
        this.total = total ;
        this.itineraryNodes = itineraryNodes ;
    }

    @Override
    public String toString() {
        return "Itinerary{" +
                "total=" + total +
                ", itineraryNodes=" + Arrays.toString(itineraryNodes) +
                '}';
    }
}
