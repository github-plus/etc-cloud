package etc.cloud.park.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import etc.cloud.commons.constants.CommonConstants;
import etc.cloud.park.mode.FreeTypeModel;
import etc.cloud.park.mode.LogModel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
@DS(value = CommonConstants.DB_NAME)
public interface LogMapper extends BaseMapper<LogModel> {

    //新增日志
    default int insertLog(LogModel logModel){
        return insert(logModel);
    }



    //查询日志
    @Select({"<script>"+
            "SELECT * FROM log " +
            "WHERE 1=1 " +
            "<if test='community!=null'>and community like #{community} </if>" +
            "<if test='modeName!=null'>and mode_name like #{modeName} </if>" +
            "<if test='impName!=null'>and imp_name like #{impName} </if>" +
            "<if test='opContent!=null'>and op_content like #{opContent} </if>" +
            "<if test='carNumber!=null'>and car_number like #{carNumber} </if>" +
            "<if test='beginTime!=null'>and UNIX_TIMESTAMP(op_time) &gt;= UNIX_TIMESTAMP(#{beginTime}) </if>" +
            "<if test='endTime!=null'>and UNIX_TIMESTAMP(op_time) &lt;= UNIX_TIMESTAMP(#{endTime}) </if>" +
            "limit #{currentSe},#{limit}"+
            "</script>"})

    List<LogModel> inquiryOfLog(@Param(value = "currentSe") Integer currentSe,
                                        @Param(value = "limit") Integer limit,
                                        @Param(value = "empId") String empId,
                                        @Param(value = "community") String community,
                                        @Param(value = "modeName") String modeName,
                                        @Param(value = "impName") String impName,
                                        @Param(value = "opType") String opType,
                                        @Param(value = "opContent") String opContent,
                                        @Param(value = "carNumber") String carNumber,
                                        @Param(value = "opResult") String opResult,
                                        @Param(value = "beginTime")Timestamp beginTime,
                                        @Param(value = "endTime") Timestamp endTime);
}
