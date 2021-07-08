package etc.cloud.park.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import etc.cloud.commons.constants.CommonConstants;
import etc.cloud.park.mode.ParkOperatorModel;
import etc.cloud.park.mode.ParkingModel;
import org.springframework.stereotype.Repository;

@Repository
@DS(value = CommonConstants.DB_NAME)
public interface ParkOperatorMapper extends BaseMapper<ParkOperatorModel> {
    //车厂操作员查询
    default Page selectAll(Page page, QueryWrapper<ParkOperatorModel> wrapper){
        selectPage(page,wrapper);
        return page;

    }
    //新增车场操作员
    default int insertParkOperator(ParkOperatorModel parkOperatorModel){
        return insert(parkOperatorModel);

    }
    //更新车场操作员
    default int alterParkOperator(ParkOperatorModel parkOperatorModel,QueryWrapper<ParkOperatorModel> wrapper){
        return  update(parkOperatorModel,wrapper);
    }
    //删除车场操作员
    default int deleteParkOperator(QueryWrapper<ParkOperatorModel> wrapper){
        return delete(wrapper);
    }


}
