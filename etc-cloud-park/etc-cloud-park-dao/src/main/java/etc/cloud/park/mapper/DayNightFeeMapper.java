package etc.cloud.park.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import etc.cloud.commons.constants.CommonConstants;
import etc.cloud.park.mode.DayNightFeeModel;
import org.springframework.stereotype.Repository;

@Repository
@DS(value = CommonConstants.DB_NAME)
public interface DayNightFeeMapper extends BaseMapper<DayNightFeeModel> {


}
