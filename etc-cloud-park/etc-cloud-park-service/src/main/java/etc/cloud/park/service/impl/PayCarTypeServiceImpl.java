package etc.cloud.park.service.impl;


import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import etc.cloud.auth.mapper.LoginFilterMapper;
import etc.cloud.auth.mode.TokenModel;
import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.park.mapper.PayCarTypeMapper;

import etc.cloud.park.mode.FreeTypeModel;
import etc.cloud.park.mode.LogModel;
import etc.cloud.park.mode.MonthlyCarPayRulesModel;
import etc.cloud.park.mode.PayCarTypeModel;
import etc.cloud.park.service.PayCarTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class PayCarTypeServiceImpl extends ServiceImpl<PayCarTypeMapper, PayCarTypeModel> implements PayCarTypeService {

    @Autowired
    private PayCarTypeMapper payCarTypeMapper;
    @Autowired
    private LogServiceImpl logService;
    @Autowired
    private LoginFilterMapper loginFilterMapper;
    //查收费车型
    //参数：current limit token
    @Override
    public Object inquiryOfPayCarType(String data) {
        //解析json
        JSONObject jsonObject = new JSONObject(data);
        int current = jsonObject.getInt("current");
        int limit = jsonObject.getInt("limit");
        //String token = jsonObject.getStr("token");
        String token = "";
        //设置实体类
        PayCarTypeModel payCarTypeModel = new PayCarTypeModel();
        //设置page 和 QueryWrapper
        Page<PayCarTypeModel> page = new Page(current,limit);

        QueryWrapper<PayCarTypeModel> wrapper = new QueryWrapper<>();
        // 调用Mapper接口查询
        page = payCarTypeMapper.selectAll(page,wrapper);
        //返回查询总数和结果
        return MsgResponse.ok().data("total",page.getTotal()).data("rows",page.getRecords());
    }
    //增收费车型
    //参数：token PayCarTypeModel对象
    @Override
    public Object insertPayCarType(String data,String token) {
        //解析数据
        JSONObject jsonObject = new JSONObject(data);
        //String token = jsonObject.getStr("token");
        //String token = "";
        //设置实体类属性
        JSONObject object1 = jsonObject.getJSONObject("stus");
        PayCarTypeModel payCarTypeModel = new PayCarTypeModel();
        //自增序号无需设置
        payCarTypeModel.setPayTempCarName(object1.getStr("payTempCarName"));
        payCarTypeModel.setIsDefault(object1.getInt("isDefault"));
        payCarTypeModel.setIsMan(object1.getInt("isMan"));

        //调用mapper
        int i =payCarTypeMapper.insertPayCarType(payCarTypeModel);

        //查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();
        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("基础设置");//	模块名称	String 必填
        logModel.setImpName("临时车收费规则");//	接口名称	String
        logModel.setOpType("新增收费车型");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("新增收费车型："+payCarTypeModel.getPayTempCarName());//	操作内容	String  必填
        //logModel.setCarNumber("");//	车牌号码	String
        logModel.setOpResult(i>0?"成功":"失败");//	操作结果	String  必填
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        logModel.setOpTime(ts);//	操作时间	TimeStamp  必填
        logService.addLog(logModel);

        if(i!=-1) { return MsgResponse.ok();}
        else {return MsgResponse.error();}

    }
    //改收费车型
    //收费类型和收费设置的id不在此处进行更改，该接口只应用于对收费车型设置页面的修改
    @Override
    public Object alterPayCarType(String data,String token) {
        //解析数据
        JSONObject jsonObject = new JSONObject(data);
        //处理token
        //String token = jsonObject.getStr("token");
        //设置实体类属性
        JSONObject object1 = jsonObject.getJSONObject("stus");
        PayCarTypeModel payCarTypeModel = new PayCarTypeModel();
        payCarTypeModel.setPkPayTempCarTypeId(object1.getInt("pkPayTempCarTypeId"));
        payCarTypeModel.setPayTempCarName(object1.getStr("payTempCarName"));
        payCarTypeModel.setIsDefault(object1.getInt("isDefault"));
        payCarTypeModel.setIsMan(object1.getInt("isMan"));

        //设置wrapper
        QueryWrapper<PayCarTypeModel> wrapper = new QueryWrapper<>();
        //临时车收费车型编号设置为修改条件
        wrapper.eq("pk_pay_temp_car_type_id",payCarTypeModel.getPkPayTempCarTypeId());
        //调用mapper
        int i = payCarTypeMapper.alterPayCarType(payCarTypeModel,wrapper);

        //查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();
        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("基础设置");//	模块名称	String 必填
        logModel.setImpName("临时车收费规则");//	接口名称	String
        logModel.setOpType("修改收费车型");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("修改收费车型："+payCarTypeModel.getPayTempCarName());//	操作内容	String  必填
        //logModel.setCarNumber("");//	车牌号码	String
        logModel.setOpResult(i>0?"成功":"失败");//	操作结果	String  必填
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        logModel.setOpTime(ts);//	操作时间	TimeStamp  必填
        logService.addLog(logModel);

        if(i!=-1) return MsgResponse.ok();
        else {return MsgResponse.error();}
    }
    //删收费车型
    @Override
    public Object deletePayCarType(String data,String token) {
        //解析数据
        JSONObject jsonObject = new JSONObject(data);
        //String token = jsonObject.getStr("token");
        //String token = "";
        //设置实体类属性
        //JSONObject object1 = jsonObject.getJSONObject("stus");
        PayCarTypeModel payCarTypeModel = new PayCarTypeModel();
        payCarTypeModel.setPkPayTempCarTypeId(jsonObject.getInt("pkPayTempCarTypeId"));

        //设置wrapper
        QueryWrapper<PayCarTypeModel> wrapper = new QueryWrapper<>();
        //临时车收费车型编号设置为删除条件
        wrapper.eq("pk_pay_temp_car_type_id",payCarTypeModel.getPkPayTempCarTypeId());
        //调用mapper
        int i = payCarTypeMapper.deletePayCarType(wrapper);

        //查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();
        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("基础设置");//	模块名称	String 必填
        logModel.setImpName("临时车收费规则");//	接口名称	String
        logModel.setOpType("删除收费车型");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("删除收费车型："+jsonObject.getInt("pkPayTempCarTypeId"));//	操作内容	String  必填
        //logModel.setCarNumber("");//	车牌号码	String
        logModel.setOpResult(i>0?"成功":"失败");//	操作结果	String  必填
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        logModel.setOpTime(ts);//	操作时间	TimeStamp  必填
        logService.addLog(logModel);


        if(i!=-1) return MsgResponse.ok();
        else {return MsgResponse.error();}
    }
}
