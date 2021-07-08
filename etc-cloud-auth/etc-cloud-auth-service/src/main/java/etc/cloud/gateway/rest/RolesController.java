package etc.cloud.gateway.rest;

import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.gateway.service.impl.LoginFilterServiceImpl;
import etc.cloud.gateway.service.impl.RoleWorkerServiceImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("etc-cloud/role-worker/")
@CrossOrigin
public class RolesController {

    @Autowired
    private RoleWorkerServiceImpl roleWorkerService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LoginFilterServiceImpl loginFilterService;

    //查找角色
    @PostMapping("findrole")
    public Object findRole(@RequestBody String data){

        //获取token
        String token = request.getHeader("X-Token");
        //查询该账号是否有登录
        String account = loginFilterService.userFilter(token);

        if (account == null){
            return MsgResponse.error().msg("登录已过期");
        }
        //查询该账号是否有这个接口的权限
        Boolean menu = loginFilterService.findMenu(account, 21);
        if(menu == null) return MsgResponse.error().msg("该账号没有这个权限");

        return roleWorkerService.findRole(data).toString();
    }

    //新增角色
    @PostMapping("addrole")
    public Object addrole(@RequestBody String data){

        //获取token
        String token = request.getHeader("X-Token");
        //查询该账号是否有登录
        String account = loginFilterService.userFilter(token);

        if (account == null){
            return MsgResponse.error().msg("登录已过期");
        }
        //查询该账号是否有这个接口的权限
        Boolean menu = loginFilterService.findMenu(account, 1);
        if(menu == null){
            return MsgResponse.error().msg("该账号没有这个权限");
        }

        return roleWorkerService.addrole(data,token).toString();

    }

    //新增角色
    @PostMapping("updaterole")
    public Object updaterole(@RequestBody String data){

        //获取token
        String token = request.getHeader("X-Token");
        //查询该账号是否有登录
        String account = loginFilterService.userFilter(token);

        if (account == null){
            return MsgResponse.error().msg("登录已过期");
        }
        //查询该账号是否有这个接口的权限
        Boolean menu = loginFilterService.findMenu(account, 21);
        if(menu == null) return MsgResponse.error().msg("该账号没有这个权限");
        return roleWorkerService.updaterole(data).toString();
    }

    //删除角色
    @PostMapping("deleterole")
    public Object delete(@RequestBody String data){

        //获取token
        String token = request.getHeader("X-Token");
        //查询该账号是否有登录
        String account = loginFilterService.userFilter(token);

        if (account == null){
            return MsgResponse.error().msg("登录已过期");
        }
        //查询该账号是否有这个接口的权限
        Boolean menu = loginFilterService.findMenu(account, 21);
        if(menu == null) return MsgResponse.error().msg("该账号没有这个权限");

        return roleWorkerService.deleterole(data).toString();
    }

}
