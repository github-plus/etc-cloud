package etc.cloud.park.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import etc.cloud.auth.mapper.LoginFilterMapper;
import etc.cloud.auth.mode.TokenModel;
import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.park.mapper.ParkOperatorMapper;
import etc.cloud.park.mode.LogModel;
import etc.cloud.park.mode.ParkOperatorModel;

import etc.cloud.park.mode.ParkingModel;
import etc.cloud.park.service.ParkOperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class ParkOperatorServiceImpl extends ServiceImpl<ParkOperatorMapper, ParkOperatorModel> implements ParkOperatorService {

    @Autowired
    private ParkOperatorMapper parkOperatorMapper;
    @Autowired
    private LogServiceImpl logService;
    @Autowired
    private LoginFilterMapper loginFilterMapper;
    @Override
    public Object inqueryOperatorHandler(int current, int limit, String token, ParkOperatorModel parkOperatorModel){

        Page<ParkOperatorModel> page = new Page(current,limit);

        QueryWrapper<ParkOperatorModel> wrapper = new QueryWrapper<>();

        page = parkOperatorMapper.selectAll(page,wrapper);
        return MsgResponse.ok().data("total",page.getTotal()).data("rows",page.getRecords());
    }
    @Override
    public Object addParkOperatorHandler( String token,ParkOperatorModel parkOperatorModel){
        //验证token

        int i =parkOperatorMapper.insertParkOperator(parkOperatorModel);

        //查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();
        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("基础设置");//	模块名称	String 必填
        logModel.setImpName("车场操作员");//	接口名称	String
        logModel.setOpType("新增车场操作员");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("新增车场操作员："+parkOperatorModel.getOpName());//	操作内容	String  必填
        //logModel.setCarNumber("");//	车牌号码	String
        logModel.setOpResult(i>0?"成功":"失败");//	操作结果	String  必填
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        logModel.setOpTime(ts);//	操作时间	TimeStamp  必填
        logService.addLog(logModel);

        if(i!=-1) return MsgResponse.ok();
        else {return MsgResponse.error();}

    }
    @Override
    public Object alterParkOperatorHandler( String token,ParkOperatorModel parkOperatorModel){
        QueryWrapper<ParkOperatorModel> wrapper = new QueryWrapper<>();
        wrapper.eq("pk_account",parkOperatorModel.getPkAccount());
        int i = parkOperatorMapper.alterParkOperator(parkOperatorModel,wrapper);

        //查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();
        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("基础设置");//	模块名称	String 必填
        logModel.setImpName("车场操作员");//	接口名称	String
        logModel.setOpType("修改车场操作员");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("修改车场操作员："+parkOperatorModel.getOpName());//	操作内容	String  必填
        //logModel.setCarNumber("");//	车牌号码	String
        logModel.setOpResult(i>0?"成功":"失败");//	操作结果	String  必填
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        logModel.setOpTime(ts);//	操作时间	TimeStamp  必填
        logService.addLog(logModel);

        if(i!=-1) return MsgResponse.ok();
        else {return MsgResponse.error();}
    }
    @Override
    public Object deleteParkOperatorHandler( String token,ParkOperatorModel parkOperatorModel){

        String name = parkOperatorModel.getOpName();

        QueryWrapper<ParkOperatorModel> wrapper = new QueryWrapper<>();
        wrapper.eq("pk_account",parkOperatorModel.getPkAccount());
        int i = parkOperatorMapper.deleteParkOperator(wrapper);

        //查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();
        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("基础设置");//	模块名称	String 必填
        logModel.setImpName("车场操作员");//	接口名称	String
        logModel.setOpType("删除车场操作员");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("删除车场操作员："+name );//	操作内容	String  必填
        //logModel.setCarNumber("");//	车牌号码	String
        logModel.setOpResult(i>0?"成功":"失败");//	操作结果	String  必填
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        logModel.setOpTime(ts);//	操作时间	TimeStamp  必填
        logService.addLog(logModel);

        if(i!=-1) return MsgResponse.ok();
        else {return MsgResponse.error();}
    }






}
