package tea4life.gateway_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

@SpringBootApplication
@EnableWebFluxSecurity
@EnableFeignClients
public class GatewayServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServerApplication.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()

                // Storage Service
                .route("storage-service-route", r -> r
                        .path("/storage-service/**")
                        .filters(f ->
                                f.rewritePath("/storage-service/(?<segment>.*)", "/${segment}")
                        )
                        .uri("lb://TEA4LIFE-STORAGE-SERVICE")
                )

                // User Service
                .route("user-service-route", r -> r
                        .path("/user-service/**")
                        .filters(f ->
                                f.rewritePath("/user-service/(?<segment>.*)", "/${segment}")
                        )
                        .uri("lb://TEA4LIFE-USER-SERVICE")
                )

                // Product Service
                .route("product-service-route", r -> r
                        .path("/product-service/**")
                        .filters(f ->
                                f.rewritePath("/product-service/(?<segment>.*)", "/${segment}")
                        )
                        .uri("lb://TEA4LIFE-PRODUCT-SERVICE")
                )

                .build();
    }


}
