package etc.cloud.auth.vo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.lang.reflect.Type;

/**
 * <p>
 *
 * </p>
 *
 * @author langwen
 * @since 2020-10-20
 */
@Data
public class WorkerVO {


    //注册账号（唯一）
    private String account;

    //职工姓名
    private String workerName;

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

    //角色名称
    private String roleName;

    //角色类型
    private  String roleType;




}
