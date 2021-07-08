package etc.cloud.park.rest;

import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.gateway.service.impl.LoginFilterServiceImpl;
import etc.cloud.park.mode.select.MonthSelect;
import etc.cloud.park.service.impl.MonthInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/etc-cloud/month_info")
@CrossOrigin
public class MonthInfoController {
    @Autowired
    private MonthInfoServiceImpl monthInfoService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LoginFilterServiceImpl loginFilterService;

    //月租车管理页面
    @PostMapping("/select_wrapper")
    public Object selectWrapper(@RequestBody(required = false) String data)
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
        return monthInfoService.SelectWrapper(data);
    }

    //新增请求
    @PostMapping("/require_add")
    public Object RequireAdd(@RequestBody(required = false) String data)
    {
        //获取token
        String token = request.getHeader("X-Token");

        //查询该账号是否有登录
        String account = loginFilterService.userFilter(token);

        if (account == null){
            return MsgResponse.error().msg("登录已过期");
        }
        //查询该账号是否有这个接口的权限
        Boolean menu = loginFilterService.findMenu(account, 16);
        if(menu == null) return MsgResponse.error().msg("该账号没有这个权限");
        return monthInfoService.AddRequire(data);
    }

    //新增
    @PostMapping("/add")
    public Object AddMonthInfo(@RequestBody(required = false) String data)
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
        return monthInfoService.AddMonthInfo(data,token);
    }

    //更新月租车
    @PostMapping("/update")
    public Object Update(@RequestBody(required = false) String data)
    {        //获取token
        String token = request.getHeader("X-Token");

        //查询该账号是否有登录
        String account = loginFilterService.userFilter(token);

        if (account == null){
            return MsgResponse.error().msg("登录已过期");
        }
        //查询该账号是否有这个接口的权限
        Boolean menu = loginFilterService.findMenu(account, 11);
        if(menu == null) return MsgResponse.error().msg("该账号没有这个权限");
        return monthInfoService.UpdateMonthInfo(data,token);
    }

    //删除月租车
    @PostMapping("/delete")
    public Object deleteMonthcar(@RequestBody String data){
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
        return monthInfoService.DeleteMonthInfo(data,token);
    }

    @PostMapping("/set_share_mode")
    public Object setShareMode(@RequestBody(required = false) String data)
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
        return monthInfoService.ShareMode(data,token);
    }

    //按下共享车位之后获得的共享车信息
    @PostMapping("/show_share_info")
    public Object gethareInfo(@RequestBody(required = false) String data)
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
        return monthInfoService.ShowShareInfo(data);
    }

    //废弃
    @PostMapping("/shareInfo")
    public Object showShareInfo(@RequestBody(required = false) String data)
    {        //获取token
        String token = request.getHeader("X-Token");

        //查询该账号是否有登录
        String account = loginFilterService.userFilter(token);

        if (account == null){
            return MsgResponse.error().msg("登录已过期");
        }
        //查询该账号是否有这个接口的权限
        Boolean menu = loginFilterService.findMenu(account, 11);
        if(menu == null) return MsgResponse.error().msg("该账号没有这个权限");
        return monthInfoService.ShareInfoDTO(data);
    }

    //废弃
    @PostMapping("/save_share_info")
    public Object saveShareInfo(@RequestBody (required = false)String data)
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
        return monthInfoService.SaveShareInfo(data);
    }

    //增加共享车信息
    @PostMapping("/add_share_info")
    public Object addShareInfo(@RequestBody(required = false) String data)
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
        return monthInfoService.AddShareInfo(data,token);
    }

    //删除共享车信息
    @PostMapping("/delete_share_info")
    public Object deleteShareInfo(@RequestBody(required = false) String data)
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
        return monthInfoService.DeleteShareInfo(data,token);
    }

    //自定义请求
    @PostMapping("/defined_require")
    public Object definedRequire(@RequestBody(required = false) String data)
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
        return monthInfoService.DefinedRequire(data);
    }

    //自定义缴费
    @PostMapping("/defined")
    public Object defined(@RequestBody(required = false) String data)
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
        return monthInfoService.Defined(data,token);
    }

    //延期缴费请求
    @PostMapping("/delay_require")
    public Object payRequire(@RequestBody(required = false) String data)
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
        return monthInfoService.DelayPayRequire(data);
    }

    //延期缴费
    @PostMapping("/delay")
    public Object delay(@RequestBody(required = false) String data)
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
        return monthInfoService.DelayPay(data,token);
    }

    //纠正请求
    @PostMapping("/postpone_require")
    public Object postponeRequire(@RequestBody(required = false) String data)
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
        return monthInfoService.PostponeReqiure(data);
    }

    //纠正
    @PostMapping("/postpone")
    public Object postpone(@RequestBody(required = false) String data)
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
        return monthInfoService.Postpone(data,token);
    }

    //退款请求
    @PostMapping("/refund_require")
    public Object refundRequire(@RequestBody(required = false)String data)
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
        return monthInfoService.RefundRequire(data);
    }

    //退款
    @PostMapping("/refund")
    public Object refund(@RequestBody(required = false)String data)
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
        return monthInfoService.Refund(data,token);
    }
}
