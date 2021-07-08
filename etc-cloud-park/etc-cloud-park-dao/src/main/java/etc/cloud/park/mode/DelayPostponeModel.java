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
@TableName(value = "delay_postpone")
public class DelayPostponeModel {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO,value = "pk_id")
    private Integer pkId;

    @TableField(value = "car_number")
    private String carNumber;

    @TableField(value = "month_standard")
    private Integer monthStandard;

    @TableField(value = "validity_start")
    private Timestamp validityStart;

    @TableField(value = "original_validity_end")
    private Timestamp originalValidityEnd;

    @TableField(value = "validity_end")
    private Timestamp validityEnd;

    @TableField(value = "emp_name")
    private String empName;

    @TableField(value = "note")
    private String note;
}
