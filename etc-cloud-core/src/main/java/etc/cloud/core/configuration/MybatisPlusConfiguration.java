package etc.cloud.core.configuration;

import etc.cloud.core.condition.DataBaseRequireCondition;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author xlm
 */

@Slf4j
@EnableTransactionManagement
@Configuration
public class MybatisPlusConfiguration {

    @Bean
    @Conditional(value = DataBaseRequireCondition.class)
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
        paginationInterceptor.setDialectType("mysql");
        return paginationInterceptor;
    }
}
