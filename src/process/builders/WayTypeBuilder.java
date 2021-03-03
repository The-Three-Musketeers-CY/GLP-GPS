package process.builders;

import model.WayType;
import model.identifiers.TransportIdentifier;
import model.identifiers.WayIdentifier;
import model.repositories.WayTypeRepository;

public class WayTypeBuilder {

    public static final int DEFAULT_FOOT_SPEED = 5;

    public static final int DEFAULT_ROAD_CAR_SPEED = 80;
    public static final int DEFAULT_ROAD_BICYCLE_SPEED = 10;
    public static final int DEFAULT_ROAD_BUS_SPEED = 70;

    public static final int DEFAULT_URBAN_ROAD_CAR_SPEED = 50;
    public static final int DEFAULT_URBAN_ROAD_BICYCLE_SPEED = 10;
    public static final int DEFAULT_URBAN_ROAD_BUS_SPEED = 35;

    public static final int DEFAULT_HIGHWAY_CAR_SPEED = 130;
    public static final int DEFAULT_BUS_LANE_BUS_SPEED = 40;
    public static final int DEFAULT_CYCLE_LANE_BICYCLE_SPEED = 15;

    public static final int DEFAULT_METRO_SPEED = 60;
    public static final int DEFAULT_BOAT_SPEED = 20;
    public static final int DEFAULT_PLANE_SPEED = 700;
    public static final int DEFAULT_TRAIN_SPEED = 230;

    private WayTypeRepository wayTypeRepository = WayTypeRepository.getInstance();

    public void buildWayTypes() {
        for (WayIdentifier identifier : WayIdentifier.values()) {
            WayType wayType = buildWayType(identifier);
            wayTypeRepository.getWayTypes().put(identifier, wayType);
        }
    }

    private WayType buildWayType(WayIdentifier identifier) {
        WayType wayType;
        TransportIdentifier[] transports;
        switch (identifier) {
            case FOOT:
                transports = new TransportIdentifier[] { TransportIdentifier.FOOT };
                wayType = new WayType(identifier, transports);
                wayType.setSpeed(TransportIdentifier.FOOT, DEFAULT_FOOT_SPEED);
                break;
            case METRO:
                transports = new TransportIdentifier[] { TransportIdentifier.METRO };
                wayType = new WayType(identifier, transports);
                wayType.setSpeed(TransportIdentifier.METRO, DEFAULT_METRO_SPEED);
                break;
            case BOAT_LANE:
                transports = new TransportIdentifier[] { TransportIdentifier.BOAT };
                wayType = new WayType(identifier, transports);
                wayType.setSpeed(TransportIdentifier.BOAT, DEFAULT_BOAT_SPEED);
                break;
            case BUS_LANE:
                transports = new TransportIdentifier[] { TransportIdentifier.BUS };
                wayType = new WayType(identifier, transports);
                wayType.setSpeed(TransportIdentifier.BUS, DEFAULT_BUS_LANE_BUS_SPEED);
                break;
            case CYCLE_LANE:
                transports = new TransportIdentifier[] { TransportIdentifier.BICYCLE };
                wayType = new WayType(identifier, transports);
                wayType.setSpeed(TransportIdentifier.BICYCLE, DEFAULT_CYCLE_LANE_BICYCLE_SPEED);
                break;
            case PLANE_LANE:
                transports = new TransportIdentifier[] { TransportIdentifier.PLANE };
                wayType = new WayType(identifier, transports);
                wayType.setSpeed(TransportIdentifier.PLANE, DEFAULT_PLANE_SPEED);
                break;
            case RAILWAY:
                transports = new TransportIdentifier[] { TransportIdentifier.TRAIN };
                wayType = new WayType(identifier, transports);
                wayType.setSpeed(TransportIdentifier.TRAIN, DEFAULT_TRAIN_SPEED);
                break;
            case HIGHWAY:
                transports = new TransportIdentifier[] { TransportIdentifier.CAR };
                wayType = new WayType(identifier, transports);
                wayType.setSpeed(TransportIdentifier.CAR, DEFAULT_HIGHWAY_CAR_SPEED);
                break;
            case ROAD:
                transports = new TransportIdentifier[] { TransportIdentifier.CAR, TransportIdentifier.BICYCLE, TransportIdentifier.BUS };
                wayType = new WayType(identifier, transports);
                wayType.setSpeed(TransportIdentifier.CAR, DEFAULT_ROAD_CAR_SPEED);
                wayType.setSpeed(TransportIdentifier.BICYCLE, DEFAULT_ROAD_BICYCLE_SPEED);
                wayType.setSpeed(TransportIdentifier.BUS, DEFAULT_ROAD_BUS_SPEED);
                break;
            case URBAN_ROAD:
                transports = new TransportIdentifier[] { TransportIdentifier.CAR, TransportIdentifier.BICYCLE, TransportIdentifier.BUS };
                wayType = new WayType(identifier, transports);
                wayType.setSpeed(TransportIdentifier.CAR, DEFAULT_URBAN_ROAD_CAR_SPEED);
                wayType.setSpeed(TransportIdentifier.BICYCLE, DEFAULT_URBAN_ROAD_BICYCLE_SPEED);
                wayType.setSpeed(TransportIdentifier.BUS, DEFAULT_URBAN_ROAD_BUS_SPEED);
                break;
            default:
                // Relever une exception
                return null;
        }

        return wayType;
    }

}
