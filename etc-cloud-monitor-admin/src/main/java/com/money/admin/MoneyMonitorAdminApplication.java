package com.money.admin;

import etc.cloud.core.configuration.CoMoneyConfigurationLogo;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

/**
 * @author lm_xu
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class })
@EnableAdminServer
@EnableDiscoveryClient
@ComponentScans(value = {
        @ComponentScan(value = "etc.cloud.core")
})
public class MoneyMonitorAdminApplication {
    public static void main(String[] args) {

        new SpringApplicationBuilder()
                .bannerMode(Banner.Mode.OFF)
                .listeners(new CoMoneyConfigurationLogo())
                .sources(MoneyMonitorAdminApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}
