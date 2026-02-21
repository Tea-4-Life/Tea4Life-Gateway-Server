package tea4life.gateway_server.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tea4life.gateway_server.dto.ApiResponse;
import tea4life.gateway_server.dto.UserPermissionsResponse;

/**
 * Admin 2/20/2026
 *
 **/
@FeignClient(name = "TEA4LIFE-USER-SERVICE", path = "/internal/users")
public interface UserInternalClient {

    @GetMapping("/{keycloakId}/permissions")
    ApiResponse<UserPermissionsResponse> getUserPermissions(@PathVariable("keycloakId") String keycloakId);

}
