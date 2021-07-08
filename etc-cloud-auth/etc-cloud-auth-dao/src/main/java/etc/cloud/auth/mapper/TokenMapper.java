package etc.cloud.auth.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import etc.cloud.auth.mode.TokenModel;
import etc.cloud.commons.constants.CommonConstants;
import org.springframework.stereotype.Repository;

@Repository
@DS(value = CommonConstants.DB_NAME)
public interface TokenMapper extends BaseMapper<TokenModel> {
    @Override
    int insert(TokenModel entity);
}
