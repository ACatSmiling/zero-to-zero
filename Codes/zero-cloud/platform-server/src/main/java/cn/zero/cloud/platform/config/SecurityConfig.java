package cn.zero.cloud.platform.config;

import cn.zero.cloud.platform.config.properties.SecurityProperties;
import cn.zero.cloud.platform.security.PrivilegeHttpSecurityExpressionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

/**
 * @author Xisun Wang
 * @since 6/7/2024 15:03
 */
@Configuration
@Order(Ordered.LOWEST_PRECEDENCE - 3)
@EnableWebSecurity
public class SecurityConfig {
    private final SecurityProperties securityProperties;

    @Autowired
    public SecurityConfig(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    private static final List<String> IGNORED_URLS = List.of(
            "/css/**", "/html/**", "/img/**",
            "/js/**", "/libs/**", "/version.txt",
            "/login/**"
    );

    @Bean
    public PrivilegeHttpSecurityExpressionHandler httpExpressionHandler() {
        return new PrivilegeHttpSecurityExpressionHandler();
    }

    @Bean
    public WebSecurityCustomizer configure() {
        // Define public access resources white list
        return web -> {
            web.ignoring().requestMatchers(toAntPathRequestMatchers());
            if (securityProperties.isDebug()) {
                web.ignoring().requestMatchers("/actuator/**") //For run monitor
                        .requestMatchers(antMatcher("/swagger-ui.html")) //For doc
                        .requestMatchers(antMatcher("/webjars/springfox-swagger-ui/**"))
                        .requestMatchers(antMatcher("/swagger-resources/**"))
                        .requestMatchers(antMatcher("/v2/api-docs/**"));
            }
        };
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        var authorizationManager = new WebExpressionAuthorizationManager("isLoggedIn()");
        authorizationManager.setExpressionHandler(httpExpressionHandler());

        http
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                // .addFilterBefore(new SaTokenAuthenticationFilter(), SaTokenAuthenticationFilter.class)
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(antMatcher("/cache/**")).permitAll()
                        .requestMatchers(antMatcher("/exception/**")).permitAll()
                        .requestMatchers(antMatcher("/world/**")).permitAll()
                        .requestMatchers(antMatcher("/kafka/**")).permitAll()
                        .requestMatchers(antMatcher("/login/**")).permitAll()
                        .requestMatchers(antMatcher("/**"))
                        .access(authorizationManager)
                );

        return http.build();
    }

    private AntPathRequestMatcher[] toAntPathRequestMatchers() {
        return SecurityConfig.IGNORED_URLS.stream().map(AntPathRequestMatcher::antMatcher).toList().toArray(new AntPathRequestMatcher[0]);
    }
}
