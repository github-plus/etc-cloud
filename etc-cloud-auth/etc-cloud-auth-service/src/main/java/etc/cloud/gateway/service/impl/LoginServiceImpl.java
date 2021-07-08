package etc.cloud.gateway.service.impl;

import etc.cloud.auth.mapper.TokenMapper;
import etc.cloud.auth.mapper.WorkerMapper;
import etc.cloud.auth.mode.TokenModel;
import etc.cloud.auth.mode.WorkerModel;
import etc.cloud.auth.service.ILoginService;
import etc.cloud.commons.mode.MsgResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LoginServiceImpl implements ILoginService {


//    @Resource
//    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private WorkerMapper workerMapper;

    @Autowired
    private TokenMapper tokenMapper;

//    @Override
//    public Object loginHandler(int account, String passWord) {
        @Override
        public Object loginHandler(String workerName, String passWord) {
//        preCheckCondition(account,passWord);//判断账号密码是否为空
            preCheckCondition(workerName,passWord);//判断账号密码是否为空
        //生成token
        String token = UUID.randomUUID() + "";

        //存储token和用户
        TokenModel add = new TokenModel();

        //查询是账号密码是否存在
        WorkerModel worker = workerMapper.selectUser(workerName, passWord);
            add.setAccount(worker.getAccount());
            add.setToken(token);
            // add.setAccount(account);
            System.out.println("################");
            System.out.println(worker);

        if (worker != null){//判断是否查到用户
            System.out.println(token);
            //没写的：把token存储在redis,暂时存在数据库中
            tokenMapper.insert(add);
            //redisTemplate.opsForValue().set(token, userName, Duration.ofMinutes(30L));
            return MsgResponse.ok().data("token",token);
//          return MsgResponse.ofSuccessMsg("登陆成功",worker);
        }
        return MsgResponse.ofFail("账号密码错误");

    }

    public Object loginOut(String data){


            return null;
    }

}
