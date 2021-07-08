package etc.cloud.gateway.service.impl;

import etc.cloud.auth.mapper.HomeMapper;
import etc.cloud.auth.service.IHomeService;
import etc.cloud.auth.vo.HomeVO;
import etc.cloud.auth.vo.MoneyVO;
import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.gateway.tool.GetTimeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class HomeServiceImpl implements IHomeService {

    @Autowired
    private HomeMapper homeMapper;

    //查找所有
    public Object findPark(){

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


        return MsgResponse.ok().data("projectTotal", projectTotal).data("parkTotal", parkTotal).
                data("carTotal", carTotal).data("tempCarTotal",tempCarToatl).data("residueCarTotal",residueCarTotal)
                .data("monthCarTotal", monthCarTotal);
    }

    //查找停车场
    public Object findAll(){
        /*
        查询总数
         */
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

        /*
        查询当前停车场
         */
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置你想要的格式

        /*本年度每个月总金额*/
        String startYearTime = df.format(GetTimeTool.getBeginDayOfYear());
        String endYearTime = df.format(GetTimeTool.getEndDayOfYear());
        System.out.println("本年开始时间："+startYearTime);
        System.out.println("本年结束时间："+endYearTime);
        //开始查询
        List<MoneyVO> yearMoney = homeMapper.findYearMoney(startYearTime, endYearTime);

        //获取查询长度
        int size1 = yearMoney.size();
        //新建一个数组
        ArrayList<Integer> list = new ArrayList<>();
        //提前给数组赋值0
        for (int i = 0;i<12;i++){
            list.add(0);
        }

        //对有收入的天数插入值
        for (int i=0;i<size1;i++){
            Integer date1 = Integer.valueOf(yearMoney.get(i).getDate());
            list.set(date1-1,yearMoney.get(i).getMoney());
        }
        System.out.println("本年度总收入："+list);

        /*本月每天总金额*/
        //获取本月开始时间和本月结束时间
        String startMonthTime = df.format(GetTimeTool.getBeginDayOfMonth());
        String endMonthTime = df.format(GetTimeTool.getEndDayOfMonth());

        //开始查询
        List<MoneyVO> monthMoney = homeMapper.findMonthMoney(startMonthTime, endMonthTime);

        //获取本月天数
        int diffDays = GetTimeTool.getDiffDays(GetTimeTool.getBeginDayOfMonth(), GetTimeTool.getEndDayOfMonth());

        //获取查询长度
        int size = monthMoney.size();
        //新建一个数组
        List<Integer> list1=new ArrayList<>();
        //提前给数组赋值0
        for (int i = 0;i<diffDays;i++){
            list1.add(0);
        }
        //对有收入的天数插入值
        for (int i=0;i<size;i++){
            Integer date = Integer.valueOf(monthMoney.get(i).getDate());
            list1.set(date-1,monthMoney.get(i).getMoney());
        }
        System.out.println("本月每日总收入："+list1);


        /*本日每小时金额*/
        SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置你想要的格式

        //获取当日开始时间和结束时间
        String dayBegin = day.format(GetTimeTool.getDayBegin());
        String dayEnd = day.format(GetTimeTool.getDayEnd());

        System.out.println("本日开始时间："+dayBegin);
        System.out.println("本日结束时间"+dayEnd);

        //开始查询
        List<MoneyVO> dayMoney = homeMapper.findDayMoney(dayBegin, dayEnd);
        //获取查询个数
        int size2 = dayMoney.size();
        //新建一个数组
        ArrayList<Integer> list2 = new ArrayList<>();
        //提前给数组赋值0
        for (int i = 0;i<24;i++){
            list2.add(0);
        }

        //对有收入的小时插入值
        for (int i=0;i<size2;i++){
            Integer date2 = Integer.valueOf(dayMoney.get(i).getDate());
            list2.set(date2,dayMoney.get(i).getMoney());
        }
        System.out.println("本日每小时收入为："+list2);

//        return MsgResponse.ok().data("projectTotal", projectTotal).data("parkTotal", parkTotal).
//                data("carTotal", carTotal).data("tempCarTotal",tempCarToatl).
//                data("residueCarTotal",residueCarTotal).data("monthCarTotal", monthCarTotal).
//                data("yearMoney",list).data("monthMoney",list1).data("dayMoney",list2);


        return MsgResponse.ok().data("home",homeVO).
                data("yearMoney",list).data("monthMoney",list1).data("dayMoney",list2);
    }


}
