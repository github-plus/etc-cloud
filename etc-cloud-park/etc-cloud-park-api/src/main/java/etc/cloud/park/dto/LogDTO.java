package etc.cloud.park.dto;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogDTO {

    private int pkId;//	记录序号	Int
    private String empId;//	用户账号	Int  必填
    private String community;//	小区名称	String
    private String modeName;//	模块名称	String 必填
    private String impName;//	接口名称	String
    private String opType;//	操作方法： 新增月租车，缴费
    private String opContent;//	操作内容	String  必填
    private String carNumber;//	车牌号码	String
    private String opResult;//	操作结果	String  必填
    private String opTime;//	操作时间	String 必填

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
