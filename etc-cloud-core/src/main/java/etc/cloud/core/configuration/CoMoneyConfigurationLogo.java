package etc.cloud.core.configuration;

import etc.cloud.commons.constants.ConMoneyLog;
import etc.cloud.commons.toolkit.VersionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

/**
 * @author lm_xu
 */
@Slf4j
@Configuration
public class CoMoneyConfigurationLogo implements ApplicationListener<ApplicationEnvironmentPreparedEvent>, CommandLineRunner {


    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        System.out.println("\033[31;5m" + buildBannerText() + "\033[0m");
    }

    private String buildBannerText() {
        Pair<String, String> logoInfo = VersionUtils.getProjectLogoInfo();
        return ConMoneyLog.LINE_SEPARATOR
                + ConMoneyLog.LINE_SEPARATOR
                + ConMoneyLog.LOGO_STR
                + ConMoneyLog.LINE_SEPARATOR
                + " :: " + logoInfo.getValue() + "-" + logoInfo.getKey() + " :: "
                + ConMoneyLog.LINE_SEPARATOR;
    }


    @Override
    public void run(String... args) throws Exception {
        log.info("启动完毕...");
    }
}