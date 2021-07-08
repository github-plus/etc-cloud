package etc.cloud.park.service.impl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import etc.cloud.auth.mapper.LoginFilterMapper;
import etc.cloud.auth.mode.TokenModel;
import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.park.mapper.AppointmentMapper;
import etc.cloud.park.mapper.ParkingMapper;
import etc.cloud.park.mode.AppointmentModel;
import etc.cloud.park.mode.LogModel;
import etc.cloud.park.mode.ParkingModel;
import etc.cloud.park.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class AppointmentServiceImpl extends ServiceImpl<AppointmentMapper, AppointmentModel> implements AppointmentService {

    @Autowired
    private ParkingMapper parkingMapper;
    @Autowired
    private LogServiceImpl logService;
    @Autowired
    private LoginFilterMapper loginFilterMapper;

    //添加预约车信息
    public Object addcar(String data,String token){

        //解析JSON数据
        JSONObject jsonObject = new JSONObject(data);

        //获取token
//        String token = jsonObject.getStr("token");

        //获取预约车数据
        Object car = jsonObject.get("query");
        //解析预约车数据
        JSONObject jsonObject1 = new JSONObject(car);


        //新建一个预约车
        AppointmentModel appointmentModel = new AppointmentModel();

        appointmentModel.setPkSecretKey(jsonObject1.getStr("pkSecretKey"));
        appointmentModel.setCarNumber(jsonObject1.getStr("carNumber"));
        appointmentModel.setCarOwner(jsonObject1.getStr("carOwner"));
        //开始时间
        String startTime = jsonObject1.getStr("startTime");

        String replaceStartTime = startTime.replace("T", " ").replace("Z", "");
        appointmentModel.setStartTime(replaceStartTime);

        //结束时间
        String endTime = jsonObject1.getStr("endTime");
        String replaceEndTime = endTime.replace("T", " ").replace("Z", "");
        appointmentModel.setEndTime(replaceEndTime);

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss ");
        String applyTime = sdf.format(date).toString();
        appointmentModel.setApplyTime(applyTime);

        appointmentModel.setRoomOwner(jsonObject1.getStr("roomOwner"));
        appointmentModel.setRoom(jsonObject1.getStr("room"));
        appointmentModel.setTel(jsonObject1.getStr("tel"));

        //新增预约车车默认是0拒绝
        int status = 0;
        appointmentModel.setStatus(status);
        //预约车id
        String id = UUID.randomUUID().toString();
        appointmentModel.setId(id);


        //开始添加预约车
        boolean addcar = this.save(appointmentModel);

        //查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();

        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("访客管理");//	模块名称	String 必填
        logModel.setImpName("预约车");//	接口名称	String
        logModel.setOpType("新增预约车");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("新增预约入场车辆");//	操作内容	String  必填
        //logModel.setCarNumber("");//	车牌号码	String
        logModel.setOpResult(addcar?"成功":"失败");//	操作结果	String  必填
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        logModel.setOpTime(ts);//	操作时间	TimeStamp  必填
        logService.addLog(logModel);
        if (addcar){

            return MsgResponse.ok().msg("预约车添加成功");
        }

        return MsgResponse.error().msg("预约车添加失败");
    }

    //修改预约车信息
    public Object updatecar(String data,String token){


            //解析JSON数据
            JSONObject jsonObject = new JSONObject(data);

            //获取token
            //String token = jsonObject.getStr("token");

            //获取预约车数据
            Object car = jsonObject.get("query");
            //解析预约车数据
            JSONObject jsonObject1 = new JSONObject(car);

            //新建一个预约车
            AppointmentModel appointmentModel = new AppointmentModel();

            appointmentModel.setId(jsonObject1.getStr("id"));
            appointmentModel.setPkSecretKey(jsonObject1.getStr("pkSecretKey"));
            appointmentModel.setCarNumber(jsonObject1.getStr("carNumber"));
            appointmentModel.setCarOwner(jsonObject1.getStr("carOwner"));
        //开始时间
        String startTime = jsonObject1.getStr("startTime");
        String replaceStartTime = startTime.replace("T", " ").replace("Z", "");
        appointmentModel.setStartTime(replaceStartTime);

        //结束时间
        String endTime = jsonObject1.getStr("endTime");
        String replaceEndTime = endTime.replace("T", " ").replace("Z", "");
        appointmentModel.setEndTime(replaceEndTime);

            //appointmentModel.setApplyTime(jsonObject1.getDate("applyTime"));

            appointmentModel.setRoomOwner(jsonObject1.getStr("roomOwner"));
            appointmentModel.setRoom(jsonObject1.getStr("room"));
            appointmentModel.setTel(jsonObject1.getStr("tel"));

            //开始修改预约车
            boolean updatecar = this.updateById(appointmentModel);
        //查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();

        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("人力行政");//	模块名称	String 必填
        logModel.setImpName("访客管理");//	接口名称	String
        logModel.setOpType("修改预约车");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("修改预约入场车辆");//	操作内容	String  必填
        //logModel.setCarNumber("");//	车牌号码	String
        logModel.setOpResult(updatecar?"成功":"失败");//	操作结果	String  必填
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        logModel.setOpTime(ts);//	操作时间	TimeStamp  必填
        logService.addLog(logModel);
            if (updatecar){

                return MsgResponse.ok().msg("预约车修改成功");
            }

            return MsgResponse.error().msg("预约车修改失败");
    }


    //修改预约车状态
    public Object updateStatus(String data,String token){

        //解析JSON数据
        JSONObject jsonObject = new JSONObject(data);

        //获取token
       // String token = jsonObject.getStr("token");

        //新建一个预约车
        AppointmentModel appointmentModel = new AppointmentModel();
        appointmentModel.setId(jsonObject.getStr("id"));
        appointmentModel.setStatus(jsonObject.getInt("status"));

        System.out.println("传输的数据为："+appointmentModel);
        //开始更改状态
        boolean updatestatus = this.updateById(appointmentModel);
//查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();

        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("人力行政");//	模块名称	String 必填
        logModel.setImpName("访客管理");//	接口名称	String
        logModel.setOpType("修改预约车");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("修改预约入场车辆状态");//	操作内容	String  必填
        //logModel.setCarNumber("");//	车牌号码	String
        logModel.setOpResult(updatestatus?"成功":"失败");//	操作结果	String  必填
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        logModel.setOpTime(ts);//	操作时间	TimeStamp  必填
        logService.addLog(logModel);
        if (updatestatus){

            return MsgResponse.ok().msg("状态修改成功");
        }
        return MsgResponse.error().msg("状态修改失败");
    }

    //查询预约车信息
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
        String carOwner = jsonObject1.getStr("carOwner");
        String tel = jsonObject1.getStr("tel");
        String room = jsonObject1.getStr("room");
        String startTime = jsonObject1.getStr("startTime");
        String endTime = jsonObject1.getStr("endTime");

        //创建查询需求
        Page<AppointmentModel> pageResult = new Page<>(current, limit);
        QueryWrapper<AppointmentModel> wrapper = new QueryWrapper<>();

        if (carNumber != null){
            wrapper.eq("car_number", carNumber);
        }
        if (carOwner != null ){
            wrapper.eq("car_owner", carOwner);
        }
        if (tel != null){
            wrapper.eq("tel", tel);
        }
        if (room != null){
            wrapper.eq("room", room);
        }
        if (startTime != null){
            wrapper.ge("apply_time", startTime);
        }
        if (endTime != null){
            wrapper.le("apply_time", endTime);
        }

        //开始查询
        Page<AppointmentModel> page1 = this.page(pageResult, wrapper);
        //结果总个数
        long total = pageResult.getTotal();
        //解析结果
        List<AppointmentModel> rows = pageResult.getRecords();

        //查询所有停车场数据
        List<ParkingModel> park = parkingMapper.findPark();

        return MsgResponse.ok().data("total",total).data("rows", rows).data("park", park);
    }

}
