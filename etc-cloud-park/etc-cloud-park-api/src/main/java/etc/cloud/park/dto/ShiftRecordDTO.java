package etc.cloud.park.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShiftRecordDTO {

    private String operatorName;

    private String loginTime;

    private String exitTime;

    private Integer openGate;

    private Integer inDeal;

    private Integer outDeal;

    private Integer money;

    private Integer derateMoney;
}
