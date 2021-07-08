package etc.cloud.park.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TempPayDTO {
    private String tempStandard;

    private String carNumber;

    private String discountName;

    private String inTime;

    private String outTime;

    private String parkTime;

    private String outChannel;

    private String outOperator;

    private String freeType;

    private String image;

    private Integer money;

    private Integer derateMoney;

    private Integer realMoney;

    private Integer payOnline;
}
