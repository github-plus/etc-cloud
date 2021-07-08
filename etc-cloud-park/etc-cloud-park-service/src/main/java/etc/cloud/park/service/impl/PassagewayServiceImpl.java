package etc.cloud.park.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import etc.cloud.auth.mapper.LoginFilterMapper;
import etc.cloud.auth.mode.TokenModel;
import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.park.mapper.MonitoringCenterMapper;
import etc.cloud.park.mapper.ParkOperatorMapper;
import etc.cloud.park.mapper.PassagewayMapper;
import etc.cloud.park.mode.LogModel;
import etc.cloud.park.mode.MonitoringCenterModel;
import etc.cloud.park.mode.ParkOperatorModel;
import etc.cloud.park.mode.PassagewayModel;
import etc.cloud.park.service.ParkOperatorService;
import etc.cloud.park.service.PassagewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.sql.Timestamp;

@Service
public class PassagewayServiceImpl extends ServiceImpl<PassagewayMapper, PassagewayModel> implements PassagewayService {

    @Autowired
    private PassagewayMapper passagewayMapper;
    @Autowired
    private MonitoringCenterMapper monitoringCenterMapper;
    @Autowired
    private LogServiceImpl logService;
    @Autowired
    private LoginFilterMapper loginFilterMapper;

    @Override
    public Object inqueryPassagewayHandler(int current, int limit,int centerId, String token, PassagewayModel passagewayModel) {
        //验证token
        //System.out.println("test:"+passagewayModel.getPkChanId());
//        //查询监控中心Id
//        Page<MonitoringCenterModel> page_center = new Page();
//        QueryWrapper<MonitoringCenterModel> wrapper_center = new QueryWrapper<>();
//        wrapper_center.eq("name",centerName);
//        page_center = monitoringCenterMapper.selectAll(page_center,wrapper_center);
//        int center_id = page_center.getRecords().get(0).getPkCenterId();

        //查询对应的监控中心控制下的通道
        Page<PassagewayModel> page = new Page(current,limit);
        page.setDesc("pk_chan_id");
        QueryWrapper<PassagewayModel> wrapper = new QueryWrapper<>();

        wrapper.eq("center_id",centerId);
        page = passagewayMapper.selectAll(page,wrapper);

        return MsgResponse.ok().data("total",page.getTotal()).data("rows",page.getRecords());
    }

    @Override
    public Object addPassagewayHandler(String token,PassagewayModel passagewayModel) {
        //验证token
        //查询监控中心Id
//

        Page<MonitoringCenterModel> page_center = new Page();
//        QueryWrapper<MonitoringCenterModel> wrapper_center = new QueryWrapper<>();
//        wrapper_center.eq("name",centerName);
//        //获得查询结果
//        page_center = monitoringCenterMapper.selectAll(page_center,wrapper_center);
//        //得到监控中心id
//        int center_id = page_center.getRecords().get(0).getPkCenterId();
//        passagewayModel.setCenterId(center_id);
        int i =passagewayMapper.insertPassageway(passagewayModel);

        //查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();
        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("基础设置");//	模块名称	String 必填
        logModel.setImpName("车场通道");//	接口名称	String
        logModel.setOpType("新增车场通道");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("新增车场通道："+passagewayModel.getChanName());//	操作内容	String  必填
        //logModel.setCarNumber("");//	车牌号码	String
        logModel.setOpResult(i>0?"成功":"失败");//	操作结果	String  必填
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        logModel.setOpTime(ts);//	操作时间	TimeStamp  必填
        logService.addLog(logModel);

        if(i!=-1) return MsgResponse.ok();
        else {return MsgResponse.error();}
    }

    @Override
    public Object alterPassagewayHandler(String token, PassagewayModel passagewayModel) {
        QueryWrapper<PassagewayModel> wrapper = new QueryWrapper<>();
        wrapper.eq("pk_chan_id",passagewayModel.getPkChanId());
        int i = passagewayMapper.alterPassageway(passagewayModel,wrapper);

        //查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();
        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("基础设置");//	模块名称	String 必填
        logModel.setImpName("车场通道");//	接口名称	String
        logModel.setOpType("修改车场通道");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("修改车场通道："+passagewayModel.getChanName());//	操作内容	String  必填
        //logModel.setCarNumber("");//	车牌号码	String
        logModel.setOpResult(i>0?"成功":"失败");//	操作结果	String  必填
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        logModel.setOpTime(ts);//	操作时间	TimeStamp  必填
        logService.addLog(logModel);

        if(i!=-1) return MsgResponse.ok();
        else {return MsgResponse.error();}
    }

    @Override
    public Object deletePassagewayHandler(String token, PassagewayModel passagewayModel) {

        String name = passagewayModel.getChanName();
        QueryWrapper<PassagewayModel> wrapper = new QueryWrapper<>();
        //设置删除条件
        wrapper.eq("pk_chan_id",passagewayModel.getPkChanId());
        //调用删除方法
        int i = passagewayMapper.deletePassageway(wrapper);

        //查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();
        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("基础设置");//	模块名称	String 必填
        logModel.setImpName("车场通道");//	接口名称	String
        logModel.setOpType("删除车场通道");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("删除车场通道："+name);//	操作内容	String  必填
        //logModel.setCarNumber("");//	车牌号码	String
        logModel.setOpResult(i>0?"成功":"失败");//	操作结果	String  必填
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        logModel.setOpTime(ts);//	操作时间	TimeStamp  必填
        logService.addLog(logModel);

        if(i!=-1) return MsgResponse.ok();
        else {return MsgResponse.error();}
    }
}
