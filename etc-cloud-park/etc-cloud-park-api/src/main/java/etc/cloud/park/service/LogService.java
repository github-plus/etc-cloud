package etc.cloud.park.service;

import etc.cloud.park.mode.LogModel;

public interface LogService {
    Object addLog(LogModel logModel);
    Object inquiryOfLog(String data);
}
