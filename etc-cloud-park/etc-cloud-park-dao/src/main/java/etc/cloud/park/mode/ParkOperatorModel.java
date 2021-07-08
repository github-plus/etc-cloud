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
@TableName(value = "operator")
public class ParkOperatorModel {
    private int pkAccount;
    private String opName;
    private String passord;
    private int state;
    private  String pk_secret_key; //车厂密钥

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
