package bbojk.sideprojectplatformbackend.auth.server.authentication;

import bbojk.sideprojectplatformbackend.auth.server.authentication.jwt.Jwt;
import bbojk.sideprojectplatformbackend.auth.server.authentication.refresh.RefreshToken;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Collections;

/**
 * Result of {@link org.springframework.security.authentication.AuthenticationProvider#authenticate(Authentication)
 * AuthenticationProvider.authenticate()} that contains {@link Authentication#getPrincipal() user principal}
 * and tokens issued for them.
 */
public class AccessTokenAuthenticationToken extends AbstractAuthenticationToken {
    private final Object principal;
    @Getter
    private final Jwt jwt;
    @Getter
    private final RefreshToken refreshToken;

    public AccessTokenAuthenticationToken(
            Object principal, Jwt jwt, RefreshToken refreshToken) {
        super(Collections.emptyList());
        this.principal = principal;
        this.jwt = jwt;
        this.refreshToken = refreshToken;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public Object getCredentials() {
        return "";
    }
}
