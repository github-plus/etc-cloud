package etc.cloud.park;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import etc.cloud.core.configuration.CoMoneyConfigurationLogo;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;


@SpringBootApplication
@EnableDiscoveryClient
@ComponentScans(value = {
        @ComponentScan(value = "etc.cloud.core.configuration"),
        @ComponentScan(value = "etc.cloud")
})
@MapperScans(value = {
        @MapperScan(value = "etc.cloud.park.mapper")
})
public class ParkApplication {
    public static void main(String[] args) {
//        SpringApplication.run(ParkApplication.class, args);
        new SpringApplicationBuilder()
                .bannerMode(Banner.Mode.OFF)
                .listeners(new CoMoneyConfigurationLogo())
                .sources(ParkApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}
