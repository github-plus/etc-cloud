package etc.cloud.auth.mapper;

import etc.cloud.auth.vo.HomeVO;
import etc.cloud.auth.vo.MoneyVO;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface HomeMapper {

    //查找项目总数
    @Select("select count(*) from project ")
    int findProjectTotal();

    //查找车场总数
    @Select("select count(*) from park ")
    int findParkTotal();

    //查找车位总数
    @Select("select sum(car_sum) from param")
    int findCarTotal();

    //临时车车位总数
    @Select("select sum(temp_sum) " +
            "from param ")
    int findTempCar();
    //查找剩余车位
    List findResidueCarTotal();

    //查询本月的所有收入
    @Select(value = "select date_format(pay_time,'%d') date, sum(real_money) money " +
            "from order_number " +
            "where date_format(pay_time,'%Y-%m-%d') BETWEEN #{startTime} and #{endTime} group by date")
    List<MoneyVO> findMonthMoney(String startTime, String endTime);

    //查询本年的所有收入
    @Select(value = "select date_format(pay_time,'%m') date, sum(real_money) money " +
            "from order_number " +
            "where date_format(pay_time,'%Y-%m-%d') BETWEEN #{startTime} and #{endTime} group by date")
    List<MoneyVO> findYearMoney(String startTime, String endTime);

    @Select(value = "select date_format(pay_time,'%H') date, sum(real_money) money " +
            "from order_number " +
            "where date_format(pay_time,'%Y-%m-%d %H:%m:%S') BETWEEN #{startTime} and #{endTime} group by date")
    List<MoneyVO> findDayMoney(String startTime, String endTime);



}
