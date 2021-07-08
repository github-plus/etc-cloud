package etc.cloud.park.service.impl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import etc.cloud.auth.mapper.LoginFilterMapper;
import etc.cloud.auth.mode.TokenModel;
import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.commons.tool.TurnTimeStamp;
import etc.cloud.park.mapper.PrefabricationMapper;
import etc.cloud.park.mode.LogModel;
import etc.cloud.park.mode.PrefabricateModel;
import etc.cloud.park.service.PrefabricationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
public class PrefabricationServiceImpl extends ServiceImpl<PrefabricationMapper, PrefabricateModel> implements PrefabricationService {

    @Autowired
    private PrefabricationMapper prefabricationMapper;

    @Autowired
    private LogServiceImpl logService;
    @Autowired
    private LoginFilterMapper loginFilterMapper;
    //添加停车场
    public Object addcar(String data,String token){

        //解析JSON数据
        JSONObject js = new JSONObject(data);


        //获取token
       // String token = js.getStr("token");

        //需要从获取到的token中查找到职工名称
        String workerName = "操作员";

        //获取要添加的数据
        Object car = js.get("query");
        JSONObject jsonObject = new JSONObject(car);
        //新建一个需要存储的类
        PrefabricateModel prefabricateModel = new PrefabricateModel();

        //产生唯一id
        String id = UUID.randomUUID().toString();
        //存入id
        prefabricateModel.setId(id);
        //存入数据
        prefabricateModel.setCarNumber(jsonObject.getStr("carNumber"));
        prefabricateModel.setCarOwner(jsonObject.getStr("carOwner"));
        prefabricateModel.setFeeType(jsonObject.getInt("feeType"));
        prefabricateModel.setRoom(jsonObject.getStr("room"));
        prefabricateModel.setTel(jsonObject.getStr("tel"));
        prefabricateModel.setNote(jsonObject.getStr("note"));

        //登记时间
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss ");
        String registerTime = sdf.format(date).toString();

//        Timestamp timestamp = Timestamp.valueOf(registerTime);
        prefabricateModel.setRegisterTime(registerTime);

        //开始时间
        String startTime = jsonObject.getStr("startTime");
        String replaceStartTime = startTime.replace("T", " ").replace("Z", "");
        prefabricateModel.setStartTime(replaceStartTime);

        //结束时间
        String endTime = jsonObject.getStr("endTime");
        String replaceEndTime = endTime.replace("T", " ").replace("Z", "");
        prefabricateModel.setEndTime(replaceEndTime);
        //操作员：从token中查找得到当前操作员
        prefabricateModel.setWorkerName(workerName);

        System.out.println("要插入的数据");
        System.out.println(prefabricateModel);
        //开始存入数据库
        Boolean insert = this.save(prefabricateModel);

        //查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();

        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("访客管理");//	模块名称	String 必填
        logModel.setImpName("预制车");//	接口名称	String
        logModel.setOpType("新增预制车");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("新增预制车车辆");//	操作内容	String  必填
        //logModel.setCarNumber("");//	车牌号码	String
        logModel.setOpResult(insert?"成功":"失败");//	操作结果	String  必填
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        logModel.setOpTime(ts);//	操作时间	TimeStamp  必填
        logService.addLog(logModel);
        //添加预制车成功
        if (insert ){

            return MsgResponse.ok().msg("添加预制车成功");
        }

        return MsgResponse.error().msg("添加预制车失败");
    }

    //修改预制车
    public Object updatecar(String data,String token){

        //解析JSON数据
        JSONObject js = new JSONObject(data);

        //获取token
        //String token = js.getStr("token");

        //根据token查找到worker_name
        String workerName = "workerName";
        //获取修改内容
        Object car = js.get("query");
        JSONObject jsonObject = new JSONObject(car);

        //新建一个预制车来存储数据
        PrefabricateModel prefabricateModel = new PrefabricateModel();

        //开始存储数据
        prefabricateModel.setId(jsonObject.getStr("id"));
        prefabricateModel.setCarNumber(jsonObject.getStr("carNumber"));
        prefabricateModel.setCarOwner(jsonObject.getStr("carName"));
        prefabricateModel.setFeeType(jsonObject.getInt("feeType"));
        prefabricateModel.setRoom(jsonObject.getStr("rom"));
        prefabricateModel.setTel(jsonObject.getStr("tel"));
        prefabricateModel.setNote(jsonObject.getStr("note"));
        //开始时间
        String startTime = jsonObject.getStr("startTime");
        String replaceStartTime = startTime.replace("T", " ").replace("Z", "");
        prefabricateModel.setStartTime(replaceStartTime);

        //结束时间
        String endTime = jsonObject.getStr("endTime");
        String replaceEndTime = endTime.replace("T", " ").replace("Z", "");
        prefabricateModel.setEndTime(replaceEndTime);

        prefabricateModel.setWorkerName(workerName);

        int update = prefabricationMapper.updateById(prefabricateModel);

                //查找到当前操作员（根据token）
                TokenModel finduser = loginFilterMapper.finduser(token);
                String account = finduser.getAccount();

                //记录日志
                LogModel logModel = new LogModel();
                logModel.setEmpId(account);//	用户账号	String  必填
                logModel.setCommunity("华源小区");//	小区名称	String
                logModel.setModeName("访客管理");//	模块名称	String 必填
                logModel.setImpName("预制车");//	接口名称	String
                logModel.setOpType("修改预制车");//	操作方法： 新增月租车，缴费
                logModel.setOpContent("修改预制车车辆");//	操作内容	String  必填
                //logModel.setCarNumber("");//	车牌号码	String
                logModel.setOpResult(update>0?"成功":"失败");//	操作结果	String  必填
                Timestamp ts = new Timestamp(System.currentTimeMillis());
                logModel.setOpTime(ts);//	操作时间	TimeStamp  必填
                logService.addLog(logModel);
        if (update == 1){
            return MsgResponse.ok().msg("更新预制车成功");
        }

        return MsgResponse.error().msg("更新预制车失败");
    }

