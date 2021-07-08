package etc.cloud.park.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceLogDTO {
    private String channelName;

    private String deviceName;

    private String offline;

    private String content;
}
