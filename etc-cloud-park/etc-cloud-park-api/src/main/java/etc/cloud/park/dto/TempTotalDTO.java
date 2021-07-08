package etc.cloud.park.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TempTotalDTO {

    private String date;

    private Integer money;

    private Integer freeMoney;

    private Integer discountMoney;

    private Integer onlineMoney;

    private Integer offlineMoney;

    private Integer totalRealMoney;
}
