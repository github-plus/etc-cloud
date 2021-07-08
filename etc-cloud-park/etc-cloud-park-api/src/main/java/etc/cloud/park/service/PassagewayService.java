package etc.cloud.park.service;

import etc.cloud.park.mode.ParkOperatorModel;
import etc.cloud.park.mode.PassagewayModel;

public interface PassagewayService {

    Object inqueryPassagewayHandler(int current, int limit,int centerId, String token, PassagewayModel passagewayModel);
    Object addPassagewayHandler( String token,PassagewayModel passagewayModel);
    Object alterPassagewayHandler( String token,PassagewayModel passagewayModel);
    Object deletePassagewayHandler( String token,PassagewayModel passagewayModel);
}
