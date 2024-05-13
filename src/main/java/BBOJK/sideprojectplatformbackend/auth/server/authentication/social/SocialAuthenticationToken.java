package bbojk.sideprojectplatformbackend.auth.server.authentication.social;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;

import java.util.Collections;

@Getter
public class SocialAuthenticationToken extends AbstractAuthenticationToken {
    private final BearerTokenAuthenticationToken bearerToken;
    private final HttpServletRequest request;

    public SocialAuthenticationToken(BearerTokenAuthenticationToken bearerToken,
                                     HttpServletRequest request) {
        super(Collections.emptyList());
        this.bearerToken = bearerToken;
        this.request = request;
    }


    @Override
    public Object getPrincipal() {
        return bearerToken.getPrincipal();
    }

    @Override
    public Object getCredentials() {
        return bearerToken.getCredentials();
    }
}
