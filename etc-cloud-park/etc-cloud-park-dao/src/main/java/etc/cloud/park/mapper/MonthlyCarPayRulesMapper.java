package etc.cloud.park.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import etc.cloud.commons.constants.CommonConstants;
import etc.cloud.park.mode.MonthlyCarPayRulesModel;
import etc.cloud.park.mode.ParkOperatorModel;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
@DS(value = CommonConstants.DB_NAME)
public interface MonthlyCarPayRulesMapper extends BaseMapper<MonthlyCarPayRulesModel> {
    //月租车规则查询
    default Page selectAll(Page page, QueryWrapper<MonthlyCarPayRulesModel> wrapper){
        selectPage(page,wrapper);
        return page;

    }
    //新增月租车规则
    default int insertMonthlyCarPayRules(MonthlyCarPayRulesModel monthlyCarPayRulesModel){
        return insert(monthlyCarPayRulesModel);

    }
    //更新月租车规则
    default int alterMonthlyCarPayRules(MonthlyCarPayRulesModel monthlyCarPayRulesModel,QueryWrapper<MonthlyCarPayRulesModel> wrapper){
        return  update(monthlyCarPayRulesModel,wrapper);
    }
    //删除月租车规则
    default int deleteMonthlyCarPayRules(QueryWrapper<MonthlyCarPayRulesModel> wrapper){
        return delete(wrapper);
    }

    @Update("update month_car_strand set pay_mode = #{mode} ")
    int updateMonthlyCarPayMode(int mode);
}
