package etc.cloud.park.service.impl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import etc.cloud.auth.mapper.LoginFilterMapper;
import etc.cloud.auth.mode.TokenModel;
import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.commons.tool.TurnTimeStamp;
import etc.cloud.park.mapper.*;
import etc.cloud.park.mode.*;
import etc.cloud.park.service.TempcarRulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TempcarRulesServiceImpl  implements TempcarRulesService {

    @Autowired
    private TimesFeeMapper timesFeeMapper;
    @Autowired
    private PayCarTypeMapper payCarTypeMapper;
    @Autowired
    private TimeQuantumFeeMapper timeQuantumFeeMapper;
    @Autowired
    private DayNightFeeMapper dayNightFeeMapper;
    @Autowired
    private LogServiceImpl logService;
    @Autowired
    private LoginFilterMapper loginFilterMapper;


    //查询按次收费
    @Override
    public Object inqueryTimesFee(String data) {
        //解析数据
        JSONObject jsonObject = new JSONObject(data);

        int typeId = jsonObject.getInt("PayCarTypeId");//收费车型Id

        //解析token
        //String token = jsonObject.getStr("token");
        String token = "";

        PayCarTypeModel payCarTypeModel = payCarTypeMapper.selectByPayCarTypeId(typeId);

        int i =payCarTypeModel.getChargeType();

        //逻辑判断，如果数据库中该收费类型的收费方法不是按次收费，则返回空数组
        if (1!= i){
            return MsgResponse.ok().data("rows",null);
        }else {
            //设置实体类属性

            TimesFeeModel timesFeeModel = new TimesFeeModel();
            int ChargeId = payCarTypeModel.getChargeId();  //获得按次收费序号id

            timesFeeModel = timesFeeMapper.selectById(ChargeId);  //根据id查询
            //System.out.println(timesFeeModel);
            return MsgResponse.ok().data("rows",timesFeeModel);
        }
    }
    //新增按次收费
    @Override
    public Object addTimesFee(String data,String token) {
        //解析数据
        JSONObject jsonObject = new JSONObject(data);

        int typeId = jsonObject.getInt("PayCarTypeId");//收费车型Id

        //解析token
        //String token = jsonObject.getStr("token");
        //String token = "";
        //获取收费车型对应的对象
        PayCarTypeModel payCarTypeModel = payCarTypeMapper.selectByPayCarTypeId(typeId);


        //设置实体类属性
        JSONObject object1 = jsonObject.getJSONObject("stus");
        TimesFeeModel timesFeeModel = new TimesFeeModel();
        // 按次收费金额
        timesFeeModel.setPay(object1.getInt("pay"));
        // 是否自然天：0 否（24小时）1 是（自然天24小时）
        timesFeeModel.setIsNatureDay(object1.getInt("isNatureDay"));
        // 是否含有免费分钟：0 否 1 有（默认0）
        timesFeeModel.setIsFree(object1.getInt("isFree"));
        // 免费分钟
        timesFeeModel.setFreeTime(object1.getInt("freeTime"));
        // 单次最高收费金额
        timesFeeModel.setCharge(object1.getInt("charge"));
        // 周期内最高收费
        timesFeeModel.setPeriodCharge(object1.getInt("periodCharge"));
        // 计费后是否有首段：0 无 1 有
        timesFeeModel.setIsHead(object1.getInt("isHead"));
        // 计费周期内多次是否进出计费累计限额：0 否 1是
        timesFeeModel.setIsMore(object1.getInt("isMore"));
        // 备注
        timesFeeModel.setNote(object1.getStr("note"));

        int j = timesFeeMapper.insertTimesFee(timesFeeModel);


        //得到新增的这条数据的主键id
        int feeId = timesFeeModel.getPkTimesFeeId();

        int ChargeId = payCarTypeModel.getChargeId();  //获得元数据中按次收费序号id
        if (ChargeId != 0){
            //先确定删哪张表的数据
            if(payCarTypeModel.getChargeType()==1){
                //删掉旧的
                QueryWrapper<TimesFeeModel> wrapper = new QueryWrapper<>();
                wrapper.eq("pk_times_fee_id",ChargeId);
                timesFeeMapper.delete(wrapper);
            }else if(payCarTypeModel.getChargeType()==2){
                QueryWrapper<TimeQuantumFeeModel> wrapper = new QueryWrapper<>();
                wrapper.eq("pk_time_quantum_fee_id",ChargeId);
                timeQuantumFeeMapper.delete(wrapper);
            }else if(payCarTypeModel.getChargeType()==3){
                QueryWrapper<DayNightFeeModel> wrapper = new QueryWrapper<>();
                wrapper.eq("pk_day_night_fee_id",ChargeId);
                dayNightFeeMapper.delete(wrapper);
            }

        }

            //写入新的
            payCarTypeModel.setChargeId(feeId);
            //设置收费方法
            payCarTypeModel.setChargeType(1);

            QueryWrapper<PayCarTypeModel> wrapper2 = new QueryWrapper<>();
            wrapper2.eq("pk_pay_temp_car_type_id",typeId);
            int i =payCarTypeMapper.updateById(payCarTypeModel);


        //查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();
        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("基础设置");//	模块名称	String 必填
        logModel.setImpName("临时车收费规则");//	接口名称	String
        logModel.setOpType("新增按次收费");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("新增按次收费规则");//	操作内容	String  必填
        //logModel.setCarNumber("");//	车牌号码	String
        logModel.setOpResult(j>0?"成功":"失败");//	操作结果	String  必填
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        logModel.setOpTime(ts);//	操作时间	TimeStamp  必填
        logService.addLog(logModel);

        if(i!=-1) return MsgResponse.ok();
        else {return MsgResponse.error();}


    }
    //查询分时段收费
    @Override
    public Object inqueryTimeQuantumFee(String data) {
        //解析数据
        JSONObject jsonObject = new JSONObject(data);

        int typeId = jsonObject.getInt("PayCarTypeId");//收费车型Id

        //解析token
        //String token = jsonObject.getStr("token");
        String token = "";

        PayCarTypeModel payCarTypeModel = payCarTypeMapper.selectByPayCarTypeId(typeId);

        int i =payCarTypeModel.getChargeType();

        //逻辑判断，如果数据库中该收费类型的收费方法不是分时段收费，则返回空数组
        if (2!= i){
            return MsgResponse.ok().data("rows",null);
        }else {
            //设置实体类属性

            TimeQuantumFeeModel timeQuantumFeeModel = new TimeQuantumFeeModel();
            int ChargeId = payCarTypeModel.getChargeId();  //获得按时间段收费序号id
            timeQuantumFeeModel = timeQuantumFeeMapper.selectById(ChargeId); //根据id查询


            return MsgResponse.ok().data("rows",timeQuantumFeeModel);
        }
    }

    //新增分时段收费
    @Override
    public Object addTimeQuantumFee(String data,String token) {
        //解析数据
        JSONObject jsonObject = new JSONObject(data);

        int typeId = jsonObject.getInt("PayCarTypeId");//收费车型Id

        //解析token
        //String token = jsonObject.getStr("token");
        //String token = "";
        //获取收费车型对应的对象
        PayCarTypeModel payCarTypeModel = payCarTypeMapper.selectByPayCarTypeId(typeId);


        //设置实体类属性
        JSONObject object1 = jsonObject.getJSONObject("stus");
        TimeQuantumFeeModel timeQuantumFeeModel = new TimeQuantumFeeModel();

        //首段计时时长：分钟
        timeQuantumFeeModel.setTimeFirst(object1.getInt("timeFirst"));
        //计时单位：比如以20分钟为一个单位进行计费
        timeQuantumFeeModel.setTimeCellFirst(object1.getInt("timeCellFirst"));
        //计时金额（一个单位的计费金额量）
        timeQuantumFeeModel.setTimeAmountFirst(object1.getInt("timeAmountFirst"));
        //第二段计时时长：分钟
        timeQuantumFeeModel.setTimeSecond(object1.getInt("timeSecond"));
        //计时单位
        timeQuantumFeeModel.setTimeCellSecond(object1.getInt("timeCellSecond"));
        //计时金额
        timeQuantumFeeModel.setTimeAmountSecond(object1.getInt("timeAmountSecond"));
        //第三段计时时长
        timeQuantumFeeModel.setTimeThird(object1.getInt("timeThird"));
        //计时单位
        timeQuantumFeeModel.setTimeCellThird(object1.getInt("timeCellThird"));
        //计时金额
        timeQuantumFeeModel.setTimeAmountThird(object1.getInt("timeAmountThird"));
        //计时单位
        timeQuantumFeeModel.setTimeCellFinal(object1.getInt("timeCellFinal"));
        //计时金额
        timeQuantumFeeModel.setTimeAmountFinal(object1.getInt("timeAmountFinal"));
        //是否自然天：0 否（24小时）1 是（自然天24小时）
        timeQuantumFeeModel.setIsNatureDay(object1.getInt("isNatureDay"));
        //是否含有免费分钟：0 否 1 有（默认0）
        timeQuantumFeeModel.setIsFree(object1.getInt("isFree"));
        //免费分钟
        timeQuantumFeeModel.setFreeTime(object1.getInt("freeTime"));
        //单次最高收费金额
        timeQuantumFeeModel.setCharge(object1.getInt("charge"));
        //周期内最高收费
        timeQuantumFeeModel.setPeriodCharge(object1.getInt("periodCharge"));
        //计费后是否有首段：0 无 1 有
        timeQuantumFeeModel.setIsHead(object1.getInt("isHead"));
        //计费周期内多次是否进出计费累计限额：0 否 1是
        timeQuantumFeeModel.setIsMore(object1.getInt("isMore"));
        //备注
        timeQuantumFeeModel.setNote(object1.getStr("note"));

        //进行新增操作
        int j = timeQuantumFeeMapper.insert(timeQuantumFeeModel);
        //得到新增的这条数据的主键id
        int feeId = timeQuantumFeeModel.getPkTimeQuantumFeeId();

        int ChargeId = payCarTypeModel.getChargeId();  //获得原数据中按次收费序号id
        if (ChargeId != 0){
            //先确定删哪张表的数据
            if(payCarTypeModel.getChargeType()==1){
                //删掉旧的
                QueryWrapper<TimesFeeModel> wrapper = new QueryWrapper<>();
                wrapper.eq("pk_times_fee_id",ChargeId);
                timesFeeMapper.delete(wrapper);
            }else if(payCarTypeModel.getChargeType()==2){
                QueryWrapper<TimeQuantumFeeModel> wrapper = new QueryWrapper<>();
                wrapper.eq("pk_time_quantum_fee_id",ChargeId);
                timeQuantumFeeMapper.delete(wrapper);
            }else if(payCarTypeModel.getChargeType()==3){
                QueryWrapper<DayNightFeeModel> wrapper = new QueryWrapper<>();
                wrapper.eq("pk_day_night_fee_id",ChargeId);
                dayNightFeeMapper.delete(wrapper);
            }

        }

            //写入新的
            payCarTypeModel.setChargeId(feeId);
            //设置收费方法
            payCarTypeModel.setChargeType(2);
            QueryWrapper<PayCarTypeModel> wrapper2 = new QueryWrapper<>();
            wrapper2.eq("pk_pay_temp_car_type_id",typeId);
            int i = payCarTypeMapper.updateById(payCarTypeModel);

        //查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();
        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("基础设置");//	模块名称	String 必填
        logModel.setImpName("临时车收费规则");//	接口名称	String
        logModel.setOpType("新增分时段收费");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("新增分时段收费规则");//	操作内容	String  必填
        //logModel.setCarNumber("");//	车牌号码	String
        logModel.setOpResult(j>0?"成功":"失败");//	操作结果	String  必填
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        logModel.setOpTime(ts);//	操作时间	TimeStamp  必填
        logService.addLog(logModel);

        if(i!=-1) return MsgResponse.ok();
        else {return MsgResponse.error();}
    }
    //查询白天夜间收费
    @Override
    public Object inqueryDayNightFee(String data) {
        //解析数据
        JSONObject jsonObject = new JSONObject(data);

        int typeId = jsonObject.getInt("PayCarTypeId");//收费车型Id

        //解析token
        //String token = jsonObject.getStr("token");
        String token = "";
        //根据id获取收费车型对象
        PayCarTypeModel payCarTypeModel = payCarTypeMapper.selectByPayCarTypeId(typeId);
        //得到收费方式编号
        int i =payCarTypeModel.getChargeType();

        //逻辑判断，如果数据库中该收费类型的收费方法不是白天夜间收费，则返回空数组
        if (3!= i){
            return MsgResponse.ok().data("rows",null);
        }else {
            //设置实体类属性

            DayNightFeeModel dayNightFeeModel = new DayNightFeeModel();
            int ChargeId = payCarTypeModel.getChargeId();  //获得白天夜间收费序号id
            dayNightFeeModel = dayNightFeeMapper.selectById(ChargeId); //根据id查询


            return MsgResponse.ok().data("rows",dayNightFeeModel);
        }
    }
    //新增白天夜间收费
    @Override
    public Object addDayNightFee(String data,String token) {
        //解析数据
        JSONObject jsonObject = new JSONObject(data);

        int typeId = jsonObject.getInt("PayCarTypeId");//收费车型Id

        //解析token
        //String token = jsonObject.getStr("token");
        //String token = "";
        //获取收费车型对应的对象
        PayCarTypeModel payCarTypeModel = payCarTypeMapper.selectByPayCarTypeId(typeId);


        //设置实体类属性
        JSONObject object1 = jsonObject.getJSONObject("stus");
        DayNightFeeModel dayNightFeeModel = new DayNightFeeModel();

        //白天开始时间，如“08:00”
        dayNightFeeModel.setDayTimeBegin(object1.getStr("dayTimeBegin"));
        //白天结束时间，如“18:00”
        dayNightFeeModel.setDayTimeEnd(object1.getStr("dayTimeEnd"));
        //首段计时时长：分钟
        dayNightFeeModel.setDayTimeFirst(object1.getInt("dayTimeFirst"));
        //首段计时单位：比如以20分钟为一个单位进行计费
        dayNightFeeModel.setDayTimeCellFirst(object1.getInt("dayTimeCellFirst"));
        //首段计时金额（一个单位的计费金额量）
        dayNightFeeModel.setDayTimeAmountFirst(object1.getInt("dayTimeAmountFirst"));
        //白天二段计时单位
        dayNightFeeModel.setDayTimeCellFinal(object1.getInt("dayTimeCellFinal"));
        //白天二段计时金额
        dayNightFeeModel.setDayTimeAmountFinal(object1.getInt("dayTimeAmountFinal"));
        //夜间开始时间，如“18:00”
        dayNightFeeModel.setNightTimeBegin(object1.getStr("nightTimeBegin"));
        //夜间结束时间，如“08:00”
        dayNightFeeModel.setNightTimeEnd(object1.getStr("nightTimeEnd"));
        //首段计时时长：分钟
        dayNightFeeModel.setNightTimeFirst(object1.getInt("nightTimeFirst"));
        //首段计时单位：比如以20分钟为一个单位进行计费
        dayNightFeeModel.setNightTimeCellFirst(object1.getInt("nightTimeCellFirst"));
        //首段计时金额（一个单位的计费金额量）
        dayNightFeeModel.setNightTimeAmountFirst(object1.getInt("nightTimeAmountFirst"));
        //夜间二段计时单位
        dayNightFeeModel.setNightTimeCellFinal(object1.getInt("nightTimeCellFinal"));
        //夜间二段计时金额
        dayNightFeeModel.setNightTimeAmountFinal(object1.getInt("nightTimeAmountFinal"));


        //是否自然天：0 否（24小时）1 是（自然天24小时）
        dayNightFeeModel.setIsNatureDay(object1.getInt("isNatureDay"));
        //是否含有免费分钟：0 否 1 有（默认0）
        dayNightFeeModel.setIsFree(object1.getInt("isFree"));
        //免费分钟
        dayNightFeeModel.setFreeTime(object1.getInt("freeTime"));
        //单次最高收费金额
        dayNightFeeModel.setCharge(object1.getInt("charge"));
        //周期内最高收费
        dayNightFeeModel.setPeriodCharge(object1.getInt("periodCharge"));
        //计费后是否有首段：0 无 1 有
        dayNightFeeModel.setIsHead(object1.getInt("isHead"));
        //计费周期内多次是否进出计费累计限额：0 否 1是
        dayNightFeeModel.setIsMore(object1.getInt("isMore"));
        //备注
        dayNightFeeModel.setNote(object1.getStr("note"));

        //进行新增操作
        int j  = dayNightFeeMapper.insert(dayNightFeeModel);
        //得到新增的这条数据的主键id
        int feeId = dayNightFeeModel.getPkDayNightFeeId();

        int ChargeId = payCarTypeModel.getChargeId();  //获得原数据中按次收费序号id
        if (ChargeId != 0){
            //先确定删哪张表的数据
            if(payCarTypeModel.getChargeType()==1){
                //删掉旧的
                QueryWrapper<TimesFeeModel> wrapper = new QueryWrapper<>();
                wrapper.eq("pk_times_fee_id",ChargeId);
                timesFeeMapper.delete(wrapper);
            }else if(payCarTypeModel.getChargeType()==2){
                QueryWrapper<TimeQuantumFeeModel> wrapper = new QueryWrapper<>();
                wrapper.eq("pk_time_quantum_fee_id",ChargeId);
                timeQuantumFeeMapper.delete(wrapper);
            }else if(payCarTypeModel.getChargeType()==3){
                QueryWrapper<DayNightFeeModel> wrapper = new QueryWrapper<>();
                wrapper.eq("pk_day_night_fee_id",ChargeId);
                dayNightFeeMapper.delete(wrapper);
            }

        }

        //写入新的
        payCarTypeModel.setChargeId(feeId);
        //设置收费方法
        payCarTypeModel.setChargeType(3);
        QueryWrapper<PayCarTypeModel> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("pk_pay_temp_car_type_id",typeId);
        int i = payCarTypeMapper.updateById(payCarTypeModel);

        //查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();
        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("基础设置");//	模块名称	String 必填
        logModel.setImpName("临时车收费规则");//	接口名称	String
        logModel.setOpType("新增白天夜间收费");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("新增白天夜间收费规则");//	操作内容	String  必填
        //logModel.setCarNumber("");//	车牌号码	String
        logModel.setOpResult(j>0?"成功":"失败");//	操作结果	String  必填
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        logModel.setOpTime(ts);//	操作时间	TimeStamp  必填
        logService.addLog(logModel);

        if(i!=-1) return MsgResponse.ok();
        else {return MsgResponse.error();}
    }

    //停车时长计算
    @Override
    public  Map<String, Integer> countTime(Timestamp startTime, Timestamp endTime){
        SimpleDateFormat timeformat = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
        long t1 = endTime.getTime();
        long t2 = startTime.getTime();
        int hours=(int) ((t1 - t2)/(1000*60*60));
        int minutes=(int) (((t1 - t2)/1000-hours*(60*60))/60);
        int second=(int) ((t1 - t2)/1000-hours*(60*60)-minutes*60);
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("hours",hours);
        map.put("minutes",minutes);
        map.put("second",second);
        return map;

    }

    //不跨周期按次计费方法
    @Override
    public int OneTimesFee(TimesFeeModel timesFeeModel, Date startTime, Date endTime){
        int resultFee = 0 ;
        //获取收费规则
        int pay = timesFeeModel.getPay();// 按次收费金额
        int is_nature_day = timesFeeModel.getIsNatureDay();// 是否自然天：0 否（24小时）1 是（自然天24小时）
        int is_free = timesFeeModel.getIsFree();// 是否含有免费分钟：0 否 1 有（默认0） 是指这个免费分钟数算不算钱
        int free_time = timesFeeModel.getFreeTime();// 免费分钟
        int charge = timesFeeModel.getCharge();// 单次最高收费金额
        int period_charge = timesFeeModel.getPeriodCharge();// 周期内最高收费
        int is_head = timesFeeModel.getIsHead();// 计费后是否有首段：0 无 1 有
        int is_more =  timesFeeModel.getIsMore();// 计费周期内多次是否进出计费累计限额：0 否 1是
        //单次不跨周期的收费
        //算相差分钟数
        int dateDiff = TurnTimeStamp.dateDiff(startTime,endTime, Calendar.MINUTE);
        //如果在免费时间内，免费，如果不在，按次收费
        if(dateDiff<=free_time) resultFee = 0;
        else resultFee  = pay;

        return resultFee;
    }
    //按次计费规则计算
    @Override
    public  int CountTimesFee(TimesFeeModel timesFeeModel, Date startTime, Date endTime){
        int resultFee = 0 ;
        //获取收费规则
        int pay = timesFeeModel.getPay();// 按次收费金额
        int is_nature_day = timesFeeModel.getIsNatureDay();// 是否自然天：0 否（24小时）1 是（自然天24小时）
        int is_free = timesFeeModel.getIsFree();// 是否含有免费分钟：0 否 1 有（默认0） 是指这个免费分钟数算不算钱
        int free_time = timesFeeModel.getFreeTime();// 免费分钟
        int charge = timesFeeModel.getCharge();// 单次最高收费金额
        int period_charge = timesFeeModel.getPeriodCharge();// 周期内最高收费
        int is_head = timesFeeModel.getIsHead();// 计费后是否有首段：0 无 1 有
        int is_more =  timesFeeModel.getIsMore();// 计费周期内多次是否进出计费累计限额：0 否 1是
        if(is_nature_day == 0){//24小时
            int dateDiff = TurnTimeStamp.dateDiff(startTime, endTime, Calendar.HOUR);
            if(dateDiff<=24) resultFee = OneTimesFee(timesFeeModel,startTime,endTime);
            else {
                int Y = (int) Math.floor(dateDiff/24);
                resultFee = pay *(Y+1);
            }
        }else {//自然天
            java.util.Calendar calst = java.util.Calendar.getInstance();
            java.util.Calendar caled = java.util.Calendar.getInstance();

            calst.setTime(startTime);
            caled.setTime(endTime);
            int dayDiff = 0; //相差天数
            if(calst.get(Calendar.YEAR)==caled.get(Calendar.YEAR))//年份相同
            {//天数的相差就可作为自然天相差
                dayDiff = caled.get(Calendar.DAY_OF_YEAR)-calst.get(Calendar.DAY_OF_YEAR);
            }else{//跨年了
                //临时日历（与开始时间相同，因为要进行加法运算）
                java.util.Calendar calTemp = java.util.Calendar.getInstance();
                calTemp.setTime(startTime);

                //只比较年月日的方法
                LocalDate localDate1 = ZonedDateTime.ofInstant(calTemp.getTime().toInstant(), ZoneId.systemDefault()).toLocalDate();
                LocalDate localDate2 = ZonedDateTime.ofInstant(endTime.toInstant(), ZoneId.systemDefault()).toLocalDate();

                //如果年月日不同
                while(!localDate1.isEqual(localDate2)){
                    calTemp.add(Calendar.DAY_OF_MONTH,1);//日加一
                    //重新给起始的赋值
                    localDate1 = ZonedDateTime.ofInstant(calTemp.getTime().toInstant(), ZoneId.systemDefault()).toLocalDate();
                    dayDiff++;//计数器加一
                }

            }
            resultFee = pay *(dayDiff+1);


        }
        return resultFee;
    }

    //不跨周期分时段计费方法
    @Override
    public  int OneTimeQuantumFee(TimeQuantumFeeModel timeQuantumFeeModel, Date startTime, Date endTime){
        int resultFee = 0 ;
        java.util.Calendar calst = java.util.Calendar.getInstance();
        java.util.Calendar caled = java.util.Calendar.getInstance();

        calst.setTime(startTime);
        caled.setTime(endTime);
        //获取收费规则
        //	timeFirst;//首段计时时长：分钟
        int timeFirst = timeQuantumFeeModel.getTimeFirst();
        // int	timeCellFirst;//计时单位：比如以20分钟为一个单位进行计费
        int timeCellFirst = timeQuantumFeeModel.getTimeCellFirst();
        // int	timeAmountFirst;//计时金额（一个单位的计费金额量）
        int timeAmountFirst = timeQuantumFeeModel.getTimeAmountFirst();
        // int	timeSecond;//第二段计时时长：分钟
        int timeSecond = timeQuantumFeeModel.getTimeSecond();
        // int	timeCellSecond;//计时单位
        int timeCellSecond = timeQuantumFeeModel.getTimeCellSecond();
        // int	timeAmountSecond;//计时金额
        int timeAmountSecond = timeQuantumFeeModel.getTimeAmountSecond();
        // int	timeThird;//第三段计时时长
        int timeThird = timeQuantumFeeModel.getTimeThird();
        // int	timeCellThird;//计时单位
        int timeCellThird = timeQuantumFeeModel.getTimeCellThird();
        // int	timeAmountThird;//计时金额
        int timeAmountThird = timeQuantumFeeModel.getTimeAmountThird();
        // int	timeCellFinal;//计时单位
        int timeCellFinal = timeQuantumFeeModel.getTimeCellFinal();
        // int	timeAmountFinal;//计时金额
        int timeAmountFinal = timeQuantumFeeModel.getTimeAmountFinal();
        // int	isNatureDay;//是否自然天：0 否（24小时）1 是（自然天24小时）
        int isNatureDay = timeQuantumFeeModel.getIsNatureDay();
        // int	isFree;//是否含有免费分钟：0 否 1 有（默认0）
        int isFree = timeQuantumFeeModel.getIsFree();
        // int	freeTime;//免费分钟
        int freeTime = timeQuantumFeeModel.getFreeTime();
        // int	charge;//单次最高收费金额
        int charge = timeQuantumFeeModel.getCharge();
        // int	periodCharge;//周期内最高收费
        int periodCharge = timeQuantumFeeModel.getPeriodCharge();
        // int	isHead;//计费后是否有首段：0 无 1 有
        int isHead = timeQuantumFeeModel.getIsHead();
        // int	isMore;//计费周期内多次是否进出计费累计限额：0 否 1是
        int isMore = timeQuantumFeeModel.getIsMore();

        int firstfee = (timeFirst/timeCellFirst)*timeAmountFirst;
        int secondfee = 0;
        int thirdfee = 0;
        //如果有二段
        if(timeSecond!= 0 ){secondfee = (timeSecond/timeCellSecond)*timeAmountSecond;
            //如果有三段
            if(timeThird!= 0){ thirdfee =  (timeThird/timeCellThird)*timeAmountThird;}
        }



        //单次不跨周期的收费
        //算相差分钟数
        int dateDiff = TurnTimeStamp.dateDiff(startTime,endTime, Calendar.MINUTE);
        //如果在免费时间内，免费，如果不在，分时段收费
        if(dateDiff<=freeTime) resultFee = 0;
        else {
            if(isFree == 1) {//免费的时间不收费
                calst.add(Calendar.MINUTE,freeTime);
                dateDiff  = dateDiff - freeTime;
            }
            if(dateDiff<=timeFirst){ //if 时间<首段时间
                resultFee = (int) (Math.ceil(dateDiff/(double)timeCellFirst) * timeAmountFirst);
            }else{//如果存在二段且时间在一二段之间
                dateDiff = dateDiff - timeFirst;
                if(secondfee!=0 && (dateDiff<=timeSecond)){//剪掉之后小于第二段时间
                    resultFee = (int) (firstfee + Math.ceil((dateDiff)/(double)timeCellSecond) * timeAmountSecond);
                }else if(secondfee!=0) {//剩余时间大于第二段时间
                    dateDiff = dateDiff - timeSecond; //减去第二段时间
                    if(thirdfee!=0 &&(dateDiff<=timeThird)){//如果存在三段且在二三段之间
                        resultFee = (int) (firstfee + secondfee +Math.ceil(dateDiff/(double)timeCellThird)*timeAmountThird);
                    }else if(thirdfee!=0) {//剪完三段之后还有剩余时间
                        dateDiff =  dateDiff - timeThird;//减去第三段
                        resultFee = (int) (firstfee + secondfee +thirdfee + (Math.ceil(dateDiff/(double)timeCellFinal))*timeAmountFinal);
                    }else {//如果不存在三段
                        resultFee = (int) (firstfee + secondfee + Math.ceil(dateDiff/(double)timeCellFinal)* timeAmountFinal);
                    }
                }else {
                    //如果不存在二段
                    resultFee = (int) (firstfee + Math.ceil(dateDiff/(double)timeCellFinal)*timeAmountFinal);
                }
            }

        }
        //如果大于单次最高收费，按最高收费计算
        if(resultFee>charge) resultFee = charge;

        return resultFee;
    }

    //分时段一天收费
    @Override
    public  int DayTimeQuantumFee(TimeQuantumFeeModel timeQuantumFeeModel) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
        Date startDate = null;
        Date endDate = null;
        startDate = format.parse("2020-11-9,00:00:00");
        endDate = format.parse("2020-11-10,00:00:00");
        return OneTimeQuantumFee(timeQuantumFeeModel,startDate,endDate);
    }

    //分时段收费规则计算
    @Override
    public int CountTimeQuantumFee(TimeQuantumFeeModel timeQuantumFeeModel, Date startTime, Date endTime) throws ParseException {
        int resultFee = 0 ;
        java.util.Calendar calst = java.util.Calendar.getInstance();
        java.util.Calendar caled = java.util.Calendar.getInstance();

        calst.setTime(startTime);
        caled.setTime(endTime);

        //获取收费规则
        //	timeFirst;//首段计时时长：分钟
        int timeFirst = timeQuantumFeeModel.getTimeFirst();
        // int	timeCellFirst;//计时单位：比如以20分钟为一个单位进行计费
        int timeCellFirst = timeQuantumFeeModel.getTimeCellFirst();
        // int	timeAmountFirst;//计时金额（一个单位的计费金额量）
        int timeAmountFirst = timeQuantumFeeModel.getTimeAmountFirst();
        // int	timeSecond;//第二段计时时长：分钟
        int timeSecond = timeQuantumFeeModel.getTimeSecond();
        // int	timeCellSecond;//计时单位
        int timeCellSecond = timeQuantumFeeModel.getTimeCellSecond();
        // int	timeAmountSecond;//计时金额
        int timeAmountSecond = timeQuantumFeeModel.getTimeAmountSecond();
        // int	timeThird;//第三段计时时长
        int timeThird = timeQuantumFeeModel.getTimeThird();
        // int	timeCellThird;//计时单位
        int timeCellThird = timeQuantumFeeModel.getTimeCellThird();
        // int	timeAmountThird;//计时金额
        int timeAmountThird = timeQuantumFeeModel.getTimeAmountThird();
        // int	timeCellFinal;//计时单位
        int timeCellFinal = timeQuantumFeeModel.getTimeCellFinal();
        // int	timeAmountFinal;//计时金额
        int timeAmountFinal = timeQuantumFeeModel.getTimeAmountFinal();
        // int	isNatureDay;//是否自然天：0 否（24小时）1 是（自然天24小时）
        int isNatureDay = timeQuantumFeeModel.getIsNatureDay();
        // int	isFree;//是否含有免费分钟：0 否 1 有（默认0）
        int isFree = timeQuantumFeeModel.getIsFree();
        // int	freeTime;//免费分钟
        int freeTime = timeQuantumFeeModel.getFreeTime();
        // int	charge;//单次最高收费金额
        int charge = timeQuantumFeeModel.getCharge();
        // int	periodCharge;//周期内最高收费
        int periodCharge = timeQuantumFeeModel.getPeriodCharge();
        // int	isHead;//计费后是否有首段：0 无 1 有
        int isHead = timeQuantumFeeModel.getIsHead();
        // int	isMore;//计费周期内多次是否进出计费累计限额：0 否 1是
        int isMore = timeQuantumFeeModel.getIsMore();

        if(isNatureDay == 0){//24小时
            int dateDiff = TurnTimeStamp.dateDiff(startTime, endTime, Calendar.HOUR);
            //if <=24小时
            if(dateDiff<=24) resultFee = OneTimeQuantumFee(timeQuantumFeeModel,startTime,endTime);
            else { //if >24小时
                int Y = (int) Math.floor(dateDiff/24);
                calst.add(Calendar.DAY_OF_MONTH,Y);//将入场日期往后推Y天
                Date newdate = calst.getTime(); // 新的开始日期
                //费用 = 跨越天数 * 每天收费 + 剩余时间按时段收费（24小时内）
                resultFee = Y * (DayTimeQuantumFee(timeQuantumFeeModel)) + OneTimeQuantumFee(timeQuantumFeeModel,newdate,endTime);
            }
        }else {//自然天

            int dayDiff = 0; //相差天数
            if(calst.get(Calendar.YEAR)==caled.get(Calendar.YEAR))//年份相同
            {//天数的相差就可作为自然天相差
                dayDiff = caled.get(Calendar.DAY_OF_YEAR)-calst.get(Calendar.DAY_OF_YEAR);
            }else{//跨年了
                //设置一个临时日历
                java.util.Calendar calTemp = java.util.Calendar.getInstance();
                calTemp.setTime(startTime);

                //只比较年月日的方法
                LocalDate localDate1 = ZonedDateTime.ofInstant(calTemp.getTime().toInstant(), ZoneId.systemDefault()).toLocalDate();
                LocalDate localDate2 = ZonedDateTime.ofInstant(endTime.toInstant(), ZoneId.systemDefault()).toLocalDate();

                //如果年月日不同
                while(!localDate1.isEqual(localDate2)){
                    calTemp.add(Calendar.DAY_OF_MONTH,1);//日加一
                    //重新给起始的赋值
                    localDate1 = ZonedDateTime.ofInstant(calTemp.getTime().toInstant(), ZoneId.systemDefault()).toLocalDate();
                    dayDiff++;//计数器加一
                }
            }

            if(dayDiff==0){//未跨越自然天
                resultFee = OneTimeQuantumFee(timeQuantumFeeModel,startTime,endTime);
            }else {//跨越了自然天
                //设置起始时间的下一天
                Calendar newEndCal = Calendar.getInstance();
                newEndCal.set(
                        calst.get(Calendar.YEAR),calst.get(Calendar.MONTH),calst.get(Calendar.DAY_OF_MONTH)+1,0,0,0
                );
                //设置结束时间的当天
                Calendar newStartCal = Calendar.getInstance();
                newStartCal.set(
                        caled.get(Calendar.YEAR),caled.get(Calendar.MONTH),caled.get(Calendar.DAY_OF_MONTH),0,0,0
                );
                //费用组成：三部分
                //第一部分：入场当天的费用
                int firstfee = OneTimeQuantumFee(timeQuantumFeeModel,startTime,newEndCal.getTime());
                //第二部分：跨越的自然天的费用
                int secondfee = (dayDiff-1) * DayTimeQuantumFee(timeQuantumFeeModel);
                //第三部分：出场当天的费用
                int finalfee = OneTimeQuantumFee(timeQuantumFeeModel,newStartCal.getTime(),endTime);
                resultFee = firstfee + secondfee + finalfee;
            }

        }
        return resultFee;
    }


    @Override
    public int DayToDay(DayNightFeeModel dayNightFeeModel, Date startTime, Date endTime) {
        int resultFee = 0 ;

        // int	dayTimeFirst;//首段计时时长：分钟
        int dayTimeFirst = dayNightFeeModel.getDayTimeFirst();
        // int	dayTimeCellFirst;//首段计时单位：比如以20分钟为一个单位进行计费
        int dayTimeCellFirst = dayNightFeeModel.getDayTimeCellFirst();
        // int	dayTimeAmountFirst;//首段计时金额（一个单位的计费金额量）
        int dayTimeAmountFirst = dayNightFeeModel.getDayTimeAmountFirst();
        // int	dayTimeCellFinal;//白天二段计时单位
        int dayTimeCellFinal = dayNightFeeModel.getDayTimeCellFinal();
        // int	dayTimeAmountFinal;//白天二段计时金额
        int dayTimeAmountFinal = dayNightFeeModel.getDayTimeAmountFinal();

        // int	charge;//单次最高收费金额
        int charge = dayNightFeeModel.getCharge();


        //单次不跨周期的收费
        //算相差分钟数
        int dateDiff = TurnTimeStamp.dateDiff(startTime,endTime, Calendar.MINUTE);

            //如果出入场都是同一天白天\

                //小于首段
                if(dateDiff<=dayTimeFirst){resultFee = (int) (Math.ceil(dateDiff/(double)dayTimeCellFirst))*dayTimeAmountFirst;}
                else{//超出首段
                    resultFee = (int) (Math.ceil(dayTimeFirst/(double)dayTimeCellFirst)*dayTimeAmountFirst + Math.ceil((dateDiff-dayTimeFirst)/(double)dayTimeCellFinal)*dayTimeAmountFinal);
                }

        return resultFee;
    }

    @Override
    public int NightToNight(DayNightFeeModel dayNightFeeModel, Date startTime, Date endTime) {
        int resultFee = 0;

        // int	nightTimeFirst;//首段计时时长：分钟
        int nightTimeFirst = dayNightFeeModel.getNightTimeFirst();
        // int	nightTimeCellFirst;//首段计时单位：比如以20分钟为一个单位进行计费
        int nightTimeCellFirst = dayNightFeeModel.getNightTimeCellFirst();
        // int	nightTimeAmountFirst;//首段计时金额（一个单位的计费金额量）
        int nightTimeAmountFirst = dayNightFeeModel.getNightTimeAmountFirst();
        // int	nightTimeCellFinal;//夜间二段计时单位
        int nightTimeCellFinal = dayNightFeeModel.getNightTimeCellFinal();
        // int	nightTimeAmountFinal;//夜间二段计时金额
        int nightTimeAmountFinal = dayNightFeeModel.getNightTimeAmountFinal();
        // int	charge;//单次最高收费金额
        int charge = dayNightFeeModel.getCharge();


        //单次不跨周期的收费
        //算相差分钟数
        int dateDiff = TurnTimeStamp.dateDiff(startTime, endTime, Calendar.MINUTE);

            //如果出入场都是同一天黑夜

                //小于首段
                if (dateDiff <= nightTimeFirst) {
                    resultFee = (int) (Math.ceil(dateDiff / (double)nightTimeCellFirst)) * nightTimeAmountFirst;
                } else {//超出首段
                    resultFee = (int) (Math.ceil(nightTimeFirst /(double) nightTimeCellFirst) * nightTimeAmountFirst + Math.ceil((dateDiff - nightTimeFirst) / (double)nightTimeCellFinal) * nightTimeAmountFinal);
                }


    return  resultFee;
    }

    @Override
    public int DayToNight(DayNightFeeModel dayNightFeeModel, Date startTime, Date endTime) {
        int resultFee = 0 ;
        java.util.Calendar calst = java.util.Calendar.getInstance();
        java.util.Calendar caled = java.util.Calendar.getInstance();

        calst.setTime(startTime);
        caled.setTime(endTime);
        //获取收费规则

        // String	dayTimeBegin;//白天开始时间，如“06:30”
        String dayTimeBegin = dayNightFeeModel.getDayTimeBegin();
        String[] arr = dayTimeBegin.split(":");
        int daybeginhours =Integer.parseInt(arr[0]); //6
        int daybeginminutes = Integer.parseInt(arr[1]);  //30
        // String	dayTimeEnd;//白天结束时间，如“20:00”

        String dayTimeEnd = dayNightFeeModel.getDayTimeEnd();
        String[] arr1 = dayTimeEnd.split(":");
        int dayendhours =Integer.parseInt(arr1[0]);  //20
        int dayendminutes = Integer.parseInt(arr1[1]);  //0

        // int	dayTimeFirst;//首段计时时长：分钟
        int dayTimeFirst = dayNightFeeModel.getDayTimeFirst();
        // int	dayTimeCellFirst;//首段计时单位：比如以20分钟为一个单位进行计费
        int dayTimeCellFirst = dayNightFeeModel.getDayTimeCellFirst();
        // int	dayTimeAmountFirst;//首段计时金额（一个单位的计费金额量）
        int dayTimeAmountFirst = dayNightFeeModel.getDayTimeAmountFirst();
        // int	dayTimeCellFinal;//白天二段计时单位
        int dayTimeCellFinal = dayNightFeeModel.getDayTimeCellFinal();
        // int	dayTimeAmountFinal;//白天二段计时金额
        int dayTimeAmountFinal = dayNightFeeModel.getDayTimeAmountFinal();
        // String	nightTimeBegin;//夜间开始时间，如“18:00”
        String nightTimeBegin = dayNightFeeModel.getNightTimeBegin();
        // String	nightTimeEnd;//夜间结束时间，如“08:00”
        String nightTimeEnd = dayNightFeeModel.getNightTimeEnd();
        // int	nightTimeFirst;//首段计时时长：分钟
        int nightTimeFirst = dayNightFeeModel.getNightTimeFirst();
        // int	nightTimeCellFirst;//首段计时单位：比如以20分钟为一个单位进行计费
        int nightTimeCellFirst = dayNightFeeModel.getNightTimeCellFirst();
        // int	nightTimeAmountFirst;//首段计时金额（一个单位的计费金额量）
        int nightTimeAmountFirst = dayNightFeeModel.getNightTimeAmountFirst();
        // int	nightTimeCellFinal;//夜间二段计时单位
        int nightTimeCellFinal = dayNightFeeModel.getNightTimeCellFinal();
        // int	nightTimeAmountFinal;//夜间二段计时金额
        int nightTimeAmountFinal = dayNightFeeModel.getNightTimeAmountFinal();
        // int	charge;//单次最高收费金额
        int charge = dayNightFeeModel.getCharge();
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            Calendar cal3 = Calendar.getInstance();
            Calendar cal4 = Calendar.getInstance();
            Calendar cal5 = Calendar.getInstance();
            //6.30
            cal1.set(calst.get(Calendar.YEAR),calst.get(Calendar.MONTH),calst.get(Calendar.DAY_OF_MONTH),daybeginhours,daybeginminutes,0);
            //20.30
            cal2.set(calst.get(Calendar.YEAR),calst.get(Calendar.MONTH),calst.get(Calendar.DAY_OF_MONTH),dayendhours,dayendminutes,0);
            //第二天6.30
            cal3.set(calst.get(Calendar.YEAR),calst.get(Calendar.MONTH),calst.get(Calendar.DAY_OF_MONTH)+1,daybeginhours,daybeginminutes,0);
            //第二天20.30
            cal4.set(calst.get(Calendar.YEAR),calst.get(Calendar.MONTH),calst.get(Calendar.DAY_OF_MONTH)+1,dayendhours,dayendminutes,0);
            //第三天6.30
            cal5.set(calst.get(Calendar.YEAR),calst.get(Calendar.MONTH),calst.get(Calendar.DAY_OF_MONTH)+2,daybeginhours,daybeginminutes,0);


            //与界点的时间差
            int MinDiff = TurnTimeStamp.dateDiff(startTime,cal2.getTime(),Calendar.MINUTE);
            int delay = 0;//延迟时间
            if(MinDiff<=dayTimeFirst){//如果在白天一段内
                int i = (int) (Math.ceil(MinDiff/(double)dayTimeCellFirst)*dayTimeCellFirst);//往后推一个cell周期
                Calendar calnew = Calendar.getInstance();
                calnew.setTime(startTime);
                calnew.add(Calendar.MINUTE,i);
                //推迟周期后的时间和界点做差得到延迟时间
                delay = TurnTimeStamp.dateDiff(cal2.getTime(),calnew.getTime(),Calendar.MINUTE)+1;

            }else {
                //去掉一段按cellFinal延一个周期
                int i = (int) (Math.ceil((MinDiff-dayTimeFirst)/(double)dayTimeCellFinal)*dayTimeCellFinal);
                Calendar calnew = Calendar.getInstance();
                calnew.setTime(startTime);
                calnew.add(Calendar.MINUTE,i+dayTimeFirst);//得到延迟后的日历
                //做差得到延迟分钟数
                delay = TurnTimeStamp.dateDiff(cal2.getTime(),calnew.getTime(),Calendar.MINUTE)+1;
            }
            if (TurnTimeStamp.dateDiff(cal2.getTime(),endTime,Calendar.MINUTE)<=delay){
                //变成未跨的day to day 了
                resultFee = DayToDay(dayNightFeeModel,startTime,endTime);
            }else{
                //将计算的时间后移
                cal2.add(Calendar.MINUTE,delay);
                int dayfee = DayToDay(dayNightFeeModel,startTime,cal2.getTime());
                int nightfee = NightToNight(dayNightFeeModel,cal2.getTime(),endTime);
                resultFee = dayfee + nightfee;
            }


            //如果大于单次最高收费，按最高收费计算
            if(resultFee>charge) resultFee = charge;

            return resultFee;
        }


    @Override
    public int NightToDay(DayNightFeeModel dayNightFeeModel, Date startTime, Date endTime) {
        int resultFee = 0 ;
        java.util.Calendar calst = java.util.Calendar.getInstance();
        java.util.Calendar caled = java.util.Calendar.getInstance();

        calst.setTime(startTime);
        caled.setTime(endTime);
        //获取收费规则

        // String	dayTimeBegin;//白天开始时间，如“06:30”
        String dayTimeBegin = dayNightFeeModel.getDayTimeBegin();
        String[] arr = dayTimeBegin.split(":");
        int daybeginhours =Integer.parseInt(arr[0]); //6
        int daybeginminutes = Integer.parseInt(arr[1]);  //30
        // String	dayTimeEnd;//白天结束时间，如“20:00”

        String dayTimeEnd = dayNightFeeModel.getDayTimeEnd();
        String[] arr1 = dayTimeEnd.split(":");
        int dayendhours =Integer.parseInt(arr1[0]);  //20
        int dayendminutes = Integer.parseInt(arr1[1]);  //0

        // int	dayTimeFirst;//首段计时时长：分钟
        int dayTimeFirst = dayNightFeeModel.getDayTimeFirst();
        // int	dayTimeCellFirst;//首段计时单位：比如以20分钟为一个单位进行计费
        int dayTimeCellFirst = dayNightFeeModel.getDayTimeCellFirst();
        // int	dayTimeAmountFirst;//首段计时金额（一个单位的计费金额量）
        int dayTimeAmountFirst = dayNightFeeModel.getDayTimeAmountFirst();
        // int	dayTimeCellFinal;//白天二段计时单位
        int dayTimeCellFinal = dayNightFeeModel.getDayTimeCellFinal();
        // int	dayTimeAmountFinal;//白天二段计时金额
        int dayTimeAmountFinal = dayNightFeeModel.getDayTimeAmountFinal();
        // String	nightTimeBegin;//夜间开始时间，如“18:00”
        String nightTimeBegin = dayNightFeeModel.getNightTimeBegin();
        // String	nightTimeEnd;//夜间结束时间，如“08:00”
        String nightTimeEnd = dayNightFeeModel.getNightTimeEnd();
        // int	nightTimeFirst;//首段计时时长：分钟
        int nightTimeFirst = dayNightFeeModel.getNightTimeFirst();
        // int	nightTimeCellFirst;//首段计时单位：比如以20分钟为一个单位进行计费
        int nightTimeCellFirst = dayNightFeeModel.getNightTimeCellFirst();
        // int	nightTimeAmountFirst;//首段计时金额（一个单位的计费金额量）
        int nightTimeAmountFirst = dayNightFeeModel.getNightTimeAmountFirst();
        // int	nightTimeCellFinal;//夜间二段计时单位
        int nightTimeCellFinal = dayNightFeeModel.getNightTimeCellFinal();
        // int	nightTimeAmountFinal;//夜间二段计时金额
        int nightTimeAmountFinal = dayNightFeeModel.getNightTimeAmountFinal();
        // int	charge;//单次最高收费金额
        int charge = dayNightFeeModel.getCharge();

        Calendar cal0 = Calendar.getInstance();
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        Calendar cal3 = Calendar.getInstance();
        Calendar cal4 = Calendar.getInstance();
        Calendar cal5 = Calendar.getInstance();
        //0.00-6.30
        cal0.set(calst.get(Calendar.YEAR),calst.get(Calendar.MONTH),calst.get(Calendar.DAY_OF_MONTH),0,0,0);
        //6.30
        cal1.set(calst.get(Calendar.YEAR),calst.get(Calendar.MONTH),calst.get(Calendar.DAY_OF_MONTH),daybeginhours,daybeginminutes,0);
        //20.30
        cal2.set(calst.get(Calendar.YEAR),calst.get(Calendar.MONTH),calst.get(Calendar.DAY_OF_MONTH),dayendhours,dayendminutes,0);
        //第二天6.30
        cal3.set(calst.get(Calendar.YEAR),calst.get(Calendar.MONTH),calst.get(Calendar.DAY_OF_MONTH)+1,daybeginhours,daybeginminutes,0);
        //第二天20.30
        cal4.set(calst.get(Calendar.YEAR),calst.get(Calendar.MONTH),calst.get(Calendar.DAY_OF_MONTH)+1,dayendhours,dayendminutes,0);
        //第三天6.30
        cal5.set(calst.get(Calendar.YEAR),calst.get(Calendar.MONTH),calst.get(Calendar.DAY_OF_MONTH)+2,daybeginhours,daybeginminutes,0);

        if(calst.after(cal0) && calst.before(cal1)&&caled.after(cal1) && caled.before(cal2)){
            cal3=cal1;
        }
        //与界点的时间差
        int MinDiff = TurnTimeStamp.dateDiff(startTime,cal3.getTime(),Calendar.MINUTE);
        int delay = 0;//延迟时间
        if(MinDiff<=nightTimeFirst){//如果在夜间一段内
            int i = (int) (Math.ceil(MinDiff/(double)nightTimeCellFirst)*nightTimeCellFirst);//往后推一个cell周期
            Calendar calnew = Calendar.getInstance();
            calnew.setTime(startTime);
            calnew.add(Calendar.MINUTE,i);
            //推迟周期后的时间和界点做差得到延迟时间
            delay = TurnTimeStamp.dateDiff(cal3.getTime(),calnew.getTime(),Calendar.MINUTE)+1;

        }else {
            //去掉一段按cellFinal延一个周期
            int i = (int) (Math.ceil((MinDiff-nightTimeFirst)/(double)nightTimeCellFinal)*nightTimeCellFinal);
            Calendar calnew = Calendar.getInstance();
            calnew.setTime(startTime);
            calnew.add(Calendar.MINUTE,i+nightTimeFirst);//得到延迟后的日历
            //做差得到延迟分钟数
            delay = TurnTimeStamp.dateDiff(cal3.getTime(),calnew.getTime(),Calendar.MINUTE)+1;
        }
        //将计算的时间后移
        if (TurnTimeStamp.dateDiff(cal3.getTime(),endTime,Calendar.MINUTE)<=delay){
            //变成未跨的night to night 了
            resultFee = NightToNight(dayNightFeeModel,startTime,endTime);
        }else {
            cal3.add(Calendar.MINUTE,delay);

            int nightfee = NightToNight(dayNightFeeModel,startTime,cal3.getTime());
            int dayfee = DayToDay(dayNightFeeModel,cal3.getTime(),endTime);
            resultFee = dayfee + nightfee;
        }


        //如果大于单次最高收费，按最高收费计算
        if(resultFee>charge) resultFee = charge;

        return resultFee;

    }

    //白天夜间周期内单次计算
    @Override
    public int OneDayNightFee(DayNightFeeModel dayNightFeeModel, Date startTime, Date endTime) {
        int resultFee = 0 ;
        java.util.Calendar calst = java.util.Calendar.getInstance();
        java.util.Calendar caled = java.util.Calendar.getInstance();

        calst.setTime(startTime);
        caled.setTime(endTime);
        //获取收费规则

        // String	dayTimeBegin;//白天开始时间，如“06:30”
        String dayTimeBegin = dayNightFeeModel.getDayTimeBegin();
        String[] arr = dayTimeBegin.split(":");
        int daybeginhours =Integer.parseInt(arr[0]); //6
        int daybeginminutes = Integer.parseInt(arr[1]);  //30
        // String	dayTimeEnd;//白天结束时间，如“20:00”

        String dayTimeEnd = dayNightFeeModel.getDayTimeEnd();
        String[] arr1 = dayTimeEnd.split(":");
        int dayendhours =Integer.parseInt(arr1[0]);  //20
        int dayendminutes = Integer.parseInt(arr1[1]);  //0

        // int	dayTimeFirst;//首段计时时长：分钟
        int dayTimeFirst = dayNightFeeModel.getDayTimeFirst();
        // int	dayTimeCellFirst;//首段计时单位：比如以20分钟为一个单位进行计费
        int dayTimeCellFirst = dayNightFeeModel.getDayTimeCellFirst();
        // int	dayTimeAmountFirst;//首段计时金额（一个单位的计费金额量）
        int dayTimeAmountFirst = dayNightFeeModel.getDayTimeAmountFirst();
        // int	dayTimeCellFinal;//白天二段计时单位
        int dayTimeCellFinal = dayNightFeeModel.getDayTimeCellFinal();
        // int	dayTimeAmountFinal;//白天二段计时金额
        int dayTimeAmountFinal = dayNightFeeModel.getDayTimeAmountFinal();
        // String	nightTimeBegin;//夜间开始时间，如“18:00”
        String nightTimeBegin = dayNightFeeModel.getNightTimeBegin();
        // String	nightTimeEnd;//夜间结束时间，如“08:00”
        String nightTimeEnd = dayNightFeeModel.getNightTimeEnd();
        // int	nightTimeFirst;//首段计时时长：分钟
        int nightTimeFirst = dayNightFeeModel.getNightTimeFirst();
        // int	nightTimeCellFirst;//首段计时单位：比如以20分钟为一个单位进行计费
        int nightTimeCellFirst = dayNightFeeModel.getNightTimeCellFirst();
        // int	nightTimeAmountFirst;//首段计时金额（一个单位的计费金额量）
        int nightTimeAmountFirst = dayNightFeeModel.getNightTimeAmountFirst();
        // int	nightTimeCellFinal;//夜间二段计时单位
        int nightTimeCellFinal = dayNightFeeModel.getNightTimeCellFinal();
        // int	nightTimeAmountFinal;//夜间二段计时金额
        int nightTimeAmountFinal = dayNightFeeModel.getNightTimeAmountFinal();
        // int	isNatureDay;//是否自然天：0 否（24小时）1 是（自然天24小时）
        int isNatureDay = dayNightFeeModel.getIsNatureDay();
        // int	isFree;//是否含有免费分钟：0 否 1 有（默认0）
        int isFree = dayNightFeeModel.getIsFree();
        // int	freeTime;//免费分钟
        int freeTime = dayNightFeeModel.getFreeTime();
        // int	charge;//单次最高收费金额
        int charge = dayNightFeeModel.getCharge();
        // int	periodCharge;//周期内最高收费
        int periodCharge = dayNightFeeModel.getPeriodCharge();
        // int	isHead;//计费后是否有首段：0 无 1 有
        int isHead = dayNightFeeModel.getIsHead();
        // int	isMore;//计费周期内多次是否进出计费累计限额：0 否 1是
        int isMore = dayNightFeeModel.getIsMore();

        //单次不跨周期的收费
        //算相差分钟数
        int dateDiff = TurnTimeStamp.dateDiff(startTime,endTime, Calendar.MINUTE);
        //如果在免费时间内，免费，如果不在，分时段收费
        if(dateDiff<=freeTime) resultFee = 0;
        else {//不免费
            if(isFree == 1) {//免费的时间不收费
                calst.add(Calendar.MINUTE,freeTime);
                dateDiff  = dateDiff - freeTime;
            }
            Calendar cal0 = Calendar.getInstance();
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            Calendar cal3 = Calendar.getInstance();
            Calendar cal4 = Calendar.getInstance();
            Calendar cal5 = Calendar.getInstance();
            //0.00-6.30
            cal0.set(calst.get(Calendar.YEAR),calst.get(Calendar.MONTH),calst.get(Calendar.DAY_OF_MONTH),0,0,0);
            //6.30
            cal1.set(calst.get(Calendar.YEAR),calst.get(Calendar.MONTH),calst.get(Calendar.DAY_OF_MONTH),daybeginhours,daybeginminutes,0);
            //20.30
            cal2.set(calst.get(Calendar.YEAR),calst.get(Calendar.MONTH),calst.get(Calendar.DAY_OF_MONTH),dayendhours,dayendminutes,0);
            //第二天6.30
            cal3.set(calst.get(Calendar.YEAR),calst.get(Calendar.MONTH),calst.get(Calendar.DAY_OF_MONTH)+1,daybeginhours,daybeginminutes,0);
            //第二天20.30
            cal4.set(calst.get(Calendar.YEAR),calst.get(Calendar.MONTH),calst.get(Calendar.DAY_OF_MONTH)+1,dayendhours,dayendminutes,0);
            //第三天6.30
            cal5.set(calst.get(Calendar.YEAR),calst.get(Calendar.MONTH),calst.get(Calendar.DAY_OF_MONTH)+2,daybeginhours,daybeginminutes,0);


            //如果出入场都是同一天白天\
            if((calst.after(cal1) && calst.before(cal2))&&(caled.after(cal1) && caled.before(cal2))){

               resultFee = DayToDay(dayNightFeeModel,startTime,endTime);

                }

            //如果出入场都是同一天黑夜
            if((calst.after(cal2) && calst.before(cal3))&&(caled.after(cal2) && caled.before(cal3))){
               resultFee = NightToNight(dayNightFeeModel,startTime,endTime);
            }
            //白跨黑
            if((calst.after(cal1) && calst.before(cal2))&&(caled.after(cal2) && caled.before(cal3))){
                resultFee = DayToNight(dayNightFeeModel,startTime,endTime);
            }
            //黑跨白
            if(((calst.after(cal2) && calst.before(cal3))&&(caled.after(cal3) && caled.before(cal4)))||((calst.after(cal0) && calst.before(cal1))&&(caled.after(cal1) && caled.before(cal2)))){

                resultFee = NightToDay(dayNightFeeModel,startTime,endTime);
            }
            //白跨黑跨白
            if((calst.after(cal1) && calst.before(cal2))&&(caled.after(cal3) && caled.before(cal4))){

                //与界点的时间差
                int MinDiff = TurnTimeStamp.dateDiff(startTime,cal2.getTime(),Calendar.MINUTE);
                int delay = 0;//延迟时间
                if(MinDiff<=dayTimeFirst){//如果在白天一段内
                    int i = (int) (Math.ceil(MinDiff/(double)dayTimeCellFirst)*dayTimeCellFirst);//往后推一个cell周期
                    Calendar calnew = Calendar.getInstance();
                    calnew.setTime(startTime);
                    calnew.add(Calendar.MINUTE,i);
                    //推迟周期后的时间和界点做差得到延迟时间
                    delay = TurnTimeStamp.dateDiff(cal2.getTime(),calnew.getTime(),Calendar.MINUTE)+1;

                }else {
                    //去掉一段按cellFinal延一个周期
                    int i = (int) (Math.ceil((MinDiff-dayTimeFirst)/(double)dayTimeCellFinal)*dayTimeCellFinal);
                    Calendar calnew = Calendar.getInstance();
                    calnew.setTime(startTime);
                    calnew.add(Calendar.MINUTE,i+dayTimeFirst);//得到延迟后的日历
                    //做差得到延迟分钟数
                    delay = TurnTimeStamp.dateDiff(cal2.getTime(),calnew.getTime(),Calendar.MINUTE)+1;
                }
                if (TurnTimeStamp.dateDiff(cal3.getTime(),endTime,Calendar.MINUTE)<=delay){
                    //变成未跨的day to night 了
                    resultFee = DayToNight(dayNightFeeModel,startTime,endTime);
                }else {

                    //将计算的时间后移
                    cal2.add(Calendar.MINUTE, delay);
                    cal3.add(Calendar.MINUTE, delay);

                    int nightfee = NightToNight(dayNightFeeModel, cal2.getTime(), cal3.getTime());
                    int dayfee1 = DayToDay(dayNightFeeModel, startTime, cal2.getTime());
                    int dayfee2 = DayToDay(dayNightFeeModel, cal3.getTime(), endTime);
                    resultFee = dayfee1 + dayfee2 + nightfee;
                }

            }
            //黑跨白跨黑
            if(((calst.after(cal2) && calst.before(cal3))&&(caled.after(cal4) && caled.before(cal5)))||((calst.after(cal0) && calst.before(cal1))&&(caled.after(cal2) && caled.before(cal3)))){

                if(calst.after(cal0) && calst.before(cal1)&&caled.after(cal2) && caled.before(cal3)){
                    cal3 = cal1;
                    cal4 = cal2;
                }
                //与界点的时间差
                int MinDiff = TurnTimeStamp.dateDiff(startTime,cal3.getTime(),Calendar.MINUTE);
                int delay = 0;//延迟时间
                if(MinDiff<=nightTimeFirst){//如果在夜间一段内
                    int i = (int) (Math.ceil(MinDiff/(double)nightTimeCellFirst)*nightTimeCellFirst);//往后推一个cell周期
                    Calendar calnew = Calendar.getInstance();
                    calnew.setTime(startTime);
                    calnew.add(Calendar.MINUTE,i);
                    //推迟周期后的时间和界点做差得到延迟时间
                    delay = TurnTimeStamp.dateDiff(cal3.getTime(),calnew.getTime(),Calendar.MINUTE)+1;

                }else {
                    //去掉一段按cellFinal延一个周期
                    int i = (int) (Math.ceil((MinDiff-nightTimeFirst)/(double)nightTimeCellFinal)*nightTimeCellFinal);
                    Calendar calnew = Calendar.getInstance();
                    calnew.setTime(startTime);
                    calnew.add(Calendar.MINUTE,i+nightTimeFirst);//得到延迟后的日历
                    //做差得到延迟分钟数
                    delay = TurnTimeStamp.dateDiff(cal3.getTime(),calnew.getTime(),Calendar.MINUTE)+1;
                }
                if (TurnTimeStamp.dateDiff(cal4.getTime(),endTime,Calendar.MINUTE)<=delay){
                    //变成未跨的night to day 了
                    resultFee = NightToDay(dayNightFeeModel,startTime,endTime);
                }else {

                    //将计算的时间后移
                    cal3.add(Calendar.MINUTE, delay);
                    cal4.add(Calendar.MINUTE, delay);

                    int nightfee1 = NightToNight(dayNightFeeModel, startTime, cal3.getTime());
                    int nightfee2 = NightToNight(dayNightFeeModel, cal4.getTime(), endTime);
                    int dayfee = DayToDay(dayNightFeeModel, cal3.getTime(), cal4.getTime());
                    resultFee = dayfee + nightfee1 + nightfee2;
                }
            }

        }
        if(resultFee>charge) resultFee = charge;
        return resultFee;
    }
    //白天夜间跨周期计算
    @Override
    public int CountDayNightFee(DayNightFeeModel dayNightFeeModel, Date startTime, Date endTime) throws ParseException {
        int resultFee = 0;
        java.util.Calendar calst = java.util.Calendar.getInstance();
        java.util.Calendar caled = java.util.Calendar.getInstance();

        calst.setTime(startTime);
        caled.setTime(endTime);
        //获取收费规则

        // String	dayTimeBegin;//白天开始时间，如“06:30”
        String dayTimeBegin = dayNightFeeModel.getDayTimeBegin();
        String[] arr = dayTimeBegin.split(":");
        int daybeginhours = Integer.parseInt(arr[0]); //6
        int daybeginminutes = Integer.parseInt(arr[1]);  //30
        // String	dayTimeEnd;//白天结束时间，如“20:00”
        String dayTimeEnd = dayNightFeeModel.getDayTimeEnd();
        String[] arr1 = dayTimeEnd.split(":");
        int dayendhours = Integer.parseInt(arr1[0]);  //20
        int dayendminutes = Integer.parseInt(arr1[1]);  //0
        // int	dayTimeFirst;//首段计时时长：分钟
        int dayTimeFirst = dayNightFeeModel.getDayTimeFirst();
        // int	dayTimeCellFirst;//首段计时单位：比如以20分钟为一个单位进行计费
        int dayTimeCellFirst = dayNightFeeModel.getDayTimeCellFirst();
        // int	dayTimeAmountFirst;//首段计时金额（一个单位的计费金额量）
        int dayTimeAmountFirst = dayNightFeeModel.getDayTimeAmountFirst();
        // int	dayTimeCellFinal;//白天二段计时单位
        int dayTimeCellFinal = dayNightFeeModel.getDayTimeCellFinal();
        // int	dayTimeAmountFinal;//白天二段计时金额
        int dayTimeAmountFinal = dayNightFeeModel.getDayTimeAmountFinal();
        // String	nightTimeBegin;//夜间开始时间，如“18:00”
        String nightTimeBegin = dayNightFeeModel.getNightTimeBegin();
        // String	nightTimeEnd;//夜间结束时间，如“08:00”
        String nightTimeEnd = dayNightFeeModel.getNightTimeEnd();
        // int	nightTimeFirst;//首段计时时长：分钟
        int nightTimeFirst = dayNightFeeModel.getNightTimeFirst();
        // int	nightTimeCellFirst;//首段计时单位：比如以20分钟为一个单位进行计费
        int nightTimeCellFirst = dayNightFeeModel.getNightTimeCellFirst();
        // int	nightTimeAmountFirst;//首段计时金额（一个单位的计费金额量）
        int nightTimeAmountFirst = dayNightFeeModel.getNightTimeAmountFirst();
        // int	nightTimeCellFinal;//夜间二段计时单位
        int nightTimeCellFinal = dayNightFeeModel.getNightTimeCellFinal();
        // int	nightTimeAmountFinal;//夜间二段计时金额
        int nightTimeAmountFinal = dayNightFeeModel.getNightTimeAmountFinal();
        // int	isNatureDay;//是否自然天：0 否（24小时）1 是（自然天24小时）
        int isNatureDay = dayNightFeeModel.getIsNatureDay();
        // int	isFree;//是否含有免费分钟：0 否 1 有（默认0）
        int isFree = dayNightFeeModel.getIsFree();
        // int	freeTime;//免费分钟
        int freeTime = dayNightFeeModel.getFreeTime();
        // int	charge;//单次最高收费金额
        int charge = dayNightFeeModel.getCharge();
        // int	periodCharge;//周期内最高收费
        int periodCharge = dayNightFeeModel.getPeriodCharge();
        // int	isHead;//计费后是否有首段：0 无 1 有
        int isHead = dayNightFeeModel.getIsHead();
        // int	isMore;//计费周期内多次是否进出计费累计限额：0 否 1是
        int isMore = dayNightFeeModel.getIsMore();


        int dateDiff = TurnTimeStamp.dateDiff(startTime, endTime, Calendar.HOUR);
        //if <=24小时
        if (dateDiff <= 24) resultFee = OneDayNightFee(dayNightFeeModel, startTime, endTime);
        else { //if >24小时
            int Y = (int) Math.floor(dateDiff / 24);
            calst.add(Calendar.DAY_OF_MONTH, Y);//将入场日期往后推Y天
            Date newdate = calst.getTime(); // 新的开始日期
            //费用 = 跨越天数 * 每天收费 + 剩余时间按时段收费（24小时内）
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            Calendar cal3 = Calendar.getInstance();
            //6.30
            cal1.set(calst.get(Calendar.YEAR), Calendar.MONTH, Calendar.DAY_OF_MONTH, daybeginhours, daybeginminutes, 0);
            //20.30
            cal2.set(calst.get(Calendar.YEAR), Calendar.MONTH, Calendar.DAY_OF_MONTH, dayendhours, dayendminutes, 0);
            //第二天6.30
            cal3.set(calst.get(Calendar.YEAR), Calendar.MONTH, Calendar.DAY_OF_MONTH + 1, daybeginhours, daybeginminutes, 0);

            int dayfee = DayToDay(dayNightFeeModel, cal1.getTime(), cal2.getTime());
            int nightfee = NightToNight(dayNightFeeModel, cal2.getTime(), cal3.getTime());
            int oneDayFee = 0;
            if(dayfee + nightfee <= charge) oneDayFee = dayfee + nightfee;
            else oneDayFee = charge;
            resultFee = Y * (oneDayFee) + OneDayNightFee(dayNightFeeModel, newdate, endTime);
        }
        return resultFee;
    }
    //测试收费规则
    @Override
    public Object testTimesFee(String data) throws ParseException {
        //解析数据
        JSONObject jsonObject = new JSONObject(data);

        int typeId = jsonObject.getInt("PayCarTypeId");//收费车型Id

        //解析token
        //String token = jsonObject.getStr("token");
        //String token = "";
        //获取收费车型对应的对象
        PayCarTypeModel payCarTypeModel = payCarTypeMapper.selectByPayCarTypeId(typeId);
        //获取收费方式类型
        int chargeType = payCarTypeModel.getChargeType();
        //获取具体id
        int feeId = payCarTypeModel.getChargeId();

        //从前端获取数据
        JSONObject object1 = jsonObject.getJSONObject("stus");
        // 获取车牌号
        String carId = object1.getStr("carId");
        //获取入场时间
        String beginTime = object1.getStr("beginTime");
        //转成timestamp
        //Timestamp timestampBegin = TurnTimeStamp.TurnStamp(beginTime);
        Timestamp timestampBegin = Timestamp.valueOf(beginTime);
        //转成Date
        Date startDate = TurnTimeStamp.StringTurnDate(beginTime);
        //获取出场时间
        String endTime = object1.getStr("endTime");
        //Timestamp timestampEnd = TurnTimeStamp.TurnStamp(endTime);
        Timestamp timestampEnd = Timestamp.valueOf(endTime);
        Date endDate = TurnTimeStamp.StringTurnDate(endTime);
        //计算停车时间
        Map<String, Integer> map = (HashMap<String, Integer>) countTime(timestampBegin,timestampEnd);
        //hours,minutes,second
        String returnTime = map.get("hours")+"小时"+map.get("minutes")+"分钟"+map.get("second");
        int returnfee = 0;
        if(chargeType ==1){
            TimesFeeModel timesFeeModel = timesFeeMapper.selectById(feeId);
            returnfee = CountTimesFee(timesFeeModel,startDate,endDate);
        }
        if(chargeType ==2){
            TimeQuantumFeeModel timeQuantumFeeModel = timeQuantumFeeMapper.selectById(feeId);
            returnfee = CountTimeQuantumFee(timeQuantumFeeModel,startDate,endDate);
        }
        if(chargeType ==3){
            DayNightFeeModel dayNightFeeModel = dayNightFeeMapper.selectById(feeId);
            returnfee = CountDayNightFee(dayNightFeeModel,startDate,endDate);
        }


        return MsgResponse.ok().data("parkTime",returnTime).data("fee",returnfee);


    }


}
