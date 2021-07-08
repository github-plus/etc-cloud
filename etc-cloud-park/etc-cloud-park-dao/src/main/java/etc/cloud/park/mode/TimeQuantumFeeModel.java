package etc.cloud.park.mode;

import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName(value = "time_quantum_fee")
public class TimeQuantumFeeModel {

    //分时间段收费表
    @TableId(type = IdType.AUTO,value = "pk_time_quantum_fee_id")
    private int	pkTimeQuantumFeeId;//分时间段收费编号
    private int	timeFirst;//首段计时时长：分钟
    private int	timeCellFirst;//计时单位：比如以20分钟为一个单位进行计费
    private int	timeAmountFirst;//计时金额（一个单位的计费金额量）
    private int	timeSecond;//第二段计时时长：分钟
    private int	timeCellSecond;//计时单位
    private int	timeAmountSecond;//计时金额
    private int	timeThird;//第三段计时时长
    private int	timeCellThird;//计时单位
    private int	timeAmountThird;//计时金额
    private int	timeCellFinal;//计时单位
    private int	timeAmountFinal;//计时金额
    private int	isNatureDay;//是否自然天：0 否（24小时）1 是（自然天24小时）
    private int	isFree;//是否含有免费分钟：0 否 1 有（默认0）
    private int	freeTime;//免费分钟
    private int	charge;//单次最高收费金额
    private int	periodCharge;//周期内最高收费
    private int	isHead;//计费后是否有首段：0 无 1 有
    private int	isMore;//计费周期内多次是否进出计费累计限额：0 否 1是
    private String note;//备注

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
