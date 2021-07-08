package etc.cloud.park.service;

import com.baomidou.mybatisplus.extension.service.IService;
import etc.cloud.park.mode.AppointmentModel;

public interface AppointmentService  extends IService<AppointmentModel> {

    //添加预约车
    Object addcar(String data,String token);

    //修改预约车信息
    Object updatecar(String data,String token);

    //修改预约车状态
    Object updateStatus(String data,String token);

    //查询预约车信息
    Object findcar(String data);

}
