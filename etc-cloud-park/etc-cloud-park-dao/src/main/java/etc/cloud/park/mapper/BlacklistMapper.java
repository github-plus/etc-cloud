package etc.cloud.park.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import etc.cloud.park.mode.BlacklistModel;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistMapper extends BaseMapper<BlacklistModel> {

    //查找操作员
    @Select("select workers.worker_name,workers.account from token,workers " +
            "where token.account = workers.account " +
            "and token.token=#{token}")
    BlacklistModel findoperator(String token);

    @Select("select account from token where token=#{token}")
    String findaccount(String token);

}
