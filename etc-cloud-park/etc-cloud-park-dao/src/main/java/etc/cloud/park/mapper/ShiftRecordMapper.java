package etc.cloud.park.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import etc.cloud.commons.constants.CommonConstants;
import etc.cloud.park.mode.ShiftRecordModel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
@DS(value = CommonConstants.DB_NAME)
public interface ShiftRecordMapper extends BaseMapper<ShiftRecordModel> {

    @Select("<script>" +
            "SELECT SQL_CALC_FOUND_ROWS * FROM shift_record " +
            "WHERE 1=1 " +
            "<if test='loginStart!=null'>and UNIX_TIMESTAMP(login_time) &gt;= UNIX_TIMESTAMP(#{loginStart}) </if>" +
            "<if test='loginEnd!=null'>and UNIX_TIMESTAMP(login_time) &lt;= UNIX_TIMESTAMP(#{loginEnd}) </if>" +
            "<if test='operator!=null'>and operator_name = #{operator} </if>" +
            "limit #{currentSe},#{limitSe}"+
            "</script>")
    List<ShiftRecordModel> getShiftRecord(@Param(value = "currentSe") Integer current,
                                          @Param(value = "limitSe") Integer limit,
                                          @Param(value = "loginStart")Timestamp loginStart,
                                          @Param(value = "loginEnd") Timestamp loginEnd,
                                          @Param(value = "operator") String operator);

    @Select("SELECT FOUND_ROWS()")
    Integer SqlTotal();

    //操作员名称
    @Select("SELECT op_name FROM operator")
    List<String> getOperatorName();
}
