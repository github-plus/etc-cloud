package etc.cloud.park.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import etc.cloud.commons.constants.CommonConstants;
import etc.cloud.park.mode.TimeQuantumFeeModel;
import etc.cloud.park.mode.TimesFeeModel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@DS(value = CommonConstants.DB_NAME)
public interface TimeQuantumFeeMapper extends BaseMapper<TimeQuantumFeeModel> {



}
