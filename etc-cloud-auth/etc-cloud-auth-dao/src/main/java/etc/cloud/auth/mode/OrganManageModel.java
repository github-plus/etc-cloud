package etc.cloud.auth.mode;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.google.gson.Gson;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

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
@TableName(value = "organ_manage")
public class OrganManageModel implements Serializable {

    private static final long serialVersionUID = 1L;

    //部门id
    @TableId(value = "organ_id", type = IdType.ID_WORKER)
    private String organId;

   //部门名称
    private String organName;

    //部门类别 0独立机构 1下辖机构
    private Integer organType;

    //部门描述
    private String organDescribe;

    //所属省
    private String province;

    //所属市
    private String city;

    //所属县
    private String county;

    //具体地址
    private String address;

    //电话
    private String tel;

    //创建时间
    private Date createTime;

    //修改时间
    private Date updateTime;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
