package etc.cloud.park.mode;

import com.baomidou.mybatisplus.annotation.TableName;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Timestamp;
import java.util.Date;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
@TableName(value = "log")
public class LogModel {
    //日志表
    private int pkId;//	记录序号	Int
    private String empId;//	用户账号	String  必填
    private String community;//	小区名称	String
    private String modeName;//	模块名称	String 必填
    private String impName;//	接口名称	String
    private String opType;//	操作方法： 新增月租车，缴费
    private String opContent;//	操作内容	String  必填
    private String carNumber;//	车牌号码	String
    private String opResult;//	操作结果	String  必填
    private Timestamp opTime;//	操作时间	TimeStamp  必填

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
