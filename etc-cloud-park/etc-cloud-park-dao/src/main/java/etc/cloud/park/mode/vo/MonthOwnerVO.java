package etc.cloud.park.mode.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthOwnerVO {
private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO,value = "pk_month_id")
    private Integer pkMonthId;

    private String carNumber;

    private Integer monthStandard;

    private Timestamp validityStart;

    private Timestamp validityEnd;

    private String carport;

    private Integer keyState;

    private Integer state;

    private Integer carType;

    private String note;

    private Timestamp createTime;

    private String room;

    @TableField(value = "car_owner.user_name")
    private String userName;

    private Integer identityType;

    private String identity;

    private Integer carNumberType;

    private Integer pledge;

    private String tel;

    private Integer total;
}
