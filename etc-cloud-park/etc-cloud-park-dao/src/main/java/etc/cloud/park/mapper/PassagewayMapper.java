package etc.cloud.park.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import etc.cloud.commons.constants.CommonConstants;
import etc.cloud.park.mode.ParkOperatorModel;
import etc.cloud.park.mode.PassagewayModel;
import org.springframework.stereotype.Repository;

@Repository
@DS(value = CommonConstants.DB_NAME)
public interface PassagewayMapper extends BaseMapper<PassagewayModel> {
    //通道查询
    default Page selectAll(Page page, QueryWrapper<PassagewayModel> wrapper){
        selectPage(page,wrapper);
        return page;

    }
    //新增通道
    default int insertPassageway(PassagewayModel passagewayModel){
        return insert(passagewayModel);

    }
    //更新通道
    default int alterPassageway(PassagewayModel passagewayModel,QueryWrapper<PassagewayModel> wrapper){
        return  update(passagewayModel,wrapper);
    }
    //删除通道
    default int deletePassageway(QueryWrapper<PassagewayModel> wrapper){
        return delete(wrapper);
    }

}
