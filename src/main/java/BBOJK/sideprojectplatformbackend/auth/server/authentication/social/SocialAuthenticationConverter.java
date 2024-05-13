package bbojk.sideprojectplatformbackend.auth.server.authentication.social;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.authentication.AuthenticationConverter;

public class SocialAuthenticationConverter implements AuthenticationConverter {
    private final BearerTokenResolver tokenResolver;

    public SocialAuthenticationConverter(BearerTokenResolver tokenResolver) {
        this.tokenResolver = tokenResolver;
    }

    @Override
    public Authentication convert(HttpServletRequest request) {
        String bearerToken = tokenResolver.resolve(request);
        if (bearerToken == null) {
            return null;
        }

        return new BearerTokenAuthenticationToken(bearerToken);
    }
}
