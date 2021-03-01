package model;

public class CoveredNode {

    private Node node ;
    private Node previousNode ;
    private int weight ;

    public CoveredNode(Node node, Node previousNode, int weight){
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

    public int getWeight() {
        return weight;
    }
}
