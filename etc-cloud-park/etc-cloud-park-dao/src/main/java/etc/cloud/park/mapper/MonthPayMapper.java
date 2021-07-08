package etc.cloud.park.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import etc.cloud.commons.constants.CommonConstants;
import etc.cloud.park.mode.MonthPayModel;
import etc.cloud.park.mode.vo.MonthPayVO;
import etc.cloud.park.mode.vo.TotalMoneyVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Repository
@DS(value = CommonConstants.DB_NAME)
public interface MonthPayMapper extends BaseMapper<MonthPayModel> {
    @Select({"<script>"+
            "SELECT SQL_CALC_FOUND_ROWS * FROM month_pay " +
            "LEFT JOIN month_info on month_pay.month_id = month_info.pk_month_id " +
            "LEFT JOIN order_number on month_pay.order_number = order_number.pk_order_number " +
            "LEFT JOIN order_official on order_number.order_official = order_official.pk_order_official " +
            "WHERE 1=1 " +
            "AND is_refund=0 " +
            "<if test='payType!=null'>and pay_type = #{payType} </if>" +
            "<if test='monthStandard!=null'>and month_pay.month_standard = #{monthStandard} </if>" +
            "<if test='isOnline!=null'>and is_online = #{isOnline} </if>" +
            "<if test='room!=null'>and room = #{room} </if>" +
            "<if test='payStart!=null'>and UNIX_TIMESTAMP(pay_time) &gt;= UNIX_TIMESTAMP(#{payStart}) </if>" +
            "<if test='payEnd!=null'>and UNIX_TIMESTAMP(pay_time) &lt;= UNIX_TIMESTAMP(#{payEnd}) </if>" +
            "<if test='carNumber!=null'>and car_number = #{carNumber} </if>" +
            "<if test='operator!=null'>and emp_name = #{operator} </if>" +
            "<if test='payState!=null'>and pay_state = #{payState} </if>" +
            "<if test='orderNumber!=null'>and order_number = #{orderNumber} </if>" +
            "limit #{currentSe},#{limitSe}"+
            "</script>"})
    List<MonthPayVO> getMonthPayVO(@Param(value = "currentSe") Integer current,
                                   @Param(value = "limitSe") Integer limit,
                                   @Param(value = "payType") Integer payType,
                                   @Param(value = "monthStandard") Integer monthStandard,
                                   @Param(value = "isOnline") Integer isOnline,
                                   @Param(value = "room") String room,
                                   @Param(value = "payStart") Timestamp payStart,
                                   @Param(value = "payEnd") Timestamp payEnd,
                                   @Param(value = "carNumber") String carNumber,
                                   @Param(value = "operator") String operator,
                                   @Param(value = "payState") Integer payState,
                                   @Param(value = "orderNumber") String orderNumber);

    @Select("SELECT FOUND_ROWS()")
    Integer SqlTotal();

    @Select("select SQL_CALC_FOUND_ROWS SUM(real_money) as realMoney,date_format(order_number.create_time, '%Y-%m-%d') createTime, count(*) coun " +
            "from order_number " +
            "WHERE order_number.car_type=0 " +
            "AND is_refund=0 " +
            "group by date_format(create_time, '%Y-%m-%d') " +
            "desc " +
            "limit #{currentSe},#{limitSe}")
    List<TotalMoneyVO> getTotalList(@Param(value = "currentSe") Integer current,
                                    @Param(value = "limitSe") Integer limit);

    @Select("select SUM(real_money) as realMoney,date_format(order_number.create_time, '%Y-%m-%d') createTime, count(*) coun " +
            "from order_number " +
            "LEFT JOIN order_official on order_number.order_official = order_official.pk_order_official " +
            "WHERE order_official.pay_type=#{payType} " +
            "AND order_number.car_type=0 " +
            "AND is_refund=0 " +
            "group by date_format(order_number.create_time, '%Y-%m-%d') " +
            "desc " +
            "limit #{currentSe},#{limitSe}")
    List<TotalMoneyVO> getTypeTotalList(@Param(value = "currentSe") Integer current,
                                        @Param(value = "limitSe") Integer limit,
                                        @Param(value = "payType") Integer payType);

    @Select("select SUM(real_money) as realMoney,date_format(order_number.create_time, '%Y-%m-%d') createTime, count(*) coun " +
            "from order_number " +
            "LEFT JOIN order_official on order_number.order_official = order_official.pk_order_official " +
            "WHERE order_number.is_online=0 " +
            "AND is_refund=0 " +
            "group by date_format(order_number.create_time, '%Y-%m-%d') " +
            "desc " +
            "limit #{currentSe},#{limitSe}")
    List<TotalMoneyVO> getOfflineTotalList(@Param(value = "currentSe") Integer current,
                                        @Param(value = "limitSe") Integer limit);

    @Select("SELECT * FROM month_pay " +
            "LEFT JOIN order_number on month_pay.order_number = order_number.pk_order_number " +
            "LEFT JOIN month_info on month_pay.month_id =month_info.pk_month_id " +
            "WHERE order_number.is_refund=0 " +
            "AND month_pay.month_id=#{monthId} " +
            "order by pk_id desc " +
            "limit 0,#{limit}")
    List<MonthPayVO> getMonthPayList(@Param(value = "limit") Integer limit,
                                     @Param(value = "monthId")Integer monthId);

    @Update("UPDATE order_number " +
            "SET is_refund=1," +
            "refund=#{refund}," +
            "refund_time=#{refundTime} " +
            "WHERE pk_order_number=#{orderNumber}")
    Integer UpdateIsRefund(@Param(value = "orderNumber")String orderNumber,
                           @Param(value = "refund")Integer refund,
                           @Param(value = "refundTime")Timestamp refundTime);

    @Update("UPDATE month_info " +
            "SET validity_start=#{validityStart}," +
            "validity_end=#{validityEnd} " +
            "WHERE pk_month_id=#{monthId}")
    Integer UpdateMonthInfo(@Param(value = "validityStart")Timestamp validityStart,
                            @Param(value = "validityEnd")Timestamp validityEnd,
                            @Param(value = "monthId")Integer monthId);

    @Update("UPDATE month_pay " +
            "SET validity_start=#{validityStart}," +
            "validity_end=#{validityEnd} " +
            "WHERE month_id=#{month_id}")
    Integer UpdateMonthPay(@Param(value = "validityStart")Timestamp validityStart,
                           @Param(value = "validityEnd")Timestamp validityEnd,
                           @Param(value = "monthId")Integer monthId);

    @Select("SELECT COUNT(*) FROM month_pay " +
            "LEFT JOIN order_number on month_pay.order_number = order_number.pk_order_number " +
            "LEFT JOIN month_info on month_pay.month_id =month_info.pk_month_id " +
            "WHERE order_number.is_refund = 0 " +
            "AND month_pay.month_id=#{monthId} ")
    Integer countMonthPayTotal(@Param(value = "monthId") Integer monthId);
}
