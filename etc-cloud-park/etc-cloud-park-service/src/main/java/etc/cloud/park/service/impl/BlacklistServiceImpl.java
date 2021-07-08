package etc.cloud.park.service.impl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import etc.cloud.auth.mapper.LoginFilterMapper;
import etc.cloud.auth.mode.TokenModel;
import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.park.mapper.BlacklistMapper;
import etc.cloud.park.mode.BlacklistModel;
import etc.cloud.park.mode.LogModel;
import etc.cloud.park.service.BlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class BlacklistServiceImpl extends ServiceImpl<BlacklistMapper, BlacklistModel> implements BlacklistService {

    @Autowired
    private LogServiceImpl logService;
    @Autowired
    private LoginFilterMapper loginFilterMapper;
    //增加黑名单
    public Object addcar(String data,String token){

        //解析JSON数据
        JSONObject jsonObject = new JSONObject(data);

        //获取token
        //String token = jsonObject.getStr("token");

        //取得数据
        Object car = jsonObject.get("query");

        //解析数据
        JSONObject jsonObject1 = new JSONObject(car);

        //新建一个黑名单
        BlacklistModel blacklistModel = new BlacklistModel();
        blacklistModel.setCarNumber(jsonObject1.getStr("carNumber"));
        //开始时间
        String startTime = jsonObject1.getStr("startTime");
        String replaceStartTime = startTime.replace("T", " ").replace("Z", "");
        blacklistModel.setStartTime(replaceStartTime);

        //结束时间
        String endTime = jsonObject1.getStr("endTime");
        String replaceEndTime = endTime.replace("T", " ").replace("Z", "");
        blacklistModel.setEndTime(replaceEndTime);

        //缺一个注册时间

        blacklistModel.setNote(jsonObject1.getStr("note"));
        //产生黑名单表的id
        String pkBlacklistId = UUID.randomUUID().toString();
        blacklistModel.setPkBlacklistId(pkBlacklistId);

        //查找操作员
        //BlacklistModel findoperator = baseMapper.findoperator(token);

        //存入操作员
       // blacklistModel.setAccount(findoperator.getAccount());
       // blacklistModel.setWorkerName(findoperator.getWorkerName());
        String workerNamer = "操作员";
        blacklistModel.setWorkerName(workerNamer);
        //开始存入
        boolean addcar = this.save(blacklistModel);

        //查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();

        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("访客管理");//	模块名称	String 必填
        logModel.setImpName("黑名单");//	接口名称	String
        logModel.setOpType("新增黑名单");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("新增黑名单车辆");//	操作内容	String  必填
        //logModel.setCarNumber("");//	车牌号码	String
        logModel.setOpResult(addcar?"成功":"失败");//	操作结果	String  必填
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        logModel.setOpTime(ts);//	操作时间	TimeStamp  必填
        logService.addLog(logModel);
        if (addcar){

            return MsgResponse.ok().msg("添加黑名单成功");
        }

        return MsgResponse.error().msg("添加黑名单失败");
    }

    //修改黑名单
    public Object updatecar(String data,String token){


        //解析JSON数据
        JSONObject jsonObject = new JSONObject(data);

        //获取token
       // String token = jsonObject.getStr("token");

        //取得数据
        Object car = jsonObject.get("query");

        //解析数据
        JSONObject jsonObject1 = new JSONObject(car);

        //新建一个黑名单
        BlacklistModel blacklistModel = new BlacklistModel();
        blacklistModel.setPkBlacklistId(jsonObject1.getStr("pkBlacklistId"));
        blacklistModel.setCarNumber(jsonObject1.getStr("carNumber"));
        //开始时间
        String startTime = jsonObject1.getStr("startTime");
        String replaceStartTime = startTime.replace("T", " ").replace("Z", "");
        blacklistModel.setStartTime(replaceStartTime);

        //结束时间
        String endTime = jsonObject1.getStr("endTime");
        String replaceEndTime = endTime.replace("T", " ").replace("Z", "");
        blacklistModel.setEndTime(replaceEndTime);

        blacklistModel.setNote(jsonObject1.getStr("note"));

        //查找操作员
        //BlacklistModel findoperator = baseMapper.findoperator(token);
        //存入操作员
        //blacklistModel.setAccount(findoperator.getAccount());
        //blacklistModel.setWorkerName(findoperator.getWorkerName());
        String workerNamer = "操作员";
        blacklistModel.setWorkerName(workerNamer);
        System.out.println("黑名单车："+blacklistModel);
        //开始存入
        boolean updatecar = this.updateById(blacklistModel);

        //查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();

        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("访客管理");//	模块名称	String 必填
        logModel.setImpName("黑名单");//	接口名称	String
        logModel.setOpType("新增黑名单");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("新增黑名单车辆");//	操作内容	String  必填
        //logModel.setCarNumber("");//	车牌号码	String
        logModel.setOpResult(updatecar?"成功":"失败");//	操作结果	String  必填
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        logModel.setOpTime(ts);//	操作时间	TimeStamp  必填
        logService.addLog(logModel);

        if (updatecar){

            return MsgResponse.ok().msg("修改黑名单成功");
        }

        return MsgResponse.error().msg("修改黑名单失败");
    }

    //删除黑名单
    public Object deletecar(String data,String token){

        //解析JSON数据
        JSONObject jsonObject = new JSONObject(data);

        //获取token
        //String token = jsonObject.getStr("token");

        //获取需要删除的id
        String pkBlacklistId = jsonObject.getStr("pkBlacklistId");

        //开始删除这条数据
        boolean deletecar = this.removeById(pkBlacklistId);

        //查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();

        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("访客管理");//	模块名称	String 必填
        logModel.setImpName("黑名单");//	接口名称	String
        logModel.setOpType("删除黑名单");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("删除黑名单车辆");//	操作内容	String  必填
        //logModel.setCarNumber("");//	车牌号码	String
        logModel.setOpResult(deletecar?"成功":"失败");//	操作结果	String  必填
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        logModel.setOpTime(ts);//	操作时间	TimeStamp  必填
        logService.addLog(logModel);
        if (deletecar){

            return MsgResponse.ok().msg("删除成功");
        }
        return MsgResponse.error().msg("删除失败");
    }

    //查询黑名单
    public Object findcar(String data){

        //解析JSON数据
        JSONObject jsonObject = new JSONObject(data);

        //获取token
        String token = jsonObject.getStr("token");

        //解析当前页和一页个数
        Integer limit = jsonObject.getInt("limit");
        Integer current = jsonObject.getInt("current");
        current = (current - 1)*limit;

        //获取预约车信息
        Object query = jsonObject.get("query");
        JSONObject jsonObject1 = new JSONObject(query);

        String carNumber = jsonObject1.getStr("carNumber");
        String startTime = jsonObject1.getStr("startTime");
        String endTime = jsonObject1.getStr("endTime");

        //创建查询需求
        Page<BlacklistModel> pageResult = new Page<>(current, limit);
        QueryWrapper<BlacklistModel> wrapper = new QueryWrapper<>();

        if (carNumber != null){
            wrapper.eq("car_number", carNumber);
        }
        if (startTime != null){
            wrapper.ge("register_time", startTime);
        }
        if (endTime != null){
            wrapper.le("register_time", endTime);
        }

        //开始查询
        Page<BlacklistModel> page1 = this.page(pageResult, wrapper);
        //结果总个数
        long total = pageResult.getTotal();
        //解析结果
        List<BlacklistModel> rows = pageResult.getRecords();

        return MsgResponse.ok().data("total",total).data("rows", rows);
    }
}
