package process.builders;

import model.*;
import model.identifiers.POIIdentifier;
import model.identifiers.WayIdentifier;
import model.repositories.WayTypeRepository;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import process.factories.NodeFactory;
import process.repositories.MapRepository;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;


public class MapBuilder {

    private MapRepository mapRepository = MapRepository.getInstance();

    public MapBuilder(String path){
        File file = new File(path);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            // Parsing & Normalizing XML map
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();

            // Getting all nodes
            NodeList nl = doc.getElementsByTagName("node");

            // Processing all nodes
            for (int indexNode=0; indexNode<nl.getLength(); indexNode++){
                Node nd = nl.item(indexNode);
                if(nd.getNodeType()== Node.ELEMENT_NODE){
                    Element elt = (Element)nd;
                    String posX = elt.getAttribute("x");
                    String posY = elt.getAttribute("y");
                    Element poi = (Element) elt.getElementsByTagName("poi").item(0);
                    String type = null;
                    String name = null;
                    if(poi != null){
                        type = poi.getAttribute("type");
                        name = poi.getTextContent();
                    }
                    model.Node node = NodeFactory.creatNode(Float.parseFloat(posX),Float.parseFloat(posY), name, (type != null) ? POIIdentifier.valueOf(type) : null);
                    mapRepository.addNode(node);
                }
            }
            // Traitement des liens entre les noeuds (après avoir tous stocké tous les noeuds et leurs différents identifiants)
            for (int indexNode = 0; indexNode < nl.getLength(); indexNode++) {
                Node nd = nl.item(indexNode);
                if(nd.getNodeType()== Node.ELEMENT_NODE) {
                    Element elt = (Element) nd;
                    model.Node node = mapRepository.getNode(Float.parseFloat(elt.getAttribute("x")), Float.parseFloat(elt.getAttribute("y")));
                    if (node != null) {
                        NodeList nodeList = elt.getElementsByTagName("adjacentNode");
                        for (int indexAdjacentNode = 0; indexAdjacentNode < nodeList.getLength(); indexAdjacentNode++) {
                            Node ndAdjacent = nodeList.item(indexAdjacentNode);
                            if (ndAdjacent.getNodeType() == Node.ELEMENT_NODE) {
                                Element eltAdjacent = (Element) ndAdjacent;
                                String posNDAdjacentX = eltAdjacent.getAttribute("x");
                                String posNDAdjacentY = eltAdjacent.getAttribute("y");
                                String typeAdjacent = eltAdjacent.getAttribute("type");
                                model.Node adjNode = mapRepository.getNode(Float.parseFloat(posNDAdjacentX), Float.parseFloat(posNDAdjacentY));
                                mapRepository.addWayToNode(node.getId(), adjNode.getId(), WayIdentifier.valueOf(typeAdjacent));
                            }
                        }
                    }
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Veuillez entrer une valeur correct : " + e.getMessage());
        }
    }

    public Map buildMap() {
        // Initializing map
        Map map = new Map();

        // Process...
        for (model.Node node : mapRepository.getNodes().values()) {
            map.getNodes().put(node.getId(), node);
        }

        NetworkBuilder networkBuilder = new NetworkBuilder();
        networkBuilder.buildNetworks(map);
        TransportBuilder transportBuilder = new TransportBuilder();
        transportBuilder.buildTransports();
        WayTypeBuilder wayTypeBuilder = new WayTypeBuilder();
        wayTypeBuilder.buildWayTypes();

        for (model.Node node : mapRepository.getNodes().values()) {
            HashMap<String, WayIdentifier> ways = mapRepository.getNodeWays(node.getId());
            if (ways != null)
                for (String adjNodeID : ways.keySet()) {
                    WayIdentifier way = ways.get(adjNodeID);
                    for (Network network : map.getNetworks().values()) {
                        if (network.isAcceptedWay(way)) {
                            network.addWay(WayTypeRepository.getInstance().getWayTypes().get(way), node, map.getNodes().get(adjNodeID));
                        }
                    }
                }
        }

        // Returning map after building it correctly
        return map;
    }

}
