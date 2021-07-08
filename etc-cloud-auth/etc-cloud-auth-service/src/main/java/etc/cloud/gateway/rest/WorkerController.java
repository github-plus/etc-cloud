package etc.cloud.gateway.rest;

import cn.hutool.json.JSONArray;


import cn.hutool.json.JSONObject;
import etc.cloud.auth.mode.WorkerModel;
import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.gateway.service.impl.LoginFilterServiceImpl;
import etc.cloud.gateway.service.impl.WorkerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@RestController
@RequestMapping("etc-cloud/workers")
@CrossOrigin
public class WorkerController {

    @Autowired
    private WorkerServiceImpl workerService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LoginFilterServiceImpl loginFilterService;
    //增加员工接口
    @PostMapping("/addworker")
    public Object addworker(@RequestBody String data){

        //获取token
        String token = request.getHeader("X-Token");
        //查询该账号是否有登录
        String account = loginFilterService.userFilter(token);

        if (account == null){
            return MsgResponse.error().msg("登录已过期");
        }
        //查询该账号是否有这个接口的权限
        Boolean menu = loginFilterService.findMenu(account, 19);
        if(menu == null) return MsgResponse.error().msg("该账号没有这个权限");

        return workerService.addworker(data).toString();
    }

    //修改员工接口
    @PostMapping("/updateworker")
    public Object updateWorker(@RequestBody String data){
//获取token
        String token = request.getHeader("X-Token");
        //查询该账号是否有登录
        String account = loginFilterService.userFilter(token);

        if (account == null){
            return MsgResponse.error().msg("登录已过期");
        }
        //查询该账号是否有这个接口的权限
        Boolean menu = loginFilterService.findMenu(account, 19);
        if(menu == null) return MsgResponse.error().msg("该账号没有这个权限");
        return workerService.updateworker(data).toString();
    }

    //修改密码
    @PostMapping("/updatepassword")
    public MsgResponse updatepassword(@RequestBody String data){
//获取token
        String token = request.getHeader("X-Token");
        //查询该账号是否有登录
        String account = loginFilterService.userFilter(token);

        if (account == null){
            return MsgResponse.error().msg("登录已过期");
        }
        //查询该账号是否有这个接口的权限
        Boolean menu = loginFilterService.findMenu(account, 19);
        if(menu == null) return MsgResponse.error().msg("该账号没有这个权限");

        //json数据解析
        JSONObject jsonObject = new JSONObject(data);

        //获取worker数据
        JSONObject jsonObject1 = jsonObject.getJSONObject("worker");
        //获取workerModel对象
        WorkerModel workerModel = new WorkerModel();


        workerModel.setAccount(jsonObject1.getStr("account"));
        workerModel.setPassword(jsonObject1.getStr("password"));

        //开始修改密码
        boolean updatepassword = workerService.updateById(workerModel);

        if (updatepassword){

            return MsgResponse.ok();
        }
        return MsgResponse.error();
    }


    //删除接口
    @PostMapping("/deleteworker")
    public Object deleteworker(@RequestBody String data){
//获取token
        String token = request.getHeader("X-Token");
        //查询该账号是否有登录
        String account = loginFilterService.userFilter(token);

        if (account == null){
            return MsgResponse.error().msg("登录已过期");
        }
        //查询该账号是否有这个接口的权限
        Boolean menu = loginFilterService.findMenu(account, 19);
        if(menu == null) return MsgResponse.error().msg("该账号没有这个权限");
        return workerService.deleteWorker(data).toString();
    }

    //查找员工
    @PostMapping("/findworker")
    public Object findWorker(@RequestBody String data){

//获取token
        String token = request.getHeader("X-Token");
        //查询该账号是否有登录
        String account = loginFilterService.userFilter(token);

        if (account == null){
            return MsgResponse.error().msg("登录已过期");
        }
        //查询该账号是否有这个接口的权限
        Boolean menu = loginFilterService.findMenu(account, 19);
        if(menu == null) return MsgResponse.error().msg("该账号没有这个权限");
        return workerService.findWorker(data).toString();
    }

    //分配角色
    @PostMapping("/distributerole")
    public Object distributerole(@RequestBody String data){

        //获取token
        String token = request.getHeader("X-Token");
        //查询该账号是否有登录
        String account = loginFilterService.userFilter(token);

        if (account == null){
            return MsgResponse.error().msg("登录已过期");
        }
        //查询该账号是否有这个接口的权限
        Boolean menu = loginFilterService.findMenu(account, 19);
        if(menu == null) return MsgResponse.error().msg("该账号没有这个权限");
        return workerService.distributerole(data).toString();
    }



}
