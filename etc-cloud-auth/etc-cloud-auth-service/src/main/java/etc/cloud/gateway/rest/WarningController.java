package etc.cloud.gateway.rest;


import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.gateway.service.impl.LoginFilterServiceImpl;
import etc.cloud.gateway.service.impl.WarningServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("etc-cloud/warning/")
@CrossOrigin
public class WarningController {
    @Autowired
    private WarningServiceImpl warningService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LoginFilterServiceImpl loginFilterService;
    //查询所有
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
        Boolean menu = loginFilterService.findMenu(account, 8);
        if(menu == null) return MsgResponse.error().msg("该账号没有这个权限");

        return warningService.findAll().toString();

    }
}
