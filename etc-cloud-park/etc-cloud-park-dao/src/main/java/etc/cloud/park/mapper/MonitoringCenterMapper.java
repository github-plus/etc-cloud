package etc.cloud.park.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import etc.cloud.commons.constants.CommonConstants;
import etc.cloud.park.mode.MonitoringCenterModel;
import org.springframework.stereotype.Repository;

@Repository
@DS(value = CommonConstants.DB_NAME)
public interface MonitoringCenterMapper extends BaseMapper<MonitoringCenterModel> {

    //监控中心查询
    default Page selectAll(Page page, QueryWrapper<MonitoringCenterModel> wrapper){
        selectPage(page,wrapper);
        return page;

    }
    //新增监控中心
    default int insertMonitoringCenter(MonitoringCenterModel monitoringCenterModel){
        return insert(monitoringCenterModel);

    }
    //更新监控中心
    default int alterMonitoringCenter(MonitoringCenterModel monitoringCenterModel,QueryWrapper<MonitoringCenterModel> wrapper){
        return  update(monitoringCenterModel,wrapper);
    }
    //删除监控中心
    default int deleteMonitoringCenter(QueryWrapper<MonitoringCenterModel> wrapper){

        return delete(wrapper);
    }



}
