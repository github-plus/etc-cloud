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
 * @since 2020-10-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("workers")
public class WorkerModel implements Serializable {

    private static final long serialVersionUID = 1L;

    //注册账号（唯一）
    @TableId(value = "account", type = IdType.ID_WORKER)
    private String account;

    //注册密码
    private String password;

    //职工姓名
    private String workerName;

    //年龄
    private Integer age;

    //性别
    private String sex;

    //身份证号
    private String identity;

    //电话
    private String tel;

    //所属机构
    private String projectId;

    //所属机构名字
    private String projectName;

    //所属部门id
    private String organId;

    //所属部门名称
    private String organName;

    //所属岗位
    private String belongStation;

    //所属角色
    private String roleId;

    //角色名字
    private String roleName;

    //角色类型
    private String roleType;

    //备注
    private String note;

    //创建时间
    private Date createTime;

    //修改时间
    private Date updateTime;


}
