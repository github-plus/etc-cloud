package etc.cloud.park.service;

import etc.cloud.commons.tool.TurnTimeStamp;
import etc.cloud.park.mode.DayNightFeeModel;
import etc.cloud.park.mode.TimeQuantumFeeModel;
import etc.cloud.park.mode.TimesFeeModel;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public interface TempcarRulesService {
    //临时车收费规则设置的接口
    //查询按次收费
    Object inqueryTimesFee(String data);
    //查询分时段收费
    Object inqueryTimeQuantumFee( String data);
    //查询白天夜间收费
    Object inqueryDayNightFee( String data);
    //新增按次收费
    Object addTimesFee(String data,String token);
    //新增分时段收费
    Object addTimeQuantumFee( String data,String token);
    //新增白天夜间收费
    Object addDayNightFee( String data,String token);
    //测试收费
    Object testTimesFee(String data) throws ParseException;
    Map<String, Integer> countTime(Timestamp startTime, Timestamp endTime);
    //不跨周期按次计费方法
    int OneTimesFee(TimesFeeModel timesFeeModel, Date startTime, Date endTime);
    //按次计费规则计算
    int CountTimesFee(TimesFeeModel timesFeeModel, Date startTime, Date endTime);

    //不跨周期分时段计费方法
    int OneTimeQuantumFee(TimeQuantumFeeModel timeQuantumFeeModel, Date startTime, Date endTime);

    //分时段一天收费
    int DayTimeQuantumFee(TimeQuantumFeeModel timeQuantumFeeModel) throws ParseException ;
    //分时段收费规则计算
    int CountTimeQuantumFee(TimeQuantumFeeModel timeQuantumFeeModel, Date startTime, Date endTime) throws ParseException ;
    //白天到白天
    int DayToDay(DayNightFeeModel dayNightFeeModel, Date startTime, Date endTime);
    //夜间到夜间
    int NightToNight(DayNightFeeModel dayNightFeeModel, Date startTime, Date endTime);
    //白天到夜间
    int DayToNight(DayNightFeeModel dayNightFeeModel, Date startTime, Date endTime);
    //夜间到白天
    int NightToDay(DayNightFeeModel dayNightFeeModel, Date startTime, Date endTime);
    //不跨周期白天夜间计费方法
    int OneDayNightFee(DayNightFeeModel dayNightFeeModel, Date startTime, Date endTime);
    //白天夜间收费规则计算
    int CountDayNightFee(DayNightFeeModel dayNightFeeModel, Date startTime, Date endTime) throws ParseException ;

}
