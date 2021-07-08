package etc.cloud.park.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundDTO {
    private Integer monthId;

    private String carNumber;

    private String originalValidity;

    private String validityStart;

    private String validityEnd;

    private Integer pledge;

    private Integer monthStandard;

    private Integer refundMoney;
}
