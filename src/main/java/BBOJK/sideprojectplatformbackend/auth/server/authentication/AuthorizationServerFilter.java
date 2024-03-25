package bbojk.sideprojectplatformbackend.auth.server.authentication;

import bbojk.sideprojectplatformbackend.auth.server.jwt.Jwt;
import bbojk.sideprojectplatformbackend.auth.server.refresh.RefreshToken;
import bbojk.sideprojectplatformbackend.auth.server.authentication.form.UsernamePasswordAuthenticationConverter;
import bbojk.sideprojectplatformbackend.auth.server.http.AccessTokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.web.authentication.DelegatingAuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;
import java.util.List;

public class AuthorizationServerFilter extends ConvertedAuthenticationProcessingFilter {
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher("/token", HttpMethod.POST.name());
    private static final AuthenticationConverter converter = createDelegatingConverter();
    private final HttpMessageConverter<AccessTokenResponse> messageConverter;

    public AuthorizationServerFilter(AuthenticationManager authenticationManager,
                                     HttpMessageConverter<AccessTokenResponse> messageConverter) {
        this(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager, messageConverter);
    }

    public AuthorizationServerFilter(
            RequestMatcher requiresAuthenticationRequestMatcher,
            AuthenticationManager authenticationManager,
            HttpMessageConverter<AccessTokenResponse> messageConverter) {
        super(requiresAuthenticationRequestMatcher, converter, authenticationManager);
        this.messageConverter = messageConverter;
        setAuthenticationSuccessHandler(this::sendAccessTokenResponse);
    }

    private static AuthenticationConverter createDelegatingConverter() {
        return new DelegatingAuthenticationConverter(List.of(
                new RefreshTokenAuthenticationConverter(),
                new UsernamePasswordAuthenticationConverter()
        ));
    }

    private void sendAccessTokenResponse(HttpServletRequest request, HttpServletResponse response,
                                         Authentication authentication) throws IOException {
        AccessTokenAuthenticationToken accessTokenAuthentication =
                (AccessTokenAuthenticationToken) authentication;
        Jwt jwt = accessTokenAuthentication.getJwt();
        RefreshToken refreshToken = accessTokenAuthentication.getRefreshToken();
        AccessTokenResponse accessTokenResponse = new AccessTokenResponse(jwt, refreshToken);
        HttpOutputMessage outputMessage = new ServletServerHttpResponse(response);
        messageConverter.write(accessTokenResponse, null, outputMessage);
    }
}
