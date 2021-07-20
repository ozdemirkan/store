package com.ozdemirkan.store.order.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.ozdemirkan.store.order.repository")
@EntityScan(basePackages = "com.ozdemirkan.store.order.entity")
public class JpaConfig {
}
