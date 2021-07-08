package etc.cloud.auth.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import etc.cloud.auth.mode.TokenModel;
import etc.cloud.auth.vo.WorkerVO;
import etc.cloud.commons.constants.CommonConstants;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@DS(value = CommonConstants.DB_NAME)
public interface LoginFilterMapper extends BaseMapper<TokenModel> {

    //根据token找到账号
    @Select("select account from token where token=#{token}")
    TokenModel finduser(String token);


    //根据token找到账号和账号名
    @Select("select workers.account,workers.worker_name,workers.project_id " +
            "from workers,token " +
            "where token.account=workers.account " +
            "and token.token=#{token}")
    WorkerVO findaccount(String token);

    //查找权限
    @Select("select menu_role.menu_name " +
            "from menu_role,role_worker " +
            "where role_worker.role_id = menu_role.role_id " +
            "and role_worker.account= #{account}")
    String findMenu(String account);


    //删除token
    @Delete("delete from token where token=#{token}")
    Boolean loginout(String token);
}
