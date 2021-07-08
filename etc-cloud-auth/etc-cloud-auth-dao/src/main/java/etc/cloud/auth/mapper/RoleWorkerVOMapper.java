package etc.cloud.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import etc.cloud.auth.mode.RoleWorkerModel;
import org.apache.ibatis.annotations.Update;

public interface RoleWorkerVOMapper extends BaseMapper<RoleWorkerModel> {
    @Update("update role_worker set role_id=#{roleId} where account=#{account}")
    boolean updaterole(String account, String roleId);
}
