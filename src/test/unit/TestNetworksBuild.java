package test.unit;

import static org.junit.Assert.assertNotNull;

import model.Map;
import model.identifiers.NetworkIdentifier;
import org.junit.Before;
import org.junit.Test;
import process.builders.NetworkBuilder;

/**
 * Unit test of networks building
 */
public class TestNetworksBuild {

    private Map map;

    @Before
    public void prepareNetworks() {
        map = new Map();
        NetworkBuilder networkBuilder = new NetworkBuilder();
        networkBuilder.buildNetworks(map);
    }

    @Test
    public void testNetworksConstructedSuccessfully() {
        for (NetworkIdentifier identifier : NetworkIdentifier.values()) {
            assertNotNull(map.getNetworks().get(identifier));
        }
    }

}
