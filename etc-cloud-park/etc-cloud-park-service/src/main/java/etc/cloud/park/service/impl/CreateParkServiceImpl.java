package etc.cloud.park.service.impl;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import etc.cloud.auth.mapper.LoginFilterMapper;
import etc.cloud.auth.mode.TokenModel;
import etc.cloud.commons.mode.MsgResponse;

import etc.cloud.park.mapper.ParkingMapper;
import etc.cloud.park.mode.LogModel;
import etc.cloud.park.mode.ParkingModel;
import etc.cloud.park.service.CreateParkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class CreateParkServiceImpl extends ServiceImpl<ParkingMapper,ParkingModel> implements CreateParkService {

    @Autowired
    private ParkingMapper parkingMapper;
    @Autowired
    private LogServiceImpl logService;
    @Autowired
    private LoginFilterMapper loginFilterMapper;


    //创建车场模块
    @Override
    public Object inqueryInfoHandler(int current, int limit, String token,ParkingModel parkingModel ){

        // 此处应有token检查

//        int beginNumber = (current-1)*limit;
//        List<ParkingModel> list = parkingMapper.selectAll(beginNumber,limit);
//        int total = parkingMapper.selectCount();

        Page<ParkingModel> page = new Page();
        page.setCurrent(current);
        page.setSize(limit);

        QueryWrapper<ParkingModel> wrapper = new QueryWrapper<>();
        if(parkingModel.getParkName()!=null){
            wrapper.like("park_name",parkingModel.getParkName());
        }
        page = parkingMapper.selectAll(page,wrapper);

        System.out.println(page.getRecords());

        return MsgResponse.ok().data("total",page.getTotal()).data("rows",page.getRecords());


    }
    //新增车场
    @Override
    public Object addParkHandler(String token, ParkingModel parkingModel) {



        int i =parkingMapper.insertPark(parkingModel);


        //查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();
        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("基础设置");//	模块名称	String 必填
        logModel.setImpName("创建车场");//	接口名称	String
        logModel.setOpType("新增车场");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("新增车场"+parkingModel.getParkName());//	操作内容	String  必填
        //logModel.setCarNumber("");//	车牌号码	String
        logModel.setOpResult(i>0?"成功":"失败");//	操作结果	String  必填
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        logModel.setOpTime(ts);//	操作时间	TimeStamp  必填
        logService.addLog(logModel);


        if(i!=-1) return MsgResponse.ok();
        else {return MsgResponse.error();}
    }

    @Override
    public Object alterParkHandler(String token, ParkingModel parkingModel) {
        //验证token

        QueryWrapper<ParkingModel> wrapper = new QueryWrapper<>();
        wrapper.eq("pk_secret_key",parkingModel.getPkSecretKey());
        int i =parkingMapper.alterPark(parkingModel,wrapper);

        //查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();
        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("基础设置");//	模块名称	String 必填
        logModel.setImpName("创建车场");//	接口名称	String
        logModel.setOpType("修改车场");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("修改车场"+parkingModel.getParkName());//	操作内容	String  必填
        //logModel.setCarNumber("");//	车牌号码	String
        logModel.setOpResult(i>0?"成功":"失败");//	操作结果	String  必填
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        logModel.setOpTime(ts);//	操作时间	TimeStamp  必填
        logService.addLog(logModel);


        if(i!=-1) return MsgResponse.ok();
        else {return MsgResponse.error();}
    }

    @Override
    public String inquiryParkKey(String parkName) {
        return parkingMapper.selectKey(parkName);
    }
}
