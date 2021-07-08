package etc.cloud.park.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import etc.cloud.commons.constants.CommonConstants;
import etc.cloud.park.mode.ParkingModel;
import etc.cloud.park.mode.ParkingParameterModel;
import org.springframework.stereotype.Repository;

@Repository
@DS(value = CommonConstants.DB_NAME)
public interface ParkingParameterMapper extends BaseMapper<ParkingParameterModel> {

    //车厂参数查询
    default Page selectAll(Page page, QueryWrapper<ParkingParameterModel> wrapper){
        selectPage(page,wrapper);
        return page;

    }
    //新增车场参数设置
    default int insertParkingParameter(ParkingParameterModel parkingParameterModel){
        return insert(parkingParameterModel);

    }
    //更新车场参数设置
    default int alterParkingParameter(ParkingParameterModel parkingParameterModel,QueryWrapper<ParkingParameterModel> wrapper){
        return  update(parkingParameterModel,wrapper);
    }
}
