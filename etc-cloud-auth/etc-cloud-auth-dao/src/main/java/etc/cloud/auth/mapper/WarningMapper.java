package etc.cloud.auth.mapper;

import etc.cloud.auth.vo.WarningVO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface WarningMapper {

    //搜索当前异常个数
    @Select("select count(*) " +
            "from device_log " +
            "where content = 0 ")
    int findWarning();

    //查找本月异常
    @Select("select * from device_log " +
            "where date_format(create_time,'%Y-%m-%d %H:%m:%S') BETWEEN #{startTime} and #{endTime}")
    List<WarningVO> findMonthEventAll(String startTime, String endTime);


    //本月已处理警告事件
    @Select("select * from device_log " +
            "where content = 1 and " +
            "date_format(create_time,'%Y-%m-%d %H:%m:%S') BETWEEN #{startTime} and #{endTime}")
    List<WarningVO> findMonthEventDeal(String startTime, String endTime);
}
