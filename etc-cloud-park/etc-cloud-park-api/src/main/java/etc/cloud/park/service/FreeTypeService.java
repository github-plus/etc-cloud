package etc.cloud.park.service;

public interface FreeTypeService {
    //免费类型设置的接口
    //查询
    Object inqueryFreeTypeHandler(String data);
    //新增
    Object addFreeTypeHandler( String data,String token);
    //删除
    Object deleteFreeTypeHandler( String data,String token);
}
