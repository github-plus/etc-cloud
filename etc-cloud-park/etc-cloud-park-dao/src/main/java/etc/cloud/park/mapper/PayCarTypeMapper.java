package etc.cloud.park.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import etc.cloud.commons.constants.CommonConstants;
import etc.cloud.park.mode.LogModel;
import etc.cloud.park.mode.PassagewayModel;
import etc.cloud.park.mode.PayCarTypeModel;
import etc.cloud.park.mode.TimesFeeModel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Repository
@DS(value = CommonConstants.DB_NAME)
public interface PayCarTypeMapper extends BaseMapper<PayCarTypeModel> {

    //收费车型查询
    default Page selectAll(Page page, QueryWrapper<PayCarTypeModel> wrapper){
        selectPage(page,wrapper);
        return page;

    }
    //根据id查询具体的一条收费车型数据
    @Select({"<script>"+
            "SELECT * FROM pay_car_type " +
            "WHERE 1=1 " +
            "and pk_pay_temp_car_type_id = #{typeId} " +
            "</script>"})
    PayCarTypeModel selectByPayCarTypeId(@Param(value = "typeId") Integer typeId);

    //新增收费车型
    default int insertPayCarType(PayCarTypeModel payCarTypeModel){
        return insert(payCarTypeModel);

    }
    //更新收费车型
    default int alterPayCarType(PayCarTypeModel payCarTypeModel,QueryWrapper<PayCarTypeModel> wrapper){
        return  update(payCarTypeModel,wrapper);
    }
    //删除收费车型
    default int deletePayCarType(QueryWrapper<PayCarTypeModel> wrapper){
        return delete(wrapper);
    }


}
