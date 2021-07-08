package etc.cloud.park.service;

import etc.cloud.park.mode.MonitoringCenterModel;
import etc.cloud.park.mode.ParkOperatorModel;

public interface MonitoringCenterService {
    Object inqueryMonitoringCenterHandler(int current, int limit, String token, MonitoringCenterModel monitoringCenterModel);
    Object addMonitoringCenterHandler( String token,MonitoringCenterModel monitoringCenterModel);
    Object alterMonitoringCenterHandler( String token,MonitoringCenterModel monitoringCenterModel);
    Object deleteMonitoringCenterHandler( String token,MonitoringCenterModel monitoringCenterModel);
}
