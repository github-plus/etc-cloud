package etc.cloud.park.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import etc.cloud.commons.constants.CommonConstants;
import etc.cloud.park.mode.MonthInfoModel;
import etc.cloud.park.mode.vo.MonthOwnerVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

@Repository
@DS(value = CommonConstants.DB_NAME)
public interface MonthInfoMapper extends BaseMapper<MonthInfoModel> {

    @Select("SELECT * FROM month_info LEFT JOIN car_owner on owner_id=pk_user_id " +
            "limit #{currentSe},#{limitSe}")
    List<MonthOwnerVO> getMonthOwnerVO(Integer currentSe,Integer limitSe);

    @Select({"<script>"+
            "SELECT SQL_CALC_FOUND_ROWS * FROM month_info " +
            "LEFT JOIN car_owner on owner_id=pk_user_id "+
            "WHERE 1=1 " +
            "<if test='monthStandard!=null'>and month_standard = #{monthStandard} </if>" +
            "<if test='state!=null'>and state = #{state} </if>" +
            "<if test='keyState!=null'>and key_state = #{keyState} </if>" +
            "<if test='room!=null'>and month_info.room = #{room} </if>" +
            "<if test='carNumber!=null'>and car_number = #{carNumber} </if>" +
            "<if test='userName!=null'>and car_owner.user_name = #{userName} </if>" +
            "<if test='carPort!=null'>and carport = #{carPort} </if>" +
            "<if test='tel!=null'>and tel = #{tel} </if>"+
            "<if test='createStart!=null'>and UNIX_TIMESTAMP(create_time) &gt;= UNIX_TIMESTAMP(#{createStart}) </if>" +
            "<if test='createEnd!=null'>and UNIX_TIMESTAMP(create_time) &lt;= UNIX_TIMESTAMP(#{createEnd}) </if>" +
            "limit #{currentSe},#{limitSe} "+
            "</script>"})
    List<MonthOwnerVO> getMonthSelect(@Param(value = "currentSe") Integer currentSe, @Param(value = "limitSe") Integer limitSe,
                                      @Param(value = "monthStandard") Integer monthStandard,
                                      @Param(value = "state") Integer state,
                                      @Param(value = "keyState") Integer keyState,
                                      @Param(value = "room") String room,
                                      @Param(value = "carNumber") String carNumber,
                                      @Param(value = "userName") String userName,
                                      @Param(value = "carPort") String carPort,
                                      @Param(value = "tel") String tel,
                                      @Param(value = "createStart") Timestamp createStart,
                                      @Param(value = "createEnd") Timestamp createEnd);

    @Select("SELECT FOUND_ROWS()")
    Integer SqlTotal();

    @Select("SELECT pay_fee FROM month_car_strand")
    List<Integer> getMonthStandard();

    @Select("SELECT owner_id FROM month_info " +
            "WHERE pk_month_id = #{monthId}")
    Integer getUserId(@Param(value = "monthId") Integer monthId);

    @Select("SELECT car_number FROM month_info " +
            "WHERE pk_month_id = #{monthId}")
    String getMonthCarNumber(@Param(value = "monthId") Integer monthId);

    @Update("UPDATE month_info " +
            "SET month_standard = #{monthStandard}," +
            "validity_start = #{validityStart}," +
            "validity_end = #{validityEnd} " +
            "WHERE pk_month_id = #{monthId}")
    Integer updateMonthInfo(@Param(value = "monthId") Integer monthId,
                            @Param(value = "monthStandard") Integer monthStandard,
                            @Param(value = "validityStart") Timestamp validityStart,
                            @Param(value = "validityEnd") Timestamp validityEnd);

    @Update("UPDATE month_info " +
            "SET validity_start = #{validityStart}," +
            "validity_end = #{validityEnd} " +
            "WHERE pk_month_id = #{monthId}")
    Integer updatePostpone(@Param(value = "monthId") Integer monthId,
                            @Param(value = "validityStart") Timestamp validityStart,
                            @Param(value = "validityEnd") Timestamp validityEnd);

    @Select("SELECT validity_end FROM month_info " +
            "WHERE pk_month_id = #{monthId}")
    Timestamp getOriValidityEnd(@Param(value = "monthId") Integer monthId);

    @Update("UPDATE month_info " +
            "SET state=#{state} " +
            "WHERE pk_month_id = #{monthId}")
    Integer updateMonthState(@Param(value = "monthId")Integer monthId,
                             @Param(value = "state") Integer state);

    @Select("SELECT COUNT(*) FROM month_info WHERE car_number = #{carNumber} AND state!=2")
    Integer countMonthInfo(@Param(value = "carNumber") String carNumber);

    @Select("SELECT COUNT(*) FROM month_info WHERE car_number = #{carNumber} AND state=2")
    Integer countMonthCancel(@Param(value = "carNumber") String carNumber);

    @Select("SELECT validity_end FROM month_info WHERE pk_month_id=#{monthId}")
    Timestamp getValidityEnd(@Param(value = "monthId")Integer monthId);

    @Select("SELECT pk_month_id FROM month_info WHERE car_number=#{carNumber}")
    Integer getMonthInfoId(@Param(value = "carNumber")String carNumber);
}
