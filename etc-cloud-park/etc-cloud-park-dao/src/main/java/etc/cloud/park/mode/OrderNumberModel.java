package etc.cloud.park.mode;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
@TableName(value = "order_number")
public class OrderNumberModel {
    @TableField(value = "pk_order_number")
    private String pkOrderNumber;

    @TableField(value = "order_office")
    private String orderOffice;

    @TableField(value = "money")
    private Integer money;

    @TableField(value = "derate_money")
    private Integer derateMoney;

    @TableField(value = "real_money")
    private Integer realMoney;

    @TableField(value = "pay_state")
    private Integer payState;

    @TableField(value = "account_state")
    private Integer accountState;

    @TableField(value = "car_type")
    private Integer carType;

    @TableField(value = "discount_name")
    private String discountName;

    @TableField(value = "refund")
    private Integer refund;

    @TableField(value = "refund_time")
    private Timestamp refundTime;

    @TableField(value = "pay_time")
    private Timestamp payTime;

    @TableField(value = "create_time")
    private Timestamp createTime;

    @TableField(value = "update_time")
    private Timestamp updateTime;

    @TableField(value = "is_online")
    private Integer isOnline;

    @TableField(value = "is_free")
    private Integer isFree;
}
