package data;

import java.util.HashMap;

public class GlobalNetwork {

    private HashMap<String,Network> networks = null ;

    private static GlobalNetwork instance = new GlobalNetwork() ;

    private GlobalNetwork(){

    }

    public static GlobalNetwork getInstance() {
        return instance;
    }

    public void addNetwork(Network network){

        String type = network.getType() ;
        //do a verif if networks already exist
        networks.put(type,network);

    }

}
