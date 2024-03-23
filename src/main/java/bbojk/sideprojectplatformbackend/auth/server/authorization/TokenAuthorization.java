package bbojk.sideprojectplatformbackend.auth.server.authorization;

import bbojk.sideprojectplatformbackend.auth.RefreshToken;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class TokenAuthorization {
    private String id;
    private String principalName;
    private RefreshToken refreshToken;

    public TokenAuthorization(String principalName, RefreshToken refreshToken) {
        this(UUID.randomUUID().toString(), principalName, refreshToken);
    }

    public TokenAuthorization(String id, String principalName, RefreshToken refreshToken) {
        this.id = id;
        this.principalName = principalName;
        this.refreshToken = refreshToken;
    }

    public static TokenAuthorizationBuilder builder(TokenAuthorization from) {
        return new TokenAuthorizationBuilder()
                .id(from.getId())
                .principalName(from.getPrincipalName())
                .refreshToken(from.getRefreshToken());
    }
}
