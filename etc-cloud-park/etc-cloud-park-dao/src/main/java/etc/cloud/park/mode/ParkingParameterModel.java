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
@TableName(value = "param")
public class ParkingParameterModel {
    private int pkId;	//记录序号	Int
    private String secretKey;//	停车场秘钥	String
    private int tempChargeMode;	//临时车收费模式：0 收费1 不收费	Int
    private int carSum;//	车位总数	Int
    private int tempSum;//	临时车总数	Int
    private int tempInMode;//	临时车进场管控模式：     0 严进1 宽进	Int
    private int monthResidue;//	月租车剩余天数提醒	Int
    private String catalogue;//	本地图片存放共享目录:范例//192.168.10.11/pic/image	String
    private int monthOverMode;//	月租车过期处理方式：     0 过期N天禁止进场1 过期N天按临时车收费	Int
    private int monthOverDay;//	月租车过期N天处理	Int
    private int tempNonRc;//	临时车无匹配入场记录收费金额	Int
    private int isPayOnline;//	是否启动线上支付：     0 否1 是	int
    private int payOnlineFreeMinute;//	支付后免费N分钟	Int
    private int isAutoPolice;//	是否启动警车自动放行：     0 否1 是	Int
    private int isAutoArmy;//	是否启动军车自动放行：     0 否1 是	Int
    private int isMerchantDiscount;//	启动商户打折：     0 否1 是	Int
    private int isAreaCharge;//	启动区域指定收费：     0 否1 是	Int
    private int isTelDirect;//	启动电话簿：     0 否1 是	Int
    private int isNonFell;//	启动无感支付：     0 否1 是	Int
    private int isValueCar;//	启动储值车：     0 否1 是	Int
    private int isNonCarNumber;//	无牌车重复扫码进场：0 否 1 是	Int
    private int isNewCar;//	新能源车独立计费： 0 否 1 是	Int
    private int isCarIdentity;//	是否启动车型识别系统：0 否 1 是	Int
    private int blThreshold;//	识别度可信阈值	Int
    private int shareMode; // 共享模式参数

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
