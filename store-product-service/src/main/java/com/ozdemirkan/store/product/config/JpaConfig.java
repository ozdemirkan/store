package com.ozdemirkan.store.product.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.ozdemirkan.store.product.repository")
@EntityScan(basePackages = "com.ozdemirkan.store.product.entity")
public class JpaConfig {
}
