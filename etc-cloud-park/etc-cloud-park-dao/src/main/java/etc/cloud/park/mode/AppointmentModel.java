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
 * @since 2020-11-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("appointment")
public class AppointmentModel implements Serializable {

    private static final long serialVersionUID = 1L;

    //主键id
    private String id;

    //停车场秘钥
    private String pkSecretKey;

    //车牌号
    private String carNumber;

    //车主
    private String carOwner;

    //预约开始时间
    private String startTime;

    //预约结束时间
    private String endTime;

    //申请时间
    private String applyTime;

    //业主姓名
    private String roomOwner;

    //房号
    private String room;

    //电话号码
    private String tel;

    //状态 1通过 0拒绝
    private Integer status;


}
