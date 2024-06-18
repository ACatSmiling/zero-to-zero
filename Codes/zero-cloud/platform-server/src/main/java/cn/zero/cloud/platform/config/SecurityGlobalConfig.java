package cn.zero.cloud.platform.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * @author Xisun Wang
 * @since 6/18/2024 13:23
 */
@Configuration
@Order(Ordered.LOWEST_PRECEDENCE)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityGlobalConfig extends GlobalMethodSecurityConfiguration {
}
