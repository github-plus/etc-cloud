package etc.cloud.park.mode.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceInfoVO {
    private Integer pkDeviceId;

    private String deviceName;

    private String chanName;

    private Integer state;
}
