package in.woowa.platform.osori.admin.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Created by seooseok on 2016. 7. 5..
 * Spring default auditing configuration
 */
@Configuration
@EnableJpaAuditing
public class AuditingConfiguration {

    @Bean
    AuditorAwareImpl auditorAware() {
        return new AuditorAwareImpl();
    }
}
