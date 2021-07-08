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
@TableName(value = "device_info")
public class DeviceInfoModel {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO,value = "pk_ device_id")
    private Integer pkDeviceId;

    @TableField(value = "device_name")
    private String deviceName;

    @TableField(value = "channel_id")
    private Integer channelId;

    @TableField(value = "state")
    private Integer state;
}
