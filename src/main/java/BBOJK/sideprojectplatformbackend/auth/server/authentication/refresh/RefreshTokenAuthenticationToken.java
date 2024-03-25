package bbojk.sideprojectplatformbackend.auth.server.authentication.refresh;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

public class RefreshTokenAuthenticationToken extends AbstractAuthenticationToken {
    @Getter
    private final String refreshToken;

    /**
     * Creates a token with the supplied array of authorities.
     */
    public RefreshTokenAuthenticationToken(String refreshToken) {
        super(Collections.emptyList());
        this.refreshToken = refreshToken;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return refreshToken;
    }
}
