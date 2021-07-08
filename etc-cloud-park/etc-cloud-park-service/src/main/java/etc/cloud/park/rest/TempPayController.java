package etc.cloud.park.rest;

import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.gateway.service.impl.LoginFilterServiceImpl;
import etc.cloud.park.service.impl.TempPayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/etc-cloud/temp_pay")
@CrossOrigin
public class TempPayController {
    @Autowired
    private TempPayServiceImpl tempPayService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LoginFilterServiceImpl loginFilterService;

    @PostMapping("/select_wrapper")
    public Object selectWrapper(@RequestBody(required = false) String data) {
        //获取token
        String token = request.getHeader("X-Token");

        //查询该账号是否有登录
        String account = loginFilterService.userFilter(token);

        if (account == null){
            return MsgResponse.error().msg("登录已过期");
        }
        //查询该账号是否有这个接口的权限
        Boolean menu = loginFilterService.findMenu(account, 12);
        if(menu == null) return MsgResponse.error().msg("该账号没有这个权限");
        return tempPayService.SelectTempPay(data);
    }

    @PostMapping("/select_total")
    public Object selectTotal(@RequestBody(required = false) String data)
    {
        //获取token
        String token = request.getHeader("X-Token");

        //查询该账号是否有登录
        String account = loginFilterService.userFilter(token);

        if (account == null){
            return MsgResponse.error().msg("登录已过期");
        }
        //查询该账号是否有这个接口的权限
        Boolean menu = loginFilterService.findMenu(account, 12);
        if(menu == null) return MsgResponse.error().msg("该账号没有这个权限");
        return tempPayService.SelectTempTotal(data);
    }

    @PostMapping("/select_labor")
    public Object selectLabor(@RequestBody(required = false) String data)
    {
        //获取token
        String token = request.getHeader("X-Token");

        //查询该账号是否有登录
        String account = loginFilterService.userFilter(token);

        if (account == null){
            return MsgResponse.error().msg("登录已过期");
        }
        //查询该账号是否有这个接口的权限
        Boolean menu = loginFilterService.findMenu(account, 12);
        if(menu == null) return MsgResponse.error().msg("该账号没有这个权限");
        return tempPayService.SelectOfflinePay(data);
    }

    @PostMapping("/select_online")
    public Object selectOnline(@RequestBody(required = false) String data)
    {
        //获取token
        String token = request.getHeader("X-Token");

        //查询该账号是否有登录
        String account = loginFilterService.userFilter(token);

        if (account == null){
            return MsgResponse.error().msg("登录已过期");
        }
        //查询该账号是否有这个接口的权限
        Boolean menu = loginFilterService.findMenu(account, 12);
        if(menu == null) return MsgResponse.error().msg("该账号没有这个权限");
        return tempPayService.SelectOnlinePay(data);
    }
}
