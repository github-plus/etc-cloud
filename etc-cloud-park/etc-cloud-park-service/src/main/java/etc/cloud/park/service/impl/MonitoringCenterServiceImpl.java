package etc.cloud.park.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import etc.cloud.auth.mapper.LoginFilterMapper;
import etc.cloud.auth.mode.TokenModel;
import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.park.mapper.MonitoringCenterMapper;
import etc.cloud.park.mode.LogModel;
import etc.cloud.park.mode.MonitoringCenterModel;
import etc.cloud.park.mode.ParkOperatorModel;
import etc.cloud.park.service.MonitoringCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class MonitoringCenterServiceImpl extends ServiceImpl<MonitoringCenterMapper, MonitoringCenterModel> implements MonitoringCenterService {

    @Autowired
    private MonitoringCenterMapper monitoringCenterMapper;
    @Autowired
    private LogServiceImpl logService;
    @Autowired
    private LoginFilterMapper loginFilterMapper;

    @Override
    public Object inqueryMonitoringCenterHandler(int current, int limit, String token, MonitoringCenterModel monitoringCenterModel) {

        Page<MonitoringCenterModel> page = new Page(current,limit);
        page.setDesc("pk_center_id");
        QueryWrapper<MonitoringCenterModel> wrapper = new QueryWrapper<>();

        page = monitoringCenterMapper.selectAll(page,wrapper);
        return MsgResponse.ok().data("total",page.getTotal()).data("rows",page.getRecords());

    }

    @Override
    public Object addMonitoringCenterHandler(String token, MonitoringCenterModel monitoringCenterModel) {

        //验证token

        int i =monitoringCenterMapper.insertMonitoringCenter(monitoringCenterModel);

        //查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();
        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("基础设置");//	模块名称	String 必填
        logModel.setImpName("车场通道");//	接口名称	String
        logModel.setOpType("新增监控中心");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("新增监控中心"+monitoringCenterModel.getName());//	操作内容	String  必填
        //logModel.setCarNumber("");//	车牌号码	String
        logModel.setOpResult(i>0?"成功":"失败");//	操作结果	String  必填
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        logModel.setOpTime(ts);//	操作时间	TimeStamp  必填
        logService.addLog(logModel);

        if(i!=-1) return MsgResponse.ok();
        else {return MsgResponse.error();}

    }

    @Override
    public Object alterMonitoringCenterHandler(String token, MonitoringCenterModel monitoringCenterModel) {
        QueryWrapper<MonitoringCenterModel> wrapper = new QueryWrapper<>();
        wrapper.eq("pk_center_id",monitoringCenterModel.getPkCenterId());
        int i = monitoringCenterMapper.alterMonitoringCenter(monitoringCenterModel,wrapper);

        //查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();
        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("基础设置");//	模块名称	String 必填
        logModel.setImpName("车场通道");//	接口名称	String
        logModel.setOpType("修改监控中心");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("修改监控中心"+monitoringCenterModel.getName());//	操作内容	String  必填
        //logModel.setCarNumber("");//	车牌号码	String
        logModel.setOpResult(i>0?"成功":"失败");//	操作结果	String  必填
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        logModel.setOpTime(ts);//	操作时间	TimeStamp  必填
        logService.addLog(logModel);

        if(i!=-1) return MsgResponse.ok();
        else {return MsgResponse.error();}

    }

    @Override
    public Object deleteMonitoringCenterHandler(String token, MonitoringCenterModel monitoringCenterModel) {

        String name = monitoringCenterModel.getName();

        QueryWrapper<MonitoringCenterModel> wrapper = new QueryWrapper<>();
        wrapper.eq("pk_center_id",monitoringCenterModel.getPkCenterId());
        int i = monitoringCenterMapper.deleteMonitoringCenter(wrapper);

        //查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();
        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("基础设置");//	模块名称	String 必填
        logModel.setImpName("车场通道");//	接口名称	String
        logModel.setOpType("删除监控中心");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("删除监控中心:"+name);//	操作内容	String  必填
        //logModel.setCarNumber("");//	车牌号码	String
        logModel.setOpResult(i>0?"成功":"失败");//	操作结果	String  必填
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        logModel.setOpTime(ts);//	操作时间	TimeStamp  必填
        logService.addLog(logModel);

        if(i!=-1) return MsgResponse.ok();
        else {return MsgResponse.error();}
    }
}
