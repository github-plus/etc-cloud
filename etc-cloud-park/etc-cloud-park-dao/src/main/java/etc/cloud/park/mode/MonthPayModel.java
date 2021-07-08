package etc.cloud.park.mode;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName(value = "month_pay")
public class MonthPayModel {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO,value = "pk_id")
    private Integer pkId;

    @TableField(value = "month_id")
    private Integer montId;

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

    @TableField(value = "pay_mode")
    private Integer payMode;

    @TableField(value = "emp_name")
    private String empName;

    @TableField(value = "note")
    private String note;
}
