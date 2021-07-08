package etc.cloud.park.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import etc.cloud.commons.constants.CommonConstants;
import etc.cloud.park.mode.PassagewayModel;
import etc.cloud.park.mode.PassagewayParameterModel;
import org.springframework.stereotype.Repository;

@Repository
@DS(value = CommonConstants.DB_NAME)
public interface PassagewayParameterMapper extends BaseMapper<PassagewayParameterModel> {

    //通道设置查询
    default Page selectAll(Page page, QueryWrapper<PassagewayParameterModel> wrapper){
        selectPage(page,wrapper);
        return page;

    }
    //新增通道设置
    default int insertPassagewayParameter(PassagewayParameterModel passagewayParameterModel){
        return insert(passagewayParameterModel);

    }
    //更新通道设置
    default int alterPassagewayParameter(PassagewayParameterModel passagewayParameterModel,QueryWrapper<PassagewayParameterModel> wrapper){
        return  update(passagewayParameterModel,wrapper);
    }


}
