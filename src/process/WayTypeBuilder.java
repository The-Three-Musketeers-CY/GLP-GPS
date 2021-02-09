package process;

import model.WayType;
import model.enums.TransportIdentifier;
import model.enums.WayIdentifier;
import model.repositories.WayTypeRepository;

public class WayTypeBuilder {

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
            case NONE:
                return null;
            case METRO:
                transports = new TransportIdentifier[] { TransportIdentifier.METRO };
                wayType = new WayType(identifier, transports);
                wayType.setSpeed(TransportIdentifier.METRO, 60);
                break;
            case BOAT_LANE:
                transports = new TransportIdentifier[] { TransportIdentifier.BOAT };
                wayType = new WayType(identifier, transports);
                wayType.setSpeed(TransportIdentifier.BOAT, 20);
                break;
            case BUS_LANE:
                transports = new TransportIdentifier[] { TransportIdentifier.BUS };
                wayType = new WayType(identifier, transports);
                wayType.setSpeed(TransportIdentifier.BUS, 40);
                break;
            case CYCLE_LANE:
                transports = new TransportIdentifier[] { TransportIdentifier.BICYCLE };
                wayType = new WayType(identifier, transports);
                wayType.setSpeed(TransportIdentifier.BICYCLE, 15);
                break;
            case PLANE_LINE:
                transports = new TransportIdentifier[] { TransportIdentifier.PLANE };
                wayType = new WayType(identifier, transports);
                wayType.setSpeed(TransportIdentifier.PLANE, 700);
                break;
            case RAILWAY:
                transports = new TransportIdentifier[] { TransportIdentifier.TRAIN };
                wayType = new WayType(identifier, transports);
                wayType.setSpeed(TransportIdentifier.TRAIN, 230);
                break;
            case HIGHWAY:
                transports = new TransportIdentifier[] { TransportIdentifier.CAR };
                wayType = new WayType(identifier, transports);
                wayType.setSpeed(TransportIdentifier.CAR, 130);
                break;
            case ROAD:
                transports = new TransportIdentifier[] { TransportIdentifier.CAR, TransportIdentifier.BICYCLE, TransportIdentifier.BUS };
                wayType = new WayType(identifier, transports);
                wayType.setSpeed(TransportIdentifier.CAR, 80);
                wayType.setSpeed(TransportIdentifier.BICYCLE, 10);
                wayType.setSpeed(TransportIdentifier.BUS, 70);
                break;
            case URBAN_ROAD:
                transports = new TransportIdentifier[] { TransportIdentifier.CAR, TransportIdentifier.BICYCLE, TransportIdentifier.BUS };
                wayType = new WayType(identifier, transports);
                wayType.setSpeed(TransportIdentifier.CAR, 50);
                wayType.setSpeed(TransportIdentifier.BICYCLE, 10);
                wayType.setSpeed(TransportIdentifier.BUS, 35);
                break;
            default:
                // Relever une exception
                return null;
        }

        return wayType;
    }

}
