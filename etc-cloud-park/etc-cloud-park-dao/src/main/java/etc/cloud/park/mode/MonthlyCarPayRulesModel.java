package etc.cloud.park.mode;

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
@TableName(value = "month_car_strand")
public class MonthlyCarPayRulesModel {

    //月租车收费规则设置实体类
    private int pkMonthStrandardId;//	月租车规则编号	Int
    private String payName;//	缴费名称	String
    private int parkArea;//	停车位置：0 室外 1室内	Int
    private int payFee;//	缴费金额：每月多少元	Int
    private int isPayOnline;//	是否设置线上支付：0 否 1是（打勾的选择框）	Int
    private int payMode;//	开通线上支付模式：0代表不开启1代表微信，2代表支付宝，99代表所有都开通（上面新增新增设置旁的那个框）	Int
    private int highMonth;//	单次最高月数	Int
    private int banDelay;//	有效期高于N天禁止延期	Int
    private String note;//	备注	String
    private int chanRootId;//	通道权限（例如1代表进口权限打勾，2代表出口权限打勾, 3代表全选）	Int



    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
