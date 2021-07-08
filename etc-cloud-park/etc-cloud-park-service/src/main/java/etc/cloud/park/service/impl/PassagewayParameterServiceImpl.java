package etc.cloud.park.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import etc.cloud.auth.mapper.LoginFilterMapper;
import etc.cloud.auth.mode.TokenModel;
import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.park.mapper.PassagewayMapper;
import etc.cloud.park.mapper.PassagewayParameterMapper;
import etc.cloud.park.mode.LogModel;
import etc.cloud.park.mode.PassagewayModel;
import etc.cloud.park.mode.PassagewayParameterModel;
import etc.cloud.park.service.PassagewayParameterService;
import etc.cloud.park.service.PassagewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class PassagewayParameterServiceImpl extends ServiceImpl<PassagewayParameterMapper, PassagewayParameterModel> implements PassagewayParameterService {

    @Autowired
    private PassagewayParameterMapper passagewayParameterMapper;
    @Autowired
    private LogServiceImpl logService;
    @Autowired
    private LoginFilterMapper loginFilterMapper;

    @Override
    public Object inqueryPassagewayParameterHandler(String token, PassagewayParameterModel passagewayParameterModel) {
        Page<PassagewayParameterModel> page = new Page();
        //page.setDesc("channel_param_id");
        QueryWrapper<PassagewayParameterModel> wrapper = new QueryWrapper<>();

        page = passagewayParameterMapper.selectAll(page,wrapper);

        return MsgResponse.ok().data("total",page.getTotal()).data("rows",page.getRecords());
    }

    @Override
    public Object addPassagewayParameterHandler(String token, PassagewayParameterModel passagewayParameterModel) {
        int i =passagewayParameterMapper.insertPassagewayParameter(passagewayParameterModel);

        //查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();
        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("基础设置");//	模块名称	String 必填
        logModel.setImpName("车场通道");//	接口名称	String
        logModel.setOpType("新增车场通道参数");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("新增车场通道参数");//	操作内容	String  必填
        //logModel.setCarNumber("");//	车牌号码	String
        logModel.setOpResult(i>0?"成功":"失败");//	操作结果	String  必填
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        logModel.setOpTime(ts);//	操作时间	TimeStamp  必填
        logService.addLog(logModel);


        if(i!=-1) return MsgResponse.ok();
        else {return MsgResponse.error();}
    }

    @Override
    public Object alterPassagewayParameterHandler(String token, PassagewayParameterModel passagewayParameterModel) {
        QueryWrapper<PassagewayParameterModel> wrapper = new QueryWrapper<>();
        wrapper.eq("channel_param_id",passagewayParameterModel.getChannelParamId());
        int i = passagewayParameterMapper.alterPassagewayParameter(passagewayParameterModel,wrapper);

        //查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();
        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("基础设置");//	模块名称	String 必填
        logModel.setImpName("车场通道");//	接口名称	String
        logModel.setOpType("修改车场通道参数");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("修改车场通道参数");//	操作内容	String  必填
        //logModel.setCarNumber("");//	车牌号码	String
        logModel.setOpResult(i>0?"成功":"失败");//	操作结果	String  必填
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        logModel.setOpTime(ts);//	操作时间	TimeStamp  必填
        logService.addLog(logModel);

        if(i!=-1) return MsgResponse.ok();
        else {return MsgResponse.error();}
    }
}