    //删除预制车
    public Object deletecar(String data,String token){

        //解析JSON数据
        JSONObject jsonObject = new JSONObject(data);

        //获取token
        //String token = jsonObject.getStr("token");

        //获取要删除的id
        String id = jsonObject.getStr("id");

        //根据主键删除
        int delete = prefabricationMapper.deleteById(id);

        //查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();

        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("访客管理");//	模块名称	String 必填
        logModel.setImpName("预制车");//	接口名称	String
        logModel.setOpType("删除预制车");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("删除预制车车辆");//	操作内容	String  必填
        //logModel.setCarNumber("");//	车牌号码	String
        logModel.setOpResult(delete>0?"成功":"失败");//	操作结果	String  必填
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        logModel.setOpTime(ts);//	操作时间	TimeStamp  必填
        logService.addLog(logModel);
        //判断是否删除了数据
        if (delete != 0){

            return MsgResponse.ok().msg("删除成功");
        }

        return MsgResponse.error().msg("删除失败");
    }

    //查询预制车
    public Object findcar(String data){

        //解析JSON数据
        JSONObject jsonObject = new JSONObject(data);

        //获取token
        String token = jsonObject.getStr("token");

        //解析当前页和一页个数
        Integer limit = jsonObject.getInt("limit");
        Integer current = jsonObject.getInt("current");
        current = (current - 1)*limit;

        //解析要查找的数据
//        Object query = jsonObject.get("query");
//        JSONObject jsonObject1 = new JSONObject(query);
        JSONObject jsonObject1 = jsonObject.getJSONObject("query");

        //获取到车牌号码
        String carNumber = jsonObject1.getStr("carNumber");

        //获取车主姓名
        String carOwner = jsonObject1.getStr("carOwner");

        //获取电话号码
        String tel = jsonObject1.getStr("tel");

        //获取房号
        String room = jsonObject1.getStr("room");

        //获取查询登记开始时间  此时间是查询登记的，非查询生效时间
        String startTime = jsonObject1.getStr("startTime");

        //获取登录结束时间
        String endTime = jsonObject1.getStr("endTime");

        //创建查询需求
        Page<PrefabricateModel> pageResult = new Page<>(current, limit);
        QueryWrapper<PrefabricateModel> wrapper = new QueryWrapper<>();

        //插入查询条件
        //根据车牌号查询
        if (carNumber != null){
            wrapper.eq("car_number",carNumber);
        }
        //根据车主查询
        if (carOwner != null){
            wrapper.eq("car_owner", carOwner);
        }
        //根据电话号码查询
        if (tel != null){
            wrapper.eq("tel", tel);
        }
        //根据房号查询
        if (room != null){
            wrapper.eq("room", room);
        }
        //登记时间大于查询开始时间
        if (startTime != null){
            wrapper.ge("register_time", startTime);
        }
        //登记时间小于查询结束时间
        if (endTime != null){
            wrapper.le("register_time", endTime);
        }

        //开始查询
        this.page(pageResult, wrapper);
        //结果总个数
        long total = pageResult.getTotal();
        //解析结果
        List<PrefabricateModel> rows = pageResult.getRecords();

        //开始根据条件查询
//        List<PrefabricateModel> findcar = prefabricationMapper.findcar(limit, current,
//                carNumber, carOwner, tel, room, startTime, endTime);
//        List<PrefabricateModel> findcar = prefabricationMapper.find();
//        System.out.println(findcar);
        //如果查询到有数据
        if (total != 0){

            return MsgResponse.ok().data("total",total).data("rows",rows);
        }

        return MsgResponse.ok().msg("暂时没有预制车");
    }

}
