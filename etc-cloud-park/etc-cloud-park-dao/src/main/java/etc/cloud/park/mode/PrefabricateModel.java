package etc.cloud.park.mode;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author langwen
 * @since 2020-10-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("prefabricate")
public class PrefabricateModel implements Serializable {

    private static final long serialVersionUID = 1L;

    //自增ID
    private String id;

    //车牌号
    private String carNumber;

    //车主
    private String carOwner;

    //房号
    private String room;

    //电话
    private String tel;

    //收费标准 1 免费车辆2小车收费
    private Integer feeType;

    //登记时间
    private String registerTime;

    //开始时间
    private String startTime;

    //结束时间
    private String endTime;

    //操作员
    private String workerName;

    //备注
    private String note;


}
