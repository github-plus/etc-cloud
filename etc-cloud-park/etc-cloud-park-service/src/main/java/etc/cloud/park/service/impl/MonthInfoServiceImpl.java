package etc.cloud.park.service.impl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.JsonObject;
import etc.cloud.auth.mapper.LoginFilterMapper;
import etc.cloud.auth.mode.TokenModel;
import etc.cloud.auth.vo.WorkerVO;
import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.commons.tool.OrderNumber;
import etc.cloud.commons.tool.TimestampCount;
import etc.cloud.commons.tool.TurnTimeStamp;
import etc.cloud.park.dto.MonthInfoDTO;
import etc.cloud.park.dto.RefundDTO;
import etc.cloud.park.dto.ShareInfoDto;
import etc.cloud.park.mapper.*;
import etc.cloud.park.mode.*;
import etc.cloud.park.mode.select.MonthSelect;
import etc.cloud.park.mode.vo.MonthOwnerVO;
import etc.cloud.park.mode.vo.MonthPayVO;
import etc.cloud.park.service.LogService;
import etc.cloud.park.service.MonthInfoService;
import org.bouncycastle.util.Times;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MonthInfoServiceImpl implements MonthInfoService {
    @Autowired
    private MonthInfoMapper monthInfoMapper;

    @Autowired
    private CarOwnerMapper carOwnerMapper;

    @Autowired
    private ShareInfoMapper shareInfoMapper;

    @Autowired
    private OrderNumberMapper orderNumberMapper;

    @Autowired
    private DelayPostponeMapper delayPostponeMapper;

    @Autowired
    private MonthPayMapper monthPayMapper;

    @Autowired
    private LoginFilterMapper loginFilterMapper;

    @Autowired
    private LogService logService;

    //月租车显示（按条件，同时传出所有月租车数据，包括用户信息，用于更新月租车信息的提取
    @Override
    public Object SelectWrapper(String data)
    {
        JSONObject js=new JSONObject(data);

        Integer current1 = js.getInt("current");
        Integer limit1 = js.getInt("limit");
        String token = js.getStr("token");//未完成

        Integer currentSe=(current1-1)*limit1;

        //获取月租车筛选条件
        JSONObject jsonObject = js.getJSONObject("query");

        Integer monthStandard = jsonObject.getInt("monthStandard");
        Integer state = jsonObject.getInt("state");
        Integer keyState = jsonObject.getInt("keyState");
        String room = jsonObject.getStr("room");
        String carNumber = jsonObject.getStr("carNumber");
        String userName = jsonObject.getStr("userName");
        String tel = jsonObject.getStr("tel");
        String carPort = jsonObject.getStr("carport");
        String createStart = jsonObject.getStr("createStart");
        String createEnd = jsonObject.getStr("createEnd");


        //根据条件检索数据库信息
        List<MonthOwnerVO> monthOwnerVOS=monthInfoMapper.getMonthSelect(currentSe,limit1,monthStandard,state,keyState,room,
                carNumber,userName,carPort,tel,TurnTimeStamp.TurnStamp(createStart),TurnTimeStamp.TurnStampEnd(createEnd));

        Integer total=monthInfoMapper.SqlTotal();

        List<MonthInfoDTO> monthInfoDTOS=new ArrayList<>();

        for(Integer i=0;i<monthOwnerVOS.size();i++)
        {
            MonthInfoDTO monthInfoDTO=new MonthInfoDTO();

            monthInfoDTO.setMonthId(monthOwnerVOS.get(i).getPkMonthId());
            monthInfoDTO.setCarNumber(monthOwnerVOS.get(i).getCarNumber());
            monthInfoDTO.setMonthStandard(monthOwnerVOS.get(i).getMonthStandard().toString()+"元/月");

            switch (monthOwnerVOS.get(i).getKeyState())
            {
                case 0:
                    monthInfoDTO.setKeyState("已开启");
                    break;
                case 1:
                    monthInfoDTO.setKeyState("未开启");
                    break;
            }
            switch (monthOwnerVOS.get(i).getState())
            {
                case 0:
                    monthInfoDTO.setState("正常使用");
                    break;
                case 1:
                    monthInfoDTO.setState("已过期");
                    break;
                case 2:
                    monthInfoDTO.setState("已注销");
                    break;
            }
            switch (monthOwnerVOS.get(i).getCarType())
            {
                case 0:
                    monthInfoDTO.setCarType("轿车");
                    break;
                case 1:
                    monthInfoDTO.setCarType("货车");
                    break;
                case 2:
                    monthInfoDTO.setCarType("客车");
                    break;
                case 3:
                    monthInfoDTO.setCarType("挂车");
                    break;
                case 4:
                    monthInfoDTO.setCarType("摩托车");
                    break;
            }
            switch (monthOwnerVOS.get(i).getIdentityType())
            {
                case 0:
                    monthInfoDTO.setIdentityType("中华人民共和国居民身份证");
                    break;
            }
            switch (monthOwnerVOS.get(i).getCarNumberType())
            {
                case 0:
                    monthInfoDTO.setCarNumberType("黄牌");
                    break;
                case 1:
                    monthInfoDTO.setCarNumberType("蓝牌");
                    break;
                case 2:
                    monthInfoDTO.setCarNumberType("绿牌");
                    break;
                case 3:
                    monthInfoDTO.setCarNumberType("白牌");
                    break;
            }

            monthInfoDTO.setCreateTime(TurnTimeStamp.TurnString(monthOwnerVOS.get(i).getCreateTime()));
            monthInfoDTO.setValidityStart(TurnTimeStamp.TurnString(monthOwnerVOS.get(i).getValidityStart()));
            monthInfoDTO.setValidityEnd(TurnTimeStamp.TurnString(monthOwnerVOS.get(i).getValidityEnd()));
            monthInfoDTO.setNote(monthOwnerVOS.get(i).getNote());
            monthInfoDTO.setRoom(monthOwnerVOS.get(i).getRoom());
            monthInfoDTO.setUserName(monthOwnerVOS.get(i).getUserName());
            monthInfoDTO.setTel(monthOwnerVOS.get(i).getTel());
            monthInfoDTO.setCarport(monthOwnerVOS.get(i).getCarport());
            monthInfoDTO.setMonthId(monthOwnerVOS.get(i).getPkMonthId());

            monthInfoDTO.setIdentity(monthOwnerVOS.get(i).getIdentity());
            monthInfoDTO.setPledge(monthOwnerVOS.get(i).getPledge());

            monthInfoDTOS.add(monthInfoDTO);
        }

        // 下拉框
        List<Integer> mapperMonthStandard = monthInfoMapper.getMonthStandard();


        //返回前端信息
        return MsgResponse.ok()
                .data("total",total)
                .data("rows",monthInfoDTOS)
                .data("monthStandardList",mapperMonthStandard);
    }


    //月租车信息添加请求
    @Override
    public Object AddRequire(String data)
    {
        JSONObject js=new JSONObject(data);
        String token = js.getStr("token");

        List<Integer> monthStandard = monthInfoMapper.getMonthStandard();

        return  MsgResponse.ok().data("payFee",monthStandard);
    }

    //添加月租车信息
    @Override
    public Object AddMonthInfo(String data,String token) {
        JSONObject js = new JSONObject(data);

        //获取月租车筛选条件
        JSONObject jsonObject = js.getJSONObject("query");

        Integer monthStandard = jsonObject.getInt("monthStandard");
        String carNumber = jsonObject.getStr("carNumber");
        String room = jsonObject.getStr("room");
        String userName = jsonObject.getStr("userName");
        String carPort = jsonObject.getStr("carPort");//车位需要修改为String
        String tel = jsonObject.getStr("tel");
        Integer carNumberType = jsonObject.getInt("carNumberType");
        Integer identityType = jsonObject.getInt("identityType");
        String identity = jsonObject.getStr("identity");
        Integer carType = jsonObject.getInt("carType");
        Integer pledge = jsonObject.getInt("pledge");
        String validityStart = jsonObject.getStr("validityStart");
        String validityEnd = jsonObject.getStr("validityEnd");
        String note = jsonObject.getStr("note");

        //数据库中如果存在该车辆同时为使用中或者过期，则返回提示：有该车辆，过期的需要去延期缴费处更改
        Integer integer = monthInfoMapper.countMonthInfo(carNumber);
        if (integer > 0) {
            return MsgResponse.error().msg("已存在该车辆");
        }

        //查找到当前操作员（根据token）
        WorkerVO findaccount = loginFilterMapper.findaccount(token);
        String account = findaccount.getAccount();
        String workerName = findaccount.getWorkerName();

        //转换为timestamp以及提取当前时间
        Timestamp turnStart = TurnTimeStamp.TurnStamp(validityStart);
        Timestamp turnEnd = TurnTimeStamp.TurnStampEnd(validityEnd);
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        //如果前端给出的数据不足一个月进行提示
        Integer month = TimestampCount.Month(turnStart, turnEnd);
        if (month <= 0) {
            return MsgResponse.error().msg("天数不足一个月");
        }

        //该属性为判断数据库中是否有该车牌号，如果为注销车辆则更新，如果没有的话则新增
        Integer isCancel = monthInfoMapper.countMonthCancel(carNumber);

        //根据身份证号判断是否有对应的用户信息
        Integer userId = carOwnerMapper.getUserId(identity);
        Integer pkUserId = 0;
        //车主实体类,车辆编号有问题
        CarOwnerModel carOwnerModel = new CarOwnerModel();
        carOwnerModel.setUserName(userName);
        carOwnerModel.setIdentity(identity);
        carOwnerModel.setIdentityType(identityType);
        if (!StringUtils.isEmpty(tel))
            carOwnerModel.setTel(tel);
        Integer InUpFlag=0;
        //如果没有用户信息的话
        if (userId == null) {
            if (isCancel.equals(0)) {
                InUpFlag = carOwnerMapper.insert(carOwnerModel);
                pkUserId = carOwnerModel.getPkUserId();
            }
            //判断是否插入用户信息成功
            if (InUpFlag == 0) {
                return MsgResponse.error().msg("无法生成户主或更新注销用户信息");
            }
        }
        else {//有用户信息则
            pkUserId = userId;
        }

        if (pkUserId.equals(0)) {
            return MsgResponse.error().msg("没有获得户主编号");
        }

        //月租车实体
        MonthInfoModel monthInfoModel = new MonthInfoModel();
        monthInfoModel.setCarNumber(carNumber);
        monthInfoModel.setCarNumberType(carNumberType);
        monthInfoModel.setCarport(carPort);
        monthInfoModel.setCarType(carType);
        monthInfoModel.setNote(note);
        monthInfoModel.setMontStandard(monthStandard);
        monthInfoModel.setPledge(pledge);
        monthInfoModel.setOwnerId(pkUserId);
        monthInfoModel.setValidityStart(turnStart);
        monthInfoModel.setValidityEnd(turnEnd);
        monthInfoModel.setRoom(room);

        //创建月租车编号字段
        Integer pkMonthId=0;
        //如果数据库中没有该信息则新增月租车
        if(isCancel.equals(0)) {
            int insert1 = monthInfoMapper.insert(monthInfoModel);
            pkMonthId = monthInfoModel.getPkMonthId();

            if (insert1 == 0) {
                return MsgResponse.error().msg("无法生成月租车");
            }
        }
        else {
            //如果小于当前时间则更改状态为使用状态，如果大于当前时间则不更改使用状态
            if (turnStart.getTime() < currentTime.getTime()) {
                monthInfoModel.setState(0);
            }
            pkMonthId = monthInfoMapper.getMonthInfoId(carNumber);
            monthInfoModel.setPkMonthId(pkMonthId);
            Integer update = monthInfoMapper.updateById(monthInfoModel);
            if (update.equals(0)) {
                return MsgResponse.error().msg("月租车注销状态更改失败");
            }
        }

        //生成订单编号
        String orderNumberId = OrderNumber.CreateOrderNumber();

        //获取总金额
        Integer money = month * monthStandard + pledge;

        OrderNumberModel orderNumberModel = new OrderNumberModel();
        orderNumberModel.setMoney(money);
        orderNumberModel.setRealMoney(money);
        orderNumberModel.setPkOrderNumber(orderNumberId);
        orderNumberModel.setAccountState(0);
        orderNumberModel.setCarType(0);
        orderNumberModel.setPayState(1);
        orderNumberModel.setAccountState(0);
        orderNumberModel.setDerateMoney(0);
        orderNumberModel.setIsOnline(0);
        orderNumberModel.setIsFree(0);

        int insert2 = orderNumberMapper.insert(orderNumberModel);

        if (insert2 == 0) {
            return MsgResponse.error().msg("无法生成订单编号");
        }

        MonthPayModel monthPayModel = new MonthPayModel();
        monthPayModel.setMonthStandard(monthStandard);
        monthPayModel.setMontId(pkMonthId);
        monthPayModel.setOrderNumber(orderNumberId);
        monthPayModel.setOriginalValidity(TurnTimeStamp.TurnStamp(validityStart));
        monthPayModel.setValidityStart(TurnTimeStamp.TurnStamp(validityStart));
        monthPayModel.setValidityEnd(TurnTimeStamp.TurnStampEnd(validityEnd));
        monthPayModel.setPayMode(5);

        //根据token获取到操作员，存放在月租车缴费单中
        monthPayModel.setEmpName(workerName);

        int insert3 = monthPayMapper.insert(monthPayModel);
        if (insert3 == 0) {
            return MsgResponse.error().msg("月租车缴费生成失败");
        }

        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("车场系统");//	模块名称	String 必填
        logModel.setImpName("月租车管理");//	接口名称	String
        logModel.setOpType("新增月租车");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("新增月租车,车牌号码为"+carNumber);//	操作内容	String  必填
        logModel.setCarNumber(carNumber);//	车牌号码	String
        logModel.setOpResult(insert3>0?"成功":"失败");//	操作结果	String  必填
        logModel.setOpTime(currentTime);//	操作时间	TimeStamp  必填
        logService.addLog(logModel);

        return MsgResponse.ok();
    }

    //删除月租车信息
    @Override
    public Object DeleteMonthInfo(String data,String token)
    {
        //json数据解析
        JSONObject jsonObject = new JSONObject(data);

        //要删除的月租车编号
        int pkMonthId = jsonObject.getInt("monthId");

        //查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        //开始删除，即注销
        int delete = monthInfoMapper.updateMonthState(pkMonthId,2);
        String monthCarNumber = monthInfoMapper.getMonthCarNumber(pkMonthId);

        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("车场系统");//	模块名称	String 必填
        logModel.setImpName("月租车管理");//	接口名称	String
        logModel.setOpType("注销月租车");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("注销车牌号码为"+monthCarNumber);//	操作内容	String  必填
        logModel.setCarNumber(monthCarNumber);//	车牌号码	String
        logModel.setOpResult(delete>0?"成功":"失败");//	操作结果	String  必填
        logModel.setOpTime(currentTime);//	操作时间	TimeStamp  必填
        logService.addLog(logModel);

        if (delete == 1){
            return  MsgResponse.ok();
        }
        else {
            return MsgResponse.error().msg("注销月租车失败");
        }
    }

    //月租车信息更新
    @Override
    public Object UpdateMonthInfo(String data,String token)
    {
        //Json解析数据
        JSONObject js=new JSONObject(data);

        //获取月租车修改信息
        JSONObject jsonObject = js.getJSONObject("query");

        //月租车信息
        Integer monthId = jsonObject.getInt("monthId");
        Integer monthStandard = jsonObject.getInt("monthStandard");
        String carNumber = jsonObject.getStr("carNumber");
        String room = jsonObject.getStr("room");
        Integer carNumberType = jsonObject.getInt("carNumberType");
        String carPort = jsonObject.getStr("carPort");//车位需要修改为String
        Integer carType = jsonObject.getInt("carType");
        Integer pledge = jsonObject.getInt("pledge");
        String validityStart = jsonObject.getStr("validityStart");
        String validityEnd = jsonObject.getStr("validityEnd");
        String note = jsonObject.getStr("note");

        //用户信息
        String userName = jsonObject.getStr("userName");
        String tel = jsonObject.getStr("tel");
        Integer identityType = jsonObject.getInt("identityType");
        String identity = jsonObject.getStr("identity");

        //查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("车场系统");//	模块名称	String 必填
        logModel.setImpName("月租车管理");//	接口名称	String
        logModel.setOpType("更新月租车");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("更新车牌号"+carNumber);//	操作内容	String  必填
        logModel.setCarNumber(carNumber);//	车牌号码	String
        logModel.setOpTime(currentTime);//	操作时间	TimeStamp  必填

        //月租车实体
        MonthInfoModel monthInfoModel=new MonthInfoModel();

        monthInfoModel.setPkMonthId(monthId);
        monthInfoModel.setMontStandard(monthStandard);
        monthInfoModel.setCarNumber(carNumber);
        monthInfoModel.setRoom(room);
        monthInfoModel.setCarport(carPort);
        monthInfoModel.setCarNumberType(carNumberType);
        monthInfoModel.setCarType(carType);
        monthInfoModel.setPledge(pledge);
        monthInfoModel.setValidityStart(TurnTimeStamp.TurnStamp(validityStart));
        monthInfoModel.setValidityEnd(TurnTimeStamp.TurnStampEnd(validityEnd));
        monthInfoModel.setNote(note);

        //更新月租车信息
        int flag = monthInfoMapper.updateById(monthInfoModel);

        //获取用户信息外键
        Integer userId = monthInfoMapper.getUserId(monthId);

        //用户实体
        CarOwnerModel carOwnerModel=new CarOwnerModel();

        carOwnerModel.setPkUserId(userId);
        carOwnerModel.setIdentityType(identityType);
        carOwnerModel.setIdentity(identity);
        carOwnerModel.setUserName(userName);
        carOwnerModel.setTel(tel);

        //更新用户信息
        int flagSe = carOwnerMapper.updateById(carOwnerModel);

        if(flag==1)
        {
            if(flagSe==1)
            {
                logModel.setOpResult(flagSe>0?"用户信息更新成功":"用户信息更新失败");//	操作结果	String  必填
                logService.addLog(logModel);
                return MsgResponse.ok();
            }
            else
            {
                logModel.setOpResult(flagSe>0?"用户信息更新成功":"用户信息更新失败");//	操作结果	String  必填
                logService.addLog(logModel);
                return MsgResponse
                        .error()
                        .msg("用户信息更新失败");
            }
        }
        else
        {
            logModel.setOpResult("月租车信息更新失败");//	操作结果	String  必填
            logService.addLog(logModel);
            return MsgResponse
                    .error()
                    .msg("月租车信息更新失败");
        }
    }

    //共享车列表，在月租车页面上的共享车按钮
    //废弃方案
    @Override
    public Object ShareInfoDTO(String data)
    {
        //Json解析数据
        JSONObject js=new JSONObject(data);
        String token = js.getStr("token");
        Integer monthId = js.getInt("monthId");

        //获取月租车编号
        String monthCarNumber = monthInfoMapper.getMonthCarNumber(monthId);

        //根据编号查找共享车信息
        List<ShareInfoModel> shareInfoList = shareInfoMapper.getShareInfoByMonthId(monthId);

        List<ShareInfoDto>  shareInfoDtoList=new ArrayList<>();

        for(int i=0;i<shareInfoList.size();i++)
        {
            ShareInfoDto shareInfoDTO=new ShareInfoDto();
            shareInfoDTO.setMonthId(monthId);
            shareInfoDTO.setCarNumber(shareInfoList.get(i).getCarNumber());

            shareInfoDtoList.add(shareInfoDTO);
        }

        return MsgResponse
                .ok()
                .data("monthId",monthId)
                .data("monthCarNumber",monthCarNumber)
                .data("rows",shareInfoDtoList);
    }

    //显示共享车信息
    public Object ShowShareInfo(String data)
    {
        //Json解析数据
        JSONObject js=new JSONObject(data);
        String token = js.getStr("token");
        Integer monthId = js.getInt("monthId");

        //获取月租车编号
        String monthCarNumber = monthInfoMapper.getMonthCarNumber(monthId);

        //根据编号查找共享车信息
        List<ShareInfoModel> shareInfoList = shareInfoMapper.getShareInfoByMonthId(monthId);

        List<ShareInfoDto>  shareInfoDtoList=new ArrayList<>();

        for(int i=0;i<shareInfoList.size();i++)
        {
            ShareInfoDto shareInfoDTO=new ShareInfoDto();
            shareInfoDTO.setMonthId(monthId);
            shareInfoDTO.setCarNumber(shareInfoList.get(i).getCarNumber());

            shareInfoDtoList.add(shareInfoDTO);
        }

        return MsgResponse.ok().data("monthId",monthId)
                .data("monthCarNumber",monthCarNumber)
                .data("rows",shareInfoDtoList);
    }

    //保存月租车上的共享车信息
    //废弃方案
    public Object SaveShareInfo(String data)
    {
        JSONObject js=new JSONObject(data);

        Integer monthId = js.getInt("monthId");
        String token = js.getStr("token");
        JSONArray query = js.getJSONArray("query");


        //从数据库中获取到对应月租车的共享车信息
        List<ShareInfoModel> shareInfoList = shareInfoMapper.getShareInfoByMonthId(monthId);

        //创建不同列表和相同列表
        List<Integer> different=new ArrayList<>();
        List<Integer> same=new ArrayList<>();

        //将数据库中的共享车信息表和前端传来的信息进行一一对应
        //相同的存入same列表，不同的共享车信息存入different列表
        //将前端传来没有共享车编号的值存入数据库，将different中的对应的共享车信息删除
        for(Integer i =0;i<shareInfoList.size();i++)
        {
            System.out.println("已有编号"+shareInfoList);

            Integer pkShareId = shareInfoList.get(i).getPkShareId();

            {
                for(Integer j=0;j<query.size();j++)
                {
                    JSONObject jsonObject =query.getJSONObject(j);
                    Integer shareId = jsonObject.getInt("shareId");

                    if(shareId==null)
                        break;

                    if(i==0)
                        different.add(shareId);

                    if(pkShareId.equals(shareId)) {
                        System.out.println("不同第一次"+different);
                        if (different.contains(shareId))
                        { different.remove(shareId);}

                        if(!same.contains(shareId))
                            same.add(shareId);
                        System.out.println("不同第二次"+different);
                        break;
                    }
                    else
                    {
                        if(same.contains(pkShareId))
                            break;

                        if(!different.contains(pkShareId))
                            different.add(pkShareId);
                    }
                }
                System.out.println("最后一次不同"+different);

                System.out.println("共享车编号不为空");
            }
            System.out.println("查找不同");
        }

        //插入数据库
        for (Integer i=0;i<query.size();i++)
        {
            JSONObject jsonObject = query.getJSONObject(i);
            Integer shareId = jsonObject.getInt("shareId");
            String shareIdStr = jsonObject.getStr("shareId");

            System.out.println("问题1"+shareId);

            if(shareId==null)
            {
                System.out.println("进入数值去");

                ShareInfoModel shareInfoModel=new ShareInfoModel();
      /*          String note = jsonObject.getStr("note");
                if(!note.isEmpty())
                    shareInfoModel.setNote(note);*/
                String carNumber = jsonObject.getStr("carNumber");
                shareInfoModel.setCarNumber(carNumber);
                shareInfoModel.setMonthId(monthId);

                System.out.println("提取数值成功");

                int insert = shareInfoMapper.insert(shareInfoModel);

                System.out.println(insert);
            }

            System.out.println("问题2");
        }

        for(Integer i=0;i<different.size();i++)
        {
            Integer flag = shareInfoMapper.deleteById(different.get(i));

            if(flag.equals(0))
                return MsgResponse.error().msg("输出数据失败");
        }

        return MsgResponse.ok();
    }

    @Override
    //新增共享车信息
    public Object AddShareInfo(String data,String token)
    {
        JSONObject js=new JSONObject(data);
        Integer monthId = js.getInt("monthId");
        String carNumber = js.getStr("carNumber");
        String note = js.getStr("note");

        //查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        String monthCarNumber = monthInfoMapper.getMonthCarNumber(monthId);

        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("车场系统");//	模块名称	String 必填
        logModel.setImpName("月租车管理");//	接口名称	String
        logModel.setOpType("新增共享车");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("共享车牌号"+carNumber);//	操作内容	String  必填
        logModel.setCarNumber(monthCarNumber);//	车牌号码	String
        logModel.setOpTime(currentTime);//	操作时间	TimeStamp  必填

        Integer exitShareInfo = shareInfoMapper.countShareInfo(carNumber, monthId);
        if(!exitShareInfo.equals(0))
        {
            logModel.setOpResult("操作失败，该共享车已与该月租车共享车位");
            logService.addLog(logModel);
            return MsgResponse.error().msg("该共享车已与该月租车共享车位");
        }

        ShareInfoModel shareInfoModel=new ShareInfoModel();

        shareInfoModel.setCarNumber(carNumber);
        shareInfoModel.setMonthId(monthId);
        shareInfoModel.setNote(note);

        System.out.println("经过第一次");

        Integer insert = shareInfoMapper.insert(shareInfoModel);

        System.out.println("经过第二次");

        if (insert!=0)
        {
            logModel.setOpResult("操作成功");
            logService.addLog(logModel);
            return MsgResponse.ok();
        }
        else
        {
            logModel.setOpResult("操作失败，新增失败");
            logService.addLog(logModel);
            return MsgResponse.error().msg("新增共享车失败");
        }
    }

    //删除共享车信息
    @Override
    public Object DeleteShareInfo(String data,String token)
    {
        JSONObject js=new JSONObject(data);
        Integer monthId = js.getInt("monthId");
        String carNumber = js.getStr("carNumber");

        System.out.println(monthId+"月租车编号");

        String monthCarNumber = monthInfoMapper.getMonthCarNumber(monthId);

        //查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("车场系统");//	模块名称	String 必填
        logModel.setImpName("月租车管理");//	接口名称	String
        logModel.setOpType("删除共享车");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("删除车牌号码"+carNumber);//	操作内容	String  必填
        logModel.setCarNumber(monthCarNumber);//	车牌号码	String
        logModel.setOpTime(currentTime);//	操作时间	TimeStamp  必填

        //返回是否删除的值
        Integer flagDelete = shareInfoMapper.deleteShareInfo(monthId, carNumber);

        if(flagDelete!=0)
        {
            logModel.setOpResult("成功");
            logService.addLog(logModel);
            return MsgResponse.ok();
        }
        else {
            logModel.setOpResult("失败");
            logService.addLog(logModel);
            return MsgResponse.error().msg("删除失败");
        }
    }

    //退款请求
    public Object RefundRequire(String data)
    {
        JSONObject js=new JSONObject(data);
        String token = js.getStr("token");
        Integer monthId = js.getInt("monthId");

        List<MonthPayVO> monthPayList = monthPayMapper.getMonthPayList(1,monthId);
        if(monthPayList.size()!=0)
        {
            RefundDTO refundDTO=new RefundDTO();
            refundDTO.setMonthId(monthId);
            refundDTO.setMonthStandard(monthPayList.get(0).getMonthStandard());
            refundDTO.setCarNumber(monthPayList.get(0).getCarNumber());
            refundDTO.setOriginalValidity(TurnTimeStamp.TurnString(monthPayList.get(0).getOriginalValidity()));
            refundDTO.setValidityStart(TurnTimeStamp.TurnString(monthPayList.get(0).getValidityStart()));
            refundDTO.setValidityEnd(TurnTimeStamp.TurnString(monthPayList.get(0).getValidityEnd()));
            refundDTO.setPledge(monthPayList.get(0).getPledge());

            refundDTO.setRefundMoney(monthPayList.get(0).getRealMoney());

            return MsgResponse.ok().data("rows",refundDTO);
        }
        else
        {
            return MsgResponse.error().msg("返回数据出错");
        }

    }

    //退款
    @Override
    public Object Refund(String data,String token)
    {
        JSONObject js=new JSONObject(data);
        Integer monthId = js.getInt("monthId");

        System.out.println(monthId+"月租车编号");

        List<MonthPayVO> monthPayList = monthPayMapper.getMonthPayList(2,monthId);

        String monthCarNumber = monthInfoMapper.getMonthCarNumber(monthId);

        //查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("车场系统");//	模块名称	String 必填
        logModel.setImpName("延期缴费");//	接口名称	String
        logModel.setOpType("退款");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("退款"+monthCarNumber);//	操作内容	String  必填
        logModel.setCarNumber(monthCarNumber);//	车牌号码	String
        logModel.setOpTime(currentTime);//	操作时间	TimeStamp  必填

        if(monthPayList.size()==0) {
            logModel.setOpResult("失败,该车已无任何未退款的缴费记录");
            logModel.setOpContent("对月租车或缴费记录进行退款");
            logService.addLog(logModel);
            return MsgResponse.error().msg("该车已无任何未退款的缴费记录");
        }

        String orderNumber = monthPayList.get(0).getOrderNumber();
        Integer realMoney = monthPayList.get(0).getRealMoney();
        Integer pledge = monthPayList.get(0).getPledge();
        Timestamp timestamp=new Timestamp(System.currentTimeMillis());

        //查询是否只有一条未退款缴费记录，如果是的话，该缴费记录为刚新增的月租车的缴费记录，需要退换押金，同时注销对应的月租车
        Integer countMonthPayTotal = monthPayMapper.countMonthPayTotal(monthId);
        if(countMonthPayTotal.equals(1))
        {
            //将车辆信息注销
            Integer cancelState = monthInfoMapper.updateMonthState(monthId, 2);
            //更新订单信息，使之成功退款
            Integer refund=realMoney+pledge;
            Integer updateOrdFlag = monthPayMapper.UpdateIsRefund(orderNumber, refund, timestamp);

            if(cancelState.equals(1)&&updateOrdFlag.equals(1)) {
                logModel.setOpResult("成功");
                logModel.setOpContent("退款金额"+refund.toString()+",注销月租车");
                logService.addLog(logModel);
                return MsgResponse.ok();
            }
        }

        //退去最后一个缴费金额，同时让月租车的有效期改为上一条缴费信息的有效期
        if(monthPayList.size()==2)
        {
            Timestamp validityStart = monthPayList.get(1).getValidityStart();
            Timestamp validityEnd = monthPayList.get(1).getValidityEnd();

            Integer integer = monthPayMapper.UpdateIsRefund(orderNumber, realMoney, timestamp);
            Integer integer1 = monthPayMapper.UpdateMonthInfo(validityStart, validityEnd, monthId);

            if(integer==1&&integer1==1)
            {
                Timestamp validityEnd1 = monthInfoMapper.getValidityEnd(monthId);
                if(TimestampCount.Day(validityEnd1,currentTime)<1) {
                    Integer integer2 = monthInfoMapper.updateMonthState(monthId, 1);
                }
                logModel.setOpResult("成功");
                logModel.setOpContent("退款"+realMoney);
                logService.addLog(logModel);
                return MsgResponse.ok();
            }
            else {
                logModel.setOpResult("失败");
                logModel.setOpContent("退款");
                logService.addLog(logModel);
                return MsgResponse.error().msg("更改失败");
            }

        }
        else
        {
            logModel.setOpResult("失败");
            logModel.setOpContent("退款");
            logService.addLog(logModel);
            return MsgResponse.error().msg("没有进行过延期缴费操作");
        }

    }

    //转换共享模式
    @Override
    public Object ShareMode(String data,String token)
    {
        //Json解析数据
        JSONObject js=new JSONObject(data);
        Integer shareMode = js.getInt("shareMode");

        Integer flag = shareInfoMapper.setShareMode(shareMode);

        //查找到当前操作员（根据token）
        TokenModel finduser = loginFilterMapper.finduser(token);
        String account = finduser.getAccount();
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("车场系统");//	模块名称	String 必填
        logModel.setImpName("月租车管理");//	接口名称	String
        logModel.setOpType("共享车模式");//	操作方法： 新增月租车，缴费
        if(shareMode.equals(0))
            logModel.setOpContent("更改为模式一");//	操作内容	String  必填
        else
            logModel.setOpContent("更改为模式二");
        logModel.setOpTime(currentTime);//	操作时间	TimeStamp  必填
        logModel.setOpResult(flag.equals(1)?"成功":"失败");

        if(flag==1)
        {
            logService.addLog(logModel);
            return MsgResponse.ok();
        }
        else
        {
            logService.addLog(logModel);
            return MsgResponse.error();
        }
    }

    //自定义延期请求
    @Override
    public Object DefinedRequire(String data)
    {
        //Json解析数据
        JSONObject js=new JSONObject(data);
        String token = js.getStr("token");
        Integer monthId = js.getInt("monthId");

        MonthInfoModel monthInfoModel = monthInfoMapper.selectById(monthId);

        if(monthInfoModel==null)
            return MsgResponse.error().msg("月租车编号出错");

        List<Integer> monthStandard = monthInfoMapper.getMonthStandard();

        return MsgResponse.ok()
                .data("monthId",monthInfoModel.getPkMonthId())
                .data("carNumber",monthInfoModel.getCarNumber())
                .data("validityEnd",monthInfoModel.getValidityEnd())
                .data("pledge",monthInfoModel.getPledge())
                .data("delayStart",monthInfoModel.getValidityEnd())
                .data("monthStandard",monthStandard);
    }

    //自定义延期缴费
    @Override
    public Object Defined(String data,String token)
    {
        //Json解析数据
        JSONObject js=new JSONObject(data);

        //获取月租车延期缴费属性
        JSONObject query = js.getJSONObject("query");

        Integer monthId = query.getInt("monthId");
        String validityStart = query.getStr("validityStart");
        String validityEnd = query.getStr("validityEnd");
        Integer monthStandard = query.getInt("monthStandard");
        Integer realMoney = query.getInt("realMoney");
        String note = query.getStr("note");
        Integer payMode = query.getInt("payMode");

        //查找到当前操作员（根据token）
        WorkerVO findaccount = loginFilterMapper.findaccount(token);
        String account = findaccount.getAccount();
        String workerName = findaccount.getWorkerName();
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        String monthCarNumber = monthInfoMapper.getMonthCarNumber(monthId);

        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("车场系统");//	模块名称	String 必填
        logModel.setImpName("延期缴费");//	接口名称	String
        logModel.setOpType("自定义延期缴费");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("充值车牌号"+monthCarNumber);
        logModel.setCarNumber(monthCarNumber);
        logModel.setOpTime(currentTime);//	操作时间	TimeStamp  必填

        //时间戳转换
        Timestamp validityStartStamp = TurnTimeStamp.TurnStamp(validityStart);
        Timestamp validityEndStamp = TurnTimeStamp.TurnStampEnd(validityEnd);

        //更新月租车信息
        Integer flag = monthInfoMapper.updateMonthInfo(monthId, monthStandard,validityStartStamp,validityEndStamp);

        //生成订单编号数据
        String orderNumberId = OrderNumber.CreateOrderNumber();

        OrderNumberModel orderNumberModel=new OrderNumberModel();
        orderNumberModel.setMoney(realMoney);
        orderNumberModel.setRealMoney(realMoney);
        orderNumberModel.setPkOrderNumber(orderNumberId);
        orderNumberModel.setAccountState(0);
        orderNumberModel.setCarType(0);
        orderNumberModel.setPayState(1);
        orderNumberModel.setAccountState(0);
        orderNumberModel.setDerateMoney(0);
        orderNumberModel.setIsOnline(0);
        orderNumberModel.setIsFree(0);

        int insert1 = orderNumberMapper.insert(orderNumberModel);
        if(insert1==0)
        {
            logModel.setOpResult("失败");
            logService.addLog(logModel);
            return MsgResponse.error().msg("订单生成失败");
        }

        //如果前端为发送给线下转账方式，统一给5，即未知线下转账的方式
        if(payMode==null)
        {
            payMode=5;
        }

        //月租车缴费数据
        MonthPayModel monthPayModel=new MonthPayModel();
        monthPayModel.setMonthStandard(monthStandard);
        monthPayModel.setMontId(monthId);
        monthPayModel.setOrderNumber(orderNumberId);
        monthPayModel.setOriginalValidity(TurnTimeStamp.TurnStamp(validityStart));
        monthPayModel.setValidityStart(TurnTimeStamp.TurnStamp(validityStart));
        monthPayModel.setValidityEnd(TurnTimeStamp.TurnStampEnd(validityEnd));
        monthPayModel.setPayMode(payMode);
        monthPayModel.setNote(note);

        //未获取到token
        monthPayModel.setEmpName(workerName);

        int insert = monthPayMapper.insert(monthPayModel);

        if(insert==0)
        {
            logModel.setOpResult("失败");
            logService.addLog(logModel);
            return MsgResponse.error().msg("月租车缴费生成失败");
        }

        if(flag==1) {
            logModel.setOpResult("成功");
            logService.addLog(logModel);
            return MsgResponse.ok();
        }
        else {
            logModel.setOpResult("失败");
            logService.addLog(logModel);
            return MsgResponse.error();
        }
    }

    //延期缴费请求
    @Override
    public Object DelayPayRequire(String data)
    {
        //Json解析数据
        JSONObject js=new JSONObject(data);
        String token = js.getStr("token");
        Integer monthId = js.getInt("monthId");

        MonthInfoModel monthInfoModel = monthInfoMapper.selectById(monthId);

        if(monthInfoModel==null)
            return MsgResponse.error().msg("月租车编号出错");

        List<Integer> monthStandard = monthInfoMapper.getMonthStandard();

        return MsgResponse.ok()
                .data("monthId",monthInfoModel.getPkMonthId())
                .data("carNumber",monthInfoModel.getCarNumber())
                .data("validityEnd",monthInfoModel.getValidityEnd())
                .data("pledge",monthInfoModel.getPledge())
                .data("delayStart",monthInfoModel.getValidityEnd())
                .data("monthStandard",monthStandard);
    }

    //延期缴费
    @Override
    public Object DelayPay(String data,String token)
    {
        //Json解析数据
        JSONObject js=new JSONObject(data);

        //获取月租车延期缴费属性
        JSONObject jsonObject = js.getJSONObject("query");

        Integer monthId = jsonObject.getInt("monthId");
        String validityStart = jsonObject.getStr("validityStart");
        String validityEnd = jsonObject.getStr("validityEnd");
        Integer monthStandard = jsonObject.getInt("monthStandard");
        Integer realMoney = jsonObject.getInt("realMoney");
        String note = jsonObject.getStr("note");
        Integer payMode = jsonObject.getInt("payMode");

        //查找到当前操作员（根据token）
        WorkerVO findaccount = loginFilterMapper.findaccount(token);
        String account = findaccount.getAccount();
        String workerName = findaccount.getWorkerName();
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        String monthCarNumber = monthInfoMapper.getMonthCarNumber(monthId);

        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("车场系统");//	模块名称	String 必填
        logModel.setImpName("延期缴费");//	接口名称	String
        logModel.setOpType("延期缴费充值");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("充值车牌号"+monthCarNumber);
        logModel.setCarNumber(monthCarNumber);
        logModel.setOpTime(currentTime);//	操作时间	TimeStamp  必填

        //时间戳转换
        Timestamp validityStartStamp = TurnTimeStamp.TurnStamp(validityStart);
        Timestamp validityEndStamp = TurnTimeStamp.TurnStampEnd(validityEnd);

        //更新月租车时间信息
        Integer flag = monthInfoMapper.updateMonthInfo(monthId, monthStandard,validityStartStamp,validityEndStamp);

        //生成订单编号
        String orderNumberId = OrderNumber.CreateOrderNumber();

        MonthPayModel monthPayModel=new MonthPayModel();
        monthPayModel.setMonthStandard(monthStandard);
        monthPayModel.setMontId(monthId);
        monthPayModel.setOrderNumber(orderNumberId);
        monthPayModel.setOriginalValidity(TurnTimeStamp.TurnStamp(validityStart));
        monthPayModel.setValidityStart(TurnTimeStamp.TurnStamp(validityStart));
        monthPayModel.setValidityEnd(TurnTimeStamp.TurnStampEnd(validityEnd));
        monthPayModel.setPayMode(payMode);
        monthPayModel.setNote(note);

        //未获取到token
        monthPayModel.setEmpName(workerName);

        //插入月租车缴费信息
        int insert = monthPayMapper.insert(monthPayModel);

        if(insert==0)
        {
            logModel.setOpResult("失败");
            logService.addLog(logModel);
            return MsgResponse.error().msg("月租车缴费生成失败");
        }

        OrderNumberModel orderNumberModel=new OrderNumberModel();
        orderNumberModel.setMoney(realMoney);
        orderNumberModel.setRealMoney(realMoney);
        orderNumberModel.setPkOrderNumber(orderNumberId);
        orderNumberModel.setAccountState(0);
        orderNumberModel.setCarType(0);
        orderNumberModel.setPayState(1);
        orderNumberModel.setAccountState(0);
        orderNumberModel.setDerateMoney(0);
        orderNumberModel.setIsOnline(0);
        orderNumberModel.setIsFree(0);

        //插入订单
        int insert1 = orderNumberMapper.insert(orderNumberModel);
        if(insert1==0)
        {
            logModel.setOpResult("失败");
            logService.addLog(logModel);
            return MsgResponse.error().msg("订单生成失败");
        }

        if(flag==1) {
            logModel.setOpResult("成功");
            logService.addLog(logModel);
            return MsgResponse.ok();
        }
        else {
            logModel.setOpResult("失败");
            logService.addLog(logModel);
            return MsgResponse.error();
        }
    }

    //延期纠正请求
    @Override
    public Object PostponeReqiure(String data)
    {
//Json解析数据
        JSONObject js=new JSONObject(data);
        String token = js.getStr("token");
        Integer monthId = js.getInt("monthId");

        MonthInfoModel monthInfoModel = monthInfoMapper.selectById(monthId);

        if(monthInfoModel==null)
            return MsgResponse.error().msg("月租车编号出错");

        return MsgResponse.ok()
                .data("monthId",monthId)
                .data("carNumber",monthInfoModel.getCarNumber())
                .data("validityEnd",monthInfoModel.getValidityStart())
                .data("delayStart",monthInfoModel.getValidityStart())
                .data("delayEnd",monthInfoModel.getValidityEnd());
    }

    //延期纠正
    @Override
    public Object Postpone(String data,String token)
    {
        //Json解析数据
        JSONObject js=new JSONObject(data);

        JSONObject jsonObject=js.getJSONObject("query");

        Integer monthId = jsonObject.getInt("monthId");
        String carNumber = jsonObject.getStr("carNumber");
        String validityEnd = jsonObject.getStr("validityEnd");
        String delayStart = jsonObject.getStr("delayStart");
        String delayEnd = jsonObject.getStr("delayEnd");
        String note = jsonObject.getStr("note");

        //查找到当前操作员（根据token）
        WorkerVO findaccount = loginFilterMapper.findaccount(token);
        String account = findaccount.getAccount();
        String workerName = findaccount.getWorkerName();
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        String monthCarNumber = monthInfoMapper.getMonthCarNumber(monthId);

        //记录日志
        LogModel logModel = new LogModel();
        logModel.setEmpId(account);//	用户账号	String  必填
        logModel.setCommunity("华源小区");//	小区名称	String
        logModel.setModeName("车场系统");//	模块名称	String 必填
        logModel.setImpName("延期缴费");//	接口名称	String
        logModel.setOpType("延期缴费充值");//	操作方法： 新增月租车，缴费
        logModel.setOpContent("充值车牌号"+monthCarNumber);
        logModel.setCarNumber(monthCarNumber);
        logModel.setOpTime(currentTime);//	操作时间	TimeStamp  必填

        Timestamp delayStartStamp = TurnTimeStamp.TurnStamp(delayStart);
        Timestamp delayEndStamp = TurnTimeStamp.TurnStampEnd(delayEnd);

        Timestamp oriValidityEnd = monthInfoMapper.getOriValidityEnd(monthId);

        //修改月租车信息
        Integer flag = monthInfoMapper.updatePostpone(monthId,delayStartStamp,delayEndStamp);

        if(flag==0)
        {
            logModel.setOpResult("失败");
            logService.addLog(logModel);
            return MsgResponse.error().msg("纠正延期，更新月租车失败");
        }

        //延期纠正表插入
        DelayPostponeModel delayPostponeModel=new DelayPostponeModel();

        delayPostponeModel.setPkId(monthId);
        delayPostponeModel.setCarNumber(carNumber);
        delayPostponeModel.setOriginalValidityEnd(oriValidityEnd);
        delayPostponeModel.setValidityStart(delayStartStamp);
        delayPostponeModel.setValidityEnd(delayEndStamp);
        delayPostponeModel.setNote(note);
        delayPostponeModel.setEmpName(workerName);

        int flagSe = delayPostponeMapper.insert(delayPostponeModel);

        if(flagSe==0)
        {
            logModel.setOpResult("失败");
            logService.addLog(logModel);
            return MsgResponse.error().msg("插入延期记录失败");
        }

        Integer updateMonthPay = monthPayMapper.UpdateMonthPay(delayStartStamp, delayEndStamp, monthId);
        if(updateMonthPay.equals(0))
        {
            logModel.setOpResult("失败");
            logService.addLog(logModel);
            return MsgResponse.error().msg("缴费纠正中月租车缴费有效期更新失败");
        }

        logModel.setOpResult("成功");
        logService.addLog(logModel);
        return MsgResponse.ok();

    }
}
