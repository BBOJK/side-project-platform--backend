package bbojk.sideprojectplatformbackend.auth.server.authentication;

import bbojk.sideprojectplatformbackend.auth.Jwt;
import bbojk.sideprojectplatformbackend.auth.RefreshToken;
import bbojk.sideprojectplatformbackend.auth.server.authorization.TokenAuthorization;
import bbojk.sideprojectplatformbackend.auth.server.authorization.TokenAuthorizationService;
import bbojk.sideprojectplatformbackend.auth.server.jwt.JwtGenerator;
import bbojk.sideprojectplatformbackend.auth.server.refresh.RefreshTokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class RefreshTokenAuthenticationProvider implements AuthenticationProvider {
    private final TokenAuthorizationService authorizationService;
    private final JwtGenerator jwtGenerator;
    private final RefreshTokenGenerator refreshTokenGenerator;

    @Override
    public Authentication authenticate(
            Authentication authentication) throws AuthenticationException {
        TokenAuthorization tokenAuthorization = getAuthorization(authentication);

        Jwt jwt = jwtGenerator.generateTo(tokenAuthorization.getPrincipalName(), Collections.emptyMap());
        RefreshToken refreshToken = refreshTokenGenerator.generate();

        tokenAuthorization = new TokenAuthorization(authentication.getName(), refreshToken);
        authorizationService.save(tokenAuthorization);

        return new AccessTokenAuthenticationToken(tokenAuthorization.getPrincipalName(),
                jwt,
                refreshToken);
    }

    private TokenAuthorization getAuthorization(
            Authentication authentication) {
        RefreshTokenAuthenticationToken refreshTokenAuthentication =
                (RefreshTokenAuthenticationToken) authentication;
        return authorizationService.findByRefreshToken(refreshTokenAuthentication.getRefreshToken())
                .orElseThrow(() -> new UserNotFoundException("No user for the given refresh token."));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return RefreshTokenAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
