package etc.cloud.gateway.rest;


import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.gateway.service.impl.HomeServiceImpl;
import etc.cloud.gateway.service.impl.LoginFilterServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@CrossOrigin
@RestController
@RequestMapping("etc-cloud/home/")
public class HomeController {


    @Autowired
    private HomeServiceImpl homeService;

    @Autowired
    LoginFilterServiceImpl loginFilterService;

    @Autowired
    private HttpServletRequest request;

    //搜索所有
    @PostMapping("findall")
    public Object findAll(){

        //获取token
        String token = request.getHeader("X-Token");
        //查询该账号是否有登录
        String account = loginFilterService.userFilter(token);

        if (account == null){
            return MsgResponse.error().msg("登录已过期");
        }
        //查询该账号是否有这个接口的权限
        System.out.println("该token账号为"+account);
        Boolean menu = loginFilterService.findMenu(account, 7);
        if(menu == null){
            return MsgResponse.error().msg("该账号没有这个权限");
        }

        return homeService.findAll().toString();
    }

    //搜索停车场数据
    @PostMapping("findpark")
    public Object findPark(){

        return homeService.findPark().toString();
    }
}
