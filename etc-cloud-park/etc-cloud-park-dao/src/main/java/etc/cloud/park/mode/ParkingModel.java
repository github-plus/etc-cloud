package etc.cloud.park.mode;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName(value = "park")
public class ParkingModel {

    //@TableId(type = IdType.AUTO,value = "pk_secret_key")
    private String pkSecretKey;
    //@TableField(value = park_name")
    private String parkName;

    private String  projectName;

    private String address;

    private String community;

    private int longitude;

    private int dimension;
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
