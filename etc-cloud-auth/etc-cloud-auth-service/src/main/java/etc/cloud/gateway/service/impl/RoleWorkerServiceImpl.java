package etc.cloud.gateway.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import etc.cloud.auth.mapper.LoginFilterMapper;
import etc.cloud.auth.mapper.MenuRoleMapper;
import etc.cloud.auth.mapper.RoleWorkerMapper;
import etc.cloud.auth.mapper.WorkerMapper;
import etc.cloud.auth.mode.MenuRolesModel;
import etc.cloud.auth.mode.RolesModel;
import etc.cloud.auth.mode.TokenModel;
import etc.cloud.auth.service.IRoleWorkerService;
import etc.cloud.auth.vo.OptionVO;
import etc.cloud.auth.vo.RolesVO;
import etc.cloud.auth.vo.WorkerVO;
import etc.cloud.commons.mode.MsgResponse;
//import etc.cloud.park.mode.LogModel;
//import etc.cloud.park.service.impl.LogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RoleWorkerServiceImpl extends ServiceImpl<RoleWorkerMapper, RolesModel> implements IRoleWorkerService {

    @Autowired
    private RoleWorkerMapper roleWorkerMapper;

    @Autowired
    private MenuRoleMapper menuRoleMapper;

    @Autowired
    private WorkerMapper workerMapper;

    @Autowired
    private LoginFilterMapper loginFilterMapper;

