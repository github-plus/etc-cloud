package etc.cloud.park.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import etc.cloud.commons.constants.CommonConstants;
import etc.cloud.park.mode.FreeTypeModel;
import etc.cloud.park.mode.PayCarTypeModel;
import etc.cloud.park.mode.TimesFeeModel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@DS(value = CommonConstants.DB_NAME)
public interface TimesFeeMapper extends BaseMapper<TimesFeeModel> {

    //根据id查询具体的一条按次收费规则数据
//    @Select({"<script>"+
//            "SELECT * FROM times_fee " +
//            "WHERE 1=1 " +
//            "and pk_times_fee_id = #{feeId} " +
//            "</script>"})
//    TimesFeeModel selectById(@Param(value = "feeId") Integer feeId);

    //新增按次收费规则
    default int insertTimesFee(TimesFeeModel timesFeeModel){
        return insert(timesFeeModel);

    }


}
