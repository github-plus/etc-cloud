package etc.cloud.gateway.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import etc.cloud.auth.mapper.AutorityMapper;
import etc.cloud.auth.mapper.WorkerMapper;
import etc.cloud.auth.mode.WorkerModel;
import etc.cloud.auth.service.IAuthorityService;
import etc.cloud.auth.vo.AuthotityVO;
import etc.cloud.auth.vo.OptionVO;
import etc.cloud.commons.mode.MsgResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AutorityServiceImpl extends ServiceImpl<AutorityMapper, WorkerModel> implements IAuthorityService {

    @Autowired
    private AutorityMapper autorityMapper;

    @Autowired
    private WorkerMapper workerMapper;

    //查询对应权限所对应的员工
    public Object find(String data){

        //json解析
        JSONObject jsonObject = new JSONObject(data);

        //获取到token
        String token = jsonObject.getStr("token");


        Integer limit = jsonObject.getInt("limit");
        Integer current = jsonObject.getInt("current");
        current = (current-1)*limit;
        Object query = jsonObject.get("query");
        JSONObject jsonObject1 = new JSONObject(query);
        String roleType = jsonObject1.getStr("roleType");
        String workerName = jsonObject1.getStr(("workerName"));

        List<AuthotityVO> authotityVOS = autorityMapper.find(limit, current, roleType, workerName);

        int total = authotityVOS.size();
        //插入停车场
        for (int i = 0; i < total; i++){
            String account = authotityVOS.get(i).getAccount();
            System.out.println("账号为："+account);
            List<AuthotityVO> park = autorityMapper.findPark(account);
            authotityVOS.get(i).setPark(park);

        }
        List<OptionVO> findrole = workerMapper.findrole();
        return MsgResponse.ok().data("total",total).data("rows",authotityVOS).data("role", findrole);
    }

}
