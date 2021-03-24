package model;

public class AccessibleNode {

    private Node node ;
    private AccessibleNode previousNode ;
    private Transport transport;
    private float weight ;

    public AccessibleNode(Node node, AccessibleNode previousNode,Transport transport, float weight){
        this.node = node ;
        this.previousNode = previousNode ;
        this.transport = transport ;
        this.weight = weight ;
    }

    public Node getNode() {
        return node;
    }

    public AccessibleNode getPreviousNode() {
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

    public void setPreviousNode(AccessibleNode previousNode) {
        this.previousNode = previousNode;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
