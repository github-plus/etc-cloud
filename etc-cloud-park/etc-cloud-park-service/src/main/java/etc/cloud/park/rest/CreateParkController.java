package etc.cloud.park.rest;







import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.google.gson.JsonArray;
import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.gateway.service.impl.LoginFilterServiceImpl;
import etc.cloud.park.mapper.ParkingMapper;
import etc.cloud.park.mode.ParkingModel;
import etc.cloud.park.service.impl.CreateParkServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@RestController
@CrossOrigin
@RequestMapping("/etc-cloud/basic_setting/create_park")
public class CreateParkController {
    @Autowired
    private CreateParkServiceImpl createParkServiceImpl;
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LoginFilterServiceImpl loginFilterService;

    @PostMapping("/inquiryOfParking")
    public Object inquiryOfPark(@RequestBody String data){
        //解析数据
        JSONObject jsonObject = new JSONObject(data);
        int current = jsonObject.getInt("current");
        int limit = jsonObject.getInt("limit");
        //获取token
        String token = request.getHeader("X-Token");

        //查询该账号是否有登录
        String account = loginFilterService.userFilter(token);

        if (account == null){
            return MsgResponse.error().msg("登录已过期");
        }
        //查询该账号是否有这个接口的权限
        Boolean menu = loginFilterService.findMenu(account, 10);
        if(menu == null) return MsgResponse.error().msg("该账号没有这个权限");


        JSONObject Object1 = jsonObject.getJSONObject("stus");
        ParkingModel parkingModel = new ParkingModel();

//            parkingModel.setPkSecretKey(Object1.getStr("pkSecretKey"));
//            parkingModel.setParkName(Object1.getStr("parkName"));
//            parkingModel.setProjectName(Object1.getStr("projectName"));
//            parkingModel.setAddress(Object1.getStr("address"));
//            parkingModel.setCommunity(Object1.getStr("community"));
//            parkingModel.setDimension(Object1.getInt("dimension"));
//            parkingModel.setLongitude(Object1.getInt("longitude"));
        //调用接口
        return createParkServiceImpl.inqueryInfoHandler(current, limit,token,parkingModel);



    }

    @PostMapping("/addParking")
    public Object addPark(@RequestBody String data){
        //解析数据
        JSONObject jsonObject = new JSONObject(data);
       // String token = jsonObject.getStr("token");
        //获取token
        String token = request.getHeader("X-Token");

        //查询该账号是否有登录
        String account = loginFilterService.userFilter(token);

        if (account == null){
            return MsgResponse.error().msg("登录已过期");
        }
        //查询该账号是否有这个接口的权限
        Boolean menu = loginFilterService.findMenu(account, 10);
        if(menu == null) return MsgResponse.error().msg("该账号没有这个权限");


        JSONObject Object1 = jsonObject.getJSONObject("stus");
        ParkingModel parkingModel = new ParkingModel();
        //设置实体类
            //uuid生成车厂密钥
            String pkSecretKey = UUID.randomUUID().toString();
            parkingModel.setPkSecretKey(pkSecretKey);

            parkingModel.setParkName(Object1.getStr("parkName"));
            parkingModel.setProjectName(Object1.getStr("projectName"));
            parkingModel.setAddress(Object1.getStr("address"));
            parkingModel.setCommunity(Object1.getStr("community"));
            parkingModel.setDimension(Object1.getInt("dimension"));
            parkingModel.setLongitude(Object1.getInt("longitude"));

        return createParkServiceImpl.addParkHandler(token,parkingModel);



    }

    @PostMapping("/alterParking")
    public Object alterParking(@RequestBody String data){

        JSONObject jsonObject = new JSONObject(data);

        // String token = jsonObject.getStr("token");
        //获取token
        String token = request.getHeader("X-Token");

        //查询该账号是否有登录
        String account = loginFilterService.userFilter(token);

        if (account == null){
            return MsgResponse.error().msg("登录已过期");
        }
        //查询该账号是否有这个接口的权限
        Boolean menu = loginFilterService.findMenu(account, 10);
        if(menu == null) return MsgResponse.error().msg("该账号没有这个权限");


        JSONObject Object1 = jsonObject.getJSONObject("stus");
        ParkingModel parkingModel = new ParkingModel();

            parkingModel.setPkSecretKey(Object1.getStr("pkSecretKey"));
            parkingModel.setParkName(Object1.getStr("parkName"));
            parkingModel.setProjectName(Object1.getStr("projectName"));
            parkingModel.setAddress(Object1.getStr("address"));
            parkingModel.setCommunity(Object1.getStr("community"));
            parkingModel.setDimension(Object1.getInt("dimension"));
            parkingModel.setLongitude(Object1.getInt("longitude"));

        return createParkServiceImpl.alterParkHandler(token,parkingModel);



    }

}

