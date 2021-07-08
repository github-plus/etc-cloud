package etc.cloud.park.mode;

import com.baomidou.mybatisplus.annotation.TableName;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
@TableName(value = "free_type")
public class FreeTypeModel {
    //免费类型表
    private int pkFreeTypeId;//	免费类型编号	Int
    private String FreeTypeName;//	名称	String

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
