package bbojk.sideprojectplatformbackend.auth.server.authentication.formlogin;

import bbojk.sideprojectplatformbackend.auth.server.authentication.jwt.Jwt;
import bbojk.sideprojectplatformbackend.auth.server.authentication.refresh.RefreshToken;
import bbojk.sideprojectplatformbackend.auth.server.authentication.AccessTokenAuthenticationToken;
import bbojk.sideprojectplatformbackend.auth.server.authorization.TokenAuthorization;
import bbojk.sideprojectplatformbackend.auth.server.authorization.TokenAuthorizationService;
import bbojk.sideprojectplatformbackend.auth.server.authentication.jwt.JwtGenerator;
import bbojk.sideprojectplatformbackend.auth.server.authentication.refresh.RefreshTokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Collections;

@RequiredArgsConstructor
public class UsernamePasswordTokenAuthenticationProvider implements AuthenticationProvider {
    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final TokenAuthorizationService authorizationService;
    private final JwtGenerator jwtGenerator;
    private final RefreshTokenGenerator refreshTokenGenerator;

    @Override
    public Authentication authenticate(
            Authentication authentication) throws AuthenticationException {
        Authentication authenticationResult = daoAuthenticationProvider.authenticate(authentication);

        Jwt jwt = jwtGenerator.generateTo(authenticationResult.getName(), Collections.emptyMap());
        RefreshToken refreshToken = refreshTokenGenerator.generate();

        TokenAuthorization tokenAuthorization =
                new TokenAuthorization(authentication.getName(), refreshToken);
        authorizationService.save(tokenAuthorization);

        return new AccessTokenAuthenticationToken(authenticationResult.getPrincipal(),
                jwt,
                refreshToken);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
