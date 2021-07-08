package etc.cloud.park.rest;

import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.gateway.service.impl.LoginFilterServiceImpl;
import etc.cloud.park.service.impl.AppointmentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping("etc-cloud/visitor/appointment/")
public class AppointmentController {

    @Autowired
    private AppointmentServiceImpl appointmentService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LoginFilterServiceImpl loginFilterService;


    //新增预约车
    @PostMapping("addcar")
    public Object addcar(@RequestBody String data){

        //获取token
        String token = request.getHeader("X-Token");
        System.out.println(token);
        //查询该账号是否有登录
        String account = loginFilterService.userFilter(token);

        if (account == null){
            return MsgResponse.error().msg("登录已过期");
        }
        //查询该账号是否有这个接口的权限
        Boolean menu = loginFilterService.findMenu(account, 16);
        if(menu == null) return MsgResponse.error().msg("该账号没有这个权限");
        return appointmentService.addcar(data,token).toString();
    }

    //修改预约车信息
    @PostMapping("updatecar")
    public Object updatecar(@RequestBody String data){

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
        return appointmentService.updatecar(data,token).toString();
    }

    //修改状态
    @PostMapping("updatestatus")
    public Object updateStatus(@RequestBody String data){

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
        return appointmentService.updateStatus(data,token).toString();
    }

    //查询
    @PostMapping("findcar")
    public Object findcar(@RequestBody String data){

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
        return appointmentService.findcar(data).toString();
    }


}
