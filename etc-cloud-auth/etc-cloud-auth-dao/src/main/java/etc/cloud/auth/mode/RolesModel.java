package etc.cloud.auth.mode;

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
 * @since 2020-10-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("roles")
public class RolesModel implements Serializable {

    private static final long serialVersionUID = 1L;

    //角色id
    @TableId(value = "role_id", type = IdType.ID_WORKER_STR)
    private String roleId;

    //角色名称
    private String roleName;

    //角色类型
    private String roleType;

    //创建者
    private String createName;

    //项目id
    private String projectId;

    //创建时间
    private Date createTime;

    //修改时间
    private Date updateTime;


}
