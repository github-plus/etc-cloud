package etc.cloud.park.mode;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
@TableName(value = "pay_car_type")

//收费车型表
public class PayCarTypeModel {
    @TableId(type = IdType.AUTO,value = "pk_pay_temp_car_type_id")
    private int pkPayTempCarTypeId;//	临时车收费车型编号	Int

    private String payTempCarName;//	名称	String
    private int isDefault;//	是否默认收费类型：0 否 1 是（默认0）	Int
    private int isMan;//	是否人工调用：0 否 1是（默认0）	Int
    private int ChargeType;//	收费类型：1按次收费2分时收费3白天夜间收费	Int
    private int chargeId;//	收费设置id	Int


    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
