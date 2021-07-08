package etc.cloud.park.rest;

import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.gateway.service.impl.LoginFilterServiceImpl;
import etc.cloud.park.service.impl.BlacklistServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping("etc-cloud/visitor/blacklist/")
public class BlacklistController {

    @Autowired
    private BlacklistServiceImpl blacklistService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LoginFilterServiceImpl loginFilterService;

    @PostMapping("addcar")
    public Object addcar(@RequestBody String data){

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
        return blacklistService.addcar(data,token).toString();
    }

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
        return blacklistService.updatecar(data,token).toString();
    }

    @PostMapping("deletecar")
    public Object deletecar(@RequestBody String data){


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
        return blacklistService.deletecar(data,token).toString();
    }

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
        return blacklistService.findcar(data).toString();
    }

}
