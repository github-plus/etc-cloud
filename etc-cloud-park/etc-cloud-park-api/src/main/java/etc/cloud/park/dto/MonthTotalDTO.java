package etc.cloud.park.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthTotalDTO {
    private String date;

    private Integer money;

    private Integer weChat;

    private Integer aLi;

    private Integer offline;

    private Integer bank;

    private Integer total;
}
