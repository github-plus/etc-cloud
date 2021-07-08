package etc.cloud.commons.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author xlm
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommonConstants {

    public static final String DB_NAME = "etc_cloud_database";
    public static final String[] SPRING_BEAN_FILTER = new String[]{"etc-cloud-monitor-admin"};

}
