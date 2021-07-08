package etc.cloud.park.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TempOnlienDTO {

    private String carNmuber;

    private String orderNumber;

    private String inTime;

    private String outTime;

    private String parkTime;

    private String payTime;

    private String payMoney;

    private String payType;

    private String operator;

    private String merchantName;

    private String discountName;

    private String tempStandard;

    private String image;
}
