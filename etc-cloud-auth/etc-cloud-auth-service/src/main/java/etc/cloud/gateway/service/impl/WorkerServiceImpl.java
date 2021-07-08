package etc.cloud.gateway.service.impl;


import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import etc.cloud.auth.mapper.OrganManageMapper;
import etc.cloud.auth.mapper.RoleWorkerVOMapper;
import etc.cloud.auth.mapper.WorkerMapper;
import etc.cloud.auth.mode.RoleWorkerModel;
import etc.cloud.auth.mode.WorkerModel;
import etc.cloud.auth.service.IWorkerService;
import etc.cloud.auth.vo.OptionVO;
import etc.cloud.auth.vo.WorkerVO;
import etc.cloud.commons.mode.MsgResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkerServiceImpl extends ServiceImpl<WorkerMapper, WorkerModel> implements IWorkerService {

    @Autowired
    private WorkerMapper workerMapper;

    @Autowired
    private RoleWorkerVOMapper roleWorkerVOMapper;

    @Autowired
    private OrganManageMapper organManageMapper;

    @Autowired
    private LoginFilterServiceImpl loginFilterService;

    //添加职工
    public Object addworker(String data){


        //json数据解析
        JSONObject jsonObject = new JSONObject(data);

        //获取token
        String token = jsonObject.getStr("token");

        //解析worker数据
//        Object worker = jsonObject.get("query");
//        JSONObject jsonObject1 = new JSONObject(worker);
        JSONObject jsonObject1 = jsonObject.getJSONObject("worker");
        //获取workerModel对象
        WorkerModel workerModel1 = new WorkerModel();

        //获取账号
        workerModel1.setAccount(jsonObject1.getStr("account"));

        //查询是否有该账号
        List<WorkerModel> account = workerMapper.selectAccount(jsonObject1.getStr("account"));
        //判断账号
        if (account.size() == 1 ){
            return MsgResponse.error().msg("该账号已存在");
        }

        workerModel1.setPassword(jsonObject1.getStr("password"));
        workerModel1.setWorkerName(jsonObject1.getStr("workerName"));
        workerModel1.setAge(jsonObject1.getInt("age"));
        workerModel1.setSex(jsonObject1.getStr("sex"));
        workerModel1.setIdentity(jsonObject1.getStr("identity"));
        workerModel1.setTel(jsonObject1.getStr("tel"));

        String projectId = jsonObject1.getStr("projectId");
        workerModel1.setProjectId(projectId);
        //根部项目id查找项目名称
        String projectName = workerMapper.findProjectName(projectId);
        workerModel1.setProjectName(projectName);

        String organId = jsonObject1.getStr("organId");
        workerModel1.setOrganId(organId);
        //根据部门id查找部门名称
        String organName = organManageMapper.findOrganName(organId);

        workerModel1.setOrganName(organName);
        workerModel1.setBelongStation(jsonObject1.getStr("belongStation"));
        workerModel1.setNote(jsonObject1.getStr("note"));

        //添加职工
        int add = workerMapper.insert(workerModel1);
        if (add == 1){
            return MsgResponse.ok();
        }
        return MsgResponse.error();
    }

    //删除输出，需要删除workers和role-worker中的数据
    public Object deleteWorker(String data){

        //json数据解析
        JSONObject jsonObject = new JSONObject(data);

        //获取token
        String token = jsonObject.getStr("token");

        //要删除的账号
        String account = jsonObject.getStr("account");
        //开始删除

        //删除workers表的账号
        boolean deleteWorkeraccount = this.removeById(account);
        //删除role-worker表中的worker
       // boolean deleteaccount = workerMapper.deleteRoleWorker(account);


        return MsgResponse.ok();//删除成功


    }

    //修改员工数据
    public Object updateworker(String data){

        //json数据解析
        JSONObject jsonObject = new JSONObject(data);

        //获取token
        String token = jsonObject.getStr("token");

        //解析worker数据
//        Object worker = jsonObject.get("query");
//        JSONObject jsonObject1 = new JSONObject(worker);
        JSONObject jsonObject1 = jsonObject.getJSONObject("worker");
        //获取workerModel对象
        WorkerModel workerModel1 = new WorkerModel();
        workerModel1.setAccount(jsonObject1.getStr("account"));
        workerModel1.setPassword(jsonObject1.getStr("password"));
        workerModel1.setWorkerName(jsonObject1.getStr("workerName"));
        workerModel1.setAge(jsonObject1.getInt("age"));
        workerModel1.setSex(jsonObject1.getStr("sex"));
        workerModel1.setIdentity(jsonObject1.getStr("identity"));
        workerModel1.setTel(jsonObject1.getStr("tel"));
        String projectId = jsonObject1.getStr("projectId");
        workerModel1.setProjectId(projectId);
        //根部项目id查找项目名称
        String projectName = workerMapper.findProjectName(projectId);
        workerModel1.setProjectName(projectName);

        String organId = jsonObject1.getStr("organId");
        workerModel1.setOrganId(organId);
        //根据部门id查找部门名称
        String organName = organManageMapper.findOrganName(organId);
        workerModel1.setBelongStation(jsonObject1.getStr("belongStation"));
        workerModel1.setNote(jsonObject1.getStr("note"));

        //修改员工信息
        boolean update = this.updateById(workerModel1);
        //修改成功
        if (update){

            return MsgResponse.ok().msg("修改职工成功");
        }
        //修改失败
        return MsgResponse.error().msg("修改职工失败");
    }

    //查询员工
    public Object findWorker(String data){

        //json数据解析
        JSONObject jsonObject = new JSONObject(data);

        //获取token
        String token = jsonObject.getStr("token");

        //获取到页码数据

        Integer limit = jsonObject.getInt("limit");
        Integer current = jsonObject.getInt("current");
        current = (current - 1)*limit;

        //解析worker数据
        Object query = jsonObject.get("query");
        JSONObject jsonObject1 = new JSONObject(query);

        String workerName = jsonObject1.getStr("workerName");
        String organId = jsonObject1.getStr("organId");
        String roleType = jsonObject1.getStr("roleType");

        //获得查询结果
    List<WorkerVO> findworker = workerMapper.findworker(limit, current, workerName, organId, roleType);

        //查找所有公司部门岗位
        List<OptionVO> findorgan = workerMapper.findorgan();
        List<OptionVO> findrole = workerMapper.findrole();
        List<OptionVO> findproject = workerMapper.findproject();
        //对数据汇总
        int total = findworker.size();

    return MsgResponse.ok().data("total",total).data("rows",findworker).data("organ",findorgan).data("role",findrole).data("project",findproject);
}

    //分配角色
    public Object distributerole(String data){

        //json数据解析
        JSONObject jsonObject = new JSONObject(data);

        //获取token
        String token = jsonObject.getStr("token");

//        Object worker = jsonObject.get("query");
//        JSONObject jsonObject1 = new JSONObject(worker);
        JSONObject jsonObject1 = jsonObject.getJSONObject("worker");
        //获取workerModel对象
        RoleWorkerModel roleWorkerModel = new RoleWorkerModel();


        String account = jsonObject1.getStr("account");
        String roleId = jsonObject1.getStr("roleId");
        roleWorkerModel.setAccount(account);
        roleWorkerModel.setRoleId(roleId);

        //开始查询该账号是否有分配角色
        int findworkerrole = workerMapper.findworkerrole(account);
        //该账号已分配角色，执行更新操作
        if (findworkerrole >= 1){
            boolean updaterole = roleWorkerVOMapper.updaterole(account, roleId);
            if (updaterole){
                return MsgResponse.ok().msg("分配角色成功");
            }else {
                return MsgResponse.error().msg("分配角色失败");
            }
        }
        //该账号无分配角色，执行插入操作
        int insert = roleWorkerVOMapper.insert(roleWorkerModel);
        if (insert == 1){
            return MsgResponse.ok().msg("分配角色成功");
        }
        return MsgResponse.error().msg("分配角色失败");
    }

}
