package etc.cloud.park.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import etc.cloud.auth.mapper.LoginFilterMapper;
import etc.cloud.auth.mode.TokenModel;
import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.park.mapper.ParkingMapper;
import etc.cloud.park.mapper.ParkingParameterMapper;
import etc.cloud.park.mode.LogModel;
import etc.cloud.park.mode.ParkingModel;
import etc.cloud.park.mode.ParkingParameterModel;
import etc.cloud.park.service.CreateParkService;
import etc.cloud.park.service.ParkingParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class ParkingParameterServiceImpl extends ServiceImpl<ParkingParameterMapper, ParkingParameterModel> implements ParkingParameterService {

    @Autowired
    private ParkingParameterMapper parkingParameterMapper;
    @Autowired
    private LogServiceImpl logService;
    @Autowired
    private LoginFilterMapper loginFilterMapper;

    //车场参数模块
    @Override
    public  Object inqueryParkingParameterInfoHandler( String token, ParkingParameterModel parkingParameterModel){
        Page<ParkingModel> page = new Page();

        QueryWrapper<ParkingParameterModel> wrapper = new QueryWrapper<>();

        page = parkingParameterMapper.selectAll(page,wrapper);

        System.out.println(page.getRecords());

        return MsgResponse.ok().data("rows",page.getRecords());
    }

    @Override
    public Object addParkingParameterHandler(String token, ParkingParameterModel parkingParameterModel) {
        //验证token

        int i =parkingParameterMapper.insertParkingParameter(parkingParameterModel);

        //查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();
        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("基础设置");//	模块名称	String 必填
        logModel.setImpName("车场参数");//	接口名称	String
        logModel.setOpType("新增车场参数");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("新增车场参数");//	操作内容	String  必填
        //logModel.setCarNumber("");//	车牌号码	String
        logModel.setOpResult(i>0?"成功":"失败");//	操作结果	String  必填
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        logModel.setOpTime(ts);//	操作时间	TimeStamp  必填
        logService.addLog(logModel);

        if(i!=-1) return MsgResponse.ok();
        else {return MsgResponse.error();}
    }

    @Override
    public Object alterParkingParameterHandler(String token, ParkingParameterModel parkingParameterModel) {
        QueryWrapper<ParkingParameterModel> wrapper = new QueryWrapper<>();
        wrapper.eq("pk_id",parkingParameterModel.getPkId());
        int i =parkingParameterMapper.alterParkingParameter(parkingParameterModel,wrapper);

        //查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();
        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("基础设置");//	模块名称	String 必填
        logModel.setImpName("车场参数");//	接口名称	String
        logModel.setOpType("修改车场参数");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("修改车场参数");//	操作内容	String  必填
        //logModel.setCarNumber("");//	车牌号码	String
        logModel.setOpResult(i>0?"成功":"失败");//	操作结果	String  必填
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        logModel.setOpTime(ts);//	操作时间	TimeStamp  必填
        logService.addLog(logModel);

        if(i!=-1) return MsgResponse.ok();
        else {return MsgResponse.error();}
    }
}
