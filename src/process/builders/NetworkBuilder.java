package process.builders;

import log.LoggerUtility;
import model.Map;
import model.Network;
import model.identifiers.NetworkIdentifier;
import model.identifiers.WayIdentifier;
import org.apache.log4j.Logger;

public class NetworkBuilder {

    private Logger logger = LoggerUtility.getLogger(NetworkBuilder.class, "html");

    /**
     * This method built network for each network type
     * @see NetworkIdentifier
     */
    public void buildNetworks(Map map) {
        logger.info("Start networks construction");
        for (NetworkIdentifier type : NetworkIdentifier.values()) {
            Network network = buildNetwork(type);
            map.getNetworks().put(type, network);
        }
        logger.info(map.getNetworks().size() + " networks built successfully");
    }

    private Network buildNetwork(NetworkIdentifier type) {
        WayIdentifier[] acceptedWays ;
        switch (type) {
            case FOOT:
                logger.info("Foot network creation");
                acceptedWays = new WayIdentifier[]{ WayIdentifier.FOOT };
                break;
            case AIR:
                logger.info("Aerial network creation");
                acceptedWays = new WayIdentifier[]{ WayIdentifier.PLANE_LANE};
                break;
            case RAIL:
                logger.info("Rail network creation");
                acceptedWays = new WayIdentifier[]{ WayIdentifier.METRO, WayIdentifier.RAILWAY };
                break;
            case ROAD:
                logger.info("Road network creation");
                acceptedWays = new WayIdentifier[]{ WayIdentifier.BUS_LANE, WayIdentifier.CYCLE_LANE, WayIdentifier.HIGHWAY, WayIdentifier.ROAD, WayIdentifier.URBAN_ROAD };
                break;
            case MARITIME:
                logger.info("Maritime network creation");
                acceptedWays = new WayIdentifier[]{ WayIdentifier.BOAT_LANE };
                break;
            default:
                // TODO : Lever une exception
                return null;

        }

        return new Network(type, acceptedWays);
    }

}
