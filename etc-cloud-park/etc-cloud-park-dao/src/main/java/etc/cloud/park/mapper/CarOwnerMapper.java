package etc.cloud.park.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import etc.cloud.commons.constants.CommonConstants;
import etc.cloud.park.mode.CarOwnerModel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@DS(value = CommonConstants.DB_NAME)
public interface CarOwnerMapper extends BaseMapper<CarOwnerModel> {

    @Select("SELECT pk_user_id FROM car_owner " +
            "WHERE identity=#{identity}")
    Integer getUserId(@Param(value = "identity") String identity);
}
