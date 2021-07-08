package etc.cloud.park.rest;

import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.gateway.service.impl.LoginFilterServiceImpl;
import etc.cloud.park.service.impl.MonthlyCarPayRulesServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping("/etc-cloud/basic_setting/monthlycar_rules_setting")
public class MonthlyCarPayRulesController {
    @Autowired
    private MonthlyCarPayRulesServiceImpl monthlyCarPayRulesService;
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LoginFilterServiceImpl loginFilterService;

    @PostMapping("/inquiryOfMonthlyCarPayRules")
    public Object inquiryOfMonthlyCarPayRules(@RequestBody String data){
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
        return monthlyCarPayRulesService.inqueryMonthlyCarPayRulesHandler(data);

    }
    @PostMapping("/addMonthlyCarPayRules")
    public Object addMonthlyCarPayRules(@RequestBody String data){
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
        return monthlyCarPayRulesService.addMonthlyCarPayRulesHandler(data,token);

    }
    @PostMapping("/alterMonthlyCarPayRules")
    public Object alterMonthlyCarPayRules(@RequestBody String data){
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
        return monthlyCarPayRulesService.alterMonthlyCarPayRulesHandler(data,token);

    }
    @PostMapping("/deleteMonthlyCarPayRules")
    public Object deleteMonthlyCarPayRules(@RequestBody String data){
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
        return monthlyCarPayRulesService.deleteMonthlyCarPayRulesHandler(data,token);

    }
    @PostMapping("/updateMonthlyCarPayMode")
    public Object updateMonthlyCarPayMode(@RequestBody String data){
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
        return monthlyCarPayRulesService.updateMonthlyCarPayModeHandler(data,token);

    }

}
