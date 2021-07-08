package etc.cloud.park.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import etc.cloud.commons.constants.CommonConstants;
import etc.cloud.park.mode.vo.TempOnlineVO;
import etc.cloud.park.mode.vo.TempPayVO;
import etc.cloud.park.mode.vo.TempTotalMoneyVO;
import etc.cloud.park.mode.vo.TotalMoneyVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
@DS(value = CommonConstants.DB_NAME)
public interface TempPayMapper {
    //获取临时车缴费明细数据
    @Select({"<script>"+
            "SELECT SQL_CALC_FOUND_ROWS * FROM temp_pay " +
            "LEFT JOIN out_in_car on temp_pay.car_in_out_id = out_in_car.pk_out_id " +
            "LEFT JOIN order_number on temp_pay.order_number = order_number.pk_order_number " +
            "LEFT JOIN order_official on order_number.order_official = order_official.pk_order_official " +
            "WHERE 1=1 " +
            "<if test='payType!=null'>and pay_type = #{payType} </if>" +
            "<if test='freeType!=null'>and free_type = #{freeType} </if>" +
            "<if test='discountName!=null'>and discount_name = #{discountName} </if>" +
            "<if test='channel!=null'> and out_channel = #{channel} </if>" +
            "<if test='outStart!=null'>and UNIX_TIMESTAMP(out_time) &gt;= UNIX_TIMESTAMP(#{outStart}) </if>" +
            "<if test='outEnd!=null'>and UNIX_TIMESTAMP(out_time) &lt; = UNIX_TIMESTAMP(#{outEnd}) </if>" +
            "<if test='carNumber!=null'>and car_number = #{carNumber} </if>" +
            "<if test='operator!=null'>and out_operator = #{operator} </if>" +
            "<if test='tempStandard!=null'>and temp_standard_name = #{tempStandard} </if>" +
            "<if test='orderNumber!=null'>and order_number = #{orderNumber} </if>"+
            "limit #{currentSe},#{limitSe}"+
            "</script>"})
    List<TempPayVO> getTempPayVO(@Param(value = "currentSe") Integer current,
                                 @Param(value = "limitSe") Integer limit,
                                 @Param(value = "payType") Integer payType,
                                 @Param(value = "freeType") String freeType,
                                 @Param(value = "discountName") String discountName,
                                 @Param(value = "channel") String channel,
                                 @Param(value = "outStart") Timestamp outStart,
                                 @Param(value = "outEnd") Timestamp outEnd,
                                 @Param(value = "carNumber") String carNumber,
                                 @Param(value = "operator") String operator,
                                 @Param(value = "tempStandard") String tempStandard,
                                 @Param(value = "orderNumber") String orderNumber);

    @Select("SELECT FOUND_ROWS()")
    Integer SqlTotal();

    //临时车汇总所有收费汇总
    @Select("select SQL_CALC_FOUND_ROWS SUM(real_money) as realMoney," +
            "date_format(order_number.create_time, '%Y-%m-%d') date, count(*) coun," +
            "SUM(derate_money) as derateMoney," +
            "SUM(money) as money " +
            "from order_number " +
            "WHERE car_type = 1 " +
            "group by date_format(create_time, '%Y-%m-%d') " +
            "desc " +
            "limit #{currentSe},#{limitSe}")
    List<TempTotalMoneyVO> getTotalList(@Param(value = "currentSe") Integer current,
                                        @Param(value = "limitSe") Integer limit);

    //临时车汇总中判断是否线上的收费汇总
    @Select("SELECT SUM(real_money) as realMoney," +
            "date_format(order_number.create_time, '%Y-%m-%d') createTime, count(*) coun " +
            "from order_number " +
            "WHERE is_online = #{isOnline} " +
            "AND car_type=1 " +
            "group by date_format(create_time, '%Y-%m-%d') " +
            "desc " +
            "limit #{currentSe},#{limitSe}")
    List<TotalMoneyVO> getIsOnlineTotalList(@Param(value = "currentSe") Integer current,
                                            @Param(value = "limitSe") Integer limit,
                                            @Param(value = "isOnline") Integer isOnline);

