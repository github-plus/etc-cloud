package etc.cloud.park.service.impl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import etc.cloud.auth.mapper.LoginFilterMapper;
import etc.cloud.auth.mode.TokenModel;
import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.park.mapper.MonthlyCarPayRulesMapper;
import etc.cloud.park.mode.LogModel;
import etc.cloud.park.mode.MonthlyCarPayRulesModel;
import etc.cloud.park.service.MonthlyCarPayRulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class MonthlyCarPayRulesServiceImpl extends ServiceImpl<MonthlyCarPayRulesMapper, MonthlyCarPayRulesModel> implements MonthlyCarPayRulesService {
    @Autowired
    private MonthlyCarPayRulesMapper monthlyCarPayRulesMapper;
    @Autowired
    private LogServiceImpl logService;
    @Autowired
    private LoginFilterMapper loginFilterMapper;

    //月租车规则设置模块

    @Override
    public Object inqueryMonthlyCarPayRulesHandler(String data) {
        JSONObject jsonObject = new JSONObject(data);
        int current = jsonObject.getInt("current");
        int limit = jsonObject.getInt("limit");
        //处理token
        //String token = jsonObject.getStr("token");
        MonthlyCarPayRulesModel monthlyCarPayRulesModel = new MonthlyCarPayRulesModel();

        Page<MonthlyCarPayRulesModel> page = new Page(current,limit);

        QueryWrapper<MonthlyCarPayRulesModel> wrapper = new QueryWrapper<>();

        page = monthlyCarPayRulesMapper.selectAll(page,wrapper);
        return MsgResponse.ok().data("total",page.getTotal()).data("rows",page.getRecords());
    }

    @Override
    public Object addMonthlyCarPayRulesHandler(String data,String token) {
        //解析数据
        JSONObject jsonObject = new JSONObject(data);
        //处理token
        //String token = jsonObject.getStr("token");
        //设置实体类属性
        JSONObject object1 = jsonObject.getJSONObject("stus");
        MonthlyCarPayRulesModel monthlyCarPayRulesModel = new MonthlyCarPayRulesModel();
        //自增序号无需设置
        monthlyCarPayRulesModel.setPayName(object1.getStr("payName"));
        monthlyCarPayRulesModel.setParkArea(object1.getInt("parkArea"));
        monthlyCarPayRulesModel.setPayFee(object1.getInt("payFee"));
        monthlyCarPayRulesModel.setIsPayOnline(object1.getInt("isPayOnline"));
        monthlyCarPayRulesModel.setPayMode(object1.getInt("payMode"));
        monthlyCarPayRulesModel.setHighMonth(object1.getInt("highMonth"));
        monthlyCarPayRulesModel.setBanDelay(object1.getInt("banDelay"));
        monthlyCarPayRulesModel.setNote(object1.getStr("note"));
        monthlyCarPayRulesModel.setChanRootId(object1.getInt("chanRootId"));
        //调用mapper
        int i =monthlyCarPayRulesMapper.insertMonthlyCarPayRules(monthlyCarPayRulesModel);

        //查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();
        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("基础设置");//	模块名称	String 必填
        logModel.setImpName("月租车规则设置");//	接口名称	String
        logModel.setOpType("新增月租车规则");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("新增月租车规则"+monthlyCarPayRulesModel.getPayName());//	操作内容	String  必填
        //logModel.setCarNumber("");//	车牌号码	String
        logModel.setOpResult(i>0?"成功":"失败");//	操作结果	String  必填
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        logModel.setOpTime(ts);//	操作时间	TimeStamp  必填
        logService.addLog(logModel);

        if(i!=-1) return MsgResponse.ok();
        else {return MsgResponse.error();}
    }

    @Override
    public Object alterMonthlyCarPayRulesHandler(String data,String token) {

        //解析数据
        JSONObject jsonObject = new JSONObject(data);
        //处理token
        //String token = jsonObject.getStr("token");
        //设置实体类属性
        JSONObject object1 = jsonObject.getJSONObject("stus");
        MonthlyCarPayRulesModel monthlyCarPayRulesModel = new MonthlyCarPayRulesModel();
        monthlyCarPayRulesModel.setPkMonthStrandardId(object1.getInt("pkMonthStrandardId"));
        monthlyCarPayRulesModel.setPayName(object1.getStr("payName"));
        monthlyCarPayRulesModel.setParkArea(object1.getInt("parkArea"));
        monthlyCarPayRulesModel.setPayFee(object1.getInt("payFee"));
        monthlyCarPayRulesModel.setIsPayOnline(object1.getInt("isPayOnline"));
        monthlyCarPayRulesModel.setPayMode(object1.getInt("payMode"));
        monthlyCarPayRulesModel.setHighMonth(object1.getInt("highMonth"));
        monthlyCarPayRulesModel.setBanDelay(object1.getInt("banDelay"));
        monthlyCarPayRulesModel.setNote(object1.getStr("note"));
        monthlyCarPayRulesModel.setChanRootId(object1.getInt("chanRootId"));
        //设置wrapper
        QueryWrapper<MonthlyCarPayRulesModel> wrapper = new QueryWrapper<>();
        wrapper.eq("pk_month_strandard_id",monthlyCarPayRulesModel.getPkMonthStrandardId());
        //调用mapper
        int i = monthlyCarPayRulesMapper.alterMonthlyCarPayRules(monthlyCarPayRulesModel,wrapper);


        //查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();
        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("基础设置");//	模块名称	String 必填
        logModel.setImpName("月租车规则设置");//	接口名称	String
        logModel.setOpType("修改月租车规则");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("修改月租车规则"+monthlyCarPayRulesModel.getPayName());//	操作内容	String  必填
        //logModel.setCarNumber("");//	车牌号码	String
        logModel.setOpResult(i>0?"成功":"失败");//	操作结果	String  必填
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        logModel.setOpTime(ts);//	操作时间	TimeStamp  必填
        logService.addLog(logModel);
        if(i!=-1) return MsgResponse.ok();
        else {return MsgResponse.error();}
    }

    @Override
    public Object deleteMonthlyCarPayRulesHandler(String data,String token) {

        //解析数据
        JSONObject jsonObject = new JSONObject(data);
        //处理token
        //String token = jsonObject.getStr("token");


        //设置实体类属性
        JSONObject object1 = jsonObject.getJSONObject("stus");
        MonthlyCarPayRulesModel monthlyCarPayRulesModel = new MonthlyCarPayRulesModel();
        monthlyCarPayRulesModel.setPkMonthStrandardId(object1.getInt("pkMonthStrandardId"));
        String name = monthlyCarPayRulesModel.getPayName();
        //设置wrapper
        QueryWrapper<MonthlyCarPayRulesModel> wrapper = new QueryWrapper<>();
        wrapper.eq("pk_month_strandard_id",monthlyCarPayRulesModel.getPkMonthStrandardId());
        //调用mapper
        int i = monthlyCarPayRulesMapper.deleteMonthlyCarPayRules(wrapper);

        //查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();
        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("基础设置");//	模块名称	String 必填
        logModel.setImpName("月租车规则设置");//	接口名称	String
        logModel.setOpType("删除月租车规则");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("删除月租车规则"+name);//	操作内容	String  必填
        //logModel.setCarNumber("");//	车牌号码	String
        logModel.setOpResult(i>0?"成功":"失败");//	操作结果	String  必填
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        logModel.setOpTime(ts);//	操作时间	TimeStamp  必填
        logService.addLog(logModel);

        if(i!=-1) return MsgResponse.ok();
        else {return MsgResponse.error();}

    }
    //更新线上支付设置
    @Override
    public Object updateMonthlyCarPayModeHandler(String data,String token) {
        //解析数据
        JSONObject jsonObject = new JSONObject(data);
        //String token = jsonObject.getStr("token");
        int mode = jsonObject.getInt("mode");
        int i = monthlyCarPayRulesMapper.updateMonthlyCarPayMode(mode);

        //查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();
        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("基础设置");//	模块名称	String 必填
        logModel.setImpName("月租车规则设置");//	接口名称	String
        logModel.setOpType("更新线上支付设置");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("更新线上支付设置:"+ mode);//	操作内容	String  必填
        //logModel.setCarNumber("");//	车牌号码	String
        logModel.setOpResult(i>0?"成功":"失败");//	操作结果	String  必填
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        logModel.setOpTime(ts);//	操作时间	TimeStamp  必填
        logService.addLog(logModel);

        if(i!=-1) return MsgResponse.ok();
        else {return MsgResponse.error();}
    }
}
