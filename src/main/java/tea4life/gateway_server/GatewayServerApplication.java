package tea4life.gateway_server;

import org.springframework.beans.factory.annotation.Value;
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

    @Value("${service.url.storage}")
    private String storageServiceUrl;

    @Value("${service.url.user}")
    private String userServiceUrl;

    @Value("${service.url.product}")
    private String productServiceUrl;

    @Value("${service.url.order}")
    private String orderServiceUrl;

    @Value("${service.url.logistics}")
    private String logisticsServiceUrl;

    @Value("${service.url.recommendation}")
    private String recommendationServiceUrl;

    @Value("${service.url.workflow}")
    private String workflowServiceUrl;

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
                        .uri(storageServiceUrl)
                )

                // User Service
                .route("user-service-route", r -> r
                        .path("/user-service/**")
                        .filters(f ->
                                f.rewritePath("/user-service/(?<segment>.*)", "/${segment}")
                        )
                        .uri(userServiceUrl)
                )

                // Product Service
                .route("product-service-route", r -> r
                        .path("/product-service/**")
                        .filters(f ->
                                f.rewritePath("/product-service/(?<segment>.*)", "/${segment}")
                        )
                        .uri(productServiceUrl)
                )

                // Order Service
                .route("order-service-route", r -> r
                        .path("/order-service/**")
                        .filters(f ->
                                f.rewritePath("/order-service/(?<segment>.*)", "/${segment}")
                        )
                        .uri(orderServiceUrl)
                )

                // Logistics Service
                .route("logistics-service-route", r -> r
                        .path("/logistics-service/**")
                        .filters(f ->
                                f.rewritePath("/logistics-service/(?<segment>.*)", "/${segment}")
                        )
                        .uri(logisticsServiceUrl)
                )

                // Recommendation Service
                .route("recommendation-service-route", r -> r
                        .path("/recommendation-service/**")
                        .filters(f ->
                                f.rewritePath("/recommendation-service/(?<segment>.*)", "/${segment}")
                        )
                        .uri(recommendationServiceUrl)
                )

                // Workflow Service
                .route("workflow-service-route", r -> r
                        .path("/workflow-service/**")
                        .filters(f ->
                                f.rewritePath("/workflow-service/(?<segment>.*)", "/${segment}")
                        )
                        .uri(workflowServiceUrl)
                )

                .build();
    }
}
