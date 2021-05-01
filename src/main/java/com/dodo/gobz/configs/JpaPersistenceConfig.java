package com.dodo.gobz.configs;

import com.dodo.gobz.configs.implementations.AuditorAwareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaPersistenceConfig {
    @Bean
    AuditorAware<Long> auditorProvider() {
        return new AuditorAwareImpl();
    }
}
