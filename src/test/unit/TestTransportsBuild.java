package test.unit;

import static org.junit.Assert.assertNotNull;

import model.identifiers.TransportIdentifier;
import model.repositories.TransportRepository;
import org.junit.Before;
import org.junit.Test;
import process.builders.TransportBuilder;

/**
 * Unit test of transports building
 */
public class TestTransportsBuild {

    private TransportRepository transportRepository = TransportRepository.getInstance();

    @Before
    public void prepareTransports() {
        TransportBuilder transportBuilder = new TransportBuilder();
        transportBuilder.buildTransports();
    }

    @Test
    public void testTransportsConstructedSuccessfully() {
        for (TransportIdentifier identifier : TransportIdentifier.values()) {
            assertNotNull(transportRepository.getTransports().get(identifier));
        }
    }

}
