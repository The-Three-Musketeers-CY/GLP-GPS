package process.builders;

import model.Map;
import model.Network;
import model.identifiers.NetworkIdentifier;
import model.identifiers.WayIdentifier;

public class NetworkBuilder {

    public void buildNetworks(Map map) {
        for (NetworkIdentifier type : NetworkIdentifier.values()) {
            Network network = buildNetwork(type);
            map.getNetworks().put(type, network);
        }
    }

    private Network buildNetwork(NetworkIdentifier type) {
        WayIdentifier[] acceptedWays = null;
        switch (type) {
            case AIR:
                acceptedWays = new WayIdentifier[]{ WayIdentifier.PLANE_LINE };
                break;
            case RAIL:
                acceptedWays = new WayIdentifier[]{ WayIdentifier.METRO, WayIdentifier.RAILWAY };
                break;
            case ROAD:
                acceptedWays = new WayIdentifier[]{ WayIdentifier.BUS_LANE, WayIdentifier.CYCLE_LANE, WayIdentifier.HIGHWAY, WayIdentifier.ROAD, WayIdentifier.URBAN_ROAD };
                break;
            case MARITIME:
                acceptedWays = new WayIdentifier[]{ WayIdentifier.BOAT_LANE };
                break;
            default:
                // Renverrai une exception
                break;
        }

        return new Network(type, acceptedWays);
    }

}
