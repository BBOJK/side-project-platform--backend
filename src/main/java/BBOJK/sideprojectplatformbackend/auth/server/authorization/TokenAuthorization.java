package bbojk.sideprojectplatformbackend.auth.server.authorization;

import bbojk.sideprojectplatformbackend.auth.server.authentication.refresh.RefreshToken;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;


/**
 * An entity for storing information related to token authorizations
 * such as:
 * <ul>
 *     <li>refresh token for the user and its expiration time</li>
 *     <li>scopes for the user</li>
 * </ul>
 *
 */
@Getter
@Builder(builderClassName = "Builder", builderMethodName = "from")
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

    public static Builder from(TokenAuthorization authorization) {
        return new Builder()
                .id(authorization.getId())
                .principalName(authorization.getPrincipalName())
                .refreshToken(authorization.getRefreshToken());
    }
}
