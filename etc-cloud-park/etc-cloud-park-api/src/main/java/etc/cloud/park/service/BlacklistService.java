package etc.cloud.park.service;

import com.baomidou.mybatisplus.extension.service.IService;
import etc.cloud.park.mode.BlacklistModel;

public interface BlacklistService extends IService<BlacklistModel> {

    //新增黑名单
    Object addcar(String data,String token);

    //修改黑名单
    Object updatecar(String data,String token);

    //删除黑名单
    Object deletecar(String data, String token);

    //查询
    Object findcar(String data);

}
