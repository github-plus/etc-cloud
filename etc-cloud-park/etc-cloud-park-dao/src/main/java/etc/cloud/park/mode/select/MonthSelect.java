package etc.cloud.park.mode.select;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthSelect {
    private Integer monthStandard;

    private Integer state;

    private Integer keyState;

    private String room;

    private String createStart;

    private String createEnd;

    private String carNumber;

    private String userName;

    private String tel;

    private String carPort;
}
