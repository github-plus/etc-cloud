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

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
@TableName(value = "car_owner")
public class CarOwnerModel {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO,value = "pk_user_id")
    private Integer pkUserId;

    @TableField(value = "user_name")
    private String userName;

    @TableField(value = "identity_type")
    private Integer identityType;

    @TableField(value = "identity")
    private String identity;

    @TableField(value = "tel")
    private String tel;
}
