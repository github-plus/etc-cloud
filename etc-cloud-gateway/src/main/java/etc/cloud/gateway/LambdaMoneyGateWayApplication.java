package etc.cloud.gateway;

import etc.cloud.core.configuration.CoMoneyConfigurationLogo;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

/**
 * @author lm_xu
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScans(value = {
        @ComponentScan(value = "etc.cloud.core.configuration"),
        @ComponentScan(value = "etc.cloud.auth")
})
@MapperScans(value = {
        @MapperScan(value = "etc.cloud.auth.mapper")
})
public class LambdaMoneyGateWayApplication {

    public static void main(String[] args) {

        new SpringApplicationBuilder()
                .bannerMode(Banner.Mode.OFF)
                .listeners(new CoMoneyConfigurationLogo())
                .sources(LambdaMoneyGateWayApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}
