package etc.cloud.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import etc.cloud.auth.mode.WorkerModel;
import etc.cloud.auth.vo.AuthotityVO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AutorityMapper extends BaseMapper<WorkerModel> {

    @Select("<script>" +
            "select workers.account,workers.worker_name " +
            "from workers,role_worker,roles " +
            "where 1=1 " +
            "<if test='workerName!=null'>" +
            "and worker_name=#{workerName} " +
            "</if>" +
            "<if test='roleType!=null'>" +
            "and roles.role_type=#{roleType} and roles.role_id=role_worker.role_id " +
            "and role_worker.account=workers.account " +
            "</if>" +
            "limit #{current},#{limit}" +
            "</script>")
    List<AuthotityVO> find(int limit, int current, String roleType, String workerName);

    @Select("select park.park_name " +
            "from park,project,workers  " +
            "where park.project_name=project.project_name " +
            "and project.project_id=workers.project_id " +
            "and workers.account=#{account}")
    List<AuthotityVO> findPark( String account);
}
