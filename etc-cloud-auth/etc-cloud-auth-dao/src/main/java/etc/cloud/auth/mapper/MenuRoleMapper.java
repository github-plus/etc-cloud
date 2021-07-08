package etc.cloud.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import etc.cloud.auth.mode.MenuRolesModel;
import org.apache.ibatis.annotations.Update;

public interface MenuRoleMapper extends BaseMapper<MenuRolesModel> {

    //更新该角色对应权限
    @Update("UPDATE menu_role SET menu_name=#{menuName} WHERE role_id = #{roleId}")
    boolean updateMenu(String roleId,String menuName);
}
