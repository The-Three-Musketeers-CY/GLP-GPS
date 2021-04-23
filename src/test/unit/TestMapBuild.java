package test.unit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import config.GPSConfig;
import model.Map;
import model.Network;
import model.Node;
import model.NodeWays;
import model.identifiers.NetworkIdentifier;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import process.builders.MapBuilder;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Unit test of map building
 *
 * The test is based on the test map : /src/test/map/map.xml
 */
public class TestMapBuild {

    private Map map;

    @Before
    public void prepareMap() {
        try {
            MapBuilder mapBuilder = new MapBuilder(GPSConfig.MAP_PATH);
            map = mapBuilder.buildMap();
        }catch(SAXException | ParserConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMapConstructedSuccessfully() {
        assertNotNull(map);
    }

    @Test
    public void testNodeConstructedSuccessfully() {
        assertEquals(131, map.getNodes().size());
    }

    @Test
    public void testFootNodeWaysConstructedSuccessfully() {
        int countFootWays = 0;
        int countPOINodes = 0;
        for (NodeWays nodeWays : map.getNetworks().get(NetworkIdentifier.FOOT).getNodeWays().values()) {
            countFootWays += nodeWays.getWays().size();
        }
        for (Node node : map.getNodes().values()) {
            if (node.isPOI()) {
                countPOINodes++;
            }
        }
        assertEquals((int) Math.pow(countPOINodes, 2), countFootWays);
    }

    @Test
    public void testOtherWaysConstructedSuccessfully() {
        int countWays = 0;
        for (Network network : map.getNetworks().values()) {
            if (!network.getType().equals(NetworkIdentifier.FOOT)) {
                for (NodeWays nodeWays : network.getNodeWays().values()) {
                    countWays += nodeWays.getWays().size();
                }
            }
        }
        assertEquals(324, countWays);
    }

}
