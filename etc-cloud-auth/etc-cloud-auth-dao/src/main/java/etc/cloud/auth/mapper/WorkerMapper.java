package etc.cloud.auth.mapper;

import etc.cloud.auth.mode.WorkerModel;
import etc.cloud.auth.vo.OptionVO;
import etc.cloud.auth.vo.WorkerVO;
import etc.cloud.commons.constants.CommonConstants;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xlm
 */
@Repository
@DS(value = CommonConstants.DB_NAME)
public interface WorkerMapper extends BaseMapper<WorkerModel> {

    @Select("select * from workers where account=#{account}")
    List<WorkerModel> selectAccount( String account);

    @Select("select * from workers where worker_name=#{workerName} and password=#{password}")
    WorkerModel selectUser(String workerName, String password);


    @Select({"<script>"+
            "select DISTINCT workers.*,roles.* " +
            "FROM workers LEFT JOIN role_worker on workers.account = role_worker.account " +
            "LEFT JOIN roles on role_worker.role_id = roles.role_id where 1=1 "+
            "<if test='workerName!=null'> "+
            "and workers.worker_name = #{workerName}"+
            "</if>"+
            "<if test='organId!=null'> "+
            "and workers.organ_id = #{organId}"+
            "</if>"+
            "<if test='roleType!=null'> "+
            "and roles.role_type = #{roleType}"+
            "</if>" +
            "ORDER BY account "+
            "limit #{current},#{limit}"+
            "</script>"
    }
    )
    List<WorkerVO> findworker(int limit, int current, String workerName, String organId, String roleType);

    @Select("select organ_manage.organ_id,organ_manage.organ_name from organ_manage where 1=1")
    List<OptionVO> findorgan();

    @Select("select project.project_id,project.project_name from project where 1=1")
    List<OptionVO> findproject();

    //获取到
    @Select("select roles.role_id,roles.role_name,roles.role_type from roles where 1=1")
    List<OptionVO> findrole();

    //搜索该用户是否有角色
    @Select("select count(*) from role_worker where account=#{account}")
    int findworkerrole(String account);

    //查找项目，以给前端下拉框展示
    @Select("select project_name from project where project_id=#{projectId}")
    String findProjectName(String projectId);

    //删除role-worker中的账号
    @Delete("delete from role-worker where account =#{account} ")
    boolean deleteRoleWorker(String account);


}
