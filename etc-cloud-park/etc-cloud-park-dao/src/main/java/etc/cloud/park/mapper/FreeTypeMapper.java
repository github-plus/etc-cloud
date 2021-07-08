package etc.cloud.park.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import etc.cloud.commons.constants.CommonConstants;
import etc.cloud.park.mode.FreeTypeModel;
import etc.cloud.park.mode.MonthlyCarPayRulesModel;
import org.springframework.stereotype.Repository;

@Repository
@DS(value = CommonConstants.DB_NAME)
public interface FreeTypeMapper extends BaseMapper<FreeTypeModel> {
    //免费类型查询
    default Page selectAll(Page page, QueryWrapper<FreeTypeModel> wrapper){
        selectPage(page,wrapper);
        return page;

    }
    //新增免费类型
    default int insertFreeType(FreeTypeModel freeTypeModel){
        return insert(freeTypeModel);

    }

    //删除免费类型
    default int deleteFreeType(QueryWrapper<FreeTypeModel> wrapper){
        return delete(wrapper);
    }
}
