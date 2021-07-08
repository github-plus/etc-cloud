package etc.cloud.park.service;


import com.baomidou.mybatisplus.extension.service.IService;
import etc.cloud.park.mode.ParkingModel;

//@FunctionalInterface
public interface CreateParkService  {
    //查询信息接口
    Object inqueryInfoHandler(int current, int limit, String token,ParkingModel parkingModel);

    Object addParkHandler( String token,ParkingModel parkingModel);
    Object alterParkHandler( String token,ParkingModel parkingModel);
    String inquiryParkKey(String parkName); //根据车厂名称查询车厂密钥

}
