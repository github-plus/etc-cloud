package etc.cloud.park.service;

import etc.cloud.park.mode.ParkingModel;
import etc.cloud.park.mode.ParkingParameterModel;

public interface ParkingParameterService {

    Object inqueryParkingParameterInfoHandler( String token, ParkingParameterModel parkingParameterModel);

    Object addParkingParameterHandler(String token, ParkingParameterModel parkingParameterModel);
    Object alterParkingParameterHandler( String token,ParkingParameterModel parkingParameterModel);
}
