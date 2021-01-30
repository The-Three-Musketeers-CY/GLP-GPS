package data;

import java.util.HashMap;

public class GlobalNetwork {

    private HashMap<NetworkType,Network> networks = null ;

    private static GlobalNetwork instance = new GlobalNetwork() ;

    private GlobalNetwork(){

    }

    public static GlobalNetwork getInstance() {
        return instance;
    }

    public void addNetwork(Network network){

        NetworkType type = network.getType() ;
        //do a verif if network already exists
        networks.put(type,network);

    }

}
