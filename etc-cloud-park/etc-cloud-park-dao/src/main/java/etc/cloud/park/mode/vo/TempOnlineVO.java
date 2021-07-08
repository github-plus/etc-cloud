package etc.cloud.park.mode.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//临时车线上支付明细
public class TempOnlineVO {
    private String carNumber;

    private String orderNumber;

    private Timestamp inTime;

    private Timestamp outTime;

    private String stayTime;

    //支付时间
    private Timestamp createTime;

    //缴费金额
    private Integer realMoney;

    //支付方式，微信支付宝等
    private Integer payType;

    //操作员姓名
    private String outOperator;

    //优惠商户对象
    private String merchantName;

    //优惠方案名称
    private String discountName;

    //临时车缴费标准名称
    private String tempStandardName;

    private String image;
}
