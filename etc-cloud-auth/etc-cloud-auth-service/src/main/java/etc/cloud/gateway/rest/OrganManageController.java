package etc.cloud.gateway.rest;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import etc.cloud.auth.mode.OrganManageModel;
import etc.cloud.auth.mode.OrganRelationModel;
import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.gateway.service.impl.LoginFilterServiceImpl;
import etc.cloud.gateway.service.impl.OrganManageServiceImpl;
import etc.cloud.gateway.service.impl.OrganRelationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("etc-cloud/organ")
@CrossOrigin
public class OrganManageController {
    @Autowired
    private OrganManageServiceImpl organManageService;

    @Autowired
    private OrganRelationServiceImpl organRelationService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LoginFilterServiceImpl loginFilterService;
    //添加部门
    @PostMapping("/addorgan")
    public Object addorgan(@RequestBody String data){

        //获取token
        String token = request.getHeader("X-Token");
        //查询该账号是否有登录
        String account = loginFilterService.userFilter(token);

        if (account == null){
            return MsgResponse.error().msg("登录已过期");
        }
        //查询该账号是否有这个接口的权限
        Boolean menu = loginFilterService.findMenu(account, 18);
        if(menu == null) return MsgResponse.error().msg("该账号没有这个权限");

        return organManageService.addOrgan(data).toString();
    }

    //添加部门
    @PostMapping("/updateorgan")
    public Object updateorgan(@RequestBody String data){

        //获取token
        String token = request.getHeader("X-Token");
        //查询该账号是否有登录
        String account = loginFilterService.userFilter(token);

        if (account == null){
            return MsgResponse.error().msg("登录已过期");
        }
        //查询该账号是否有这个接口的权限
        Boolean menu = loginFilterService.findMenu(account, 18);
        if(menu == null) return MsgResponse.error().msg("该账号没有这个权限");

        return organManageService.updateOrgan(data).toString();
    }

    //删除部门
    @PostMapping("/deleteorgan")
    public Object
    deleteorgan(@RequestBody String data){

        //获取token
        String token = request.getHeader("X-Token");
        //查询该账号是否有登录
        String account = loginFilterService.userFilter(token);

        if (account == null){
            return MsgResponse.error().msg("登录已过期");
        }
        //查询该账号是否有这个接口的权限
        Boolean menu = loginFilterService.findMenu(account, 18);
        if(menu == null) return MsgResponse.error().msg("该账号没有这个权限");
        return organManageService.deleteOrgan(data).toString();
    }

    @PostMapping("/findorgan")
    public Object findAllOrgan(@RequestBody String data){
        //获取token
        String token = request.getHeader("X-Token");
        //查询该账号是否有登录
        String account = loginFilterService.userFilter(token);

        if (account == null){
            return MsgResponse.error().msg("登录已过期");
        }
        //查询该账号是否有这个接口的权限
        Boolean menu = loginFilterService.findMenu(account, 18);
        if(menu == null) return MsgResponse.error().msg("该账号没有这个权限");



        return  organManageService.findAllOrgan().toString();
    }


}