    //是否为线下判断是否为人工收取现金
    @Select({"<script>"+
            "SELECT SQL_CALC_FOUND_ROWS * FROM temp_pay " +
            "LEFT JOIN out_in_car on temp_pay.car_in_out_id = out_in_car.pk_out_id " +
            "LEFT JOIN order_number on temp_pay.order_number = order_number.pk_order_number " +
            "LEFT JOIN order_official on order_number.order_official = order_official.pk_order_official " +
            "WHERE 1=1 " +
            "and order_number.is_online = 0 " +
            "<if test='payType!=null'>and pay_type = #{payType} </if>" +
            "<if test='freeType!=null'>and free_type = #{freeType} </if>" +
            "<if test='discountName!=null'>and discount_name = #{discountName} </if>" +
            "<if test='channel!=null'> and out_channel = #{channel} </if>" +
            "<if test='outStart!=null'>and UNIX_TIMESTAMP(out_time) &gt;= UNIX_TIMESTAMP(#{outStart}) </if>" +
            "<if test='outEnd!=null'>and UNIX_TIMESTAMP(out_time) &lt; = UNIX_TIMESTAMP(#{outEnd}) </if>" +
            "<if test='carNumber!=null'>and car_number = #{carNumber} </if>" +
            "<if test='operator!=null'>and out_operator = #{operator} </if>" +
            "<if test='tempStandard!=null'>and temp_standard_name = #{tempStandard} </if>" +
            "<if test='orderNumber!=null'>and order_number = #{orderNumber} </if>"+
            "limit #{currentSe},#{limitSe}"+
            "</script>"})
    List<TempPayVO> getTempOffline(@Param(value = "currentSe") Integer current,
                                 @Param(value = "limitSe") Integer limit,
                                 @Param(value = "payType") Integer payType,
                                 @Param(value = "freeType") String freeType,
                                 @Param(value = "discountName") String discountName,
                                 @Param(value = "channel") String channel,
                                 @Param(value = "outStart") Timestamp outStart,
                                 @Param(value = "outEnd") Timestamp outEnd,
                                 @Param(value = "carNumber") String carNumber,
                                 @Param(value = "operator") String operator,
                                 @Param(value = "tempStandard") String tempStandard,
                                 @Param(value = "orderNumber") String orderNumber);

    @Select({"<script>"+
            "SELECT SQL_CALC_FOUND_ROWS * FROM temp_pay " +
            "LEFT JOIN out_in_car on temp_pay.car_in_out_id = out_in_car.pk_out_id " +
            "LEFT JOIN order_number on temp_pay.order_number = order_number.pk_order_number " +
            "LEFT JOIN order_official on order_number.order_official = order_official.pk_order_official " +
            "WHERE 1=1 " +
            "and order_number.is_online = 1 " +
            "<if test='payType!=null'>and pay_type = #{payType} </if>" +
            "<if test='merchantName!=null'>and merchant_name = #{merchantName} </if>" +
            "<if test='discountName!=null'>and discount_name = #{discountName} </if>" +
            "<if test='payStart!=null'>and UNIX_TIMESTAMP(create_time) &gt;= UNIX_TIMESTAMP(#{payStart}) </if>" +
            "<if test='payEnd!=null'>and UNIX_TIMESTAMP(create_time) &lt; = UNIX_TIMESTAMP(#{payEnd}) </if>" +
            "<if test='carNumber!=null'>and car_number = #{carNumber} </if>" +
            "<if test='operator!=null'>and out_operator = #{operator} </if>" +
            "<if test='tempStandard!=null'>and temp_standard_name = #{tempStandard} </if>" +
            "<if test='orderNumber!=null'>and order_number = #{orderNumber} </if>"+
            "limit #{currentSe},#{limitSe}"+
            "</script>"})
    List<TempOnlineVO> getTempOnline(@Param(value = "currentSe") Integer current,
                                     @Param(value = "limitSe") Integer limit,
                                     @Param(value = "payType") Integer payType,
                                     @Param(value = "merchantName") String merchantName,
                                     @Param(value = "discountName") String discountName,
                                     @Param(value = "payStart") Timestamp payStart,
                                     @Param(value = "payEnd") Timestamp payEnd,
                                     @Param(value = "carNumber") String carNumber,
                                     @Param(value = "operator") String operator,
                                     @Param(value = "tempStandard") String tempStandard,
                                     @Param(value = "orderNumber") String orderNumber);

    //操作员名称
    @Select("SELECT op_name FROM operator")
    List<String> getOperatorName();

    //临时车缴费标准名称
    @Select("SELECT temp_standard_name FROM temp_pay")
    List<String> getTempStandardName();

    //免费类型名称
    @Select("SELECT free_type_name FROM free_type")
    List<String> getFreeTypeName();

 /*   //优惠方案名称，由于暂时没有对应人建数据库，暂不处理
    @Select("SELECT merchant_name FROM merchant")
    List<String> getMerchantName();*/

    @Select("SELECT chan_name FROM channel")
    List<String> getChannelName();
}
