package etc.cloud.park.rest;


import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.gateway.service.impl.LoginFilterServiceImpl;
import etc.cloud.park.service.impl.PrefabricationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("etc-cloud/visitor/prefabrication/")
@CrossOrigin
public class PrefabricationController {

    @Autowired
    private PrefabricationServiceImpl prefabricationService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LoginFilterServiceImpl loginFilterService;

    //添加预制车
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

        return prefabricationService.addcar(data,token).toString();
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
        return prefabricationService.updatecar(data,token).toString();
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

        return prefabricationService.deletecar(data,token).toString();
    }

    @PostMapping("findcar")
    public Object findcar(@RequestBody String data){


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

        return prefabricationService.findcar(data).toString();
    }

}
