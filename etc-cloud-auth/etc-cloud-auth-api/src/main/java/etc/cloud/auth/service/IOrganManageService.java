package etc.cloud.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import etc.cloud.auth.mode.OrganManageModel;


public interface IOrganManageService extends IService<OrganManageModel> {

    Object findAllOrgan();
}
