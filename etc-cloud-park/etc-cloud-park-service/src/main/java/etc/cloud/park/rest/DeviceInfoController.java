package etc.cloud.park.rest;

import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.gateway.service.impl.LoginFilterServiceImpl;
import etc.cloud.park.service.impl.DeviceInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/etc-cloud/device")
@CrossOrigin
public class DeviceInfoController {
    @Autowired
    private DeviceInfoServiceImpl deviceInfoService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LoginFilterServiceImpl loginFilterService;

    @PostMapping("/select_wrapper")
    public Object device(@RequestBody(required = false) String data)
    {

        //获取token
        String token = request.getHeader("X-Token");

        //查询该账号是否有登录
        String account = loginFilterService.userFilter(token);

        if (account == null){
            return MsgResponse.error().msg("登录已过期");
        }
        //查询该账号是否有这个接口的权限
        Boolean menu = loginFilterService.findMenu(account, 11);
        if(menu == null) return MsgResponse.error().msg("该账号没有这个权限");
        return deviceInfoService.SelectDeviceInfo(data);
    }

    @PostMapping("select_log")
    public Object deviceLog(@RequestBody (required = false) String data)
    {

        //获取token
        String token = request.getHeader("X-Token");

        //查询该账号是否有登录
        String account = loginFilterService.userFilter(token);

        if (account == null){
            return MsgResponse.error().msg("登录已过期");
        }
        //查询该账号是否有这个接口的权限
        Boolean menu = loginFilterService.findMenu(account, 11);
        if(menu == null) return MsgResponse.error().msg("该账号没有这个权限");
        return  deviceInfoService.SelectDeviceLog(data);
    }
}
