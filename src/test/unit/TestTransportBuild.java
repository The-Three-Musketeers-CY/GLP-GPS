package test.unit;

import static org.junit.Assert.assertNotNull;

import model.identifiers.TransportIdentifier;
import model.repositories.TransportRepository;
import org.junit.Before;
import org.junit.Test;
import process.builders.TransportBuilder;

public class TestTransportBuild {

    private TransportRepository transportRepository = TransportRepository.getInstance();

    @Before
    public void prepareTransports() {
        TransportBuilder transportBuilder = new TransportBuilder();
        transportBuilder.buildTransports();
    }

    @Test
    public void testNetworksConstructedSuccessfully() {
        for (TransportIdentifier identifier : TransportIdentifier.values()) {
            assertNotNull(transportRepository.getTransports().get(identifier));
        }
    }

}
