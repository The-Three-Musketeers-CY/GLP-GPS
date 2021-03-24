package model;

public class AccessibleNode {

    private Node node ;
    private Node previousNode ;
    private Transport transport;
    private float weight ;

    public AccessibleNode(Node node, Node previousNode,Transport transport, float weight){
        this.node = node ;
        this.previousNode = previousNode ;
        this.transport = transport ;
        this.weight = weight ;
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
}
