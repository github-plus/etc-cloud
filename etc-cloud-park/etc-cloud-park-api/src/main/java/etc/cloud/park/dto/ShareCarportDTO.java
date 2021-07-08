package etc.cloud.park.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShareCarportDTO {
    private Integer monthId;

    private String carNumber;

    private String monthStandardName;

    private String validityStart;

    private String validityEnd;

    private String room;

    private Integer carPort;

    private String userName;
}
