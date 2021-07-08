package etc.cloud.park.rest;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.gateway.service.impl.LoginFilterServiceImpl;
import etc.cloud.park.mode.ParkOperatorModel;
import etc.cloud.park.mode.ParkingModel;
import etc.cloud.park.mode.PassagewayModel;
import etc.cloud.park.service.impl.ParkOperatorServiceImpl;
import etc.cloud.park.service.impl.PassagewayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@RestController
@CrossOrigin
@RequestMapping("/etc-cloud/basic_setting/passageway_setting")
public class PassagewayController {

    @Autowired
    private PassagewayServiceImpl passagewayServiceImpl;
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LoginFilterServiceImpl loginFilterService;
    //注意监控中心名称为新增的字段，不要忘记了
    @PostMapping("/inquiryOfPassageway")
    public Object inquiryOfPassageway(@RequestBody String data) {
        //获取json数据
        JSONObject jsonObject = new JSONObject(data);
        int current = jsonObject.getInt("current");
        int limit = jsonObject.getInt("limit");
        //String token = jsonObject.getStr("token");

//        String center_name = jsonObject.getStr("center_name");
        PassagewayModel passagewayModel = new PassagewayModel();
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
        //收取监控中心id
         int centerId = jsonObject.getInt("centerId");

//        JSONObject object1 = jsonObject.getJSONObject("stus");
//        passagewayModel.setPkChanId(object1.getInt("pkChanId"));
//        passagewayModel.setCenterId(object1.getInt("centerId"));
//        passagewayModel.setChanName(object1.getStr("chanName"));
//        passagewayModel.setChanType(object1.getInt("chanType"));
//        passagewayModel.setEnLeType(object1.getInt("enLeType"));
//        passagewayModel.setDeMainReIp(object1.getStr("deMainReIp"));
//        passagewayModel.setDeMainReport(object1.getInt("deMainReport"));
//        passagewayModel.setDeSndReIp(object1.getStr("deSndReIp"));
//        passagewayModel.setDeSndRePort(object1.getInt("deSndRePort"));
//        passagewayModel.setDeVsIp(object1.getStr("deVsIp"));
//        passagewayModel.setDeVsPort(object1.getInt("deVsPort"));
//        passagewayModel.setDeEtcIp(object1.getStr("deEtcIp"));
//        passagewayModel.setDeEtcPort(object1.getInt("deEtcPort"));
//        passagewayModel.setBeArea(object1.getInt("beArea"));

        //调用接口，根据监控中心id查询对应通道
        return passagewayServiceImpl.inqueryPassagewayHandler(current, limit, centerId, token, passagewayModel);

    }

    @PostMapping("/addPassageway")
    public Object addPassageway(@RequestBody String data) {
        JSONObject jsonObject = new JSONObject(data);
        //String token = jsonObject.getStr("token");
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


        JSONObject object1 = jsonObject.getJSONObject("stus");
        PassagewayModel passagewayModel = new PassagewayModel();


            passagewayModel.setCenterId(object1.getInt("centerId"));
            passagewayModel.setChanName(object1.getStr("chanName"));
            passagewayModel.setChanType(object1.getInt("chanType"));
            passagewayModel.setEnLeType(object1.getInt("enLeType"));
            passagewayModel.setDeMainReIp(object1.getStr("deMainReIp"));
            passagewayModel.setDeMainReport(object1.getInt("deMainReport"));
            passagewayModel.setDeSndReIp(object1.getStr("deSndReIp"));
//            passagewayModel.setDeSndRePort(object1.getInt("deSndRePort"));
            passagewayModel.setDeVsIp(object1.getStr("deVsIp"));
            passagewayModel.setDeVsPort(object1.getInt("deVsPort"));
            passagewayModel.setDeEtcIp(object1.getStr("deEtcIp"));
            passagewayModel.setDeEtcPort(object1.getInt("deEtcPort"));
            passagewayModel.setBeArea(object1.getInt("beArea"));
            if (object1.getInt("chanType") == 0) passagewayModel.setChannelParamId(1);
            else passagewayModel.setChannelParamId(2);


        return passagewayServiceImpl.addPassagewayHandler(token,passagewayModel);
    }

    @PostMapping("/alterPassageway")
    public Object alterPassageway(@RequestBody String data) {
        JSONObject jsonObject = new JSONObject(data);
        //String token = jsonObject.getStr("token");
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


        JSONObject object1 = jsonObject.getJSONObject("stus");
        PassagewayModel passagewayModel = new PassagewayModel();


        passagewayModel.setPkChanId(object1.getInt("pkChanId"));
        passagewayModel.setCenterId(object1.getInt("centerId"));
        passagewayModel.setChanName(object1.getStr("chanName"));
        passagewayModel.setChanType(object1.getInt("chanType"));
        passagewayModel.setEnLeType(object1.getInt("enLeType"));
        passagewayModel.setDeMainReIp(object1.getStr("deMainReIp"));
        passagewayModel.setDeMainReport(object1.getInt("deMainReport"));
        passagewayModel.setDeSndReIp(object1.getStr("deSndReIp"));
        passagewayModel.setDeSndRePort(object1.getInt("deSndRePort"));
        passagewayModel.setDeVsIp(object1.getStr("deVsIp"));
        passagewayModel.setDeVsPort(object1.getInt("deVsPort"));
        passagewayModel.setDeEtcIp(object1.getStr("deEtcIp"));
        passagewayModel.setDeEtcPort(object1.getInt("deEtcPort"));
        passagewayModel.setBeArea(object1.getInt("beArea"));
        if (object1.getInt("chanType") == 0) passagewayModel.setChannelParamId(1);
        else passagewayModel.setChannelParamId(2);

        return passagewayServiceImpl.alterPassagewayHandler(token, passagewayModel);
    }

    @PostMapping("/deletePassageway")
    public Object deletePassageway(@RequestBody String data) {
        JSONObject jsonObject = new JSONObject(data);
        //String token = jsonObject.getStr("token");
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


        JSONObject object1 = jsonObject.getJSONObject("stus");
        //接收要删除的 通道id
        int pkChanId = jsonObject.getInt("pkChanId");
        PassagewayModel passagewayModel = new PassagewayModel();

        passagewayModel.setPkChanId(pkChanId);

        return passagewayServiceImpl.deletePassagewayHandler(token, passagewayModel);
    }
}
