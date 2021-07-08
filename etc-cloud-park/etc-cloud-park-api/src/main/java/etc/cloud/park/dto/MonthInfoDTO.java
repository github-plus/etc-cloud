package etc.cloud.park.dto;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthInfoDTO {
    private Integer monthId;

    private String carNumber;

    private String monthStandard;

    private String userName;

    private String keyState;

    private String state;

    private String validityStart;

    private String validityEnd;

    private String room;

    private String tel;

    private String carport;

    private String createTime;

    private String note;

    private String carType;

    private String identityType;

    private String identity;

    private String carNumberType;

    private Integer pledge;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
