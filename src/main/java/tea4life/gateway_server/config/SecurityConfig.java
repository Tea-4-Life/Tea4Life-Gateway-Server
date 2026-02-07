package tea4life.gateway_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                        .pathMatchers("/actuator/**").permitAll()

                        // User Service
                        .pathMatchers("/user-service/public/**").permitAll()
                        .pathMatchers("/user-service/**").authenticated()

                        // Storage Service
                        .pathMatchers("/storage-service/public/**").permitAll()
                        .pathMatchers("/storage-service/**").authenticated()

                        // Product Service
                        .pathMatchers("/product-service/public/**").permitAll()
                        .pathMatchers("/product-service/**").authenticated()

                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        return http.build();
    }
}