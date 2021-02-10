package process.builders;

import model.Transport;
import model.repositories.TransportRepository;

public class TransportBuilder {

    public static final int FREE_COST = 0;

    public static final int DEFAULT_CAR_COST = 2;
    public static final float DEFAULT_METRO_BUS_COST = (float) 1.9;
    public static final int DEFAULT_TRAIN_COST = 60;
    public static final int DEFAULT_BOAT_COST = 40;
    public static final int DEFAULT_PLANE_COST = 120;

    private TransportRepository transportRepository = TransportRepository.getInstance();

    public void buildTransports() {
        for (model.identifiers.TransportIdentifier identifier : model.identifiers.TransportIdentifier.values()) {
            Transport transportIdentifier = buildTransport(identifier);
            transportRepository.addTransport(transportIdentifier);
        }
    }

    private Transport buildTransport(model.identifiers.TransportIdentifier identifier) {
        Transport transportIdentifier = null;
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
        }

        return transportIdentifier;
    }

}
