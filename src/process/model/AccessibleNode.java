package process.model;

import model.Node;
import model.Transport;

/**
 * This class represents an accessible node
 */
public class AccessibleNode {

    private Node node ;
    private Node previousNode ;
    private Transport transport;
    private float weight ;
    private float time;
    private float cost;
    private float distance;

    /**
     * Constructs an accessible node
     * @param node Node which is accessible
     * @param previousNode Node by which this node is accessible
     * @param transport Transport used to go to this accessible node
     * @param weight Weight used by Dijkstra
     * @param time Travel time
     * @param cost Cost
     * @param distance Distance travelled
     */
    public AccessibleNode(Node node, Node previousNode,Transport transport, float weight, float time, float cost, float distance){
        this.node = node ;
        this.previousNode = previousNode ;
        this.transport = transport ;
        this.weight = weight ;
        this.time = time;
        this.cost = cost;
        this.distance = distance ;
    }

    /**
     * This method returns node which is accessible
     * @return Node which is accessible
     */
    public Node getNode() {
        return node;
    }

    /**
     * This method returns node by which this node is accessible
     * @return Node by which this node is accessible
     */
    public Node getPreviousNode() {
        return previousNode;
    }

    /**
     * This method returns weight used by Dijkstra
     * @return Weight used by Dijkstra
     */
    public float getWeight() {
        return weight;
    }

    /**
     * This method returns travel time
     * @return Travel time
     */
    public float getTime() {
        return time;
    }

    /**
     * This method returns cost
     * @return Cost
     */
    public float getCost() {
        return cost;
    }

    /**
     * This method returns distance travelled
     * @return Distance travelled
     */
    public float getDistance() {
        return distance;
    }

    /**
     * This method returns transport used to go to this accessible node
     * @return Transport used to go to this accessible node
     */
    public Transport getTransport() {
        return transport;
    }

    /**
     * This method sets transport used to go to this accessible node
     * @param transport Transport used to go to this accessible node
     */
    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    /**
     * This method sets node by which this node is accessible
     * @param previousNode Node by which this node is accessible
     */
    public void setPreviousNode(Node previousNode) {
        this.previousNode = previousNode;
    }

    /**
     * This method sets weight used by Dijkstra
     * @param weight Weight used by Dijkstra
     */
    public void setWeight(float weight) {
        this.weight = weight;
    }

    /**
     * This method sets travel time
     * @param time Travel time
     */
    public void setTime(float time) {
        this.time = time;
    }

    /**
     * This method sets cost
     * @param cost Cost
     */
    public void setCost(float cost) {
        this.cost = cost;
    }

    /**
     * This method sets distance travelled
     * @param distance Distance travelled
     */
    public void setDistance(float distance) {
        this.distance = distance;
    }

}
