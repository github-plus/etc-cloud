package etc.cloud.park.service;

import com.baomidou.mybatisplus.extension.service.IService;
import etc.cloud.park.mode.PrefabricateModel;

public interface PrefabricationService extends IService<PrefabricateModel> {
    //新增预制车
    Object addcar(String data,String token);
    //修改预制车
    Object updatecar(String data,String token);
    //删除预制车
    Object deletecar(String data,String token);
    //查询预制车
    Object findcar(String data);


}
