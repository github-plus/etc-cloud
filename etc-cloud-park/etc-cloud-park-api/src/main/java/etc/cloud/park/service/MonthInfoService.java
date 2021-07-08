package etc.cloud.park.service;

import com.baomidou.mybatisplus.extension.service.IService;
import etc.cloud.park.mode.MonthInfoModel;

import java.sql.Timestamp;

public interface MonthInfoService {
    public Object SelectWrapper(String data);

    public Object AddRequire(String data);

    public Object AddMonthInfo(String data,String token);

    public Object DeleteMonthInfo(String data,String token);

    public Object UpdateMonthInfo(String data,String token);

    public Object ShareInfoDTO(String data);

    public Object ShareMode(String data,String token);

    public Object DefinedRequire(String data);

    public Object Defined(String data,String token);

    public Object DelayPayRequire(String data);

    public Object DelayPay(String data,String token);

    public Object PostponeReqiure(String data);

    public Object Postpone(String data,String token);

    public Object AddShareInfo(String data,String token);

    public Object DeleteShareInfo(String data,String token);

    public Object Refund(String data,String token);
}
