package etc.cloud.gateway.rest;


import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.gateway.service.impl.AutorityServiceImpl;
import etc.cloud.gateway.service.impl.LoginFilterServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("etc-cloud/authority/")
@CrossOrigin
public class AuthorityController {

    @Autowired
    private AutorityServiceImpl autorityService;

    @Autowired
    private LoginFilterServiceImpl loginFilterService;

    @Autowired
    private HttpServletRequest request;

    //查询
    @PostMapping("find")
    public Object find(@RequestBody String data){
        //获取token
        String token = request.getHeader("X-Token");
        //查询该账号是否有登录
        String account = loginFilterService.userFilter(token);

        if (account == null){
            return MsgResponse.error().msg("登录已过期");
        }
        //查询该账号是否有这个接口的权限
        Boolean menu = loginFilterService.findMenu(account, 20);
        if(menu == null){
            return MsgResponse.error().msg("该账号没有这个权限");
        }
        return autorityService.find(data).toString();
    }

}
