package model;

public class Itinerary {

    private float total ;
    private Node[] itineraryNodes ;

    public Itinerary(float total, Node[] itineraryNodes){
        this.total = total ;
        this.itineraryNodes = itineraryNodes ;
    }

    public float getTotal() {
        return total;
    }

    public Node[] getItineraryNodes() {
        return itineraryNodes;
    }

    @Override
    public String toString() {
        String res = "Meilleur itinéraire trouvé !\n" +
                "Durée totale : " + total +
                "\nPassage par : " + "\n";
        for (Node node : itineraryNodes) {
            res += node.toString() + "\n";
        }
        res += "}";
        return res;
    }
}
