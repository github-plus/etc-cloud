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
@TableName(value = "month_info")
public class MonthInfoModel {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO,value = "pk_month_id")
    private Integer pkMonthId;

    @TableField(value = "car_number")
    private String carNumber;

    @TableField(value = "month_standard")
    private Integer montStandard;

    @TableField(value = "month_standard_name")
    private String monthStandardName;

    @TableField(value = "owner_id")
    private Integer ownerId;

    @TableField(value = "car_number_type")
    private Integer carNumberType;

    @TableField(value = "validity_start")
    private Timestamp validityStart;

    @TableField(value = "validity_end")
    private Timestamp validityEnd;

    @TableField(value = "carport")
    private String carport;

    @TableField(value = "key_state")
    private Integer keyState;

    @TableField(value = "state")
    private Integer state;

    @TableField(value = "pledge")
    private Integer pledge;

    @TableField(value = "car_type")
    private Integer carType;

    @TableField(value = "note")
    private String note;

    @TableField(value = "create_time")
    private Timestamp createTime;

    @TableField(value = "update_time")
    private Timestamp updateTime;

    @TableField(value = "room")
    private String room;
}
