package etc.cloud.park.service;

import etc.cloud.park.mode.PassagewayModel;
import etc.cloud.park.mode.PassagewayParameterModel;

public interface PassagewayParameterService {

    Object inqueryPassagewayParameterHandler(String token, PassagewayParameterModel passagewayParameterModel);
    Object addPassagewayParameterHandler( String token,PassagewayParameterModel passagewayParameterModel);
    Object alterPassagewayParameterHandler( String token,PassagewayParameterModel passagewayParameterModel);

}
