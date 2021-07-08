package etc.cloud.gateway.service.impl;

import etc.cloud.auth.mapper.HomeMapper;
import etc.cloud.auth.mapper.WarningMapper;
import etc.cloud.auth.service.IWarningService;
import etc.cloud.auth.vo.HomeVO;
import etc.cloud.auth.vo.WarningVO;
import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.gateway.tool.GetTimeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;


@Service
public class WarningServiceImpl  implements IWarningService {

    @Autowired
    private HomeMapper homeMapper;

    @Autowired
    private WarningMapper warningMapper;

    public Object findAll(){

        //获取项目总数
        int projectTotal = homeMapper.findProjectTotal();

        //获取车场总数
        int parkTotal = homeMapper.findParkTotal();

        //查找车位总数
        int carTotal = homeMapper.findCarTotal();

        //查找临时车总数
        int tempCarToatl = homeMapper.findTempCar();

        /*
        剩余车位总数和月租车车位没做
         */
        //剩余车位 未完成
        int residueCarTotal = 10;
        //月租车车位
        int monthCarTotal = 10;

        HomeVO homeVO = new HomeVO();
        homeVO.setProjectTotal(projectTotal);
        homeVO.setParkTotal(parkTotal);
        homeVO.setCarTotal(carTotal);
        homeVO.setMonthCarTotal(monthCarTotal);
        homeVO.setTempCarTotal(tempCarToatl);
        homeVO.setResidueCarTotal(residueCarTotal);
        //查询当前异常设备
        int warning = warningMapper.findWarning();

        //获取本月时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startMonthTime = df.format(GetTimeTool.getBeginDayOfMonth());
        String endMonthTime = df.format(GetTimeTool.getEndDayOfMonth());

        //查询本月异常事件情况
        List<WarningVO> eventAll = warningMapper.findMonthEventAll(startMonthTime, endMonthTime);

        //本月异常事件警告处理总警告
        int warningEventTotal=eventAll.size();

        //查询本月异常事件警告处理已处理
        List<WarningVO> monthEventDeal = warningMapper.findMonthEventDeal(startMonthTime, endMonthTime);

        int dealWarningEventTotal=monthEventDeal.size();

        //本月设备告警处理总警告
        int warningDeviceTotal=eventAll.size();
        //本月设备告警处理总警告已处理
        int dealWarningDeviceTotal=monthEventDeal.size();

//        return MsgResponse.ok().data("projectTotal", projectTotal).data("parkTotal", parkTotal).
//                data("carTotal", carTotal).data("tempCarTotal",tempCarToatl).data("residueCarTotal",residueCarTotal).
//                data("warningEventTotal", warningEventTotal).data("dealWarningEventTotal", dealWarningEventTotal).
//                data("warningDeviceTotal", warningDeviceTotal).data("dealWarningDeviceTotal",dealWarningDeviceTotal).
//                data("monthCarTotal", monthCarTotal).data("event", event).data("warning", warning);
        return MsgResponse.ok().data("home",homeVO).data("warning", warning).data("eventAll",eventAll).
                data("warningEventTotal",warningEventTotal).data("dealWarningEventTotal",dealWarningEventTotal).
                data("warningDeviceTotal", warningDeviceTotal).data("dealWarningDeviceTotal", dealWarningDeviceTotal);
    }
}
