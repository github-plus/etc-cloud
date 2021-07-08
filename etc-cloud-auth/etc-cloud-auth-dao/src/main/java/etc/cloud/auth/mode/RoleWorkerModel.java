package etc.cloud.auth.mode;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("role_worker")
public class RoleWorkerModel {
    private String account;

    private String roleId;
}
