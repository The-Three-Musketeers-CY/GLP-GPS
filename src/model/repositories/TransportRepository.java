package model.repositories;

import model.Transport;
import model.identifiers.TransportIdentifier;

import java.util.HashMap;

public class TransportRepository {

    private HashMap<model.identifiers.TransportIdentifier, Transport> transports;
    private static TransportRepository instance = new TransportRepository();

    private TransportRepository() {
        transports = new HashMap<>();
    }

    public void addTransport(Transport transportIdentifier) {
        transports.put(transportIdentifier.getIdentifier(), transportIdentifier);
    }

    public HashMap<TransportIdentifier, Transport> getTransports() {
        return transports;
    }

    public static TransportRepository getInstance() {
        return instance;
    }

}
