package etc.cloud.park.mode.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceLogVO {
    private String chanName;

    private Timestamp offline;

    private String deviceName;

    private Integer content;
}
