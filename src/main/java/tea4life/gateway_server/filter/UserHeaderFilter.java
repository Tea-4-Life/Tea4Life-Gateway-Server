package tea4life.gateway_server.filter;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import tea4life.gateway_server.client.UserInternalClient;

@Component
@RequiredArgsConstructor
public class UserHeaderFilter implements GlobalFilter {

    /**
     * ========================================
     * QUAN TRỌNG
     * t vibe code full class này, t chả biết
     * tại sao nó lại hoạt động but okay...
     * ========================================
     */

    private final ObjectProvider<@NonNull UserInternalClient> userInternalClientProvider;

    @NullMarked
    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain
    ) {
        return exchange.getPrincipal()
                .filter(p -> p instanceof JwtAuthenticationToken)
                .cast(JwtAuthenticationToken.class)
                .flatMap(jwtAuth -> {
                    String userId = jwtAuth.getToken().getSubject();
                    String email = jwtAuth.getToken().getClaim("email");

                    return fetchUserAuthorities(userId)
                            .map(authorities -> {
                                ServerHttpRequest mutatedRequest =
                                        mutateRequest(exchange, userId, email, authorities);
                                return exchange.mutate().request(mutatedRequest).build();
                            });
                })
                .defaultIfEmpty(exchange)
                .flatMap(chain::filter);
    }

    /**
     * Hàm lấy quyền hạn từ User Service thông qua Feign Client.
     * Sử dụng Schedulers.boundedElastic() vì Feign là Blocking call trong môi trường Reactive.
     */
    private Mono<@NonNull String> fetchUserAuthorities(String userId) {
        return Mono.fromCallable(() -> {
                    UserInternalClient client = userInternalClientProvider.getIfAvailable();
                    return (client != null) ? client.getUserPermissions(userId) : null;
                })
                .subscribeOn(Schedulers.boundedElastic())
                .map(response -> {
                    if (response.getData() != null) {
                        return String.join(",", response.getData().permissions());
                    }
                    return "";
                })
                .onErrorReturn("");
    }

    /**
     * Tạo request mới với các header định danh người dùng
     */
    private ServerHttpRequest mutateRequest(
            ServerWebExchange exchange, String userId,
            String email, String authorities
    ) {
        return exchange.getRequest().mutate()
                .header("X-User-KeycloakId", userId)
                .header("X-User-Email", email)
                .header("X-User-Authorities", authorities)
                .build();
    }

}