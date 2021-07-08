package etc.cloud.park.mode.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MonthPayVO {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO,value = "pk_id")
    private Integer pkId;

    @TableField(value = "month_standard")
    private Integer monthStandard;

    @TableField(value = "original_validity")
    private Timestamp originalValidity;

    @TableField(value = "validity_start")
    private Timestamp validityStart;

    @TableField(value = "validity_end")
    private Timestamp validityEnd;

    @TableField(value = "order_number")
    private String orderNumber;

    @TableField(value = "emp_name")
    private String empName;

    @TableField(value = "month_info.car_number")
    private String carNumber;

    private String room;

    private Integer realMoney;

    private Integer payState;

    private Integer isOnline;

    private Timestamp payTime;

    private Integer payType;

    private Integer pledge;
}