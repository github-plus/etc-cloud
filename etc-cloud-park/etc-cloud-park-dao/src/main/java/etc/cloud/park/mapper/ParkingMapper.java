package etc.cloud.park.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import etc.cloud.commons.constants.CommonConstants;
import etc.cloud.park.mode.ParkingModel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@DS(value = CommonConstants.DB_NAME)
public interface ParkingMapper extends BaseMapper<ParkingModel> {

    //车厂信息查询
    default Page selectAll(Page page,QueryWrapper<ParkingModel> wrapper){
        selectPage(page,wrapper);
        return page;

    }
    //新增车场
    default int insertPark(ParkingModel parkingModel){
        return insert(parkingModel);

    }
    //更新车场
    default int alterPark(ParkingModel parkingModel,QueryWrapper<ParkingModel> wrapper){
        return  update(parkingModel,wrapper);
    }


    //根据车厂名称查询车厂密钥
    @Select({"<script>"+
            "SELECT pk_secret_key FROM park " +
            "WHERE 1=1 " +
            "and park_name = #{parkName} " +
            "</script>"})
    String selectKey(@Param(value = "parkName") String parkName);


    /*
        langwen
         */
    //查询所有停车场秘钥和名字
    @Select("select park.pk_secret_key,park.park_name from park")
    List<ParkingModel> findPark();
}
