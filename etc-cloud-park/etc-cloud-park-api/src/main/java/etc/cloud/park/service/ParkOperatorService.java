package etc.cloud.park.service;

import etc.cloud.park.mode.ParkOperatorModel;
import etc.cloud.park.mode.ParkingModel;

public interface ParkOperatorService {
    Object inqueryOperatorHandler(int current, int limit, String token, ParkOperatorModel parkOperatorModel);
    Object addParkOperatorHandler( String token,ParkOperatorModel parkOperatorModel);
    Object alterParkOperatorHandler( String token,ParkOperatorModel parkOperatorModel);
    Object deleteParkOperatorHandler( String token,ParkOperatorModel parkOperatorModel);
}
