package process.builders;

import log.LoggerUtility;
import log.config.LoggerConfig;
import model.*;
import model.identifiers.NetworkIdentifier;
import model.identifiers.POIIdentifier;
import model.identifiers.WayIdentifier;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import process.factories.NodeFactory;
import process.repositories.MapBuildRepository;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;


public class MapBuilder {

    private MapBuildRepository mapBuildRepository = MapBuildRepository.getInstance();

    private Logger logger = LoggerUtility.getLogger(MapBuilder.class, LoggerConfig.LOG_FILE_TYPE);

    /**
     * Create a MapBuilder
     * @param path the map file's path
     */
    public MapBuilder(String path) throws IllegalArgumentException, IOException, ParserConfigurationException, SAXException{

        File file = new File(path);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        logger.info("Start map construction");

        // Parsing & Normalizing XML map
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(file);
        doc.getDocumentElement().normalize();

        // Getting all nodes
        NodeList nl = doc.getElementsByTagName("node");

        // Processing all nodes
        int indexNode ;
        for (indexNode = 0; indexNode<nl.getLength(); indexNode++){
            Node nd = nl.item(indexNode);
            if(nd.getNodeType()== Node.ELEMENT_NODE){
                Element elt = (Element)nd;
                String posX = elt.getAttribute("x");
                String posY = elt.getAttribute("y");
                String id = elt.getAttribute("id");
                Element poi = (Element) elt.getElementsByTagName("poi").item(0);
                model.Node node;
                if(poi != null){
                    String type = poi.getAttribute("type");
                    String name = poi.getTextContent();
                    node = NodeFactory.create(id, Integer.parseInt(posX),Integer.parseInt(posY), name, (type != null) ? POIIdentifier.valueOf(type) : null);
                } else {
                    node = NodeFactory.create(id, Integer.parseInt(posX), Integer.parseInt(posY));
                }
                mapBuildRepository.addNode(node);
            }
        }
        logger.info("Read " + indexNode + " nodes from map file");

        // Processing all ways from nodes
        for (indexNode = 0; indexNode < nl.getLength(); indexNode++) {
            Node nd = nl.item(indexNode);
            if(nd.getNodeType()== Node.ELEMENT_NODE) {
                Element elt = (Element) nd;
                model.Node node = mapBuildRepository.getNode(Float.parseFloat(elt.getAttribute("x")), Float.parseFloat(elt.getAttribute("y")));
                if (node != null) {
                    NodeList nodeList = elt.getElementsByTagName("adjacentNode");
                    for (int indexAdjacentNode = 0; indexAdjacentNode < nodeList.getLength(); indexAdjacentNode++) {
                        Node ndAdjacent = nodeList.item(indexAdjacentNode);
                        if (ndAdjacent.getNodeType() == Node.ELEMENT_NODE) {
                            Element eltAdjacent = (Element) ndAdjacent;
                            String id = eltAdjacent.getAttribute("id");
                            String typeAdjacent = eltAdjacent.getAttribute("type");
                            model.Node adjNode = mapBuildRepository.getNode(id);
                            Way way;
                            if(!eltAdjacent.getAttribute("lineNumber").isBlank()){
                                String lineNumber = eltAdjacent.getAttribute("lineNumber");
                                way = new Way(WayIdentifier.valueOf(typeAdjacent), node, adjNode, lineNumber);
                            }
                            else {
                                way = new Way(WayIdentifier.valueOf(typeAdjacent), node, adjNode);
                            }
                            mapBuildRepository.addWayToNode(way);
                        }
                    }
                }
            }
        }
    }

    /**
     * This method built a new map with its different nodes, ways, networks
     */
    public Map buildMap() throws IllegalArgumentException{

        // Initializing map
        Map map = new Map();

        // Creating networks, transports and wayTypes
        NetworkBuilder networkBuilder = new NetworkBuilder();
        networkBuilder.buildNetworks(map);
        TransportBuilder transportBuilder = new TransportBuilder();
        transportBuilder.buildTransports();
        WayTypeBuilder wayTypeBuilder = new WayTypeBuilder();
        wayTypeBuilder.buildWayTypes();

        // Putting nodes into map
        for (model.Node node : mapBuildRepository.getNodes().values()) {
            map.getNodes().put(node.getId(), node);
        }
        logger.info("Add " + map.getNodes().size() + " nodes to the map");


        // Putting all ways into all concerned networks
        int waysCounter = 0;
        for (model.Node node : mapBuildRepository.getNodes().values()) {
            NodeWays ways = mapBuildRepository.getNodeWays(node.getId());
            if (ways != null)
                for (Way way : ways.getWays().values()) {
                    String adjNodeID = way.getNodeB().getId();
                    for (Network network : map.getNetworks().values()) {
                        if (network.isAcceptedWay(way.getIdentifier())) {
                            network.addWay(way.getIdentifier(), node, map.getNodes().get(adjNodeID), way.getLineNumber());
                            waysCounter++;
                        }
                    }
                }
        }
        logger.info("Add " + waysCounter + " ways to the map");


        // Putting foot ways
        Network footNetwork = map.getNetworks().get(NetworkIdentifier.FOOT);
        for (model.Node nodeA : map.getNodes().values()) {
            if (nodeA.isPOI()) {
                footNetwork.getNodeWays().put(nodeA.getId(), new NodeWays(nodeA));
                for (model.Node nodeB : map.getNodes().values()) {
                    if (nodeB.isPOI())
                        footNetwork.getNodeWays().get(nodeA.getId()).getWays().put(nodeB.getId(), new Way(WayIdentifier.FOOT, nodeA, nodeB));
                }
            }
        }

        // Returning map after building it correctly
        logger.info("Map built successfully");

        return map;
    }

}
