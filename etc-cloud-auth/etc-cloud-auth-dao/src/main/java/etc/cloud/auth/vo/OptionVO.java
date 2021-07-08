package etc.cloud.auth.vo;

import java.util.List;

public class OptionVO {


    //所属部门id
    private String organId;

    //所属部门名字
    private String organName;


    //所属角色
    private String roleId;

    //角色名称
    private String roleName;

    //角色类型
    private String roleType;

    //角色类型名称
    private String roleTypeName;

    //项目id
    private String projectId;

    //项目名称
    private  String projectName;
    List<OptionVO> workerVOList;
}
