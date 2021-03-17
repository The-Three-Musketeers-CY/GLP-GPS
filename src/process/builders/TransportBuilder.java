package process.builders;

import model.Transport;
import model.repositories.TransportRepository;

public class TransportBuilder {

    private static final float FREE_COST = 0;

    private static final float DEFAULT_CAR_COST = 2;
    private static final float DEFAULT_METRO_BUS_COST = (float) 1.9;
    private static final float DEFAULT_TRAIN_COST = 60;
    private static final float DEFAULT_BOAT_COST = 40;
    private static final float DEFAULT_PLANE_COST = 120;

    private TransportRepository transportRepository = TransportRepository.getInstance();

    /**
     * This method built all transports types
     * @see model.identifiers.TransportIdentifier
     */
    public void buildTransports() {
        for (model.identifiers.TransportIdentifier identifier : model.identifiers.TransportIdentifier.values()) {
            Transport transportIdentifier = buildTransport(identifier);
            transportRepository.addTransport(transportIdentifier);
        }
    }

    private Transport buildTransport(model.identifiers.TransportIdentifier identifier) {
        Transport transportIdentifier;
        switch (identifier) {
            case CAR:
                transportIdentifier = new Transport(identifier, DEFAULT_CAR_COST, false);
                break;
            case BUS:
            case METRO:
                transportIdentifier = new Transport(identifier, DEFAULT_METRO_BUS_COST, true);
                break;
            case FOOT:
            case BICYCLE:
                transportIdentifier = new Transport(identifier, FREE_COST, false);
                break;
            case TRAIN:
                transportIdentifier = new Transport(identifier, DEFAULT_TRAIN_COST, true);
                break;
            case BOAT:
                transportIdentifier = new Transport(identifier, DEFAULT_BOAT_COST, true);
                break;
            case PLANE:
                transportIdentifier = new Transport(identifier, DEFAULT_PLANE_COST, true);
                break;
            default:
                // TODO lever une exception
                return null;
        }

        return transportIdentifier;
    }

}
