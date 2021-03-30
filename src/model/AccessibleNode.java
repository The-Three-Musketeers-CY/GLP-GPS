package model;

public class AccessibleNode {

    private Node node ;
    private Node previousNode ;
    private Transport transport;
    private float weight ;
    private float time;
    private float cost;

    public AccessibleNode(Node node, Node previousNode,Transport transport, float weight, float time, float cost){
        this.node = node ;
        this.previousNode = previousNode ;
        this.transport = transport ;
        this.weight = weight ;
        this.time = time;
        this.cost = cost;
    }

    public Node getNode() {
        return node;
    }

    public Node getPreviousNode() {
        return previousNode;
    }

    public float getWeight() {
        return weight;
    }

    public float getTime() {
        return time;
    }

    public float getCost() {
        return cost;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public void setPreviousNode(Node previousNode) {
        this.previousNode = previousNode;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

}
