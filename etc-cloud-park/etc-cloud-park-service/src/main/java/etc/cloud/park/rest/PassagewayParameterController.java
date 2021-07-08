package etc.cloud.park.rest;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.gateway.service.impl.LoginFilterServiceImpl;
import etc.cloud.park.mode.PassagewayModel;
import etc.cloud.park.mode.PassagewayParameterModel;
import etc.cloud.park.service.impl.PassagewayParameterServiceImpl;
import etc.cloud.park.service.impl.PassagewayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping("/etc-cloud/basic_setting/passageway_parameters_setting")
public class PassagewayParameterController {

    @Autowired
    private PassagewayParameterServiceImpl passagewayParameterServiceImpl;
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LoginFilterServiceImpl loginFilterService;
    @PostMapping("/inquiryOfPassagewayParameter")
    public Object inquiryOfPassagewayParameter(@RequestBody String data) {

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
        PassagewayParameterModel passagewayParameterModel = new PassagewayParameterModel();

        return passagewayParameterServiceImpl.inqueryPassagewayParameterHandler(token, passagewayParameterModel);

    }

    @PostMapping("/addPassagewayParameter")
    public Object addPassagewayParameter(@RequestBody String data) {
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
        PassagewayParameterModel passagewayParameterModel = new PassagewayParameterModel();

        passagewayParameterModel.setIsUnusual(object1.getInt("isUnusual"));

        passagewayParameterModel.setUnusualTime(object1.getInt("unusualTime"));
        passagewayParameterModel.setDefaultCenterId(object1.getInt("defaultCenterId"));
        passagewayParameterModel.setOverTime(object1.getInt("overTime"));
        passagewayParameterModel.setOverCenterId(object1.getInt("overCenterId"));
        passagewayParameterModel.setMonthDim(object1.getInt("monthDim"));

        passagewayParameterModel.setIdentification(object1.getInt("identification"));
        passagewayParameterModel.setIsCorrect(object1.getInt("isCorrect"));
        passagewayParameterModel.setTempInTime(object1.getStr("tempInTime"));
        passagewayParameterModel.setVoiceControlTimeFirst(object1.getStr("voiceControlTimeFirst"));
        passagewayParameterModel.setVoiceControlFirst(object1.getInt("voiceControlFirst"));
        passagewayParameterModel.setVoiceControlTimeSecond(object1.getStr("voiceControlTimeSecond"));
        passagewayParameterModel.setVoiceControlSecond(object1.getInt("voiceControlSecond"));
        passagewayParameterModel.setChanPlaceType(object1.getInt("chanPlaceType"));
        passagewayParameterModel.setChanPlace(object1.getInt("chanPlace"));
        passagewayParameterModel.setShowScreen(object1.getStr("showScreen"));

        return passagewayParameterServiceImpl.addPassagewayParameterHandler(token, passagewayParameterModel);
    }

    @PostMapping("/alterPassagewayParameter")
    public Object alterPassagewayParameter(@RequestBody String data) {
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
        PassagewayParameterModel passagewayParameterModel = new PassagewayParameterModel();

        passagewayParameterModel.setChannelParamId(object1.getInt("channelParamId"));
        passagewayParameterModel.setIsUnusual(object1.getInt("isUnusual"));

        passagewayParameterModel.setUnusualTime(object1.getInt("unusualTime"));
        passagewayParameterModel.setDefaultCenterId(object1.getInt("defaultCenterId"));
        passagewayParameterModel.setOverTime(object1.getInt("overTime"));
        passagewayParameterModel.setOverCenterId(object1.getInt("overCenterId"));
        passagewayParameterModel.setMonthDim(object1.getInt("monthDim"));

        passagewayParameterModel.setIdentification(object1.getInt("identification"));
        passagewayParameterModel.setIsCorrect(object1.getInt("isCorrect"));
        passagewayParameterModel.setTempInTime(object1.getStr("tempInTime"));
        passagewayParameterModel.setVoiceControlTimeFirst(object1.getStr("voiceControlTimeFirst"));
        passagewayParameterModel.setVoiceControlFirst(object1.getInt("voiceControlFirst"));
        passagewayParameterModel.setVoiceControlTimeSecond(object1.getStr("voiceControlTimeSecond"));
        passagewayParameterModel.setVoiceControlSecond(object1.getInt("voiceControlSecond"));
        passagewayParameterModel.setChanPlaceType(object1.getInt("chanPlaceType"));
        passagewayParameterModel.setChanPlace(object1.getInt("chanPlace"));
        passagewayParameterModel.setShowScreen(object1.getStr("showScreen"));

        return passagewayParameterServiceImpl.alterPassagewayParameterHandler(token, passagewayParameterModel);
    }
}
