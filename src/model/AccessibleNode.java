package model;

/**
 * This class represents nodes that are accessible by other nodes
 */
public class AccessibleNode {

    private Node node ;
    private Node previousNode ;
    private float weight ;

    /**
     * This method construct the accessible node
     * @param node node
     * @param previousNode the previous node
     * @param weight node weight
     */
    public AccessibleNode(Node node, Node previousNode, float weight){
        this.node = node ;
        this.previousNode = previousNode ;
        this.weight = weight ;
    }

    /**
     * This method retrieves a node
     * @return A node
     */
    public Node getNode() {
        return node;
    }

    /**
     * This method retrieves a previous node
     * @return A previous node
     */
    public Node getPreviousNode() {
        return previousNode;
    }

    /**
     * This method retrieves the weight of a node
     * @return The weight of the node
     */
    public float getWeight() {
        return weight;
    }

    /**
     * This method sets the previous node
     * @param previousNode the previous node
     */
    public void setPreviousNode(Node previousNode) {
        this.previousNode = previousNode;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
