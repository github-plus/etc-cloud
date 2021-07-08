package etc.cloud.gateway.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import etc.cloud.auth.mapper.OrganRelationMapper;
import etc.cloud.auth.mode.OrganRelationModel;
import etc.cloud.auth.service.IOrganRelationService;
import org.springframework.stereotype.Service;

@Service
public class OrganRelationServiceImpl extends ServiceImpl<OrganRelationMapper, OrganRelationModel> implements IOrganRelationService {
}
