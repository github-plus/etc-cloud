package etc.cloud.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import etc.cloud.auth.mode.RolesModel;

public interface IRoleWorkerService extends IService<RolesModel> {

    Object findRole(String data);
}
