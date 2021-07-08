package etc.cloud.park.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import etc.cloud.commons.constants.CommonConstants;
import etc.cloud.park.mode.PrefabricateModel;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@DS(value = CommonConstants.DB_NAME)
public interface PrefabricationMapper extends BaseMapper<PrefabricateModel> {

        @Select({"<script>" +
                "select * from prefabricate " +
                "where 1=1 " +
                "<if test='carNumber!=null'>" +
                "and car_number = #{carNumber} " +
                "</if>" +
                "<if test='carOwner!=null'>" +
                "and car_owner = #{carOwner} " +
                "</if>" +
                "<if test='tel!=null'>" +
                "and tel = #{tel} " +
                "</if>" +
                "<if test='room!=null'>" +
                "and room = #{room} " +
                "</if>" +
                "limit #{current},#{limit} " +
                "</script>"
        }
        )
    List<PrefabricateModel> findcar(int limit, int current, String carNumber,
                                    String carOwner, String tel, String room,
                                    Date startTime, Date endTime);

        @Select("select * from prefabricate")
        List<PrefabricateModel> find();

}
