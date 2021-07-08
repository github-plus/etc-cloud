package etc.cloud.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import etc.cloud.auth.mode.OrganManageModel;
import etc.cloud.auth.vo.OrganRelationVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface OrganManageMapper extends BaseMapper<OrganManageModel> {

    @Select({"<script>"+
            "select * from organ_manage " +
            "where organ_id not in (select organ_id from organ_relation) "+
            "</script>"
    })
    List<OrganRelationVO> findAllOrgan();

    @Select("select organ_relation.organ_id,organ_relation.organ_name " +
            "from organ_manage,organ_relation " +
            "where organ_manage.organ_id=organ_relation.organ_father and organ_manage.organ_id=#{organId}")
    List<OrganRelationVO> findlevel(String organId);

    //根据部门id找到部门名称
    @Select("select organ_name from organ_manage " +
            "where organ_id=#{organId}")
    String findOrganName(String organId);

    //查询下拉框
    @Select("select * from organ_manage " +
            "where organ_type='1' " +
            "and organ_id not in (select organ_father from organ_relation) "
    )
    List<OrganRelationVO> findLevelOption();

    //删除部门层级
    @Delete("delete from organ_relation " +
            "where organ_id=#{organId} " +
            "or organ_father=#{organId}")
    Boolean deletelevel(String organId);
}
