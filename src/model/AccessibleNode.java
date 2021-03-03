package model;

public class AccessibleNode {

    private Node node ;
    private Node previousNode ;
    private float weight ;

    public AccessibleNode(Node node, Node previousNode, float weight){
        this.node = node ;
        this.previousNode = previousNode ;
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

    public void setPreviousNode(Node previousNode) {
        this.previousNode = previousNode;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
