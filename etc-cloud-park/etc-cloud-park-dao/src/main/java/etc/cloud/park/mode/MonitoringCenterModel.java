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
@TableName(value = "center")
public class MonitoringCenterModel {

    private int pkCenterId;
    private String name;
    private String ip;
    private String mac;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
