package model;

public class Itinerary {

    private float total ;
    private Node[] itineraryNodes ;

    public Itinerary(float total, Node[] itineraryNodes){
        this.total = total ;
        this.itineraryNodes = itineraryNodes ;
    }

    @Override
    public String toString() {
        String res = "Itinerary{" +
                "total=" + total +
                ", itineraryNodes=" + "\n";
        for (Node node : itineraryNodes) {
            res += node.toString() + "\n";
        }
        res += "}";
        return res;
    }
}
