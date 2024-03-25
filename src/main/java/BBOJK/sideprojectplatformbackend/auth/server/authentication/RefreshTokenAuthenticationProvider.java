package bbojk.sideprojectplatformbackend.auth.server.authentication;

import bbojk.sideprojectplatformbackend.auth.server.jwt.Jwt;
import bbojk.sideprojectplatformbackend.auth.server.refresh.RefreshToken;
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
import java.util.Map;

/**
 * Authentication provider for issuing access token in exchange for refresh token.
 * <p>
 * Refresh token rotation always applied.
 */
@Component
@RequiredArgsConstructor
public class RefreshTokenAuthenticationProvider implements AuthenticationProvider {
    private final TokenAuthorizationService authorizationService;
    private final JwtGenerator jwtGenerator;
    private final RefreshTokenGenerator refreshTokenGenerator;

    @Override
    public boolean supports(Class<?> authentication) {
        return RefreshTokenAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public Authentication authenticate(
            Authentication authentication) throws AuthenticationException {
        TokenAuthorization tokenAuthorization = getAuthorization(authentication);

        Map<String, String> additionalClaims = Collections.emptyMap();
        Jwt jwt = jwtGenerator.generateTo(tokenAuthorization.getPrincipalName(), additionalClaims);

        RefreshToken refreshToken = rotateRefreshToken(tokenAuthorization);

        return new AccessTokenAuthenticationToken(
                tokenAuthorization.getPrincipalName(),
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

    private RefreshToken rotateRefreshToken(TokenAuthorization tokenAuthorization) {
        RefreshToken refreshToken = refreshTokenGenerator.generate();
        TokenAuthorization.Builder authorizationBuilder = TokenAuthorization.from(tokenAuthorization);
        authorizationBuilder.refreshToken(refreshToken);
        authorizationService.save(authorizationBuilder.build());
        return refreshToken;
    }
}
