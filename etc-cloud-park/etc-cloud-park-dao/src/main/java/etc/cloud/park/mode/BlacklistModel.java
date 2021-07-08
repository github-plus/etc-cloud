package etc.cloud.park.mode;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("blacklist")
public class BlacklistModel {

    //黑名单编号
    @TableId(value = "pk_blacklist_id")
    private String pkBlacklistId;

    //车牌号码
    private String carNumber;

    //生效日期
    private String startTime;

    //结束日期
    private String endTime;

    //登记时间
    private String registerTime;
    //操作员
    private String account;

    //操作员名称
    private String workerName;
    //备注
    private String note;

}
