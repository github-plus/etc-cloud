package etc.cloud.auth.mode;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

@Data
@TableName("menu_role")
public class MenuRolesModel {
    private String roleId;

    private String menuId;

    private String menuName;

}
