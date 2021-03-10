package process.builders;

import model.Map;
import model.Network;
import model.identifiers.NetworkIdentifier;
import model.identifiers.WayIdentifier;

public class NetworkBuilder {

    /**
     * This method built network for each network type
     * @see NetworkIdentifier
     */
    public void buildNetworks(Map map) {
        for (NetworkIdentifier type : NetworkIdentifier.values()) {
            Network network = buildNetwork(type);
            map.getNetworks().put(type, network);
        }
    }

    private Network buildNetwork(NetworkIdentifier type) {
        WayIdentifier[] acceptedWays = null;
        switch (type) {
            case FOOT:
                acceptedWays = new WayIdentifier[]{ WayIdentifier.FOOT };
                break;
            case AIR:
                acceptedWays = new WayIdentifier[]{ WayIdentifier.PLANE_LANE};
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
        }

        return new Network(type, acceptedWays);
    }

}
