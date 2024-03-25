package bbojk.sideprojectplatformbackend.auth.server;

import bbojk.sideprojectplatformbackend.auth.server.authentication.AuthorizationServerFilter;
import bbojk.sideprojectplatformbackend.auth.server.authentication.ConvertedAuthenticationProcessingFilter;
import bbojk.sideprojectplatformbackend.auth.server.authentication.RefreshTokenAuthenticationProvider;
import bbojk.sideprojectplatformbackend.auth.server.authentication.form.UsernamePasswordTokenAuthenticationProvider;
import bbojk.sideprojectplatformbackend.auth.server.authorization.TokenAuthorizationService;
import bbojk.sideprojectplatformbackend.auth.server.http.AccessTokenResponse;
import bbojk.sideprojectplatformbackend.auth.server.jwt.JwtGenerator;
import bbojk.sideprojectplatformbackend.auth.server.refresh.RefreshTokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthorizationServerConfigurer
        extends AbstractHttpConfigurer<AuthorizationServerConfigurer, HttpSecurity> {

    private static final AntPathRequestMatcher ANT_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher("/token", HttpMethod.POST.name());

    private ConvertedAuthenticationProcessingFilter authFilter;

    private final List<AuthenticationProvider> providers;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final TokenAuthorizationService tokenAuthorizationService;
    private final JwtGenerator jwtGenerator;
    private final RefreshTokenGenerator refreshTokenGenerator;
    private final HttpMessageConverter<AccessTokenResponse> accessTokenResponseHttpMessageConverter;


    @Autowired
    public AuthorizationServerConfigurer(PasswordEncoder passwordEncoder,
                                         UserDetailsService userDetailsService,
                                         TokenAuthorizationService tokenAuthorizationService,
                                         JwtGenerator jwtGenerator,
                                         RefreshTokenGenerator refreshTokenGenerator,
                                         HttpMessageConverter<AccessTokenResponse> accessTokenResponseHttpMessageConverter) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.tokenAuthorizationService = tokenAuthorizationService;
        this.jwtGenerator = jwtGenerator;
        this.refreshTokenGenerator = refreshTokenGenerator;
        this.accessTokenResponseHttpMessageConverter = accessTokenResponseHttpMessageConverter;
        providers = createProviders();
    }

    @Override
    public void init(HttpSecurity builder) throws Exception {
        authFilter = new AuthorizationServerFilter(
                ANT_PATH_REQUEST_MATCHER,
                new ProviderManager(providers),
                accessTokenResponseHttpMessageConverter
        );
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        builder.addFilterBefore(postProcess(authFilter), AbstractPreAuthenticatedProcessingFilter.class);
    }

    private List<AuthenticationProvider> createProviders() {
        return List.of(
                new RefreshTokenAuthenticationProvider(
                        tokenAuthorizationService,
                        jwtGenerator,
                        refreshTokenGenerator
                ),
                new UsernamePasswordTokenAuthenticationProvider(
                        createDaoAuthProvider(),
                        tokenAuthorizationService,
                        jwtGenerator,
                        refreshTokenGenerator
                )
        );
    }

    private DaoAuthenticationProvider createDaoAuthProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider =
                new DaoAuthenticationProvider(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }
}
