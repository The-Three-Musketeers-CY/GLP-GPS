package model;

public class Itinerary {

    private int total ;
    private Node[] itineraryNodes ;

    public Itinerary(int total, Node[] itineraryNodes){
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
