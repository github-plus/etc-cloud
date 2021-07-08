package etc.cloud.park.mode.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TempPayVO {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO,value = "pk_id")
    private Integer pkId;

    private String carNumber;

    private String tempStandardName;

    private String discountName;

    private Timestamp inTime;

    private Timestamp outTime;

    private String parkTime;

    private String outChannel;

    private String outOperator;

    private String freeType;

    private String image;

    private Integer money;

    private Integer derateMoney;

    @TableField(value = "order_number.real_money")
    private Integer realMoney;

    private Integer isOnline;
}
