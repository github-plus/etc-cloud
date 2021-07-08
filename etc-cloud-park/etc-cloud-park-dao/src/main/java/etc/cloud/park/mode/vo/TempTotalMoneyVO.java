package etc.cloud.park.mode.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TempTotalMoneyVO {
    private static final long serialVersionUID = 1L;

    private String date;

    private Integer realMoney;

    private Integer derateMoney;

    private Integer money;
}
