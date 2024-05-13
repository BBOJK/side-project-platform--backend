package bbojk.sideprojectplatformbackend.auth.server.authentication.social;

import bbojk.sideprojectplatformbackend.auth.server.authentication.AccessTokenAuthenticationToken;
import bbojk.sideprojectplatformbackend.auth.server.authentication.jwt.JwtGenerator;
import bbojk.sideprojectplatformbackend.auth.server.authentication.refresh.RefreshToken;
import bbojk.sideprojectplatformbackend.auth.server.authentication.refresh.RefreshTokenGenerator;
import bbojk.sideprojectplatformbackend.auth.server.authorization.TokenAuthorization;
import bbojk.sideprojectplatformbackend.auth.server.authorization.TokenAuthorizationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;

import java.util.Collections;

public class SocialAuthenticationProvider implements AuthenticationProvider {
    private final JwtIssuerAuthenticationManagerResolver authenticationManagerResolver;
    private final TokenAuthorizationService tokenAuthorizationService;
    private final JwtGenerator jwtGenerator;
    private final RefreshTokenGenerator refreshTokenGenerator;

    public SocialAuthenticationProvider(TokenAuthorizationService tokenAuthorizationService,
                                        JwtGenerator jwtGenerator,
                                        RefreshTokenGenerator refreshTokenGenerator,
                                        String... issuerUrl) {
        this.tokenAuthorizationService = tokenAuthorizationService;
        this.jwtGenerator = jwtGenerator;
        this.refreshTokenGenerator = refreshTokenGenerator;
        authenticationManagerResolver = JwtIssuerAuthenticationManagerResolver.fromTrustedIssuers(issuerUrl);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SocialAuthenticationToken authenticationRequest = (SocialAuthenticationToken) authentication;
        AuthenticationManager authenticationManager = authenticationManagerResolver.resolve(
                authenticationRequest.getRequest());
        Authentication authenticate = authenticationManager.authenticate(authenticationRequest.getBearerToken());
        Jwt socialJwt = (Jwt) authenticate.getPrincipal();
        String email = socialJwt.getClaim("email");
        // use custom Jwt class
        bbojk.sideprojectplatformbackend.auth.server.authentication.jwt.Jwt jwt =
                jwtGenerator.generateTo(email, Collections.emptyMap());
        RefreshToken refreshToken = refreshTokenGenerator.generate();
        TokenAuthorization tokenAuthorization = new TokenAuthorization(email, refreshToken);
        tokenAuthorizationService.save(tokenAuthorization);
        return new AccessTokenAuthenticationToken(email, jwt, refreshToken);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SocialAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