//    @Autowired
//    private LogServiceImpl logService;

    public Object findRole(String data){

        //解析json包
        JSONObject jsonObject = new JSONObject(data);

        //获取token
        //String token = jsonObject.getStr("token");

        int limit = jsonObject.getInt("limit");
        int current = jsonObject.getInt("current");
        current = (current - 1)*limit;


        JSONObject jsonObject1 = jsonObject.getJSONObject("query");

        //解析查询数据
        //JSONObject jsonObject1 = query.getJSONObject(0);
        //获取角色类型
        String roleType = jsonObject1.getStr("roleType");
        String workerName = jsonObject1.getStr("workerName");

        //开始查询
        List<RolesVO> role = roleWorkerMapper.findRole(limit, current, roleType);

        int total = role.size();

        List<OptionVO> findrole = workerMapper.findrole();

        return MsgResponse.ok().data("total",total).data("rows",role).data("role", findrole);
    }

    //添加角色
    public Object addrole(String data,String token){

        //获取json
        JSONObject js = new JSONObject(data);

        //新建一个list存储权限
        ArrayList<Integer> menu = new ArrayList<>();

        //获取数据
        JSONObject role = js.getJSONObject("query");

        menu.add(role.getInt("export"));
        menu.add(role.getInt("data"));
        menu.add(role.getInt("login"));
        menu.add(role.getInt("business"));
        menu.add(role.getInt("collection"));
        menu.add(role.getInt("home"));
        menu.add(role.getInt("multiple"));
        menu.add(role.getInt("warning"));
        menu.add(role.getInt("manage"));
        menu.add(role.getInt("basics"));
        menu.add(role.getInt("day"));
        menu.add(role.getInt("charge"));
        menu.add(role.getInt("shop"));
        menu.add(role.getInt("order"));
        menu.add(role.getInt("storage"));
        menu.add(role.getInt("visitor"));
        menu.add(role.getInt("administration"));
        menu.add(role.getInt("organ"));
        menu.add(role.getInt("worker"));
        menu.add(role.getInt("autority"));
        menu.add(role.getInt("rolemenu"));
        menu.add(role.getInt("system"));
        menu.add(role.getInt("log"));
        menu.add(role.getInt("pay"));

        //将[ ]切割走
        String menuName = menu.toString().replace("[","").replace("]","");
        System.out.println("角色权限为："+menuName);
        //获取角色名称
        String roleName = js.getStr("roleName");
        //自动生成角色id
        String roleId = UUID.randomUUID().toString();
        //默认生成物业管理员
        String roleType = "物业管理员";

        //查找账号名称和项目id
        WorkerVO findaccount = loginFilterMapper.findaccount(token);
        //创建者的名字 缺失：从token中获取
        String creatName = findaccount.getWorkerName();
        //项目id 缺失：从token中获取
        String projectId = findaccount.getProjectId();

        //设定的权限
        //String menu = role.getStr("menu");


        /*开始存数据到角色*/
        RolesModel rolesModel = new RolesModel();
        rolesModel.setRoleName(roleName);
        rolesModel.setRoleType(roleType);
        rolesModel.setRoleId(roleId);
        rolesModel.setCreateName(creatName);
        rolesModel.setProjectId(projectId);
        int insert = roleWorkerMapper.insert(rolesModel);

        /*开始存数据到权限角色关联表*/
        MenuRolesModel menuRolesModel = new MenuRolesModel();
        menuRolesModel.setMenuName(menuName);
        menuRolesModel.setRoleId(roleId);
        int insert1 = menuRoleMapper.insert(menuRolesModel);

        //查找到当前操作员（根据token）
//        TokenModel finduser = loginFilterMapper.finduser(token);
//        String account = finduser.getAccount();
//
//        //记录日志
//        LogModel logModel = new LogModel();
//        logModel.setEmpId(account);//	用户账号	String  必填
//        logModel.setCommunity("华源小区");//	小区名称	String
//        logModel.setModeName("人力行政");//	模块名称	String 必填
//        logModel.setImpName("预约车");//	接口名称	String
//        logModel.setOpType("新增预约车");//	操作方法： 新增月租车，缴费
//        logModel.setOpContent("新增预约入场车辆");//	操作内容	String  必填
//        //logModel.setCarNumber("");//	车牌号码	String
//        logModel.setOpResult(insert>1?"成功":"失败");//	操作结果	String  必填
//        Timestamp ts = new Timestamp(System.currentTimeMillis());
//        logModel.setOpTime(ts);//	操作时间	TimeStamp  必填
//        logService.addLog(logModel);
        if (insert ==1 && insert1 == 1){
            return MsgResponse.ok();
        }
        return MsgResponse.error();
    }

    //修改角色
    public Object updaterole(String data){

        //获取json
        JSONObject jsonObject = new JSONObject(data);

        /*修改到角色*/
        //获取角色id
        String roleId = jsonObject.getStr("roleId");
        //获取角色名称
        String roleName = jsonObject.getStr("roleName");

        //新建一个list
        ArrayList<Integer> menu = new ArrayList<>();

        //获取数据
        JSONObject role = jsonObject.getJSONObject("query");

        menu.add(role.getInt("export"));
        menu.add(role.getInt("data"));
        menu.add(role.getInt("login"));
        menu.add(role.getInt("business"));
        menu.add(role.getInt("collection"));
        menu.add(role.getInt("home"));
        menu.add(role.getInt("multiple"));
        menu.add(role.getInt("warning"));
        menu.add(role.getInt("manage"));
        menu.add(role.getInt("basics"));
        menu.add(role.getInt("day"));
        menu.add(role.getInt("charge"));
        menu.add(role.getInt("shop"));
        menu.add(role.getInt("order"));
        menu.add(role.getInt("storage"));
        menu.add(role.getInt("visitor"));
        menu.add(role.getInt("administration"));
        menu.add(role.getInt("organ"));
        menu.add(role.getInt("worker"));
        menu.add(role.getInt("autority"));
        menu.add(role.getInt("rolemenu"));
        menu.add(role.getInt("system"));
        menu.add(role.getInt("log"));
        menu.add(role.getInt("pay"));

        //将[ ]切割走
        String menuName = menu.toString().replace("[","").replace("]","");
        System.out.println("角色权限为："+menuName);

        /*开始存数据到角色*/
        RolesModel rolesModel = new RolesModel();
        rolesModel.setRoleName(roleName);
        rolesModel.setRoleId(roleId);

        int updateroleName = roleWorkerMapper.updateById(rolesModel);

        boolean updateMenu = menuRoleMapper.updateMenu(roleId, menuName);

//        /*开始存数据到权限角色关联表*/
//        MenuRolesModel menuRolesModel = new MenuRolesModel();
//        menuRolesModel.setMenuName(setting);
//        menuRolesModel.setRoleId(roleId);
//        int insert1 = menuRoleMapper.insert(menuRolesModel);

        if (updateroleName ==1 ){
            return MsgResponse.ok().msg("修改角色名称成功");
        }
        return MsgResponse.error().msg("修改角色名称失败");
    }

    //删除角色
    public Object deleterole(String data){

        //解析json包
        JSONObject js = new JSONObject(data);

        //要删除的角色id
        String roleId = js.getStr("roleId");

        boolean delete = roleWorkerMapper.deleteRole(roleId);
        roleWorkerMapper.deleteRoleWorker(roleId);
        roleWorkerMapper.deleteMenuRole(roleId);
        if (delete){
            return MsgResponse.ok().msg("删除角色成功");
        }

        return MsgResponse.error().msg("删除角色失败");
    }
}
