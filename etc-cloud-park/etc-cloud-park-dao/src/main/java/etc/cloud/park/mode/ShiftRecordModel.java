package etc.cloud.park.mode;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
@TableName(value = "shift_record")
public class ShiftRecordModel {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO,value = "pk_id")
    private Integer pkId;

    private String operatorName;

    private Timestamp loginTime;

    private Timestamp exitTime;

    private Integer openGate;

    private Integer inDeal;

    private Integer outDeal;

    private Integer money;

    @TableField(value = "derate_money")
    private Integer derateMoney;
}
