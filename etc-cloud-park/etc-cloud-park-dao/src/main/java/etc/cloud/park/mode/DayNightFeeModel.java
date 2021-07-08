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
@TableName(value = "day_night_fee")
public class DayNightFeeModel {

    //白天夜间收费表
    @TableId(type = IdType.AUTO,value = "pk_day_night_fee_id")
    private int	pkDayNightFeeId;//白天夜间计费序列id
    private String	dayTimeBegin;//白天开始时间，如“08:00”
    private String	dayTimeEnd;//白天结束时间，如“18:00”
    private int	dayTimeFirst;//首段计时时长：分钟
    private int	dayTimeCellFirst;//首段计时单位：比如以20分钟为一个单位进行计费
    private int	dayTimeAmountFirst;//首段计时金额（一个单位的计费金额量）
    private int	dayTimeCellFinal;//白天二段计时单位
    private int	dayTimeAmountFinal;//白天二段计时金额
    private String	nightTimeBegin;//夜间开始时间，如“18:00”
    private String	nightTimeEnd;//夜间结束时间，如“08:00”
    private int	nightTimeFirst;//首段计时时长：分钟
    private int	nightTimeCellFirst;//首段计时单位：比如以20分钟为一个单位进行计费
    private int	nightTimeAmountFirst;//首段计时金额（一个单位的计费金额量）
    private int	nightTimeCellFinal;//夜间二段计时单位
    private int	nightTimeAmountFinal;//夜间二段计时金额

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
