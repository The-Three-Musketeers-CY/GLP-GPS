package process.builders;

import log.LoggerUtility;
import model.Transport;
import model.repositories.TransportRepository;
import org.apache.log4j.Logger;

public class TransportBuilder {

    private static final float FREE_COST = 0;

    private static final float DEFAULT_CAR_COST = (float) 0.002;
    private static final float DEFAULT_METRO_BUS_COST = (float) 1.9;
    private static final float DEFAULT_TRAIN_COST = 60;
    private static final float DEFAULT_BOAT_COST = 40;
    private static final float DEFAULT_PLANE_COST = 120;

    private TransportRepository transportRepository = TransportRepository.getInstance();

    private Logger logger = LoggerUtility.getLogger(TransportBuilder.class, "html");

    /**
     * This method built all transports types
     * @see model.identifiers.TransportIdentifier
     */
    public void buildTransports() throws IllegalArgumentException{
        logger.info("Start transports construction");

        for (model.identifiers.TransportIdentifier identifier : model.identifiers.TransportIdentifier.values()) {
            Transport transportIdentifier = buildTransport(identifier);
            transportRepository.addTransport(transportIdentifier);
        }

        logger.info(transportRepository.getTransports().size() + " transports built successfully");
    }

    private Transport buildTransport(model.identifiers.TransportIdentifier identifier) throws IllegalArgumentException {
        Transport transport;
        switch (identifier) {
            case CAR:
                transport = new Transport(identifier, DEFAULT_CAR_COST, false);
                break;
            case BUS:
            case METRO:
                transport = new Transport(identifier, DEFAULT_METRO_BUS_COST, true);
                break;
            case FOOT:
            case BICYCLE:
                transport = new Transport(identifier, FREE_COST, false);
                break;
            case TRAIN:
                transport = new Transport(identifier, DEFAULT_TRAIN_COST, true);
                break;
            case BOAT:
                transport = new Transport(identifier, DEFAULT_BOAT_COST, true);
                break;
            case PLANE:
                transport = new Transport(identifier, DEFAULT_PLANE_COST, true);
                break;
            default:
                throw new IllegalArgumentException("Invalid transport type. Check available transports");
        }

        logger.info(identifier + " creation");

        return transport;
    }

}
