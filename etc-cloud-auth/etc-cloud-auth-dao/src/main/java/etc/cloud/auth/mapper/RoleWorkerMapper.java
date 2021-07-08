package etc.cloud.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import etc.cloud.auth.mode.RolesModel;
import etc.cloud.auth.vo.RolesVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleWorkerMapper  extends BaseMapper<RolesModel> {

    @Select("<script>" +
            "select * " +
            "from roles " +
            "where 1=1 " +
            "<if test='roleType!=null'>" +
            "and role_type=#{roleType}" +
            "</if>" +
            "</script>")
    List<RolesVO> findRole(int limit, int current, String roleType);

    @Delete("delete from roles " +
            "where role_id=#{roleId} "
            )
    boolean deleteRole(String roleId);

    //从角色权限表删除
    @Delete("delete from menu_role " +
            "where role_id=#{roleId} ")
    boolean deleteMenuRole(String roleId);

    @Delete("delete from role_worker " +
            "where role_id=#{roleId}"
            )
    boolean deleteRoleWorker(String roleId);


}
