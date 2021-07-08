package etc.cloud.gateway.rest;

import etc.cloud.auth.mapper.LoginFilterMapper;
import etc.cloud.auth.mode.WorkerModel;
import etc.cloud.auth.vo.WorkerVO;
import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.gateway.service.impl.LoginFilterServiceImpl;
import etc.cloud.gateway.service.impl.LoginServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("etc-cloud/")
@CrossOrigin
public class LoginController {


    @Autowired
    private LoginServiceImpl loginService;

    @Autowired
    private LoginFilterServiceImpl loginFilterService;

    @Autowired
    private LoginFilterMapper loginFilterMapper;

    @Autowired
    private HttpServletRequest request;

//    @PostMapping("/login")
//    public Object loginAction(@RequestParam String workername,
//                              @RequestParam String password){
//        return loginService.loginHandler(workername,password).toString();
//    }

    @PostMapping("login")
    public Object loginAction(@RequestBody WorkerModel workerModel){

        String workername = workerModel.getWorkerName();
        String account = workerModel.getAccount();
        String password = workerModel.getPassword();
        return loginService.loginHandler(workername,password).toString();
    }

    @GetMapping("logininfo")
    public Object loginInfo(@RequestParam("token") String token){


        return loginFilterService.loginFilter(token).toString();
    }

    @GetMapping("loginout")
    public Object loginout(){

        //获取token
        String token = request.getHeader("X-Token");

        //删除改token
        loginFilterMapper.loginout(token);

        return MsgResponse.ok().msg("登出成功");
    }

}
