package etc.cloud.auth.dto;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xlm
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String name;
    private String age;
    private String userName;
    private String password;



    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
