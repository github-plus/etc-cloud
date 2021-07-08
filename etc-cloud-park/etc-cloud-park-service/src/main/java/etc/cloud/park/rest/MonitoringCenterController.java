package etc.cloud.park.rest;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.gateway.service.impl.LoginFilterServiceImpl;
import etc.cloud.park.mode.MonitoringCenterModel;
import etc.cloud.park.service.impl.MonitoringCenterServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@CrossOrigin
@RequestMapping("etc-cloud/basic_setting/passageway_setting")
public class MonitoringCenterController {

    @Autowired
    private MonitoringCenterServiceImpl monitoringCenterServiceImpl;
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LoginFilterServiceImpl loginFilterService;

    @PostMapping("/inquiryOfMonitoringCenter")
    public Object inquiryOfMonitoringCenter(@RequestBody String data){

        JSONObject jsonObject = new JSONObject(data);
        int current = jsonObject.getInt("current");
        int limit = jsonObject.getInt("limit");
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

        MonitoringCenterModel monitoringCenterModel = new MonitoringCenterModel();
        return monitoringCenterServiceImpl.inqueryMonitoringCenterHandler(current, limit,token,monitoringCenterModel);

    }
    @PostMapping("/addMonitoringCenter")
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
        MonitoringCenterModel monitoringCenterModel = new MonitoringCenterModel();

        monitoringCenterModel.setName(Object1.getStr("name"));
        monitoringCenterModel.setIp(Object1.getStr("ip"));
        monitoringCenterModel.setMac(Object1.getStr("mac"));

        return monitoringCenterServiceImpl.addMonitoringCenterHandler(token,monitoringCenterModel);
    }
    @PostMapping("/alterMonitoringCenter")
    public Object alterMonitoringCenter(@RequestBody String data){
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
        MonitoringCenterModel monitoringCenterModel = new MonitoringCenterModel();

        monitoringCenterModel.setPkCenterId(Object1.getInt("pkCenterId"));
        monitoringCenterModel.setName(Object1.getStr("name"));
        monitoringCenterModel.setIp(Object1.getStr("ip"));
        monitoringCenterModel.setMac(Object1.getStr("mac"));

        return monitoringCenterServiceImpl.alterMonitoringCenterHandler(token,monitoringCenterModel);
    }
    @PostMapping("/deleteMonitoringCenter")
    public Object deleteMonitoringCenter(@RequestBody String data){
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



        MonitoringCenterModel monitoringCenterModel = new MonitoringCenterModel();
        int pkCenterId = jsonObject.getInt("pkCenterId");
        monitoringCenterModel.setPkCenterId(pkCenterId);


        return monitoringCenterServiceImpl.deleteMonitoringCenterHandler(token,monitoringCenterModel);
    }
}
