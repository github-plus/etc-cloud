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
public class ShareInfoMonthVO {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO,value = "pk_share_id")
    private Integer pkShareId;

    @TableField(value = "month_id")
    private Integer monthId;

    @TableField(value = "car_number")
    private String carNumber;

    private Integer monthStandard;

    private Timestamp validityStart;

    private Timestamp validityEnd;

    private String room;

    private Integer carPort;

    private String userName;
}
