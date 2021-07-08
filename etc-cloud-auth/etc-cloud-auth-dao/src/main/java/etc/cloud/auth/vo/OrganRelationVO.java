package etc.cloud.auth.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.google.gson.Gson;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author langwen
 * @since 2020-10-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrganRelationVO implements Serializable {

    private static final long serialVersionUID = 1L;

    //部门id
//    @TableId(value = "organ_id", type = IdType.ID_WORKER)
    private String organId;

    //部门名称
    private String organName;

    //部门类别 0独立机构 1下辖机构
    private Integer organType;

    //子类组织
    private List<OrganRelationVO> sonlevel;

    //子层级
    private String organSon;
    //子层级
    private String organSonName;


    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
