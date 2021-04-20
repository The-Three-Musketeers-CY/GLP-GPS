package test.unit;

import static org.junit.Assert.assertNotNull;

import model.identifiers.WayIdentifier;
import model.repositories.WayTypeRepository;
import org.junit.Before;
import org.junit.Test;
import process.builders.WayTypeBuilder;

public class TestWayTypeBuild {

    private WayTypeRepository wayTypeRepository = WayTypeRepository.getInstance();

    @Before
    public void prepareWayTypes() {
        WayTypeBuilder wayTypeBuilder = new WayTypeBuilder();
        wayTypeBuilder.buildWayTypes();
    }

    @Test
    public void testWayTypesConstructedSuccessfully() {
        for (WayIdentifier identifier : WayIdentifier.values()) {
            assertNotNull(wayTypeRepository.getWayTypes().get(identifier));
        }
    }

}
