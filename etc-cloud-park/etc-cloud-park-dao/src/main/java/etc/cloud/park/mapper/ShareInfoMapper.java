package etc.cloud.park.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import etc.cloud.commons.constants.CommonConstants;
import etc.cloud.park.mode.ShareInfoModel;
import etc.cloud.park.mode.vo.ShareInfoMonthVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
@DS(value = CommonConstants.DB_NAME)
public interface ShareInfoMapper extends BaseMapper<ShareInfoModel> {
    @Select("SELECT * FROM share_info " +
            "WHERE month_id = #{monthId}")
    List<ShareInfoModel> getShareInfoByMonthId(@Param(value = "monthId") Integer monthId);

    @Update("UPDATE param SET share_mode = #{shareMode} " +
            "WHERE pk_id = 1")
    Integer setShareMode(@Param(value = "shareMode") Integer shareMode);

    @Select("SELECT * FROM " +
            "(share_info LEFT JOIN month_info on month_id = pk_month_id) " +
            "LEFT JOIN car_owner on owner_id = pk_user_id " +
            "limit #{currentSe},#{limitSe}")
    List<ShareInfoMonthVO> getShareMonthVO(@Param(value = "currentSe") Integer currentSe,
                                           @Param(value = "limitSe") Integer limitSe);

    @Select({"<script>"+
            "SELECT SQL_CALC_FOUND_ROWS * FROM " +
            "share_info LEFT JOIN month_info on month_id = pk_month_id " +
            "LEFT JOIN car_owner on owner_id = pk_user_id " +
            "WHERE 1=1 " +
            "<if test='room!=null'>and room = #{room} </if>" +
            "<if test='carNumber!=null'>and share_info.car_number = #{carNumber} </if>" +
            "<if test='userName!=null'>and user_name = #{userName} </if>" +
            "<if test='carPort!=null'>and carport = #{carPort} </if>" +
            "<if test='validityEndStart!=null'>and UNIX_TIMESTAMP(validity_end) &gt;= UNIX_TIMESTAMP(#{validityEndStart}) </if>" +
            "<if test='validityEndEnd!=null'>and UNIX_TIMESTAMP(validity_end) &lt;= UNIX_TIMESTAMP(#{validityEndEnd}) </if>" +
            "limit #{currentSe},#{limitSe}"+
            "</script>"})
    List<ShareInfoMonthVO> getShareSelectWrapper(@Param(value = "currentSe") Integer currentSe,
                                                 @Param(value = "limitSe") Integer limitSe,
                                                 @Param(value = "room") String room,
                                                 @Param(value = "carNumber") String carNumber,
                                                 @Param(value = "userName") String userName,
                                                 @Param(value = "carPort") String carPort,
                                                 @Param(value = "validityEndStart") Timestamp validityEndStart,
                                                 @Param(value = "validityEndEnd") Timestamp validityEndEnd);

    @Select("SELECT FOUND_ROWS()")
    Integer SqlTotal();

    @Delete("DELETE FROM share_info WHERE month_id=#{monthId} " +
            "AND car_number=#{carNumber}")
    Integer deleteShareInfo(@Param(value = "monthId")Integer monthId,
                            @Param(value = "carNumber")String carNumber);

    @Select("SELECT COUNT(*) FROM share_info " +
            "WHERE car_number=#{carNumber} " +
            "AND month_id = #{monthId}")
    Integer countShareInfo(@Param(value = "carNumber")String carNumber,
                           @Param(value = "monthId") Integer monthId);
}
