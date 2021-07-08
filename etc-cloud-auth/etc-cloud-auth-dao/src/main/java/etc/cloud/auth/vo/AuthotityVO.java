package etc.cloud.auth.vo;

import lombok.Data;

import java.util.List;

@Data
public class AuthotityVO {

    private String account;

    private String workerName;

    private String pkSecretKey;

    private String parkName;

    private List<AuthotityVO> park;

}
