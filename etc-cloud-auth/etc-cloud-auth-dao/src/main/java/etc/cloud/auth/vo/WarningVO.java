package etc.cloud.auth.vo;

import lombok.Data;

@Data
public class WarningVO {

    private String createTime;
    private int content;
    private String projectName="项目名称";
    private String roomName="房屋名称";
}
