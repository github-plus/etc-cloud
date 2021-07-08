package etc.cloud.park.rest;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.gateway.service.impl.LoginFilterServiceImpl;
import etc.cloud.park.mode.ParkingModel;
import etc.cloud.park.mode.ParkingParameterModel;
import etc.cloud.park.service.impl.ParkingParameterServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@RestController
@CrossOrigin
@RequestMapping("/etc-cloud/basic_setting/parking_setting")
public class ParkingParameterController {

    @Autowired
    private ParkingParameterServiceImpl parkingParameterService;
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LoginFilterServiceImpl loginFilterService;
    @PostMapping("/inquiryOfParkingParameter")
    public Object inquiryOfParkingParameter(@RequestBody String data){
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
        ParkingParameterModel parkingParameterModel = new ParkingParameterModel();
        return parkingParameterService.inqueryParkingParameterInfoHandler(token,parkingParameterModel);

    }

    @PostMapping("/addParkingParameter")
    public Object addParkingParameter(@RequestBody String data){

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


        JSONObject object1 = jsonObject.getJSONObject("stus");
        ParkingParameterModel parkingParameterModel = new ParkingParameterModel();
        //新增的时候没有主键id，修改的时候需要传主键id

            parkingParameterModel.setTempChargeMode(object1.getInt("tempChargeMode"));
            parkingParameterModel.setCarSum(object1.getInt("carSum"));
            parkingParameterModel.setTempSum(object1.getInt("tempSum"));
            parkingParameterModel.setTempInMode(object1.getInt("tempInMode"));
            parkingParameterModel.setMonthResidue(object1.getInt("monthResidue"));
            parkingParameterModel.setCatalogue(object1.getStr("catalogue"));
            parkingParameterModel.setMonthOverMode(object1.getInt("monthOverMode"));
            parkingParameterModel.setMonthOverDay(object1.getInt("monthOverDay"));
            parkingParameterModel.setTempNonRc(object1.getInt("tempNonRc"));
            parkingParameterModel.setIsPayOnline(object1.getInt("isPayOnline"));
            parkingParameterModel.setPayOnlineFreeMinute(object1.getInt("payOnlineFreeMinute"));
            parkingParameterModel.setIsAutoPolice(object1.getInt("isAutoPolice"));
            parkingParameterModel.setIsAutoArmy(object1.getInt("isAutoArmy"));
            parkingParameterModel.setIsMerchantDiscount(object1.getInt("isMerchantDiscount"));
            parkingParameterModel.setIsAreaCharge(object1.getInt("isAreaCharge"));
            parkingParameterModel.setIsTelDirect(object1.getInt("isTelDirect"));
            parkingParameterModel.setIsNonFell(object1.getInt("isNonFell"));
            parkingParameterModel.setIsValueCar(object1.getInt("isValueCar"));
            parkingParameterModel.setIsNonCarNumber(object1.getInt("isNonCarNumber"));
            parkingParameterModel.setIsNewCar(object1.getInt("isNewCar"));
            parkingParameterModel.setIsCarIdentity(object1.getInt("isCarIdentity"));
            parkingParameterModel.setBlThreshold(object1.getInt("blThreshold"));



        return parkingParameterService.addParkingParameterHandler(token,parkingParameterModel);



    }

    @PostMapping("/alterParkingParameter")
    public Object alterParkingParameter(@RequestBody String data){

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


        JSONObject object1 = jsonObject.getJSONObject("stus");
        ParkingParameterModel parkingParameterModel = new ParkingParameterModel();
        //新增的时候没有主键id，修改的时候需要传主键id

            parkingParameterModel.setPkId(object1.getInt("pkId"));
            parkingParameterModel.setTempChargeMode(object1.getInt("tempChargeMode"));
            parkingParameterModel.setCarSum(object1.getInt("carSum"));
            parkingParameterModel.setTempSum(object1.getInt("tempSum"));
            parkingParameterModel.setTempInMode(object1.getInt("tempInMode"));
            parkingParameterModel.setMonthResidue(object1.getInt("monthResidue"));
            parkingParameterModel.setCatalogue(object1.getStr("catalogue"));
            parkingParameterModel.setMonthOverMode(object1.getInt("monthOverMode"));
            parkingParameterModel.setMonthOverDay(object1.getInt("monthOverDay"));
            parkingParameterModel.setTempNonRc(object1.getInt("tempNonRc"));
            parkingParameterModel.setIsPayOnline(object1.getInt("isPayOnline"));
            parkingParameterModel.setPayOnlineFreeMinute(object1.getInt("payOnlineFreeMinute"));
            parkingParameterModel.setIsAutoPolice(object1.getInt("isAutoPolice"));
            parkingParameterModel.setIsAutoArmy(object1.getInt("isAutoArmy"));
            parkingParameterModel.setIsMerchantDiscount(object1.getInt("isMerchantDiscount"));
            parkingParameterModel.setIsAreaCharge(object1.getInt("isAreaCharge"));
            parkingParameterModel.setIsTelDirect(object1.getInt("isTelDirect"));
            parkingParameterModel.setIsNonFell(object1.getInt("isNonFell"));
            parkingParameterModel.setIsValueCar(object1.getInt("isValueCar"));
            parkingParameterModel.setIsNonCarNumber(object1.getInt("isNonCarNumber"));
            parkingParameterModel.setIsNewCar(object1.getInt("isNewCar"));
            parkingParameterModel.setIsCarIdentity(object1.getInt("isCarIdentity"));
            parkingParameterModel.setBlThreshold(object1.getInt("blThreshold"));

        return parkingParameterService.alterParkingParameterHandler(token,parkingParameterModel);



    }
}
