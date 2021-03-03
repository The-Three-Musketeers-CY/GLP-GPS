package model;

public class CoveredNode {

    private Node node ;
    private Node previousNode ;
    private float weight ;

    public CoveredNode(Node node, Node previousNode, float weight){
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
}
