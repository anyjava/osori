package in.woowa.platform.osori.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import spring.trace.configuration.EnableTrace;
import spring.trace.web.TraceLogFilter;

/**
 * 오소리 어드민 어플리케이션
 * Created by seooseok on 2016. 9. 8..
 */
@EnableJpaRepositories(basePackages = {"in.woowa.platform.osori.admin"})
@EntityScan(basePackages = {"in.woowa.platform.osori.admin.entity"})
@ComponentScan(basePackages = {"in.woowa.platform.osori.admin"})
@EnableTrace(basePackages = "in.woowa.platform.osori", proxyTargetClass = true)
@SpringBootApplication
public class OsoriApplication {

    public static void main(String[] args) {
        SpringApplication.run(OsoriApplication.class, args);
    }

    @Bean
    TraceLogFilter traceLogFilter() {
        return new TraceLogFilter();
    }


}
