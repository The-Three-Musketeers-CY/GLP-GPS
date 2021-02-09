package process;

import model.Transport;
import model.repositories.TransportRepository;

public class TransportBuilder {

    private TransportRepository transportRepository = TransportRepository.getInstance();

    public void buildTransports() {
        for (model.enums.TransportIdentifier identifier : model.enums.TransportIdentifier.values()) {
            Transport transportIdentifier = buildTransport(identifier);
            transportRepository.addTransport(transportIdentifier);
        }
    }

    private Transport buildTransport(model.enums.TransportIdentifier identifier) {
        Transport transportIdentifier = null;
        switch (identifier) {
            case CAR:
                transportIdentifier = new Transport(identifier, 2, false);
                break;
            case BUS:
            case METRO:
                transportIdentifier = new Transport(identifier, (float) 1.9, true);
                break;
            case FOOT:
            case BICYCLE:
                transportIdentifier = new Transport(identifier, 0, false);
                break;
            case TRAIN:
                transportIdentifier = new Transport(identifier, 60, true);
                break;
            case BOAT:
                transportIdentifier = new Transport(identifier, 40, true);
                break;
            case PLANE:
                transportIdentifier = new Transport(identifier, 120, true);
                break;
        }

        return transportIdentifier;
    }

}
