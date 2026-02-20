package tea4life.gateway_server.dto;

import lombok.Builder;

import java.util.Set;

/**
 * Admin 2/20/2026
 *
 **/
@Builder
public record UserPermissionsResponse(
        String email,
        Set<String> permissions
) {
}
