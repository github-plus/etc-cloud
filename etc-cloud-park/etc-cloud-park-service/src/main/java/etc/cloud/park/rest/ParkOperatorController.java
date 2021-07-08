package etc.cloud.park.rest;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.gateway.service.impl.LoginFilterServiceImpl;
import etc.cloud.park.mode.ParkOperatorModel;
import etc.cloud.park.mode.ParkingModel;
import etc.cloud.park.service.impl.CreateParkServiceImpl;
import etc.cloud.park.service.impl.ParkOperatorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@RestController
@CrossOrigin
@RequestMapping("/etc-cloud/basic_setting/operator_setting")
public class ParkOperatorController {
    @Autowired
    private ParkOperatorServiceImpl parkOperatorServiceImpl;
    @Autowired
    private CreateParkServiceImpl createParkService;
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LoginFilterServiceImpl loginFilterService;

    @PostMapping("/inquiryOfOperator")
    public Object inquiryOfOperator(@RequestBody String data){

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

        ParkOperatorModel parkOperatorModel = new ParkOperatorModel();

        return parkOperatorServiceImpl.inqueryOperatorHandler(current, limit,token,parkOperatorModel);

    }
    @PostMapping("/addOperator")
    public Object addOperator(@RequestBody String data){
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
        ParkOperatorModel parkOperatorModel = new ParkOperatorModel();

            parkOperatorModel.setPkAccount(Object1.getInt("pkAccount"));
            parkOperatorModel.setOpName(Object1.getStr("opName"));
            parkOperatorModel.setPassord(Object1.getStr("passord"));
            parkOperatorModel.setState(Object1.getInt("state"));

            //根据停车场名称获得停车场密钥
            String parkKey = createParkService.inquiryParkKey(Object1.getStr("parkName"));
            parkOperatorModel.setPk_secret_key(parkKey);
        return parkOperatorServiceImpl.addParkOperatorHandler(token,parkOperatorModel);
    }
    @PostMapping("/alterOperator")
    public Object alterOperator(@RequestBody String data){
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
        ParkOperatorModel parkOperatorModel = new ParkOperatorModel();

            parkOperatorModel.setPkAccount(Object1.getInt("pkAccount"));
            parkOperatorModel.setOpName(Object1.getStr("opName"));
            parkOperatorModel.setPassord(Object1.getStr("passord"));
            parkOperatorModel.setState(Object1.getInt("state"));

        return parkOperatorServiceImpl.alterParkOperatorHandler(token,parkOperatorModel);
    }
    @PostMapping("/deleteOperator")
    public Object deleteOperator(@RequestBody String data){
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

        ParkOperatorModel parkOperatorModel = new ParkOperatorModel();

            parkOperatorModel.setPkAccount(jsonObject.getInt("pkAccount"));


        return parkOperatorServiceImpl.deleteParkOperatorHandler(token,parkOperatorModel);
    }


}
