package bbojk.sideprojectplatformbackend.auth.server.authentication;

import bbojk.sideprojectplatformbackend.auth.Jwt;
import bbojk.sideprojectplatformbackend.auth.RefreshToken;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

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
