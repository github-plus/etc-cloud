package etc.cloud.park.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthPayDTO {
    private String monthMoney;

    private String originalValidity;

    private String validityStart;

    private String validityEnd;

    private String orderNumber;

    private String operator;

    private String carNumber;

    private String room;

    private Integer realMoney;

    private String payState;

    private String isOnline;

    private String payTime;

    private String payType;
}
