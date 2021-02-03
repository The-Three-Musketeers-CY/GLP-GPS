package process;

import model.Map;
import model.POIType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatterBuilder;


public class MapBuilder {
    private MapRepository mapRepository = MapRepository.getInstance();
    public MapBuilder(String path){
        File file = new File(path);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nl = doc.getElementsByTagName("node");
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
                    model.Node node = NodeFactory.creatNode(Float.parseFloat(posX),Float.parseFloat(posY), name, POIType.valueOf(type));
                    mapRepository.addNode(node);
                    NodeList nodeList = elt.getElementsByTagName("adjacentNode");
                    for(int indexAdjacentNode=0; indexAdjacentNode<nodeList.getLength(); indexAdjacentNode++){
                        Node ndAdjacent = nodeList.item(indexAdjacentNode);
                        if(ndAdjacent.getNodeType()== Node.ELEMENT_NODE){
                            Element eltAdjacent = (Element)ndAdjacent;
                            String posNDAdjacentX = eltAdjacent.getAttribute("x");
                            String posNDAdjacentY = eltAdjacent.getAttribute("y");
                            String typeAdjacent = eltAdjacent.getAttribute("type");
                        }
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map buildMap() {
        Map map = new Map();
        return map;
    }

}
