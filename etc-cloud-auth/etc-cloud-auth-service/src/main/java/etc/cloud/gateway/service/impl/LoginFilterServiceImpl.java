package etc.cloud.gateway.service.impl;


import etc.cloud.auth.mapper.LoginFilterMapper;
import etc.cloud.auth.service.ILoginFilterService;
import etc.cloud.auth.vo.WorkerVO;
import etc.cloud.commons.mode.MsgResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class LoginFilterServiceImpl implements ILoginFilterService {

//    @Resource
//    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private LoginFilterMapper loginFilterMapper;

    //验证是否有登陆
    public Object loginFilter(String token){

        //没写完：用token去redis查询账户是否存在
        WorkerVO tokens = loginFilterMapper.findaccount(token);

        //没写完：判断该账户是否拥有对应权限

        //如果存在该账户
        if (tokens == null){
            //没写完：redis延长token存储时间
            //redisTemplate.opsForValue().set(token, userName, Duration.ofMinutes(30L))
            //没写完返回拥有的权限
            System.out.println("在过滤器判断内");
            System.out.println(tokens);
            return null;
        }
        System.out.println("在过滤器层");
        System.out.println(tokens);

        String account = tokens.getAccount();
        String workerName = tokens.getWorkerName();

        //返回权限
        return MsgResponse.ok().data("account",account).data("roles","admin").data("workerName", workerName);
        //return account;
    }

    public String userFilter(String token){

        WorkerVO tokens = loginFilterMapper.findaccount(token);
        //如果存在该账户
        if (tokens == null){
            //没写完：redis延长token存储时间
            //redisTemplate.opsForValue().set(token, userName, Duration.ofMinutes(30L))
            //没写完返回拥有的权限
            System.out.println("在过滤器判断内");
            System.out.println("该token已过期或不存在");
            return null;
        }
        System.out.println("在过滤器层");
        System.out.println(tokens);

        String account = tokens.getAccount();
        String workerName = tokens.getWorkerName();

        //返回权限
        //return MsgResponse.ok().data("account",account).data("roles","admin").data("workerName", workerName);
        return account;
    }

    public Boolean findMenu(String account,int menu){

        //查询该账户的所有权限
        String menu1 = loginFilterMapper.findMenu(account);

        //没有查询到数据，说明没有权限
        if (menu1 == null){
            return null;
        }
        //切割数据，使其成为数组
        String[] split = menu1.split(",");
        //查询是否有权限 0为无权限
        if (split[menu+1].equals("0")){

            return null;
        }

        return true;
    }
}
