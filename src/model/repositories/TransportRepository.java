package model.repositories;

import model.Transport;
import model.identifiers.TransportIdentifier;

import java.util.ArrayList;
import java.util.HashMap;

public class TransportRepository {

    private HashMap<model.identifiers.TransportIdentifier, Transport> transports;
    private static TransportRepository instance = new TransportRepository();

    private TransportRepository() {
        transports = new HashMap<>();
    }

    /**
     * This method add Transport to transports list
     * @param transportIdentifier Identifier type of the transport
     */
    public void addTransport(Transport transportIdentifier) {
        transports.put(transportIdentifier.getIdentifier(), transportIdentifier);
    }

    /**
     * This method returns all transports
     * @return The transports list
     */
    public HashMap<TransportIdentifier, Transport> getTransports() {
        return transports;
    }

    /**
     * This method returns all public transports
     * @return The public transports list
     */
    public ArrayList<Transport> getPublicTransports(){

        ArrayList<Transport> publicTransports = new ArrayList<>();

        for(Transport transport : transports.values()){
            if(transport.isPublicTransport()) publicTransports.add(transport) ;
        }

        return publicTransports ;
    }

    /**
     * This method returns all Individual transports
     * @return The individual transports list
     */
    public ArrayList<Transport> getIndividualTransports(){

        ArrayList<Transport> individualTransports = new ArrayList<>();

        for(Transport transport : transports.values()){
            if(!transport.isPublicTransport() && !transport.isFoot()) individualTransports.add(transport) ;
        }

        return individualTransports ;
    }

    /**
     * This method returns all transports without the exclusive transport that the user wants to use
     * @param exclusiveTransportIdentifier the exclusive transport that the user wants to use
     * @return The transports list without the exclusive transports, so all the transports to avoid
     */
    public ArrayList<Transport> getTransportToAvoid(TransportIdentifier exclusiveTransportIdentifier){

        Transport exclusiveTransport = transports.get(exclusiveTransportIdentifier);

        ArrayList<Transport> transportsToAvoid = new ArrayList<>();

        for(Transport transport : transports.values()){
            if(transport != exclusiveTransport) transportsToAvoid.add(transport) ;
        }

        transportsToAvoid.remove(getTransports().get(TransportIdentifier.FOOT));

        return transportsToAvoid ;
    }

    /**
     * @return Instance of this class
     */
    public static TransportRepository getInstance() {
        return instance;
    }

}
