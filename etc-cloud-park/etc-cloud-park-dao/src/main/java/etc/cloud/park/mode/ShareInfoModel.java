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

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
@TableName(value = "share_info")
public class ShareInfoModel {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO,value = "pk_share_id")
    private Integer pkShareId;

    @TableField(value = "month_id")
    private Integer monthId;

    @TableField(value = "car_number")
    private String carNumber;

    @TableField(value = "note")
    private String note;
}
