package etc.cloud.park.service.impl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import etc.cloud.auth.mapper.LoginFilterMapper;
import etc.cloud.auth.mode.TokenModel;
import etc.cloud.auth.vo.WorkerVO;
import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.park.mapper.FreeTypeMapper;
import etc.cloud.park.mapper.LogMapper;
import etc.cloud.park.mapper.MonthlyCarPayRulesMapper;
import etc.cloud.park.mode.FreeTypeModel;
import etc.cloud.park.mode.LogModel;
import etc.cloud.park.mode.MonthlyCarPayRulesModel;
import etc.cloud.park.service.FreeTypeService;
import etc.cloud.park.service.MonthlyCarPayRulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class FreeTypeServiceImpl extends ServiceImpl<FreeTypeMapper, FreeTypeModel> implements FreeTypeService {

    @Autowired
    private FreeTypeMapper freeTypeMapper;
    @Autowired
    private LogServiceImpl logService;
    @Autowired
    private LoginFilterMapper loginFilterMapper;
    //免费类型设置查询
    @Override
    public Object inqueryFreeTypeHandler(String data) {
        //解析json
        JSONObject jsonObject = new JSONObject(data);
        int current = jsonObject.getInt("current");
        int limit = jsonObject.getInt("limit");
        //String token = jsonObject.getStr("token");
        String token = "";
        //设置实体类
        FreeTypeModel freeTypeModel = new FreeTypeModel();
        //设置page 和 QueryWrapper
        Page<FreeTypeModel> page = new Page(current,limit);

        QueryWrapper<FreeTypeModel> wrapper = new QueryWrapper<>();
        // 调用Mapper接口查询
        page = freeTypeMapper.selectAll(page,wrapper);
        //返回查询总数和结果
        return MsgResponse.ok().data("total",page.getTotal()).data("rows",page.getRecords());
    }
    //新增
    @Override
    public Object addFreeTypeHandler(String data,String token) {
        //解析数据
        JSONObject jsonObject = new JSONObject(data);
        //String token = jsonObject.getStr("token");

        //设置实体类属性
        JSONObject object1 = jsonObject.getJSONObject("stus");
        FreeTypeModel freeTypeModel = new FreeTypeModel();
        //自增序号无需设置
        freeTypeModel.setFreeTypeName(object1.getStr("freeTypeName"));
        System.out.println(freeTypeModel.getFreeTypeName());
        //调用mapper
        int i =freeTypeMapper.insertFreeType(freeTypeModel);

        //查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();

        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("基础设置");//	模块名称	String 必填
        logModel.setImpName("免费类型");//	接口名称	String
        logModel.setOpType("新增免费类型");//	操作方法： 新增月租车，缴费
        logModel.setOpContent(freeTypeModel.getFreeTypeName());//	操作内容	String  必填
        //logModel.setCarNumber("");//	车牌号码	String
        logModel.setOpResult(i>0?"成功":"失败");//	操作结果	String  必填
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        logModel.setOpTime(ts);//	操作时间	TimeStamp  必填
        logService.addLog(logModel);

        if(i!=-1) {
            return MsgResponse.ok();}
        else {return MsgResponse.error();}
    }
    //删除
    @Override
    public Object deleteFreeTypeHandler(String data,String token) {
        //解析数据
        JSONObject jsonObject = new JSONObject(data);
        //String token = jsonObject.getStr("token");

        //设置实体类属性
        //JSONObject object1 = jsonObject.getJSONObject("stus");
        int pkFreeTypeId = jsonObject.getInt("pkFreeTypeId");
        FreeTypeModel freeTypeModel = new FreeTypeModel();
        freeTypeModel.setPkFreeTypeId(pkFreeTypeId);

        //设置wrapper
        QueryWrapper<FreeTypeModel> wrapper = new QueryWrapper<>();
        wrapper.eq("pk_free_type_id",freeTypeModel.getPkFreeTypeId());
        //调用mapper
        int i = freeTypeMapper.deleteFreeType(wrapper);

        //查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();

        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("基础设置");//	模块名称	String 必填
        logModel.setImpName("免费类型");//	接口名称	String
        logModel.setOpType("删除免费类型");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("删除免费类型id"+pkFreeTypeId);//	操作内容	String  必填
        //logModel.setCarNumber("");//	车牌号码	String
        logModel.setOpResult(i>0?"成功":"失败");//	操作结果	String  必填
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        logModel.setOpTime(ts);//	操作时间	TimeStamp  必填
        logService.addLog(logModel);

        if(i!=-1) return MsgResponse.ok();
        else {return MsgResponse.error();}
    }
}
